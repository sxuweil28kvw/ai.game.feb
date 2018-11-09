package leigh.ai.game.feb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import leigh.ai.game.feb.parsers.BattleResultParser;
import leigh.ai.game.feb.parsers.ParserExceptionHandler;
import leigh.ai.game.feb.parsers.PersonStatusParser;
import leigh.ai.game.feb.parsers.WeaponShopParser;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.service.map.Traffic;
import leigh.ai.game.feb.service.raid.RaidMapType;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.service.status.MyStatus.MyWeapon;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		FakeSleepUtil.sleep(3, 5);
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
		return BattleResultParser.parse(HttpUtil.get("battle.php?suodi=elite&Bout=" + turns));
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
		if(!haveHolywater) {
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
		int healHp = PersonStatusService.maxHP - PersonStatusService.HP;
		if(healHp <= 0) {
			return true;
		}
		boolean haveMedicine = false;
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().equals("回复之杖")) {
				if(JobService.canUseStaff()) {
					haveMedicine = true;
					useStaff(t);
					break;
				}
			}
			if(t.getName().equals("伤药") || t.getName().equals("万灵药")) {
				haveMedicine = true;
				BattleService.useItem(t);
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
		PersonStatusParser.itemsAfterUse(HttpUtil.get("useitem.php"));
	}
	public static boolean addAp() {
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().endsWith("之杖") && JobService.canUseStaff()) {
				useStaff(t);
				return true;
			}
			if(t.getName().equals("圣水") || t.getName().equals("烧酒") || t.getName().equals("卡博雷酒")) {
				BattleService.useItem(t.getPosition());
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
		if(!RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation)) {
			Set<Integer> value = new HashSet<Integer>();
			value.add(RaidService.myPosition);
			RaidService.deadEnemies.put(PersonStatusService.currentLocation, value);
		} else {
			RaidService.deadEnemies.get(PersonStatusService.currentLocation).add(RaidService.myPosition);
		}
	}
	public static void openChest(int userId, String username) {
		String chestResponse = HttpUtil.get("raid_binn.php");
		String chestItem = null;
		try {
			chestItem = chestResponse.split("发现了", 2)[1].split("<br>", 2)[0];
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, chestResponse, "解析开宝箱失败！");
		}
		HttpUtil.get("raid_binn.php?goto=getit&gid=" + userId);
		HttpUtil.get("move.php?display=1");
		if(logger.isInfoEnabled()) {
			logger.info("宝箱开到" + chestItem + ", 分配给了" + username);
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
	
	public static void valkyrieTa6() {
		
	}
	public static void battleTa5Turn5() {
		BattleInfo battleInfo = RaidService.battle(5);
		
	}
}
