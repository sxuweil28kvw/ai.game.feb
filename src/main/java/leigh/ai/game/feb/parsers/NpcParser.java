package leigh.ai.game.feb.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NpcParser {
	public static String parse(String str) {
		try {
			return str.split("class=\"npctell\">", 2)[1].split("</div>", 2)[0];
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, str, "解析NPC说的话失败！");
			return null;
		}
	}

	/************
	 * 从塞莱娜说的话中解析所有攻击召唤师的uri。
	 * @param selenaSaid 可以是经过NpcParser.parse方法的string，也可以是没有parse的全Html。
	 * @return 
	 */
	public static List<String> parseSelenaSummoners(String selenaSaid) {
		List<String> summoners = new ArrayList<String>(4);
		Pattern p = Pattern.compile("(npc.php\\?npcid=302&act=ST[1-4])");
		Matcher m = p.matcher(selenaSaid);
		while(m.find()) {
			summoners.add(m.group());
		}
		return summoners;
	}
}
