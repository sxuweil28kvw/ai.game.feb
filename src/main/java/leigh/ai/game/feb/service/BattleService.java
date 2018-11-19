package leigh.ai.game.feb.service;

import leigh.ai.game.feb.parsers.BattleResultParser;
import leigh.ai.game.feb.parsers.ParserExceptionHandler;
import leigh.ai.game.feb.parsers.PersonStatusParser;
import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.service.map.MapPath;
import leigh.ai.game.feb.service.map.Traffic;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleService {
	private static Logger logger = LoggerFactory.getLogger(BattleService.class);
	public static int searchUntilEnemy() {
		String search = HttpUtil.get("search.php");
		while(search.contains("什么都没发现") || search.contains("画面上数字是什么") || search.contains("验证错误，重新回答")) {
			if(search.contains("画面上数字是什么") || search.contains("验证错误，重新回答")) {
				FakeSleepUtil.sleep(2, 3);
				verify(search);
				search = HttpUtil.get("search.php");
			} else {
				search = HttpUtil.get("search.php");
			}
		}
		int level = Integer.parseInt(search.split("'search','", 2)[1].split("'")[0]);
		if(logger.isDebugEnabled()) {
			logger.debug("索敌成功，等级"+ level);
		}
		return level;
	}
	private static void verify(String search) {
		String code = "";
		try {
			Document doc = Jsoup.parse(search);
			Element td = doc.body().child(0).child(0).child(1).child(1).child(0);
			String images = td.getElementsByTag("table").get(0).child(0).child(0).child(0).html();
			String[] spl = images.split("febimg/img1/style./c");
			for(int i = 1; i < spl.length; i++) {
				code += spl[i].split(".gif", 2)[0];
			}
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, search, "解析验证码失败！");
		}
		String verify = HttpUtil.get("code_co.php?maintext=" + code);
		if(!verify.contains("正确")) {
			logger.error("验证码验证异常！");
			logger.error(verify);
			System.exit(1);
		} else {
			logger.debug("填写了验证码");
		}
	}
	public static BattleInfo fight(int level) {
		String battleResponse = HttpUtil.get("battle.php?suodi=soldier&lv=" + level);
		BattleInfo result = BattleResultParser.parse(battleResponse);
		return result;
	}
	public static void readyForBattle() {
		PersonStatusService.update();
		if(PersonStatusService.money < 2000) {
			FacilityService.drawCash(19000);
		}
		if(PersonStatusService.weapons.get(0).getAmountLeft() < 10) {
			FacilityService.repairWeapon(0);
		}
		if(PersonStatusService.HP < PersonStatusService.maxHP) {
			selfHeal();
		}
		if(PersonStatusService.AP < 10) {
			addAp();
		}
	}
	public static void addAp() {
		addAp(true);
	}
	public static boolean addAp(boolean autoBuy) {
		boolean hasShengshui = false;
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().equals("圣水") || t.getName().equals("烧酒") || t.getName().equals("卡博雷酒")) {
				hasShengshui = true;
				useItem(t.getPosition());
				return true;
			}
		}
		if(!hasShengshui) {
			if(autoBuy) {
				buyHolywater();
			} else {
				return false;
			}
		}
		return addAp(autoBuy);
	}
	public static void selfHeal() {
		selfHeal(true);
	}
	public static boolean selfHeal(boolean autoBuy) {
		int healHp = PersonStatusService.maxHP - PersonStatusService.HP;
		if(healHp <= 0) {
			return true;
		}
		boolean haveMedicine = false;
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().equals("伤药") || t.getName().equals("万灵药")) {
				haveMedicine = true;
				useItem(t.getPosition());
				logger.debug("使用物品" + t.getName());
				break;
			} else if(t.getName().equals("回复之杖") || t.getName().equals("治疗之杖") || t.getName().equals("痊愈之杖")) {
				if(JobService.canUseStaff()) {
					haveMedicine = true;
					useStaff(t);
				}
			}
			if(PersonStatusService.maxHP - PersonStatusService.HP <= 0) {
				return true;
			}
		}
		if(!haveMedicine) {
			if(autoBuy) {
				logger.debug("没药救命，先去买药");
				buyMedicine();
			} else {
				return false;
			}
		}
		return selfHeal(autoBuy);
	}
	public static void useStaff(MyItem t) {
		PersonStatusParser.parseAfterUseItem(HttpUtil.get("useitem_heal.php?goto=useitem&wrap=" + t.getPosition()));
		PersonStatusParser.itemsAfterUse(HttpUtil.get("useitem.php"));
		logger.debug("使用了回复之杖");
	}
	public static void buyMedicine() {
		if(JobService.canUseStaff()) {
			MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[]{FacilityType.itemshop}));
			for(int i = 5 - PersonStatusService.items.size() - 1; i > 0; i--) {
				FacilityService.buyItem("eaaa");
			}
		} else if(PersonStatusService.memberCard) {
			MapPath pathMember = MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.itemshopMember});
			MoveService.movePath(pathMember);
			FacilityService.buyItem("aaah");
		} else {
			MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[]{FacilityType.itemshop}));
			if(PersonStatusService.maxHP > 40) {
				FacilityService.buyItem("aaab");
			} else {
				FacilityService.buyItem("aaaa");
			}
		}
		PersonStatusParser.itemsAfterUse(HttpUtil.get("useitem.php"));
	}
	public static void useItem(String itemPosition) {
		String personStat = HttpUtil.get("useitem_co.php?goto=useitem&wrap=" + itemPosition);
		PersonStatusParser.parseAfterUseItem(personStat);
		String items = HttpUtil.get("useitem.php");
		PersonStatusParser.itemsAfterUse(items);
	}
	public static void useItem(MyItem t) {
		String personStat = HttpUtil.get("useitem_co.php?goto=useitem&wrap=" + t.getPosition());
		logger.debug("使用物品：" + t.getName());
		PersonStatusParser.parseAfterUseItem(personStat);
		String items = HttpUtil.get("useitem.php");
		PersonStatusParser.itemsAfterUse(items);
	}
	public static void buyHolywater() {
		if(PersonStatusService.memberCard) {
			if(PersonStatusService.money < 7500) {
				FacilityService.drawCash(19000);
			}
			MapPath pathMember = MapService.findFacilityExceptTraffics(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.itemshopMember}, Traffic.fly);
			MoveService.movePath(pathMember);
			FacilityService.buyItem("aaai");
		} else {
			MoveService.movePath(MapService.findFacilityExceptTraffics(PersonStatusService.currentLocation, new FacilityType[]{FacilityType.itemshop}, Traffic.fly));
			FacilityService.buyItem("aaac");
		}
	}
}
