package leigh.ai.game.feb.parsers;

public class NpcParser {
	public static String parse(String str) {
		try {
			return str.split("class=\"npctell\">", 2)[1].split("</div>", 2)[0];
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, str, "解析NPC说的话失败！");
			return null;
		}
	}
}
