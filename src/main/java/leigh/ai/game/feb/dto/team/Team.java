package leigh.ai.game.feb.dto.team;

import java.util.ArrayList;
import java.util.List;

public class Team {
	private String name;
	private List<String> members = new ArrayList<String>(5);
	private byte leaderIndex;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getMembers() {
		return members;
	}
	public void setMembers(List<String> members) {
		this.members = members;
	}
	public byte getLeaderIndex() {
		return leaderIndex;
	}
	public void setLeaderIndex(byte leaderIndex) {
		this.leaderIndex = leaderIndex;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder("队伍:");
		sb.append(name).append('[');
		for(byte i = 0; i < members.size(); i++) {
			sb.append(members.get(i));
			if(i == leaderIndex) {
				sb.append("☆");
			}
			sb.append(',');
		}
		sb.append(']');
		return sb.toString();
	}
}
