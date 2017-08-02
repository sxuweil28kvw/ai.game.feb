package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.service.MercenaryService;
import leigh.ai.game.feb.service.mercenary.Mercenary;
import leigh.ai.game.feb.service.mercenary.MercenaryStatus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MercenaryParser {

	public static void parseOuterFrame(String str) {
		try {
			Document doc = Jsoup.parse(str);
			String countStr = doc.body().child(0).child(0).child(1).child(1).child(0).child(0).child(0).child(0).html();
			MercenaryService.num = Integer.parseInt(countStr.split("：", 2)[1].split("/")[0]);
			MercenaryService.limit = Integer.parseInt(countStr.split("/")[1]);
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, str, "解析佣兵界面失败！");
		}
	}
	
	public static void parseSoldierTable(String str) {
		MercenaryService.myMercenaries.clear();
		try {
			Document doc = Jsoup.parse(str);
			Element tbody = doc.body().child(0).child(0).child(0);
			for(Element tr: tbody.children()) {
				String onclick = tr.child(0).child(0).attr("onClick");
				String[] spl = onclick.split("','");
				Mercenary m = new Mercenary();
				m.setId(Integer.parseInt(spl[0].split("'")[1]));
				String name = spl[1];
				if(name.contains("[")) {
					if(name.endsWith("[训]")) {
						m.setStatus(MercenaryStatus.train);
						name = name.substring(0, name.length() - 3);
					} else if(name.endsWith("[城]")) {
						m.setStatus(MercenaryStatus.guard);
						name = name.substring(0, name.length() - 3);
					}
				}
				m.setName(name);
				MercenaryService.myMercenaries.add(m);
			}
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, str, "解析佣兵界面失败！");
		}
	}
}
