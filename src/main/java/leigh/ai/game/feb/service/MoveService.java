package leigh.ai.game.feb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.service.map.MapPath;
import leigh.ai.game.feb.service.map.Traffic;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

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

	public static void oneMove(Integer code, Traffic traffic) {
		if(logger.isDebugEnabled()) {
			logger.debug("moving to " + code + ":" + MapService.map.get(code).getName() + ", 交通方式：" + traffic);
		}
		switch(traffic) {
		case walk:
			HttpUtil.get("move.php?mov=" + code);
			break;
		case ship:
			HttpUtil.get("shopship.php");
			HttpUtil.get("shopship_wi.php?shipto=" + code);
			HttpUtil.get("shopship_updata.php?shipto=" + code);
			HttpUtil.get("move.php?display=1");
			break;
		case fly:
			//TODO:
			break;
		}
		PersonStatusService.currentLocation = code;
	}
	public static void moveTo(int target) {
		movePath(MapService.findPath(PersonStatusService.currentLocation, target));
	}
}
