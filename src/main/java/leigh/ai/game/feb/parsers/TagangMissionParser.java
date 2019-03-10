package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.service.MapService;

public class TagangMissionParser {

	public static int[] parse(String tagangMissionResponse) {
		try {
			int[] locations = new int[2];
			String[] npctell = tagangMissionResponse.split("class=\"npctell\">", 2)[1].split("<br></div>", 2)[0].split("<br>");
			if(npctell[0].contains("冷却中")) {
				locations[0] = 0;
			} else {
				locations[0] = MapService.nameLookup.get(npctell[0].split("的委托",2)[0].split("来自")[1]);
			}
			if(npctell[1].contains("冷却中")) {
				locations[1] = 0;
			} else {
				locations[1] = MapService.nameLookup.get(npctell[1].split("的委托",2)[0].split("来自")[1]);
			}
			return locations;
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, tagangMissionResponse, "解析艾里克斯说的话失败！");
			return null;
		}
	}
	
}
