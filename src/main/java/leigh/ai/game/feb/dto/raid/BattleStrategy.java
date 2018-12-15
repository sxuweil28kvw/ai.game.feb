package leigh.ai.game.feb.dto.raid;

import java.util.List;

public class BattleStrategy {
	private Integer battlePerson;
	private List<String> weaponNeeded;
	private Integer weaponAmountNeeded;
	private Integer turns;
	private Integer healingHp;
	private String itemEquiping;
	private String[] skillsEquiping;
	public Integer getBattlePerson() {
		return battlePerson;
	}
	public void setBattlePerson(Integer battlePerson) {
		this.battlePerson = battlePerson;
	}
	public List<String> getWeaponNeeded() {
		return weaponNeeded;
	}
	public void setWeaponNeeded(List<String> weaponNeeded) {
		this.weaponNeeded = weaponNeeded;
	}
	public Integer getWeaponAmountNeeded() {
		return weaponAmountNeeded;
	}
	public void setWeaponAmountNeeded(Integer weaponAmountNeeded) {
		this.weaponAmountNeeded = weaponAmountNeeded;
	}
	public Integer getTurns() {
		return turns;
	}
	public void setTurns(Integer turns) {
		this.turns = turns;
	}
	public Integer getHealingHp() {
		return healingHp;
	}
	public void setHealingHp(Integer healingHp) {
		this.healingHp = healingHp;
	}
	public String getItemEquiping() {
		return itemEquiping;
	}
	public void setItemEquiping(String itemEquiping) {
		this.itemEquiping = itemEquiping;
	}
	public String[] getSkillsEquiping() {
		return skillsEquiping;
	}
	public void setSkillsEquiping(String[] skillsEquiping) {
		this.skillsEquiping = skillsEquiping;
	}
}
