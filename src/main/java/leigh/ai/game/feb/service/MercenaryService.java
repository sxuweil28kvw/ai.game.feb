package leigh.ai.game.feb.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import leigh.ai.game.feb.parsers.MercenaryParser;
import leigh.ai.game.feb.parsers.ParserExceptionHandler;
import leigh.ai.game.feb.service.mercenary.Mercenary;
import leigh.ai.game.feb.service.mercenary.MercenaryDetail;
import leigh.ai.game.feb.service.mercenary.MercenaryStatus;
import leigh.ai.game.feb.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*************
 * 佣兵相关
 * @author FlyQE
 *
 */
public class MercenaryService {
	private static final Logger logger = LoggerFactory.getLogger(MercenaryService.class);
	/*************
	 * 已有佣兵数量
	 */
	public static int num = 0;
	/*************
	 * 佣兵上限
	 */
	public static int limit = 0;
	public static List<Mercenary> myMercenaries = new ArrayList<Mercenary>();
	public static void update() {
		MercenaryParser.parseOuterFrame(HttpUtil.get("soldier.php"));
		MercenaryParser.parseSoldierTable(HttpUtil.get("soldier_table.php"));
	}
	public static void mercenaryBatch(int mahuaLeft) {
		if(PersonStatusService.mahua < mahuaLeft) {
			System.out.println("没有麻花了");
			return;
		}
		MercenaryService.update();
		while(MercenaryService.num < MercenaryService.limit) {
			Set<Integer> origin = new HashSet<Integer>(myMercenaries.size());
			for(Mercenary m: myMercenaries) {
				origin.add(m.getId());
			}
			FacilityService.mercenary(limit - num);
			MercenaryService.update();
			for(int i = 0; i < myMercenaries.size(); i++) {
				Mercenary m = myMercenaries.get(i);
				if(origin.contains(m.getId())) {
					continue;
				}
				if(m.getStatus().equals(MercenaryStatus.train) || m.getStatus().equals(MercenaryStatus.guard)) {
					continue;
				}
				MercenaryDetail dtl = queryDetail(m.getId());
				if(dtl.getLevel() > 1) {
					continue;
				}
				if(dtl.getPwr() < 4 || dtl.getSpd() < 4 || dtl.getDef() < 4 || dtl.getPrt() < 4 || dtl.getMaxHp() < 17) {
					fireMercenary(m.getId());
					i--;
				} else {
					trainMercenary(m.getId());
				}
			}
			MercenaryService.update();
			if(PersonStatusService.mahua < mahuaLeft) {
				System.out.println("没有麻花了");
				return;
			}
		}
	}
	private static void trainMercenary(int id) {
		HttpUtil.get("soldier_co.php?goto=train&soldier=" + id);
		HttpUtil.get("soldier_updata.php?goto=trainStart&soldier=" + id);
		MercenaryParser.parseSoldierTable(HttpUtil.get("soldier_table.php"));
	}
	public static void fireMercenary(int id) {
		HttpUtil.get("soldier_co.php?goto=delete&soldier=" + id);
		HttpUtil.get("soldier_updata.php?goto=delete&soldier=" + id);
		MercenaryParser.parseSoldierTable(HttpUtil.get("soldier_table.php"));
		logger.debug("解雇了一个佣兵。");
	}
	public static MercenaryDetail queryDetail(int id) {
		MercenaryDetail dtl = new MercenaryDetail();
		String response = HttpUtil.get("soldier_co.php?goto=showsol&soldier=" + id);
		try {
			Element tbody = Jsoup.parse(response).body().child(0).child(0);
			String levelString = tbody.child(1).child(0).html().split(":")[1];
			String[] levelsParts = levelString.split("%");
			dtl.setLevel((Integer.parseInt(levelsParts[0]) + Integer.parseInt(levelsParts[1].split("%")[0])) / 5);
			String hpStr = tbody.child(1).child(1).html().split(":")[1];
			dtl.setHp(Integer.parseInt(hpStr.split("/")[0]));
			dtl.setMaxHp(Integer.parseInt(hpStr.split("/")[1]));
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
			ParserExceptionHandler.warn(e, response, "解析佣兵详情失败！");
		}
		return dtl;
	}
}
