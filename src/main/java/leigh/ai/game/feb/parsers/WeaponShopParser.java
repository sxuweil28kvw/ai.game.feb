package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.service.PersonStatusService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeaponShopParser {
	private static Logger logger = LoggerFactory.getLogger(WeaponShopParser.class);
	public static void parseAfterRepair(String str) {
		if(!str.contains("好了")) {
			logger.warn("修理武器失败！" + str);
			return;
		}
		PersonStatusService.money = Integer.parseInt(str.split("'nowmoney','", 2)[1].split("'", 2)[0]);
		String s1 = str.split("showtext\\('wrapdy", 2)[1];
		int position = 0;
		if(s1.charAt(0) != 'E') {
			position = Integer.parseInt(s1.charAt(0) + "");
		}
		PersonStatusService.weapons.get(position).setAmountLeft(Integer.parseInt(s1.split("','", 2)[1].split("'", 2)[0]));
	}
	public static int parseRepairPrice(String str) {
		if(str.contains("新的")) {
			return 0;
		}
		if(str.contains("没啥问题")) {
			//没啥问题，给256金币就行
			return Integer.parseInt(str.split("没啥问题，给", 2)[1].split("金币就行", 2)[0]);
		}
		if(str.contains("修理大概要花")) {
			return Integer.parseInt(str.split("大概要花", 2)[1].split("金币", 2)[0]);
		}
		logger.error("无法识别的修理对话：" + str);
		System.exit(1);
		return 0;
	}
	public static int parseSellPrice(String str) {
		try {
			int indexG = str.indexOf('G');
			int numberStart = indexG - 1;
			while(numberStart >= 0) {
				if(str.charAt(numberStart) < '0' || str.charAt(numberStart) > '9') {
					break;
				}
				numberStart--;
			}
			return Integer.parseInt(str.substring(numberStart + 1, indexG));
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, str, "");
			return 0;
		}
	}
	public static int parseBuyPrice(String str) {
		try {
			int indexG = str.indexOf('G');
			int numberStart = indexG - 1;
			while(numberStart >= 0) {
				if(str.charAt(numberStart) < '0' || str.charAt(numberStart) > '9') {
					break;
				}
				numberStart--;
			}
			return Integer.parseInt(str.substring(numberStart + 1, indexG));
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, str, "");
			return 0;
		}
	}
}
