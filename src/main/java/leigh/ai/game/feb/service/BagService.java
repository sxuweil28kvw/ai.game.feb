package leigh.ai.game.feb.service;

import java.util.HashMap;
import java.util.Map;

import leigh.ai.game.feb.parsers.NumberParser;
import leigh.ai.game.feb.parsers.ParserExceptionHandler;
import leigh.ai.game.feb.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BagService {
	private static Logger logger = LoggerFactory.getLogger(BagService.class);
	public static String[] resourceNameList = {"木材","金属","蘑菇","骷髅","腐牙","魔眼","马蹄","毒液","利爪","魔翼","咒血","蛇头","龙皮"};
	public static Map<Integer, Integer> resourceAmount = new HashMap<Integer, Integer>(resourceNameList.length);
	public static void update() {
		String stuff = HttpUtil.get("stuff.php");
		try {
			Document doc = Jsoup.parse(stuff);
			Element tbody1 = doc.body().child(0).child(0).child(1).child(1).child(0).child(0).child(0);
			String bagLimitStr = tbody1.child(0).child(0).child(0).child(0).child(0).child(1).html();
			String[] spl = bagLimitStr.split("/");
			PersonStatusService.bagFree = Integer.parseInt(spl[0]);
			PersonStatusService.bagLimit = Integer.parseInt(spl[1]);
			
			if(tbody1.children().size() > 2) {
				for(int i = 2; i < tbody1.children().size() - 1; i++) {
					Element tr = tbody1.child(i).child(0).child(0).child(0).child(0);
					int resourceCode = Integer.parseInt(tr.child(0).child(0).attr("onClick").split("sf=", 2)[1].split("'", 2)[0]);
					int amount = Integer.parseInt(NumberParser.parse(tr.child(1).html()));
					resourceAmount.put(resourceCode, amount);
				}
			}
			
			if(logger.isDebugEnabled()) {
				StringBuilder log = new StringBuilder();
				log.append("空余资源背包：").append(PersonStatusService.bagFree).append("/").append(PersonStatusService.bagLimit).append("[");
				for(Integer resourceCode: resourceAmount.keySet()) {
					log.append(resourceNameList[resourceCode - 1]).append(":").append(resourceAmount.get(resourceCode)).append(",");
				}
				log.append("]");
				logger.debug(log.toString());
			}
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, stuff, "解析资源界面失败！");
		}
	}
}
