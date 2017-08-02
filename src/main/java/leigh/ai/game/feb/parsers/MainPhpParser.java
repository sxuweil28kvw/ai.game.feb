package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MapService;
import leigh.ai.game.feb.service.PersonStatusService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainPhpParser {
	private static Logger logger = LoggerFactory.getLogger(MainPhpParser.class);
	public static String parse(String mainPhp) {
		try {
			Document doc = Jsoup.parse(mainPhp);
			PersonStatusService.myjob = doc.getElementById("my_job").html();
			String personStat = mainPhp.split("<!-- 个人状态部分开始 -->", 2)[1].split("<!-- 个人状态部分结束 -->", 2)[0];
			PersonStatusService.userId = Integer.parseInt(personStat.split(LoginService.username + "\\[", 2)[1].split("\\]", 2)[0]);
			PersonStatusService.HP = Integer.parseInt(personStat.split("<b id=j_hp>", 2)[1].split("<", 2)[0]);
			PersonStatusService.maxHP = Integer.parseInt(personStat.split("<b id=j_mhp>", 2)[1].split("<", 2)[0]);
			PersonStatusService.AP = Integer.parseInt(personStat.split("<b id=j_ap>", 2)[1].split("<", 2)[0]);
			
			String currentLocation = mainPhp.split("<!-- 队伍部分结束 -->", 2)[1].split("</SCRIPT>", 2)[1].split("STYLEBLUR3\">", 2)[1].split("</td>", 2)[0];
//			String currentLocation = mainPhp.split("</iframe>", 2)[1].split("STYLEBLUR3\">", 2)[1].split("</td>", 2)[0];
			PersonStatusService.currentLocation = MapService.nameLookup.get(currentLocation);
			if(logger.isDebugEnabled()) {
				logger.debug("HP=" + PersonStatusService.HP + ", maxHP=" + PersonStatusService.maxHP
						+ ", AP=" + PersonStatusService.AP + ", currentLocation=" + PersonStatusService.currentLocation
						+ ":" + currentLocation + ",myjob=" + PersonStatusService.myjob);
			}
			
			return currentLocation;
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, mainPhp, "解析main.php异常！");
			return null;
		}
	}

}
