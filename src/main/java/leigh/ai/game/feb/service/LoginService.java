package leigh.ai.game.feb.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import leigh.ai.game.feb.parsers.MainPhpParser;
import leigh.ai.game.feb.parsers.NumberParser;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
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
		BagService.update();
	}
	public static void login() {
		login(username, password);
	}
	public static void logout() {
		HttpUtil.get("quit.php");
	}
	public static void deleteAccount(String u, String p) {
		HttpUtil.get("feb.php");
		HttpUtil.get("loginload.php");
		FakeSleepUtil.sleep(1, 2);
		HttpUtil.get("login.php");
		Map<String, Object> param = new HashMap<String, Object>();
		HttpUtil.post("delete.php", param);
		FakeSleepUtil.sleep(1, 3);
		param.put("goto", "killing");
		param.put("name", u);
		param.put("password", p);
		HttpUtil.post("delete.php", param);
		logger.debug(u + "自杀了。");
	}
	public static boolean register(String u, String p, String job, String abe,
			String answers) {
		Map<String, Object> param = new HashMap<String, Object>();
		HttpUtil.get("feb.php");
		HttpUtil.get("loginload.php");
		FakeSleepUtil.sleep(1, 2);
		HttpUtil.get("login.php");
		HttpPost post = new HttpPost(HttpUtil.FEB_HOST + "register.php");
		String registerPhp = null;
		try {
			post.addHeader("Referer", HttpUtil.FEB_HOST + "login.php");
			CloseableHttpResponse response = HttpUtil.HC.execute(post);
			registerPhp = EntityUtils.toString(response.getEntity(), "utf8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
//		String registerPhp = HttpUtil.post("register.php", param);
		String verifyCode = NumberParser.parse(registerPhp.split("goto=recode", 2)[1].split("</a>", 2)[0]);
		param.put("codepass", verifyCode);
		String nameexist = null;
		try {
			nameexist = HttpUtil.get("register_co.php?goto=thisname&maintext=" + URLEncoder.encode(u, "utf8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(!nameexist.contains("该用户名可以使用")) {
			logger.error("用户名" + u + "无法使用！");
			return false;
		}
		
		param.put("maintext", u);
		param.put("password", p);
		
		String[] abes = {"理", "炎", "风", "雷", "冰", "光", "暗"};
		int abeInt = 8;
		for(int i = 0; i < 7; i++) {
			if(abes[i].equals(abe)) {
				abeInt = i;
				break;
			}
		}
		if(abeInt == 8) {
			logger.error("没有" + abe + "这种人物支援属性！");
			return false;
		}
		param.put("abe", abeInt);
		
		String[] jobs = {"佣兵[男]","剑士[男]","战士[男]","重骑士[男]","弓箭手[男]","修道士[男]","魔法师[男]","黑暗法师[男]","S骑士[男]","龙骑士[男]","盗贼[男]","斧骑士[男]","海盗[男]","佣兵[女]","剑士[女]","重骑士[女]","天马骑士[女]","弓箭手[女]","神官骑士[女]","魔法师[女]","黑暗法师[女]","S骑士[女]","龙骑士[女]","盗贼[女]","海盗[女]"};
		String[] jobCodes = {"1a", "1b", "1c", "1d", "1e", "1g", "1h", "1j", "1k", "1l", "1m", "1n", "1o", "2a", "2b", "2c", "2d", "2e", "2g", "2h", "2j", "2k", "2l", "2m", "2o"};
		int jobInt = -1;
		for(int i = 0; i < jobs.length; i++) {
			if(jobs[i].equals(job)) {
				jobInt = i;
				break;
			}
		}
		if(jobInt == -1) {
			logger.error("职业" + job + "不存在！");
			return false;
		}
		param.put("classx", jobCodes[jobInt]);
		
		try {
			for(int i = 0; i < 5; i++) {
				param.put("problem" + (i + 1), Integer.parseInt(answers.substring(i, i + 1)) - 1);
			}
		} catch(Exception e) {
			logger.error("问题答案必须是5个数字！");
		}
		
		String reg = HttpUtil.post("register_updata.php", param);
		if(reg.contains("注册完成")) {
			logger.info("天马：" + u + "  注册完成！");
		}
		return false;
	}
}
