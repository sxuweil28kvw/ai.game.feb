package leigh.ai.game.feb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.dto.raid.RaidBattleStrategy;
import leigh.ai.game.feb.service.status.MyStatus.MyWeapon;

public class TeamRaidService {
	private static final Logger logger = LoggerFactory.getLogger(TeamRaidService.class);
	public static void battleByStrategy(RaidBattleStrategy strat) {
		// before preparation
		MultiAccountService.activate(strat.getBattler());
		if (!checkWeapon(PersonStatusService.weapons, strat.getWeapons(), strat.getWeaponAmountNeeded())) {

		}
		// battle

	}

	private static boolean checkWeapon(List<MyWeapon> myWeapons, String[] weapons, int[] weaponAmountNeeded) {
		Map<String, Integer> myWeaponAmounts = new HashMap<String, Integer>(5);
		for(MyWeapon w: myWeapons) {
			if(myWeaponAmounts.containsKey(w.getName())) {
				myWeaponAmounts.put(w.getName(), myWeaponAmounts.get(w.getName()) + w.getAmountLeft());
			} else {
				myWeaponAmounts.put(w.getName(), w.getAmountLeft());
			}
		}
		for(int i = 0; i < weapons.length; i++) {
			if(!myWeaponAmounts.containsKey(weapons[i])) {
				// TODO: should throw error?
				logger.error("没有携带指定的武器：{}", weapons[i]);
				return false;
			}
			if(myWeaponAmounts.get(weapons[i]) < weaponAmountNeeded[i]) {
				logger.debug("");
				return false;
			}
		}
		return false;
	}
}
