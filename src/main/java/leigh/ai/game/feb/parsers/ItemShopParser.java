package leigh.ai.game.feb.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static int parseRepairPrice(String str) {
		Pattern p = Pattern.compile("修理大概要花([0-9]+)金币~~");
		Matcher m = p.matcher(str);
		if(!m.find()) {
			return 0;
		}
		String group = m.group(1);
		if(group == null) {
			return 0;
		}
		return Integer.parseInt(group);
	}
	
	public static void afterBuy(String str) {
		try {
			PersonStatusService.money = Integer.parseInt(str.split("nowmoney','", 2)[1].split("'", 2)[0]);
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, str, "");
		}
	}

}
