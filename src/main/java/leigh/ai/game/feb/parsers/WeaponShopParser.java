package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.service.PersonStatusService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeaponShopParser {
	private static Logger logger = LoggerFactory.getLogger(WeaponShopParser.class);
	public static void parseAfterRepair(String str) {
		if(!str.startsWith("好了")) {
			logger.warn("修理武器失败！" + str);
			return;
		}
		PersonStatusService.money = Integer.parseInt(str.split("'nowmoney','", 2)[1].split("'", 2)[0]);
		String s1 = str.split("showtext\\('wrapdy", 2)[1];
		int position = 0;
		if(s1.charAt(0) != 'E') {
			position = Integer.parseInt(s1.charAt(0) + "");
		}
		PersonStatusService.weapons.get(position).setAmountLeft(Integer.parseInt(str.split("wep_wdy','", 2)[1].split("'")[0]));
	}
}
