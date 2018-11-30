package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.service.MapService;
import leigh.ai.game.feb.service.PersonStatusService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MoveParser {

	/***********************
	 * 
	 * 解析"move.php"
	 * 
	 * @param str
	 */
	public static void parseMove(String str) {
		try {
			Document doc = Jsoup.parse(str);
			String statusStr = doc.body().child(0).child(0).attr("onload");
			if(statusStr.startsWith("chatturn")) {
				statusStr = doc.body().child(1).child(0).attr("onload");
			}
			PersonStatusService.HP = Integer.parseInt(statusStr.split("'j_hp','", 2)[1].split("'", 2)[0]);
			PersonStatusService.maxHP = Integer.parseInt(statusStr.split("'j_mhp','", 2)[1].split("'", 2)[0]);
			PersonStatusService.AP = Integer.parseInt(statusStr.split("'j_ap','", 2)[1].split("'", 2)[0]);
			
			Element table1 = doc.body().child(1);
			Element table2 = table1.child(0).child(0).child(0).child(0);
			Element table3 = table2.child(0).child(0).child(0).child(0);
			Element table4 = table3.child(0).child(1).child(1).child(0);
			String location = table4.child(0).child(0).child(0).html();
			PersonStatusService.currentLocation = MapService.nameLookup.get(location);
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, str, "解析移动结果出错！");
		}
	}

}
