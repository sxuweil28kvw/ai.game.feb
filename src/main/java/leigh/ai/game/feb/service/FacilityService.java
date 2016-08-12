package leigh.ai.game.feb.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leigh.ai.game.feb.parsers.BankParser;
import leigh.ai.game.feb.parsers.PersonStatusParser;
import leigh.ai.game.feb.parsers.WeaponShopParser;
import leigh.ai.game.feb.service.MyStatus.MyItem;
import leigh.ai.game.feb.service.map.MapPath;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	}
	public static Map<FacilityType, List<Integer>> map;
	static {
		Integer[] itemshops = new Integer[] {1152, 1137, 1128, 1123, 1122, 1119, 1160, 1113, 1204, 1156, 1102, 1107,
				1183, 1179, 1194};
		Integer[] weaponShopes = new Integer[] {1122, 1160, 1113, 1107, 1194};
		Integer[] weaponShopds = new Integer[] {1128, 1149, 1204, 1142, 1183, };
		Integer[] weaponShopcs = new Integer[] {1152, 1137, 1119, 1198, 1156, 1102, 1179, };
		Integer[] banks = new Integer[] {1152, 1119, 1198, 1156, 1102, 1179, 1161};
		Integer[] inns = new Integer[] {1137, 1128, 1123, 1122, 1149, 1160, 1113, 1198, 1204, 1142, 1107, 1183,
				1194, 1161};
		map = new HashMap<FacilityType, List<Integer>>();
		map.put(FacilityType.itemshop, Arrays.asList(itemshops));
		map.put(FacilityType.weaponshopE, Arrays.asList(weaponShopes));
		map.put(FacilityType.weaponshopD, Arrays.asList(weaponShopds));
		map.put(FacilityType.weaponshopC, Arrays.asList(weaponShopcs));
		map.put(FacilityType.bank, Arrays.asList(banks));
		map.put(FacilityType.inn, Arrays.asList(inns));
		map.put(FacilityType.weaponshopB, Arrays.asList(new Integer[] {1161}));
		map.put(FacilityType.itemshopMember, Arrays.asList(new Integer[] {1198}));
	}
	public static void saveMoney() {
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
		MapPath path = MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.weaponshopE, FacilityType.weaponshopD, FacilityType.weaponshopC});
		MoveService.movePath(path);
		String positionCode = position == 0 ? "E" : (position + "");
		HttpUtil.get("shopwep.php");
		FakeSleepUtil.sleep(2);
		HttpUtil.get("shopwep_co.php?goto=what&wrap=" + positionCode);
		HttpUtil.get("shopwep_co.php?goto=repair&wrap=" + positionCode);
		String response = HttpUtil.get("shopwep_updata.php?goto=repair&wrap=" + positionCode);
		WeaponShopParser.parseAfterRepair(response);
	}
	public static boolean hasFacility(int currentLocation, FacilityType type) {
		return map.get(type).contains(currentLocation);
	}
	public static void drawCash(int targetMoney) {
		MapPath path = MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.bank});
		MoveService.movePath(path);
		HttpUtil.get("shopbanka.php");
		HttpUtil.get("shopbanka_wi.php");
		HttpUtil.get("shopbankc.php");
		HttpUtil.get("shopbankc_wi.php?goto=msgs");
		int mahua = (targetMoney - PersonStatusService.money) / 1000 + 1;
		HttpUtil.get("shopbankc_updata.php?goto=msgs&maintext=" + mahua);
		if(logger.isDebugEnabled()) {
			logger.debug("将" + mahua + "麻花兑换为金钱");
		}
		PersonStatusService.update();
	}
	public static void buyItem(String itemcode) {
		HttpUtil.get("shopits_co.php?goto=buy&buytype=0&item=" + itemcode);
		HttpUtil.get("shopits_updata.php?goto=buy&buytype=0&item=" + itemcode);
		PersonStatusParser.itemsAfterUse(HttpUtil.get("useitem.php"));
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
		PersonStatusParser.itemsAfterUse("useitem.php");
		return true;
	}
	
}
