package leigh.ai.game.feb.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import leigh.ai.game.feb.parsers.MercenaryParser;
import leigh.ai.game.feb.parsers.ParserExceptionHandler;
import leigh.ai.game.feb.service.mercenary.Mercenary;
import leigh.ai.game.feb.service.mercenary.MercenaryDetail;
import leigh.ai.game.feb.service.mercenary.MercenaryJob;
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
	
	public static void mercenary3333() {
		int training = 0;
		int maxTrain = PersonStatusService.goodCard ? 15 : 10;
		MercenaryService.update();
		for(Mercenary m: myMercenaries) {
			if(m.getStatus().equals(MercenaryStatus.train)) {
				training++;
			}
		}
		if(training >= maxTrain) {
			return;
		}
		while(MercenaryService.num <= MercenaryService.limit - 10) {
			Set<Integer> origin = new HashSet<Integer>(myMercenaries.size());
			for(Mercenary m: myMercenaries) {
				origin.add(m.getId());
			}
			FacilityService.mercenaryTen();
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
				if(dtl.getPwr() < 3 || dtl.getSpd() < 3 || dtl.getDef() < 3 || dtl.getPrt() < 3 || dtl.getMaxHp() < 15) {
					fireMercenary(m.getId());
					i--;
				} else {
					rename(m.getId(), "良");
					logger.debug("挑到次等苗：{}", dtl);
					trainMercenary(m.getId());
					training++;
				}
			}
			if(training >= maxTrain - 1) {
				logger.info("训练队列快满了！");
				break;
			}
			if(PersonStatusService.mahua < 45) {
				logger.info("没有麻花了！");
				break;
			}
		}
		logger.info("{}剩余麻花：{}", LoginService.username, PersonStatusService.mahua);
	}
	
	public static void mercenaryBatch(int mahuaLeft) {
		if(PersonStatusService.mahua < mahuaLeft) {
			System.out.println("没有麻花了");
			return;
		}
		MercenaryService.update();
		while(MercenaryService.num <= MercenaryService.limit - 10) {
			Set<Integer> origin = new HashSet<Integer>(myMercenaries.size());
			for(Mercenary m: myMercenaries) {
				origin.add(m.getId());
			}
			FacilityService.mercenaryTen();
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
				if(dtl.getJob() == null) {
					if(dtl.getPwr() < 4 || dtl.getSpd() < 4 || dtl.getDef() < 4 || dtl.getPrt() < 4 || dtl.getMaxHp() < 17) {
						fireMercenary(m.getId());
						i--;
					} else {
						rename(m.getId(), "苗");
						logger.debug("挑到苗子：{}", dtl);
						trainMercenary(m.getId());
					}
				} else {
					MercenaryJob job = dtl.getJob();
					if(dtl.getPwr() < job.getMinPwr() || dtl.getSpd() < job.getMinSpd() || dtl.getDef() < job.getMinDef() || dtl.getPrt() < job.getMinprt() || dtl.getMaxHp() < job.getMinHp()) {
						fireMercenary(m.getId());
						i--;
					} else {
						rename(m.getId(), "苗");
						logger.debug("挑到苗子：{}", dtl);
						trainMercenary(m.getId());
					}
				}
			}
			MercenaryService.update();
			if(PersonStatusService.mahua < mahuaLeft) {
				logger.warn("没有麻花了");
				return;
			}
		}
	}
	private static void rename(int id, String newName) {
		HttpUtil.get("soldier_co.php?goto=resetname&soldier=" + id);
		try {
			HttpUtil.get("soldier_updata.php?goto=resetname&soldier=" + id + "&maintext=" + URLEncoder.encode(newName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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
	}
	public static MercenaryDetail queryDetail(int id) {
		String response = HttpUtil.get("soldier_co.php?goto=showsol&soldier=" + id);
		return MercenaryParser.parseDetail(response);
	}
}
