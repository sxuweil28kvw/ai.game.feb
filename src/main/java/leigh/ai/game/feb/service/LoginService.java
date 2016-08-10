package leigh.ai.game.feb.service;

import java.util.HashMap;
import java.util.Map;

import leigh.ai.game.feb.parsers.MainPhpParser;
import leigh.ai.game.feb.util.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService {
	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
	public static String username;
	public static String password;
	/******************
	 * 
	 * @param u username
	 * @param p password
	 * 
	 */
	public static void login(String u, String p) {
		username = u;
		password = p;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("logn", "name");
		param.put("pass", p);
		param.put("pname", u);
		param.put("state_html", "old");
		HttpUtil.post("loginto.php", param, logger);
		
		String mainPhp = HttpUtil.get("main.php");
		MainPhpParser.parse(mainPhp);
		PersonStatusService.update();
	}
	public static void login() {
		login(username, password);
	}
	public static void logout() {
		HttpUtil.get("quit.php");
	}
}
