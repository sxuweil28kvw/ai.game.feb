package leigh.ai.game.feb.parsers;

public class NumberParser {

	public static String parse(String str) {
		try {
			String num = "";
			String[] spl = str.split("febimg/img1/style./c");
			for(int i = 1; i < spl.length; i++) {
				num += spl[i].split(".gif", 2)[0];
			}
			return num;
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, str, "解析图片数字出错！");
			return null;
		}
	}

}
