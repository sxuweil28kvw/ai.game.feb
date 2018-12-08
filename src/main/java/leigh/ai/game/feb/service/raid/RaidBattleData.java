package leigh.ai.game.feb.service.raid;

public class RaidBattleData {
	private int enemyHp;
	private int enemyDmg;
	private int enemyHit;
	private int enemyCrt;
	private int enemySpd;
	private int myHp;
	private int myDmg;
	private int myHit;
	private int myCrt;
	private int mySpd;
	/************
	 * true表示生死一战，false表示可以磨
	 */
	private boolean isOneTurn;
	public int getEnemyHp() {
		return enemyHp;
	}
	public void setEnemyHp(int enemyHp) {
		this.enemyHp = enemyHp;
	}
	public int getEnemyDmg() {
		return enemyDmg;
	}
	public void setEnemyDmg(int enemyDmg) {
		this.enemyDmg = enemyDmg;
	}
	public int getEnemyCrt() {
		return enemyCrt;
	}
	public void setEnemyCrt(int enemyCrt) {
		this.enemyCrt = enemyCrt;
	}
	public int getEnemySpd() {
		return enemySpd;
	}
	public void setEnemySpd(int enemySpd) {
		this.enemySpd = enemySpd;
	}
	public int getMyHp() {
		return myHp;
	}
	public void setMyHp(int myHp) {
		this.myHp = myHp;
	}
	public int getMyDmg() {
		return myDmg;
	}
	public void setMyDmg(int myDmg) {
		this.myDmg = myDmg;
	}
	public int getMyCrt() {
		return myCrt;
	}
	public void setMyCrt(int myCrt) {
		this.myCrt = myCrt;
	}
	public int getMySpd() {
		return mySpd;
	}
	public void setMySpd(int mySpd) {
		this.mySpd = mySpd;
	}
	public int getEnemyHit() {
		return enemyHit;
	}
	public void setEnemyHit(int enemyHit) {
		this.enemyHit = enemyHit;
	}
	public int getMyHit() {
		return myHit;
	}
	public void setMyHit(int myHit) {
		this.myHit = myHit;
	}
	/**************
	 * 
	 * @return true表示生死一战，false表示可以磨
	 */
	public boolean isOneTurn() {
		return isOneTurn;
	}
	public void setOneTurn(boolean isOneTurn) {
		this.isOneTurn = isOneTurn;
	}
	public int getEnemyMaxDmg1t() {
		if(this.getEnemyDmg() == 0 || this.getEnemyHit() == 0) {
			return 0;
		}
		boolean enemyTwice = enemySpd - mySpd >= 4;
		return enemyDmg * (enemyTwice ? 2 : 1) * (enemyCrt > 0 ? 3 : 1);
	}
	public int getMyMaxDmg1t() {
		if(this.getEnemyDmg() == 0 || this.getEnemyHit() == 0) {
			return 0;
		}
		boolean enemyTwice = enemySpd - mySpd >= 4;
		return enemyDmg * (enemyTwice ? 2 : 1) * (enemyCrt > 0 ? 3 : 1);
	}
	public int getMyMinDmgBeforeEnemy() {
		if(myHit == 0) {
			return 0;
		}
		return myDmg * (myCrt == 100 ? 3 : 1);
	}
}
