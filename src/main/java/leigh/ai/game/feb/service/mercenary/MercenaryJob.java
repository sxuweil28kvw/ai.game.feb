package leigh.ai.game.feb.service.mercenary;

import java.util.HashMap;
import java.util.Map;

public enum MercenaryJob {
	男佣("1a", 16, 3, 4, 4, 4),
	女佣("2a", 16, 3, 4, 4, 4),
	男剑("1b", 17, 5, 4, 4, 4),
	女剑("2b", 17, 5, 4, 4, 4),
	战士("1c", 15, 3, 4, 4, 5),
	男甲("1d", 15, 3, 5, 3, 5),
	女甲("2c", 15, 4, 5, 3, 5),
	天马("2d", 17, 4, 4, 5, 3),
	男弓("1e", 17, 3, 5, 4, 4),
	女弓("2e", 17, 3, 5, 4, 4),
	男光("1g", 17, 5, 4, 4, 3),
	女光("2g", 17, 5, 4, 4, 3),
	男理("1h", 18, 3, 4, 4, 3),
	女理("2h", 18, 3, 4, 4, 3),
	男暗("1j", 18, 3, 5, 5, 3),
	女暗("2j", 18, 3, 5, 5, 3),
	男骑("1k", 16, 4, 4, 4, 4),
	女骑("2k", 16, 4, 4, 4, 4),
	男龙("1l", 16, 3, 4, 4, 5),
	女龙("2l", 16, 3, 4, 4, 5),
	男贼("1m", 17, 5, 3, 4, 4),
	女贼("2m", 17, 5, 3, 4, 4),
	斧骑("1n", 16, 3, 4, 3, 5),
	海盗("1o", 16, 3, 5, 3, 5)
	;
	private String gifFileName;
	private int minHp;
	private int minPwr;
	private int minSpd;
	private int minDef;
	private int minprt;
	private static Map<String, MercenaryJob> filenameLookup = new HashMap<String, MercenaryJob>();
	private MercenaryJob(String gifFileName, int minHp, int minPwr, int minSpd, int minDef, int minprt) {
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
