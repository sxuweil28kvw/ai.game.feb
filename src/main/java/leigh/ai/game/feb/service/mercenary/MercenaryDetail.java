package leigh.ai.game.feb.service.mercenary;

public class MercenaryDetail {
	private int level;
	private int maxHp;
	private int hp;
	private int pwr;
	private int agi;
	private int spd;
	private int lck;
	private int def;
	private int prt;
	private int con;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	/***************
	 * 力魔
	 * @return
	 */
	public int getPwr() {
		return pwr;
	}
	public void setPwr(int pwr) {
		this.pwr = pwr;
	}
	/****************
	 * 技巧
	 * @return
	 */
	public int getAgi() {
		return agi;
	}
	public void setAgi(int agi) {
		this.agi = agi;
	}
	/******************
	 * 速度
	 * @return
	 */
	public int getSpd() {
		return spd;
	}
	public void setSpd(int spd) {
		this.spd = spd;
	}
	/***********
	 * 幸运
	 * @return
	 */
	public int getLck() {
		return lck;
	}
	public void setLck(int lck) {
		this.lck = lck;
	}
	/*************
	 * 守备
	 * @return
	 */
	public int getDef() {
		return def;
	}
	public void setDef(int def) {
		this.def = def;
	}
	/***************
	 * 魔防
	 * @return
	 */
	public int getPrt() {
		return prt;
	}
	public void setPrt(int prt) {
		this.prt = prt;
	}
	/**************
	 * 体格
	 * @return
	 */
	public int getCon() {
		return con;
	}
	public void setCon(int con) {
		this.con = con;
	}
}
