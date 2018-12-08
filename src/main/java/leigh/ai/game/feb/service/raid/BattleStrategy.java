package leigh.ai.game.feb.service.raid;

public class BattleStrategy {
	private String weapon;
	private int safeHp;
	private int turns;
	public String getWeapon() {
		return weapon;
	}
	public void setWeapon(String weapon) {
		this.weapon = weapon;
	}
	public int getSafeHp() {
		return safeHp;
	}
	public void setSafeHp(int safeHp) {
		this.safeHp = safeHp;
	}
	public int getTurns() {
		return turns;
	}
	public void setTurns(int turns) {
		this.turns = turns;
	}
}
