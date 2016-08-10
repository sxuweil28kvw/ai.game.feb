package leigh.ai.game.feb.service;

import leigh.ai.game.feb.parsers.TagangMissionParser;
import leigh.ai.game.feb.service.MoveService.BattleConfig;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.service.battle.BattleResult;
import leigh.ai.game.feb.service.map.MapPath;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagangMissionService {
	private static final Logger logger = LoggerFactory.getLogger(TagangMissionService.class);
	public static int mowuLocation;
	public static int mojiangLocation;
	public static MissionStatus mowuMissionStatus;
	public static MissionStatus mojiangMissionStatus;
	public static void takeMission() {
		checkMission();
		if(mowuMissionStatus.equals(MissionStatus.cooldown) && mojiangMissionStatus.equals(MissionStatus.cooldown)) {
			logger.info("塔港任务冷却中！");
			return;
		}
		if(!mowuMissionStatus.equals(MissionStatus.untake) && !mojiangMissionStatus.equals(MissionStatus.untake)) {
			return;
		}
		if(PersonStatusService.currentLocation != 1161) {
			//去塔港
			MapPath path1 = MapService.findPath(PersonStatusService.currentLocation, 1161);
			logger.debug(path1.toString());
			MoveService.movePath(path1, BattleConfig.never);
		}
		HttpUtil.get("npc.php?npcid=303B");
		String tagangMissionResponse = HttpUtil.get("npc.php?npcid=303B&act=mist");
		int[] tagangMissionPositions = TagangMissionParser.parse(tagangMissionResponse);
		if(logger.isInfoEnabled()) {
			logger.info("魔将在" + tagangMissionPositions[1] + ":" + MapService.map.get(tagangMissionPositions[1]).getName() + 
					"，魔物在" + tagangMissionPositions[0] + ":" + MapService.map.get(tagangMissionPositions[0]).getName());
		}
		mowuLocation = tagangMissionPositions[0];
		mojiangLocation = tagangMissionPositions[1];
		checkMission();
	}
	public static void fightMowuOrMojiang(int location) {
		for(int i = 0; i < 15; i++) {
			BattleService.readyForBattle();
			MoveService.movePath(MapService.findPath(PersonStatusService.currentLocation, location));
			int level = BattleService.searchUntilEnemy();
			logger.debug("魔物/魔将等级=" + level);
			FakeSleepUtil.sleep(3, 5);
			BattleInfo battleInfo = BattleService.fight(level);
			logger.debug("塔港任务战斗结果：" + battleInfo.getResult());
			FakeSleepUtil.sleep(3);
			if(battleInfo.getResult().equals(BattleResult.win)) {
				return;
			}
		}
		logger.debug("15次没打赢，放弃任务……");
	}
	/*
	public static void doMissions(int[] tagangMissionPositions) {
		MapPath ta2Mowu = MapService.findPath(1161, tagangMissionPositions[0]);
		MapPath ta2Mojiang = MapService.findPath(1161, tagangMissionPositions[1]);
		MapPath mowu2mojiang = MapService.findPath(tagangMissionPositions[0], tagangMissionPositions[1]);
		
		if(mowu2mojiang.getPathLength() > ta2Mowu.getPathLength() + ta2Mojiang.getPathLength()) {
			MoveService.movePath(ta2Mowu, BattleConfig.never);
			TagangMissionService.fightMowuOrMojiang();
			MoveService.movePath(MapService.reverseMapPath(ta2Mowu), BattleConfig.never);
			MoveService.movePath(ta2Mojiang, BattleConfig.never);
			TagangMissionService.fightMowuOrMojiang();
			MoveService.movePath(MapService.reverseMapPath(ta2Mojiang), BattleConfig.never);
		} else {
			MoveService.movePath(ta2Mowu, BattleConfig.never);
			TagangMissionService.fightMowuOrMojiang();
			MoveService.movePath(mowu2mojiang, BattleConfig.never);
			TagangMissionService.fightMowuOrMojiang();
			MoveService.movePath(MapService.reverseMapPath(ta2Mojiang), BattleConfig.never);
		}
		
		HttpUtil.get("npc.php?npcid=303B");
		HttpUtil.get("npc.php?npcid=303B&act=mist");
	}
	*/
	public static void checkMission() {
		HttpUtil.get("mission.php");
		String tagangMissionResponse = HttpUtil.get("mist.php");
		Document doc = Jsoup.parse(tagangMissionResponse);
		Element table = doc.getElementsByTag("table").get(0);
		String mowuContent = table.child(0).child(0).child(0).html();
		String mojiangContent = table.child(0).child(2).child(0).html();
		if(logger.isDebugEnabled()) {
			logger.debug("Mowu mission content=" + mowuContent);
			logger.debug("Mojiang mission content=" + mojiangContent);
		}
		if(mowuContent.contains("已经可以再") || mowuContent.contains("已超时")) {
			mowuMissionStatus = MissionStatus.untake;
		} else if(mowuContent.contains("执行中，剩余时间")) {
			mowuMissionStatus = MissionStatus.taken;
			mowuLocation = MapService.nameLookup.get(mowuContent.split(" 的委托", 2)[0].split("来自 ", 2)[1].trim());
		} else if(mowuContent.contains("已完成，剩余时间")) {
			mowuMissionStatus = MissionStatus.killed;
		} else {// *分钟后可以再接到任务
			mowuMissionStatus = MissionStatus.cooldown;
		}
		if(mojiangContent.contains("已经可以再") || mojiangContent.contains("已超时")) {
			mojiangMissionStatus = MissionStatus.untake;
		} else if(mojiangContent.contains("执行中，剩余时间")) {
			mojiangMissionStatus = MissionStatus.taken;
			mojiangLocation = MapService.nameLookup.get(mojiangContent.split(" 的委托", 2)[0].split("来自 ", 2)[1].trim());
		} else if(mojiangContent.contains("已完成，剩余时间")) {
			mojiangMissionStatus = MissionStatus.killed;
		} else {
			mojiangMissionStatus = MissionStatus.cooldown;
		}
	}
	public static void doMissions() {
		takeMission();
		if(!mowuMissionStatus.equals(MissionStatus.taken) && !mojiangMissionStatus.equals(MissionStatus.taken)) {
			MoveService.movePath(MapService.findPath(PersonStatusService.currentLocation, 1161), BattleConfig.never);
			handInMission();
			return;
		}
		BattleService.readyForBattle();
		if(!mowuMissionStatus.equals(MissionStatus.taken)) {
			MoveService.movePath(MapService.findPath(PersonStatusService.currentLocation, mojiangLocation), BattleConfig.never);
			fightMowuOrMojiang(mojiangLocation);
			MoveService.movePath(MapService.findPath(mojiangLocation, 1161), BattleConfig.never);
			handInMission();
			return;
		}
		if(!mojiangMissionStatus.equals(MissionStatus.taken)) {
			MoveService.movePath(MapService.findPath(PersonStatusService.currentLocation, mowuLocation), BattleConfig.never);
			fightMowuOrMojiang(mowuLocation);
			MoveService.movePath(MapService.findPath(mowuLocation, 1161), BattleConfig.never);
			handInMission();
			return;
		}
		MapPath ta2Mowu = MapService.findPath(1161, mowuLocation);
		MapPath ta2Mojiang = MapService.findPath(1161, mojiangLocation);
		MapPath mowu2mojiang = MapService.findPath(mowuLocation, mojiangLocation);
		MapPath me2Mowu = MapService.findPath(PersonStatusService.currentLocation, mowuLocation);
		
		if(mowu2mojiang.getPathLength() > ta2Mowu.getPathLength() + ta2Mojiang.getPathLength()) {
			MoveService.movePath(me2Mowu, BattleConfig.never);
			TagangMissionService.fightMowuOrMojiang(mowuLocation);
			MoveService.movePath(MapService.reverseMapPath(ta2Mowu), BattleConfig.never);
			MoveService.movePath(ta2Mojiang, BattleConfig.never);
			TagangMissionService.fightMowuOrMojiang(mojiangLocation);
			MoveService.movePath(MapService.reverseMapPath(ta2Mojiang), BattleConfig.never);
		} else {
			MoveService.movePath(me2Mowu, BattleConfig.never);
			TagangMissionService.fightMowuOrMojiang(mowuLocation);
			MoveService.movePath(mowu2mojiang, BattleConfig.never);
			TagangMissionService.fightMowuOrMojiang(mojiangLocation);
			MoveService.movePath(MapService.reverseMapPath(ta2Mojiang), BattleConfig.never);
		}
		handInMission();
	}
	public static void handInMission() {
		FacilityService.saveMoney();
		HttpUtil.get("npc.php?npcid=303B");
		HttpUtil.get("npc.php?npcid=303B&act=mist");
		checkMission();
		FacilityService.saveMoney();
//		FacilityService.repairWeapon(0);
	}
}
