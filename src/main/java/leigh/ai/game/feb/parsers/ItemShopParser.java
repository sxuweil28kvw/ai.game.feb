package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.service.PersonStatusService;

public class ItemShopParser {

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

	public static void afterBuy(String str) {
		try {
			PersonStatusService.money = Integer.parseInt(str.split("nowmoney','", 2)[1].split("'", 2)[0]);
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, str, "");
		}
	}

}
