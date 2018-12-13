package leigh.ai.game.feb.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.service.MercenaryService;
import leigh.ai.game.feb.service.mercenary.Mercenary;
import leigh.ai.game.feb.service.mercenary.MercenaryDetail;
import leigh.ai.game.feb.service.mercenary.MercenaryJob;
import leigh.ai.game.feb.service.mercenary.MercenaryStatus;

public class MercenaryParser {
	private static Logger logger = LoggerFactory.getLogger(MercenaryParser.class);

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
	
	public static MercenaryDetail parseDetail(String src) {
		MercenaryDetail dtl = new MercenaryDetail();
		try {
			Element tbody = Jsoup.parse(src).body().child(0).child(0);
			String levelString = tbody.child(1).child(0).html().split(":")[1];
			String[] levelsParts = levelString.split("%");
			dtl.setLevel((Integer.parseInt(levelsParts[0]) + Integer.parseInt(levelsParts[1].split("%")[0])) / 5);
			
			String hpStr = tbody.child(1).child(1).html().split(":")[1];
			dtl.setHp(Integer.parseInt(hpStr.split("/")[0]));
			dtl.setMaxHp(Integer.parseInt(hpStr.split("/")[1]));
			
			String gifFileUri = tbody.child(2).child(0).child(0).attr("src");
			String[] spl = gifFileUri.split("/");
			String gifFileName = spl[spl.length - 1].split("\\.")[0];
			dtl.setJob(MercenaryJob.lookup(gifFileName));
			
			dtl.setPwr(Integer.parseInt(tbody.child(3).child(0).html().split(":")[1]));
			dtl.setAgi(Integer.parseInt(tbody.child(3).child(1).html().split(":")[1]));
			dtl.setSpd(Integer.parseInt(tbody.child(3).child(2).html().split(":")[1]));
			dtl.setLck(Integer.parseInt(tbody.child(3).child(3).html().split(":")[1]));
			dtl.setDef(Integer.parseInt(tbody.child(4).child(0).html().split(":")[1]));
			dtl.setPrt(Integer.parseInt(tbody.child(4).child(1).html().split(":")[1]));
			dtl.setCon(Integer.parseInt(tbody.child(4).child(2).html().split(":")[1]));
			if(logger.isDebugEnabled()) {
				logger.debug("佣兵属性：" + dtl.getMaxHp() + "HP," + dtl.getPwr() + "力"
						+ dtl.getAgi() + "技" + dtl.getSpd() + "速" + dtl.getDef() + "防"
						+ dtl.getPrt() + "魔防" + dtl.getCon() + "体格");
			}
		} catch(Exception e) {
			ParserExceptionHandler.warn(e, src, "解析佣兵详情失败！");
		}
		return dtl;
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
