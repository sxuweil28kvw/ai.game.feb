package leigh.ai.game.feb.service.mercenary;

import java.util.HashMap;
import java.util.Map;

public enum MercenaryJob {
	男佣("佣兵", "1a", 16, 3, 4, 4, 4),
	女佣("佣兵", "2a", 16, 3, 4, 4, 4),
	男剑("剑士", "1b", 17, 5, 4, 4, 4),
	女剑("剑士", "2b", 17, 5, 4, 4, 4),
	战士("战士", "1c", 15, 3, 4, 4, 5),
	男甲("重骑士", "1d", 15, 3, 5, 3, 5),
	女甲("重骑士", "2c", 15, 4, 5, 3, 5),
	天马("天马骑士", "2d", 17, 4, 4, 5, 3),
	男弓("弓箭手", "1e", 17, 3, 5, 4, 4),
	女弓("弓箭手", "2e", 17, 3, 5, 4, 4),
	男光("修道士", "1g", 17, 5, 4, 4, 3),
	女光("神官骑士", "2g", 17, 5, 4, 4, 3),
	男理("魔法师", "1h", 18, 3, 4, 4, 3),
	女理("魔法师", "2h", 18, 3, 4, 4, 3),
	男暗("黑暗法师", "1j", 18, 3, 5, 5, 3),
	女暗("黑暗法师", "2j", 18, 3, 5, 5, 3),
	男骑("轻骑士", "1k", 16, 4, 4, 4, 4),
	女骑("轻骑士", "2k", 16, 4, 4, 4, 4),
	男龙("龙骑士", "1l", 16, 3, 4, 4, 5),
	女龙("龙骑士", "2l", 16, 3, 4, 4, 5),
	男贼("盗贼", "1m", 17, 5, 3, 4, 4),
	女贼("盗贼", "2m", 17, 5, 3, 4, 4),
	斧骑("斧骑士", "1n", 16, 3, 4, 3, 5),
	海盗("海盗", "1o", 16, 3, 5, 3, 5)
	;
	private String gifFileName;
	private int minHp;
	private int minPwr;
	private int minSpd;
	private int minDef;
	private int minprt;
	private String name;
	private static Map<String, MercenaryJob> filenameLookup = new HashMap<String, MercenaryJob>();
	private MercenaryJob(String name, String gifFileName, int minHp, int minPwr, int minSpd, int minDef, int minprt) {
		this.name = name;
		this.gifFileName = gifFileName;
		this.minHp = minHp;
		this.minPwr = minPwr;
		this.minSpd = minSpd;
		this.minDef = minDef;
		this.minprt = minprt;
	}
	public static MercenaryJob lookup(String gifFileName) {
		if(filenameLookup.size() == 0) {
			for(MercenaryJob job: MercenaryJob.values()) {
				filenameLookup.put(job.getGifFileName(), job);
			}
		}
		return filenameLookup.get(gifFileName);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGifFileName() {
		return gifFileName;
	}
	public void setGifFileName(String gifFileName) {
		this.gifFileName = gifFileName;
	}
	public int getMinHp() {
		return minHp;
	}
	public void setMinHp(int minHp) {
		this.minHp = minHp;
	}
	public int getMinPwr() {
		return minPwr;
	}
	public void setMinPwr(int minPwr) {
		this.minPwr = minPwr;
	}
	public int getMinSpd() {
		return minSpd;
	}
	public void setMinSpd(int minSpd) {
		this.minSpd = minSpd;
	}
	public int getMinDef() {
		return minDef;
	}
	public void setMinDef(int minDef) {
		this.minDef = minDef;
	}
	public int getMinprt() {
		return minprt;
	}
	public void setMinprt(int minprt) {
		this.minprt = minprt;
	}
}
