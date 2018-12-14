package leigh.ai.game.feb.service;

import leigh.ai.game.feb.parsers.MoveParser;
import leigh.ai.game.feb.parsers.PersonStatusParser;
import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.map.MapPath;
import leigh.ai.game.feb.service.map.Traffic;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveService {
	private static Logger logger = LoggerFactory.getLogger(MoveService.class);
	public enum BattleConfig {
		always,
		random,
		never;
	}
	public static void movePath(MapPath path) {
		movePath(path, BattleConfig.never);
	}
	public static void movePath(MapPath path, BattleConfig battleConfig) {
		if(path == null) {
			return;
		}
		while(path.getNext() != null) {
			switch(battleConfig) {
			case always:
				battleMove(path.getNext().getCode(), path.getTraffic());
				break;
			case random:
				double r = Math.random();
				if(r < 0.25) {
					battleMove(path.getNext().getCode(), path.getTraffic());
				} else {
					oneMove(path.getNext().getCode(), path.getTraffic());
				}
			case never:
				oneMove(path.getNext().getCode(), path.getTraffic());
				break;
			}
			path = path.getNext();
			FakeSleepUtil.sleep(3, 5);
		}
	}

	private static void battleMove(Integer code, Traffic traffic) {
		//TODO: 
	}

	public static String oneMove(Integer code, Traffic traffic) {
		String moveResult = null;
		if(logger.isDebugEnabled()) {
			logger.debug("moving to " + code + ":" + MapService.map.get(code).getName() + ", 交通方式：" + traffic);
		}
		switch(traffic) {
		case walk:
			moveResult = HttpUtil.get("move.php?mov=" + code);
			MoveParser.parseMove(moveResult);
			break;
		case ship:
			if(PersonStatusService.money < 200) {
				int currentLocation = PersonStatusService.currentLocation;
				FacilityService.drawCash(19000);
				MoveService.moveTo(currentLocation);
			}
			HttpUtil.get("shopship.php");
			HttpUtil.get("shopship_wi.php?shipto=" + code);
			HttpUtil.get("shopship_updata.php?shipto=" + code);
			moveResult = HttpUtil.get("move.php?display=1");
			PersonStatusParser.afterMove(moveResult);
			break;
		case fly:
			if(PersonStatusService.AP < 25) {
				BattleService.addAp(true);
				MoveService.movePath(MapService.findFacilityExceptTraffics(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.airport}, Traffic.fly));
			}
			HttpUtil.get("nwes_fly.php?goto=flya");
			HttpUtil.get("nwes_fly.php?goto=flyb&flytype=&wrap=&maintext=" + code);
			moveResult = HttpUtil.get("move.php");
			MoveParser.parseMove(moveResult);
			if(PersonStatusService.AP < 10) {
				BattleService.addAp();
			}
			MoveService.moveTo(code);
			break;
		case raid_exit:
			HttpUtil.get("raid_exit.php");
			moveResult = HttpUtil.get("move.php?display=1");
			PersonStatusParser.afterMove(moveResult);
//			RaidService.myPosition = 0;
			break;
		case border:
			String npcid = "222";
			HttpUtil.get("npc.php?npcid=" + npcid);
			HttpUtil.get("npc.php?npcid=" + npcid + "&act=goto");
			moveResult = HttpUtil.get("move.php?display=1");
			PersonStatusParser.afterMove(moveResult);
			break;
		case crack:
			npcid = "102";
			if(PersonStatusService.currentLocation == 2001) {
				npcid = "010";
			}
			HttpUtil.get("npc.php?npcid=" + npcid);
			HttpUtil.get("npc.php?npcid=" + npcid + "&act=crack");
			HttpUtil.get("npc.php?npcid=" + npcid + "&act=crackok");
			moveResult = HttpUtil.get("move.php?display=1");
			PersonStatusParser.afterMove(moveResult);
			break;
		default:
			moveResult = "";
			break;
		}
		if(PersonStatusService.currentLocation != code) {
			logger.error("移动失败！");
			System.exit(1);
		}
		return moveResult;
	}
	public static void moveTo(int target) {
		movePath(MapService.findPath(PersonStatusService.currentLocation, target));
	}
	public static boolean enterTemple() {
		MoveService.moveTo(1130);
		String npcSaid = HttpUtil.get("npc.php?npcid=112");
		
		return true;
	}
	public static boolean enterTower() {
		MoveService.moveTo(1114);
		String npctell = HttpUtil.get("npc.php?npcid=221&act=Q0_9999B");
		if(!npctell.contains("原来如此，那么你可以进入威鲁尼塔了")) {
			return false;
		}
		HttpUtil.get("move.php?display=1");
		PersonStatusService.currentLocation = -1;
		RaidService.myPosition = 0;
		FakeSleepUtil.sleep(2, 3);
		logger.debug("进入威鲁尼塔1层");
		return true;
	}
	public static boolean enterTower(int floor) {
		if(PersonStatusService.mahua < (floor - 1) * 3) {
			logger.warn("身上只有{}麻花，不够坐电梯到{}层", PersonStatusService.mahua, floor);
			return false;
		}
		MoveService.moveTo(1114);
		String npctell = HttpUtil.get("npc.php?npcid=221");
		if(!npctell.contains("Q0_9999C&mapid=" + (4000 + floor))) {
			logger.warn("进塔出错：{}层的电梯未开", floor);
			return false;
		}
		HttpUtil.get("npc.php?npcid=221&act=Q0_9999C&mapid=" + (4000 + floor));
		HttpUtil.get("move.php?display=1");
		PersonStatusService.currentLocation = -floor;
		RaidService.myPosition = 0;
		FakeSleepUtil.sleep(2, 3);
		logger.info("应该进入了威鲁尼塔{}层", floor);
		return true;
	}
	public static void moveToFacility(FacilityType... types) {
		movePath(MapService.findFacility(PersonStatusService.currentLocation, types));
	}
}
