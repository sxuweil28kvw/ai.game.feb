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
	
	/***************
	 * 解析公主对佣兵的评级
	 * @param src
	 * @return 一个String[2]。return[0]为进攻评价，return[1]为防守评价。
	 */
	public static String[] parsePrincessReview(String src) {
		Pattern pAttack = Pattern.compile("进攻能力，我觉得可以评级为([A-S]+)：");
		Matcher m = pAttack.matcher(src);
		m.find();
		String attackClass = m.group(1);
		
		Pattern pDef = Pattern.compile("防守能力，我想可以称得上([A-S]+[+-]?)：");
		m = pDef.matcher(src);
		m.find();
		String defClass = m.group(1);
		return new String[] {attackClass, defClass};
	}
}
