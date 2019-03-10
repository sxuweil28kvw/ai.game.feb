package leigh.ai.game.feb.util;

public class StringUtil {

	public static String delTags(String html) {
		StringBuilder ret = new StringBuilder();
		int len = html.length();
		for(int i = 0; i < len; ) {
			if(html.charAt(i) != '<') {
				int firstLeft = html.indexOf('<', i);
				ret.append(html.substring(i, firstLeft));
				ret.append('\t');
				i = firstLeft;
			}
			i = html.indexOf('>', i) + 1;
		}
		return ret.toString();
	}

}
