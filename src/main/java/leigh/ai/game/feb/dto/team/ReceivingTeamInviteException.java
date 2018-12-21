package leigh.ai.game.feb.dto.team;

public class ReceivingTeamInviteException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2053021297872152174L;
	
	public ReceivingTeamInviteException(String teamName) {
		super();
		this.teamName = teamName;
	}
	private String teamName;
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
}
