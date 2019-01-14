package leigh.ai.game.feb.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.parsers.BankParser;
import leigh.ai.game.feb.parsers.ItemParser;
import leigh.ai.game.feb.parsers.ItemShopParser;
import leigh.ai.game.feb.parsers.ParserExceptionHandler;
import leigh.ai.game.feb.parsers.WeaponShopParser;
import leigh.ai.game.feb.service.map.MapPath;
import leigh.ai.game.feb.service.map.Traffic;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.service.status.MyStatus.MyWeapon;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

/****************
 * 
 * 城镇设施相关service（码头不算，码头坐船在MoveService里）
 * @author leightao
 *
 */
public class FacilityService {
	private static Logger logger = LoggerFactory.getLogger(FacilityService.class);
	public enum FacilityType {
		itemshop,
		itemshopMember,
		weaponshopE,
		weaponshopD,
		weaponshopC,
		weaponshopB,
		bank,
		inn,
		airport,;

		public static FacilityType[] weaponShops() {
			return new FacilityType[] {
					weaponshopE,
					weaponshopD,
					weaponshopC,
					weaponshopB,
			};
		}
	}
	public static Map<FacilityType, List<Integer>> map;
	static {
		Integer[] itemshops = new Integer[] {1152, 1137, 1128, 1123, 1122, 1119, 1160, 1113, 1204, 1156, 1102, 1107,
				1183, 1179, 1194, 2107, 2036, 2034, 2049, 2094, 2098, 2107, 2113,
				2125, //西方三岛
				2060, 2064, 2089, 2070, 2189, 2083,//贝伦
				};
		Integer[] weaponShopes = new Integer[] {1122, 1160, 1113, 1107, 1194, 2034, 2049, 2094, 2098, 2113,
				2060, 2089, //贝伦
				};
		Integer[] weaponShopds = new Integer[] {1128, 1149, 1204, 1142, 1183,
				2064, 2189, 2083, //贝伦
				};
		Integer[] weaponShopcs = new Integer[] {1152, 1137, 1119, 1198, 1156, 1102, 1179, 2036, 2107,
				2125, //西方三岛
				2070, //贝伦
				};
		Integer[] weaponShopbs = new Integer[] {
				1161, 2191, 2194, 2195,
				};
		Integer[] banks = new Integer[] {1152, 1119, 1198, 1156, 1102, 1179, 1161, 2049, 2194, 2036,
				2125, //西方三岛
				2070, //贝伦
				};
		Integer[] inns = new Integer[] {1137, 1128, 1123, 1122, 1149, 1160, 1113, 1198, 1204, 1142, 1107, 1183,
				1194, 1161,
				2034, 2098, 2094, 2107, 2113, //艾特鲁利亚
				2004, 2033, 2016, //利基亚
				2060, 2064, 2089, 2195, 2189, 2083, //贝伦
				};
		Integer[] airports = new Integer[] {
				1152, 1137, 1128, 1123, 1122, 1119, 1149, 1113, 1204,
				1156, 1142, 1102, 1107, 1183, 1179, 
		};
		map = new HashMap<FacilityType, List<Integer>>();
		map.put(FacilityType.itemshop, Arrays.asList(itemshops));
		map.put(FacilityType.weaponshopE, Arrays.asList(weaponShopes));
		map.put(FacilityType.weaponshopD, Arrays.asList(weaponShopds));
		map.put(FacilityType.weaponshopC, Arrays.asList(weaponShopcs));
		map.put(FacilityType.bank, Arrays.asList(banks));
		map.put(FacilityType.inn, Arrays.asList(inns));
		map.put(FacilityType.weaponshopB, Arrays.asList(weaponShopbs));
		map.put(FacilityType.itemshopMember, Arrays.asList(new Integer[] {1198, 2194, 2195}));
		map.put(FacilityType.airport, Arrays.asList(airports));
	}
	public static void saveMoney() {
		MapPath path = MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.bank});
		MoveService.movePath(path);
		HttpUtil.get("shopbanka.php");
		HttpUtil.get("shopbanka_wi.php");
		HttpUtil.get("shopbankc.php");
		String bank1 = HttpUtil.get("shopbankc_wi.php?goto=gsms");
		int currentMoney = Integer.parseInt(bank1.split("您现有金币:", 2)[1].split("<", 2)[0]);
		if(currentMoney <= 20000) {
			return;
		}
		int saveMoney = ((currentMoney - 20000) / 1000 + 1) * 1000;
		if(logger.isDebugEnabled()) {
			logger.debug("I have money " + currentMoney + ", now save " + saveMoney + " to bank.");
		}
		HttpUtil.get("shopbankc_updata.php?goto=gsms&maintext=" + saveMoney);
		PersonStatusService.update();
	}
	public static void repairWeapon(int position) {
		if(PersonStatusService.money < 2000) {
			drawCash(42000);
		}
		FacilityType[] types = null;
		if(PersonStatusService.memberCard) {
			types = new FacilityType[] {FacilityType.weaponshopE, FacilityType.weaponshopD, FacilityType.weaponshopC, FacilityType.weaponshopB};
		} else {
			types = new FacilityType[] {FacilityType.weaponshopE, FacilityType.weaponshopD, FacilityType.weaponshopC};
		}
		MapPath path = MapService.findFacility(PersonStatusService.currentLocation, types);
		MoveService.movePath(path);
		String positionCode = position == 0 ? "E" : (position + "");
		HttpUtil.get("shopwep.php");
		FakeSleepUtil.sleep(2);
		HttpUtil.get("shopwep_co.php?goto=what&wrap=" + positionCode);
		int price = WeaponShopParser.parseRepairPrice(HttpUtil.get("shopwep_co.php?goto=repair&wrap=" + positionCode));
		if(price == 0) {
			return;
		}
		if(PersonStatusService.money < price) {
			drawCash(29000 + price);
		}
		String response = HttpUtil.get("shopwep_updata.php?goto=repair&wrap=" + positionCode);
		WeaponShopParser.parseAfterRepair(response);
	}
	public static boolean hasFacility(int currentLocation, FacilityType type) {
		return map.get(type).contains(currentLocation);
	}
	public static void drawCash(int targetMoney) {
		if(targetMoney < PersonStatusService.money) {
			return;
		}
		MapPath path = MapService.findFacilityExceptTraffics(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.bank}, Traffic.ship);
		MoveService.movePath(path);
		HttpUtil.get("shopbanka.php");
		HttpUtil.get("shopbanka_wi.php");
		HttpUtil.get("shopbankc.php");
		HttpUtil.get("shopbankc_wi.php?goto=msgs");
		int mahua = (targetMoney - PersonStatusService.money) / 1000 + 1;
		if(mahua > PersonStatusService.mahua) {
			mahua = PersonStatusService.mahua;
		}
		HttpUtil.get("shopbankc_updata.php?goto=msgs&maintext=" + mahua);
		if(logger.isDebugEnabled()) {
			logger.debug("将" + mahua + "麻花兑换为金钱");
		}
		PersonStatusService.update();
	}
	public static void buyItem(String itemcode) {
		int price = ItemShopParser.parseBuyPrice(HttpUtil.get("shopits_co.php?goto=buy&buytype=0&item=" + itemcode));
		if(PersonStatusService.money < price) {
			int location = PersonStatusService.currentLocation;
			drawCash(29000 + price);
			MoveService.moveTo(location);
		}
		ItemShopParser.afterBuy(HttpUtil.get("shopits_updata.php?goto=buy&buytype=0&item=" + itemcode));
		ItemParser.itemsAfterUse(HttpUtil.get("useitem.php"));
		logger.debug("购买了" + MyItem.lookup.get(itemcode));
	}
	public static int queryBankSlots() {
		MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.bank}));
		BankParser.parseBankOpening(HttpUtil.get("shopbanka.php"));
		BankParser.parseBankStorage(HttpUtil.get("shopbanka_wi.php"));
		return PersonStatusService.bankTotalSlots - PersonStatusService.bankItems.size();
	}
	public static boolean storeItem(String position) {
		MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.bank}));
		BankParser.parseBankOpening(HttpUtil.get("shopbanka.php"));
		BankParser.parseBankStorage(HttpUtil.get("shopbanka_wi.php"));
		if(PersonStatusService.bankItems.size() >= PersonStatusService.bankTotalSlots) {
			if(logger.isDebugEnabled()) {
				logger.debug("银行仓库已满！");
			}
			return false;
		}
		HttpUtil.get("shopbanka_updata.php?goto=sell&type=item&wrap=" + position);
		BankParser.parseBankStorage(HttpUtil.get("shopbanka_wi.php?type=i"));
		ItemParser.itemsAfterUse("useitem.php");
		return true;
	}
	public static boolean saveWeaponToBank(MyWeapon w) {
		MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.bank}));
		BankParser.parseBankOpening(HttpUtil.get("shopbanka.php"));
		BankParser.parseBankStorage(HttpUtil.get("shopbanka_wi.php"));
		String s1 = HttpUtil.get("shopbanka_updata.php?goto=sell&type=weapon&wrap=" + w.getPosition());
		if(!s1.startsWith("物品已经存放好了")) {
			logger.error("存放武器" + w.getName() + "失败！");
			return false;
		}
		BankParser.parseBankStorage(HttpUtil.get("shopbanka_wi.php?type=w"));
		PersonStatusService.weapons.remove(w);
		return true;
	}
	public static void sellWeapon(MyWeapon w) {
		FacilityType[] types = null;
		if(PersonStatusService.memberCard) {
			types = new FacilityType[] {FacilityType.weaponshopE, FacilityType.weaponshopD, FacilityType.weaponshopC, FacilityType.weaponshopB};
		} else {
			types = new FacilityType[] {FacilityType.weaponshopE, FacilityType.weaponshopD, FacilityType.weaponshopC};
		}
		MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, types));
		HttpUtil.get("shopwep.php");
		HttpUtil.get("shopwep_co.php?goto=what&wrap=" + w.getPosition());
		int price = WeaponShopParser.parseSellPrice(HttpUtil.get("shopwep_co.php?goto=sell&wrap=" + w.getPosition()));
		if(price + PersonStatusService.money > 50000) {
			saveMoney();
			MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, types));
			HttpUtil.get("shopwep.php");
			HttpUtil.get("shopwep_co.php?goto=what&wrap=" + w.getPosition());
			WeaponShopParser.parseSellPrice(HttpUtil.get("shopwep_co.php?goto=sell&wrap=" + w.getPosition()));
		}
		HttpUtil.get("shopwep_updata.php?goto=sell&wrap=" + w.getPosition());
		PersonStatusService.weapons.remove(w);
		if(logger.isDebugEnabled()) {
			logger.debug("出售了" + w.getName());
		}
	}
	/******************************
	 * 
	 * @param code 铁剑代码：1101，钢枪代码：2111，铁枪代码：2101，勇剑代码：1232
	 */
	public static void buyWeapon(String code) {
		HttpUtil.get("shopwep.php");
		int price = WeaponShopParser.parseBuyPrice(HttpUtil.get("shopwep_co.php?goto=buy&buytype=0&weapon=" + code));
		if(price > PersonStatusService.money) {
			int currentLocation = PersonStatusService.currentLocation;
			drawCash(19000 + price);
			MoveService.moveTo(currentLocation);
			HttpUtil.get("shopwep.php");
			HttpUtil.get("shopwep_co.php?goto=buy&buytype=0&weapon=" + code);
		}
		String buyResponse = HttpUtil.get("shopwep_updata.php?goto=buy&buytype=0&weapon=" + code);
		try {
			String wrap = buyResponse.split("wraptablereset\\(", 2)[1].split("\\)", 2)[0];
			String position = wrap.split("wrap", 2)[1].split("'", 2)[0];
			String name = wrap.split("','")[2].split("'", 2)[0];
			int amountLeft = Integer.parseInt(wrap.split("','")[3].split("'", 2)[0]);
			MyWeapon w = new MyWeapon();
			w.setName(name);
			w.setPosition(position);
			w.setAmountLeft(amountLeft);
			PersonStatusService.weapons.add(w);
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, buyResponse, "解析购买武器错误！");
		}
	}
	public static void repairWeapon(MyWeapon w) {
		if(PersonStatusService.money < 2000) {
			drawCash(31000);
		}
		FacilityType[] types = null;
		if(PersonStatusService.memberCard) {
			types = new FacilityType[] {FacilityType.weaponshopE, FacilityType.weaponshopD, FacilityType.weaponshopC, FacilityType.weaponshopB};
		} else {
			types = new FacilityType[] {FacilityType.weaponshopE, FacilityType.weaponshopD, FacilityType.weaponshopC};
		}
		MoveService.moveToFacility(types);
		String positionCode = w.getPosition();
		HttpUtil.get("shopwep.php");
		FakeSleepUtil.sleep(1);
		HttpUtil.get("shopwep_co.php?goto=what&wrap=" + positionCode);
		int price = WeaponShopParser.parseRepairPrice(HttpUtil.get("shopwep_co.php?goto=repair&wrap=" + positionCode));
		if(price == 0) {
			return;
		}
		if(PersonStatusService.money < price) {
			drawCash(18000 + price);
		}
		String response = HttpUtil.get("shopwep_updata.php?goto=repair&wrap=" + positionCode);
		WeaponShopParser.parseAfterRepair(response);
		if(logger.isDebugEnabled()) {
			logger.debug("修理了" + w.getName());
		}
	}
	public static void sellItem(MyItem t) {
		FacilityType[] types = null;
		if(PersonStatusService.memberCard) {
			types = new FacilityType[] {FacilityType.itemshop, FacilityType.itemshopMember};
		} else {
			types = new FacilityType[] {FacilityType.itemshop};
		}
		MoveService.moveToFacility(types);
		HttpUtil.get("shopits.php");
		HttpUtil.get("shopits_co.php?goto=what&wrap=" + t.getPosition());
		HttpUtil.get("shopits_co.php?goto=sell&wrap=" + t.getPosition());
		String sellResponse = HttpUtil.get("shopits_updata.php?goto=sell&wrap=" + t.getPosition());
		PersonStatusService.money = Integer.parseInt(sellResponse.split("nowmoney','", 2)[1].split("'", 2)[0]);
		ItemParser.itemsAfterUse(HttpUtil.get("useitem.php"));
		if(logger.isDebugEnabled()) {
			logger.debug("出售了" + t.getName());
		}
		if(PersonStatusService.money > 30000) {
			saveMoney();
		}
	}
	public static int storeFullShards(String itemName, int fullShards) {
		int shards = 0;
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().equals(itemName)) {
				if(t.getAmountLeft() == fullShards) {
					FacilityService.storeItem(t.getPosition());
				} else {
					shards = t.getAmountLeft();
				}
			}
		}
		return shards;
	}
	
	public static void repairItem(MyItem t) {
		FacilityType[] facilities;
		if(PersonStatusService.memberCard) {
			facilities = new FacilityType[] {FacilityType.itemshop, FacilityType.itemshopMember};
		} else {
			facilities = new FacilityType[] {FacilityType.itemshop};
		}
		MoveService.moveToFacility(facilities);
		HttpUtil.get("shopits_co.php?goto=what&wrap=" + t.getPosition());
		String response = HttpUtil.get("shopits_co.php?goto=repair&wrap=" + t.getPosition());
		if(response.contains("新的")) {
			return;
		}
		Pattern p = Pattern.compile("([0-9]+)金币");
		Matcher m = p.matcher(response);
		if(!m.find()) {
			logger.debug("{}无法修理", t.getName());
			return;
		}
		int price = Integer.parseInt(m.group(1));
		if(price > PersonStatusService.money) {
			drawCash(48000);
			MoveService.moveToFacility(facilities);
			HttpUtil.get("shopits_co.php?goto=what&wrap=" + t.getPosition());
			HttpUtil.get("shopits_co.php?goto=repair&wrap=" + t.getPosition());
		}
		response = HttpUtil.get("shopits_updata.php?goto=repair&wrap=" + t.getPosition());
		logger.debug("修理了{}", t.getName());
		// 游戏中不会调用这个，导致道具快捷栏不准确
		ItemParser.itemsAfterUse(HttpUtil.get("useitem.php"));
	}
	
	public static void repaireAllItems(List<MyItem> myItems) {
		for(MyItem t: myItems) {
			repairItem(t);
		}
	}
	
	public static boolean mercenary(int num) {
		MoveService.moveToFacility(FacilityType.inn);
		if(PersonStatusService.mahua < 5) {
			return false;
		}
		HttpUtil.get("shoptap.php");
		String innTell = HttpUtil.get("shoptap_wi.php?goto=sol");
		if(!innTell.contains("雇佣一个佣兵")) {
			return false;
		}
		for(int i = 0; i < num; i++) {
			innTell = HttpUtil.get("shoptap_co.php?goto=sol&type=1");
			if(!innTell.startsWith("这家伙归你了")) {
				return false;
			}
			logger.debug("雇佣了一个佣兵。");
			PersonStatusService.mahua -= 5;
			if(PersonStatusService.mahua < 5) {
				return false;
			}
		}
		return true;
	}
	public static boolean mercenaryTen() {
		MoveService.moveToFacility(FacilityType.inn);
		if(PersonStatusService.mahua < 45) {
			return false;
		}
		HttpUtil.get("shoptap.php");
		String innTell = HttpUtil.get("shoptap_wi.php?goto=sol");
		if(!innTell.contains("雇佣十个佣兵")) {
			logger.warn("酒馆没有雇佣是个佣兵选项！\n{}", innTell);
			return false;
		}
		innTell = HttpUtil.get("shoptap_co.php?goto=sol&type=1&num=10");
		if(!innTell.startsWith("这帮家伙归你了")) {
			return false;
		}
		logger.debug("雇佣了10个佣兵。");
		PersonStatusService.mahua -= 45;
		return true;
	}
}
