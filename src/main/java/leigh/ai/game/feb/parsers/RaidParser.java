package leigh.ai.game.feb.parsers;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.service.raid.RaidBattleData;

public class RaidParser {
	private static final Logger logger = LoggerFactory.getLogger(RaidParser.class);
	public static Set<String> parseAskForHelp(String src) {
		Document doc = Jsoup.parse(src);
		Element td = doc.body().child(0).child(0).child(1).child(1);
		String tdText = td.text();
		logger.debug("求助结果：{}", tdText);
		Pattern p = Pattern.compile("\\[[^\\]]*\\]");
		Matcher m = p.matcher(tdText);
		Set<String> result = new HashSet<String>();
		while(m.find()) {
			String group = m.group();
			result.add(group.substring(1, group.length() - 1));
		}
		return result;
	}

	public static RaidBattleData parseBattleData(String src) {
		RaidBattleData rbd = new RaidBattleData();
		Document doc = Jsoup.parse(src);
		Element rightTbody = doc.body().child(0)//table
				.child(0)//tbody
				.child(0)//tr
				.child(1)//td
				.child(0)//table
				.child(0)//tbody
				;
		int[] enemyData = new int[5], myData = new int[5];
		for(int trNum = 3; trNum <= 7; trNum ++) {
			Element tr = rightTbody.child(trNum);
			enemyData[trNum - 3] = Integer.parseInt(tr.child(1).child(0).text());
			myData[trNum - 3] = Integer.parseInt(tr.child(3).child(0).text());
		}
		rbd.setEnemyHp(enemyData[0]);
		rbd.setEnemyDmg(enemyData[1]);
		rbd.setEnemyHit(enemyData[2]);
		rbd.setEnemyCrt(enemyData[3]);
		rbd.setEnemySpd(enemyData[4]);
		rbd.setMyHp(myData[0]);
		rbd.setMyDmg(myData[1]);
		rbd.setMyHit(myData[2]);
		rbd.setMyCrt(myData[3]);
		rbd.setMySpd(myData[4]);
		
		rbd.setOneTurn(doc.body().child(0).child(0).child(0).child(0).child(0).child(0).child(1).child(1).child(0).child(0).child(0).html().contains("生死"));
		return rbd;
	}
	
}
