package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MapService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.RaidService;

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
			PersonStatusService.userId = Integer.parseInt(personStat.split(LoginService.username.replaceFirst(LoginService.username.substring(0, 1), LoginService.username.substring(0, 1).toUpperCase()) + "\\[", 2)[1].split("\\]", 2)[0]);
			PersonStatusService.HP = Integer.parseInt(personStat.split("<b id=j_hp>", 2)[1].split("<", 2)[0]);
			PersonStatusService.maxHP = Integer.parseInt(personStat.split("<b id=j_mhp>", 2)[1].split("<", 2)[0]);
			PersonStatusService.AP = Integer.parseInt(personStat.split("<b id=j_ap>", 2)[1].split("<", 2)[0]);
			
			String contentStartingFromCurrentLocation = mainPhp.split("<!-- 队伍部分结束 -->", 2)[1].split("</SCRIPT>", 2)[1].split("STYLEBLUR3\">", 2)[1];
			String currentLocation = contentStartingFromCurrentLocation.split("</td>", 2)[0];
			if(MapService.nameLookup.containsKey(currentLocation)) {
				PersonStatusService.currentLocation = MapService.nameLookup.get(currentLocation);
				if(PersonStatusService.currentLocation < 0) {
					//在副本内！解析层内位置：
					try {
						RaidService.myPosition = Integer.parseInt(contentStartingFromCurrentLocation.split("<td>\\[", 2)[1].split("\\]", 2)[0]) - 1;
						logger.debug("副本内位置{}", RaidService.myPosition);
					} catch(Exception e) {
						logger.warn("解析副本内位置失败？");
					}
				}
			}
			if(logger.isDebugEnabled()) {
				logger.debug("HP={},maxHp={},AP={},currentLocation={},myjob={}",
						PersonStatusService.HP, PersonStatusService.maxHP, PersonStatusService.AP,
						currentLocation, PersonStatusService.myjob);
			}
			return currentLocation;
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, mainPhp, "解析main.php异常！");
			return null;
		}
	}

}
