package leigh.ai.game.feb.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.parsers.MercenaryParser;
import leigh.ai.game.feb.parsers.NpcParser;
import leigh.ai.game.feb.service.mercenary.Mercenary;
import leigh.ai.game.feb.service.mercenary.MercenaryDetail;
import leigh.ai.game.feb.service.mercenary.MercenaryJob;
import leigh.ai.game.feb.service.mercenary.MercenaryStatus;
import leigh.ai.game.feb.util.HttpUtil;

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
	public static LinkedList<String> mercenaryReviewClass = new LinkedList<String>();
	static {
		mercenaryReviewClass.add("F");
		mercenaryReviewClass.add("E");
		mercenaryReviewClass.add("D");
		mercenaryReviewClass.add("C");
		mercenaryReviewClass.add("B");
		mercenaryReviewClass.add("B+");
		mercenaryReviewClass.add("A");
		mercenaryReviewClass.add("AA");
		mercenaryReviewClass.add("AAA");
		mercenaryReviewClass.add("S");
		mercenaryReviewClass.add("SS");
		mercenaryReviewClass.add("SSS");
	}
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
					if(dtl.getPwr() > 3 && dtl.getSpd() > 3 && dtl.getDef() > 3 && dtl.getPrt() > 3 && dtl.getMaxHp() > 17) {
						rename(m.getId(), "苗");
						logger.debug("挑到优质苗：{}", dtl);
					} else {
						rename(m.getId(), "良");
						logger.debug("挑到次等苗：{}", dtl);
					}
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

	public static boolean startTraining(int id) {
		HttpUtil.get("soldier_co.php?goto=train&soldier=" + id);
		String response = HttpUtil.get("soldier_updata.php?goto=trainStart&soldier=" + id);
		return response.startsWith("佣兵：");
	}
	/**************
	 * 结束训练，并将m的状态置为闲置中
	 * @param m
	 */
	public static void endTraining(Mercenary m) {
		HttpUtil.get("soldier_co.php?goto=train&soldier=" + m.getId());
		HttpUtil.get("soldier_updata.php?goto=trainEnd&soldier=" + m.getId());
		m.setStatus(MercenaryStatus.rest);
	}
	/**************
	 * 结束训练，需要调用方更新佣兵状态
	 * @param id
	 */
	public static void endTraining(int id) {
		HttpUtil.get("soldier_co.php?goto=train&soldier=" + id);
		HttpUtil.get("soldier_updata.php?goto=trainEnd&soldier=" + id);
	}
	/**************
	 * 易主
	 * @param soldierId
	 * @param username
	 */
	public static boolean giveTo(int soldierId, String username) throws SoldierBoundException {
		String s = HttpUtil.get("soldier_co.php?goto=song&soldier=" + soldierId);
		if(s.contains("绑定")) {
			throw new SoldierBoundException(soldierId);
		}
		String giveResult = null;
		try {
			giveResult = HttpUtil.get("soldier_updata.php?goto=song&soldier="
					+ soldierId + "&maintext="
					+ URLEncoder.encode(username, "utf-8"));
		} catch (UnsupportedEncodingException e) {
		}
		return giveResult.startsWith("易主完成");
	}
	
	/*****************
	 * 
	 * @param soldierId
	 * @return 一个String[2]。return[0]为进攻评价，return[1]为防守评价。
	 */
	public static String[] review(int soldierId) {
		MoveService.moveTo(1126);//鲁09
		HttpUtil.get("npc.php?npcid=314");//不关心第一次公主说的话
		HttpUtil.get("npc.php?npcid=314&act=SOL_D");//第二次，公主打开佣兵列表
		String s = NpcParser.parse(HttpUtil.get("npc.php?npcid=314&act=SOL_D2&solid=" + soldierId));
		return NpcParser.parsePrincessReview(s);
	}
	
	public static class SoldierBoundException extends RuntimeException {
		private static final long serialVersionUID = 7509801092952060566L;
		private int soldierId;
		public SoldierBoundException(int soldierId) {
			super();
			this.soldierId = soldierId;
		}
		public int getSoldierId() {
			return soldierId;
		}
		public void setSoldierId(int soldierId) {
			this.soldierId = soldierId;
		}
	}

}
