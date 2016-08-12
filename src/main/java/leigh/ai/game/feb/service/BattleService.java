package leigh.ai.game.feb.service;

import leigh.ai.game.feb.parsers.BattleResultParser;
import leigh.ai.game.feb.parsers.PersonStatusParser;
import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.MoveService.BattleConfig;
import leigh.ai.game.feb.service.MyStatus.MyItem;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.service.map.MapPath;
import leigh.ai.game.feb.util.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleService {
	private static Logger logger = LoggerFactory.getLogger(BattleService.class);
	public static int searchUntilEnemy() {
		String search = HttpUtil.get("search.php");
		while(search.contains("什么都没发现")) {
			search = HttpUtil.get("search.php");
		}
		int level = Integer.parseInt(search.split("'search','", 2)[1].split("'")[0]);
		if(logger.isDebugEnabled()) {
			logger.debug("索敌成功，等级"+ level);
		}
		return level;
	}
	public static BattleInfo fight(int level) {
		String battleResponse = HttpUtil.get("battle.php?suodi=soldier&lv=" + level);
		BattleInfo result = BattleResultParser.parse(battleResponse);
		return result;
	}
	public static void readyForBattle() {
		LoginService.login();
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
	private static void addAp() {
		boolean hasShengshui = false;
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().equals("圣水") || t.getName().equals("烧酒") || t.getName().equals("卡博雷酒")) {
				hasShengshui = true;
				useItem(t.getPosition());
				return;
			}
		}
		if(!hasShengshui) {
			buyHolywater();
			FacilityService.buyItem("aaac");
		}
		addAp();
	}
	private static void selfHeal() {
		int healHp = PersonStatusService.maxHP - PersonStatusService.HP;
		if(healHp <= 0) {
			return;
		}
		boolean haveMedicine = false;
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().equals("伤药") || t.getName().equals("万灵药")) {
				haveMedicine = true;
				useItem(t.getPosition());
				logger.debug("使用物品" + t.getName());
				break;
			} else if(t.getName().equals("回复之杖")) {
				if(JobService.canUseStaff()) {
					haveMedicine = true;
					useStaff(t);
				}
			}
		}
		if(!haveMedicine) {
			logger.debug("没药救命，先去买药");
			buyMedicine();
		}
		selfHeal();
		return;
	}
	public static void useStaff(MyItem t) {
		PersonStatusParser.parseAfterUseItem(HttpUtil.get("useitem_heal.php?goto=useitem&wrap=" + t.getPosition()));
		PersonStatusParser.itemsAfterUse(HttpUtil.get("useitem.php"));
		logger.debug("使用了回复之杖");
	}
	public static void buyMedicine() {
		if(PersonStatusService.memberCard) {
			MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[]{FacilityType.itemshopMember}));
			FacilityService.drawCash(25000);
			FacilityService.buyItem("aaah");
		} else if(JobService.canUseStaff()) {
			if(PersonStatusService.money < 1000) {
				FacilityService.drawCash(19000);
			}
			MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[]{FacilityType.itemshop}));
			FacilityService.buyItem("eaaa");
		} else {
			if(PersonStatusService.money < 1000) {
				FacilityService.drawCash(19000);
			}
			MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[]{FacilityType.itemshop}));
			FacilityService.buyItem("aaab");
		}
		PersonStatusParser.itemsAfterUse(HttpUtil.get("useitem.php"));
	}
	public static void useItem(String itemPosition) {
		String personStat = HttpUtil.get("useitem_co.php?goto=useitem&wrap=" + itemPosition);
		PersonStatusParser.parseAfterUseItem(personStat);
		String items = HttpUtil.get("useitem.php");
		PersonStatusParser.itemsAfterUse(items);
	}
	public static void buyHolywater() {
		if(PersonStatusService.memberCard) {
			MoveService.moveTo(1198);
			FacilityService.drawCash(25000);
			FacilityService.buyItem("aaai");
		} else {
			if(PersonStatusService.money < 1000) {
				FacilityService.drawCash(19000);
			}
			MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[]{FacilityType.itemshop}));
			FacilityService.buyItem("aaac");
		}
	}
}
