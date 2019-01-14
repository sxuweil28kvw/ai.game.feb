package leigh.ai.game.feb.dto.skill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySkill {
	private Set<Skill> learnt = new HashSet<Skill>();
	private List<Skill> equipped = new ArrayList<Skill>(3);
	public Set<Skill> getLearnt() {
		return learnt;
	}
	public void setLearnt(Set<Skill> learnt) {
		this.learnt = learnt;
	}
	public List<Skill> getEquipped() {
		return equipped;
	}
	public void setEquipped(List<Skill> equipped) {
		this.equipped = equipped;
	}
	public String toString() {
		return "装备着" + equipped.toString() + "; 学过的" + learnt.toString();
	}
}
