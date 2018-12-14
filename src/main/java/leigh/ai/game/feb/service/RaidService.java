package leigh.ai.game.feb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.parsers.BattleResultParser;
import leigh.ai.game.feb.parsers.ItemParser;
import leigh.ai.game.feb.parsers.ParserExceptionHandler;
import leigh.ai.game.feb.parsers.RaidParser;
import leigh.ai.game.feb.parsers.WeaponShopParser;
import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.service.battle.BattleResult;
import leigh.ai.game.feb.service.map.Traffic;
import leigh.ai.game.feb.service.raid.RaidBattleData;
import leigh.ai.game.feb.service.raid.RaidMapType;
import leigh.ai.game.feb.service.raid.RaidStopReason;
import leigh.ai.game.feb.service.status.Item;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.service.status.MyStatus.MyWeapon;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

public class RaidService {
	private static Logger logger = LoggerFactory.getLogger(RaidService.class);
	/**************
	 * 在当前一层的位置。注意是从0开始，因此所在的格子是myPosition+1格。
	 */
	public static int myPosition = 0;
	public static Map<Integer, List<RaidMapType>> raidMap;
	public static Map<Integer, Set<Integer>> deadEnemies = new HashMap<Integer, Set<Integer>>();
	static {
		try {
			initRaidMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void forceMove() {
		HttpUtil.get("move.php?goto=mov");
	}
	public static void move() {
		String moveResponse = HttpUtil.get("move.php?goto=mov");
		try {
			Document doc = Jsoup.parse(moveResponse);
			Element statusIframe = doc.body().child(0).child(0);
			String statuses = statusIframe.attr("onload");
			PersonStatusService.AP = Integer.parseInt(statuses.split("j_ap','", 2)[1].split("'", 2)[0]);
			PersonStatusService.HP = Integer.parseInt(statuses.split("j_hp','", 2)[1].split("'", 2)[0]);
			PersonStatusService.maxHP = Integer.parseInt(statuses.split("j_mhp','", 2)[1].split("'", 2)[0]);
			
			String moveResult = doc.body().child(1).child(0).child(0).child(0).child(0).child(0).child(0).child(0).child(0).child(0)
					.child(1).child(1).child(0).child(0).child(2).child(0).html();
			String[] splByBr = moveResult.split("<br />");
			if(splByBr[2].startsWith("进入了")) {
				String location = splByBr[2].substring(3);
				PersonStatusService.currentLocation = MapService.nameLookup.get(location);
				myPosition = 0;
				if(logger.isDebugEnabled()) {
					logger.debug("进入了" + location);
				}
			} else if(splByBr[2].equals("你已经被一个敌人盯上了")) {
				logger.debug("无法移动：你已经被一个敌人盯上了。");
			} else {
				int steps = Integer.parseInt(splByBr[1].substring(3,4));
				boolean stopped = false;
				f:
				for(int i = 1; i < steps; i++) {
					RaidMapType mapNode = 
							raidMap.get(PersonStatusService.currentLocation).get(myPosition + i);
					if(raidMap.get(PersonStatusService.currentLocation).size() == myPosition + i + 1) {
						stopped = true;
						myPosition += i;
						if(logger.isDebugEnabled()) {
							logger.debug("骰子投出了" + steps + ",但被挡在第" + (myPosition + 1) + "格。");
						}
						break f;
					}
					switch(mapNode) {
					case stopingEnemy:
					case stopingNpc:
					case door:
					case chest:
						if(deadEnemies.containsKey(PersonStatusService.currentLocation)
								&& deadEnemies.get(PersonStatusService.currentLocation).contains(myPosition + i)) {
							continue f;
						}
						myPosition += i;
						stopped = true;
						if(logger.isDebugEnabled()) {
							logger.debug("骰子投出了" + steps + ",但被挡在第" + (myPosition + 1) + "格。");
						}
						break f;
					default:
						continue f;
					}
				}
				if(!stopped) {
					myPosition += steps;
					if(logger.isDebugEnabled()) {
						logger.debug("骰子投出了" + steps + ",移动到第" + (myPosition + 1) + "格。");
					}
				}
			}
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, moveResponse, "解析副本移动结果失败！");
		}
		FakeSleepUtil.sleep(3, 4);
	}
	private static void initRaidMap() throws IOException {
		raidMap = new HashMap<Integer, List<RaidMapType>>();
		BufferedReader br = new BufferedReader(new InputStreamReader(MapService.class.getClassLoader().getResourceAsStream("data/map/raid.map"), "utf8"));
		String line = br.readLine();
		while(line != null) {
			if(line.equals("")) {
				continue;
			}
			String[] spl1 = line.split(":", 2);
			if(spl1.length < 2) {
				continue;
			}
			Integer code = Integer.parseInt(spl1[0]);
			String[] spl2 = spl1[1].split(",");
			List<RaidMapType> list = new ArrayList<RaidMapType>(spl2.length);
			for(int i = 0; i < spl2.length; i++) {
				list.add(RaidMapType.valueOf(spl2[i]));
			}
			raidMap.put(code, list);
			line = br.readLine();
		}
	}
	public static BattleInfo battle(int turns) {
		String s = HttpUtil.get("battle.php?suodi=elite&Bout=" + turns);
		FakeSleepUtil.sleep(3, 4);
		return BattleResultParser.parse(s);
		
	}
	
	public static void readyForGuya() {
//		LoginService.login();
		FacilityService.repairWeapon(0);
		boolean haveMedicine = false;
		boolean haveHolywater = false;
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().equals("伤药") || t.getName().equals("万灵药")) {
				haveMedicine = true;
			} else if(t.getName().equals("回复之杖")) {
				if(JobService.canUseStaff()) {
					haveMedicine = true;
					haveHolywater = true;
				}
			} else if(t.getName().equals("圣水") || t.getName().equals("烧酒") || t.getName().equals("卡博雷酒")) {
				haveHolywater = true;
			}
		}
		if(!haveMedicine) {
			BattleService.buyMedicine();
		}
		if(!JobService.canUseStaff() && !haveHolywater) {
			BattleService.buyHolywater();
		}
		
		if(PersonStatusService.HP < PersonStatusService.maxHP) {
			BattleService.selfHeal();
		}
		if(PersonStatusService.AP < 10) {
			BattleService.addAp();
		}
	}
	public static void exit() {
		MoveService.moveTo(MapService.map.get(PersonStatusService.currentLocation).getNeighbours(Traffic.raid_exit).iterator().next().getCode());
	}
	public static boolean selfHeal() {
		PersonStatusService.update();
		int healHp = PersonStatusService.maxHP - PersonStatusService.HP;
		if(healHp <= 0) {
			return true;
		}
		boolean haveMedicine = false;
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().equals("回复之杖") || t.getName().equals("治疗之杖")
					|| t.getName().equals("痊愈之杖")) {
				if(JobService.canUseStaff()) {
					haveMedicine = true;
					useStaff(t);
					break;
				}
			}
			if(t.getName().equals("伤药") || t.getName().equals("万灵药")) {
				haveMedicine = true;
				ItemService.useItem(t);
				break;
			} 
		}
		if(!haveMedicine) {
			return false;
		}
		return selfHeal();
	}
	public static void useStaff(MyItem t) {
		String useStaffResult = HttpUtil.get("useitem_heal.php?goto=teamuse&wrap=" + t.getPosition() + "&maintext=" + PersonStatusService.userId);
		try {
			PersonStatusService.HP = Integer.parseInt(useStaffResult.split("'j_hp','")[1].split("'", 2)[0]);
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, useStaffResult, "解析副本用杖结果错误：");
		}
		ItemParser.itemsAfterUse(HttpUtil.get("useitem.php"));
	}
	public static boolean addAp() {
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().endsWith("之杖") && JobService.canUseStaff()) {
				useStaff(t);
				return true;
			}
			if(t.getName().equals("圣水") || t.getName().equals("烧酒") || t.getName().equals("卡博雷酒")) {
				ItemService.useItem(t.getPosition());
				return true;
			}
		}
		return false;
	}
	public static void ta5() {
		selfHeal();
		for(int i = 0; i < 14; i++) {
			HttpUtil.get("npc.php?npcid=301");
			String st = HttpUtil.get("npc.php?npcid=301&act=ST");
			String answer = null;
			try {
				String[] stspl = st.split("febimg/img4/j");
				answer = stspl[1].split(".gif", 2)[0] + stspl[2].split(".gif", 2)[0] + stspl[3].split(".gif", 2)[0] + stspl[4].split(".gif", 2)[0];
			} catch(Exception e) {
				ParserExceptionHandler.handle(e, st, "解析人影失败！");
			}
			FakeSleepUtil.sleep(3, 4);
			HttpUtil.get("febo/npc.php?npcid=301&act=ST2");
			FakeSleepUtil.sleep(1, 3);
			HttpUtil.get("npc.php?npcid=301&act=ST3&maintext=" + answer);
		}
		logger.info("塔5答题完成。");
	}
	public static void openDoor() {
		HttpUtil.get("raid_door.php");
		HttpUtil.get("move.php?display=1");
		logger.debug("开门");
		addDeadPosition();
	}
	public static void openChest(int userId, String username) {
		String chestResponse = HttpUtil.get("raid_binn.php");
		String chestItem = null;
		try {
			chestItem = chestResponse.split("发现了", 2)[1].split("<br>", 2)[0];
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, chestResponse, "解析开宝箱失败！");
			return;
		}
		HttpUtil.get("raid_binn.php?goto=getit&gid=" + userId);
		HttpUtil.get("move.php?display=1");
		if(logger.isInfoEnabled()) {
			logger.info("宝箱开到" + chestItem + ", 分配给了" + username);
		}
		addDeadPosition();
	}
	public static void addDeadPosition() {
		if(!RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation)) {
			Set<Integer> value = new HashSet<Integer>();
			value.add(RaidService.myPosition);
			RaidService.deadEnemies.put(PersonStatusService.currentLocation, value);
		} else {
			RaidService.deadEnemies.get(PersonStatusService.currentLocation).add(RaidService.myPosition);
		}
	}
	public static int determinTurns() {
		int turns = 5;
		if(PersonStatusService.myjob.equals("刺客") || PersonStatusService.myjob.equals("密探")) {
			if(PersonStatusService.currentLocation == -2 && RaidService.myPosition == 17) {
				turns = 3;
			} else if(PersonStatusService.currentLocation == -4 && RaidService.myPosition == 13) {
				turns = 3;
			} else if(PersonStatusService.currentLocation == -4 && RaidService.myPosition >= 20) {
				turns = 1;
			}
		}
		return turns;
	}
	public static void repairAllWeapons() {
		HttpUtil.get("shopwep.php");
		for(MyWeapon w: PersonStatusService.weapons) {
			FakeSleepUtil.sleep(1);
			HttpUtil.get("shopwep_co.php?goto=what&wrap=" + w.getPosition());
			String response = HttpUtil.get("shopwep_updata.php?goto=repair&wrap=" + w.getPosition());
			WeaponShopParser.parseAfterRepair(response);
			if(logger.isDebugEnabled()) {
				logger.debug("修理了" + w.getName());
			}
		}
	}
	
	/*****************
	 * 移动到至少（maxPosition+1）格。注意position是从0开始的。
	 * 如：在塔6执行moveUntil(23)则会移动到第24格，或越过24格到达25格为止。
	 * @param maxPosition
	 * @return false表示中途被挡住。
	 */
	public static boolean moveNoBattleUntil(int maxPosition) {
		int originPosition = RaidService.myPosition;
		while(RaidService.myPosition < maxPosition) {
			RaidService.move();
			if(RaidService.myPosition == originPosition) {
				return false;
			}
		}
		return true;
	}
	public static void moveNoBattleUntil(int location, int position) {
		if(PersonStatusService.currentLocation > 0) {
			throw new IllegalStateException("无法移动：不在副本中！");
		}
		if(PersonStatusService.currentLocation < 0 && PersonStatusService.currentLocation < location) {
			logger.warn("所在层数{}高于目标层数{}！", PersonStatusService.currentLocation, location);
		}
		while(PersonStatusService.currentLocation > location) {
			RaidService.move();
		}
		while(RaidService.myPosition < position) {
			RaidService.move();
		}
	}
	public static void recallTa6Monsters() {
		Set<Integer> corpses = RaidService.deadEnemies.get(-6);
		for(int i = 1; i < 24; i++) {
			if(!RaidService.raidMap.get(-6).get(i).equals(RaidMapType.chest)) {
				corpses.remove(i);
			}
		}
	}
	public static int firstEnemyPosition(int location) {
		List<RaidMapType> map = RaidService.raidMap.get(location);
		for(int i = 0; i < map.size(); i++) {
			if(!map.get(i).equals(RaidMapType.enemy) && !map.get(i).equals(RaidMapType.stopingEnemy)) {
				continue;
			}
			if(!RaidService.deadEnemies.containsKey(location)) {
				return i;
			}
			if(RaidService.deadEnemies.get(location).contains(i)) {
				continue;
			} else {
				return i;
			}
		}
		return -1;
	}
	/************
	 * 在副本外检查圣水
	 */
	public static void ensureHolywaterOutside() {
		List<MyItem> holywater = new LinkedList<MyItem>();
		int totalAmount = 0;
		for(MyItem t: PersonStatusService.items) {
			if(t.getName().equals("圣水")) {
				holywater.add(t);
				totalAmount += t.getAmountLeft();
			}
		}
		if(PersonStatusService.memberCard && totalAmount < 25) {
			if(PersonStatusService.money > 50000 - totalAmount * 150) {
				FacilityService.saveMoney();
			}
			MoveService.moveToFacility(FacilityType.itemshopMember);
			if(holywater.size() > 0) {
				for(MyItem t: holywater) {
					FacilityService.sellItem(t);
				}
			}
			if(PersonStatusService.items.size() == 5) {
				for(MyItem t: PersonStatusService.items) {
					if(t.getName().equals("伤药") || t.getName().equals("万灵药")
							|| t.getName().equals(Item.E杖.getName())
							|| t.getName().equals(Item.D杖.getName())
							|| t.getName().equals(Item.C杖.getName())) {
						continue;
					}
					FacilityService.storeItem(t.getPosition());
				}
			}
			if(PersonStatusService.items.size() == 5) {
				FacilityService.storeItem("1");
			}
			FacilityService.buyItem(Item.会员圣水.getCode());
		} else if(!PersonStatusService.memberCard && totalAmount < 3){
			MoveService.moveToFacility(FacilityType.itemshop);
			if(holywater.size() > 0) {
				for(MyItem t: holywater) {
					FacilityService.sellItem(t);
				}
			}
			if(PersonStatusService.items.size() == 5) {
				for(MyItem t: PersonStatusService.items) {
					if(t.getName().equals("伤药") || t.getName().equals("万灵药")
							|| t.getName().equals(Item.E杖.getName())
							|| t.getName().equals(Item.D杖.getName())
							|| t.getName().equals(Item.C杖.getName())) {
						continue;
					}
					FacilityService.storeItem(t.getPosition());
				}
			}
			if(PersonStatusService.items.size() == 5) {
				FacilityService.storeItem("1");
			}
			FacilityService.buyItem(Item.小圣水.getCode());
		}
	}
	public static boolean buy5Staff() {
		PersonStatusService.update();
		boolean atBank = false;
		List<MyItem> cStaffs = new LinkedList<MyItem>();
		Item bestStaff = PersonStatusService.getBestStaff();
		for(MyItem item: PersonStatusService.items) {
			if(!item.getName().equals(bestStaff.getName())) {
				if(!atBank) {
					MoveService.moveToFacility(FacilityType.bank);
					atBank= true;
				}
				if(!FacilityService.storeItem(item.getPosition())) {
					return false;
				}
			} else {
				cStaffs.add(item);
			}
		}
		List<MyItem> staffsToSell = new ArrayList<MyItem>(cStaffs.size());
		int sellMoney = 0;
		for(MyItem staff: cStaffs) {
			if(staff.getAmountLeft() < 14) {
				staffsToSell.add(staff);
				sellMoney += staff.getAmountLeft() * 75;
			}
		}
		if(PersonStatusService.money < 8000) {
			if(!atBank) {
				MoveService.moveToFacility(FacilityType.bank);
				atBank= true;
			}
			FacilityService.drawCash(19000 - sellMoney + 1125 * (5 - cStaffs.size() + staffsToSell.size()));
		}
		MoveService.moveToFacility(FacilityType.itemshop);
		for(MyItem staff: staffsToSell) {
			FacilityService.sellItem(staff);
		}
		for(int i = cStaffs.size() - staffsToSell.size(); i < 5; i++) {
			FacilityService.buyItem(bestStaff.getCode());
		}
		return true;
	}
	public static RaidStopReason raidBattle() {
		BattleInfo result;
		do {
			RaidBattleData data = RaidParser.parseBattleData(HttpUtil.get("pve.php"));
			
			int enemyMaxDmg1t = data.getEnemyMaxDmg1t();
			if(enemyMaxDmg1t >= PersonStatusService.maxHP) {
				//what to do?
			} else if(enemyMaxDmg1t >= data.getMyHp()) {
				if(!RaidService.selfHeal()) {
					return RaidStopReason.noHeal;
				}
				data = RaidParser.parseBattleData(HttpUtil.get("pve.php"));
			}
			
			if(PersonStatusService.AP < 10) {
				if(!RaidService.addAp()) {
					return RaidStopReason.noAp;
				}
			}
			
			int turns;
			
			if(data.isOneTurn()) {
				turns = 5;
			} else if(enemyMaxDmg1t == 0) {
				turns = 5;
			} else {
				turns = data.getMyHp() / enemyMaxDmg1t;
			}
			
			if(!RaidService.ensureWeapon()) {
				return RaidStopReason.noWeapon;
			}
			result = battle(turns);
		} while(!result.getResult().equals(BattleResult.win));
		return null;
	}
	/****************
	 * 副本中检查是否还有武器的方法；当前武器无耐久时切换剩余武器。
	 * @return
	 */
	public static boolean ensureWeapon() {
		PersonStatusService.update();
		if(PersonStatusService.weapons.get(0).getAmountLeft() > 0) {
			return true;
		}
		for(int i = 1; i < PersonStatusService.weapons.size(); i++) {
			if(PersonStatusService.weapons.get(i).getAmountLeft() > 0) {
				PersonStatusService.equipWeapon(PersonStatusService.weapons.get(i));
				return true;
			}
		}
		return false;
	}

}
