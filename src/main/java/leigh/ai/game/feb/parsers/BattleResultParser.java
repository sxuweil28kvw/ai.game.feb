package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.service.battle.BattleResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleResultParser {
	private static Logger logger = LoggerFactory.getLogger(BattleResultParser.class);
	public static BattleInfo parse(String battleResponse) {
		try {
			BattleInfo result = new BattleInfo();
			String[] splByExp = battleResponse.split("\\(经验", 2);
			String[] spl1 = splByExp[0].split(">");
			String resultStr = spl1[spl1.length - 1];
			if(resultStr.contains("被击败")) {
				result.setResult(BattleResult.win);
			} else if(resultStr.contains("重伤")) {
				result.setResult(BattleResult.lose);
			} else {
				result.setResult(BattleResult.draw);
			}
			// 解析战斗后HP和AP
			String[] splByIfram = splByExp[1].split("<iframe");
			String iframe = splByIfram[splByIfram.length - 1];
			PersonStatusService.HP = Integer.parseInt(iframe.split("'j_hp','", 2)[1].split("'", 2)[0]);
			PersonStatusService.AP = Integer.parseInt(iframe.split("'j_ap','", 2)[1].split("'", 2)[0]);
			Document doc = Jsoup.parse(battleResponse);
			Element lvup = doc.getElementById("battle_lvup");
			if(!lvup.attr("style").contains("display:none")) {
				// 升级了！
				PersonStatusService.maxHP = Integer.parseInt(lvup.child(0).child(0).child(0).child(0).child(0).child(1).child(0).child(0).child(0).child(0).child(1).child(0).html());
			}
			if(logger.isDebugEnabled()) {
				logger.debug("after battle, HP=" + PersonStatusService.HP + ", AP=" + PersonStatusService.AP
						+ ", maxHp=" + PersonStatusService.maxHP);
			}
			
			String[] splByGoldenFont = splByExp[1].split("<font color=#f7e957>");
			String[] otherInfo = new String[splByGoldenFont.length - 1];
			for(int i = 1; i < splByGoldenFont.length; i++) {
				otherInfo[i - 1] = splByGoldenFont[i].split("</font>")[0];
			}
			result.setOtherInfo(otherInfo);
			return result;
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, battleResponse, "解析战斗结果错误！");
			return null;
		}
	}
}
