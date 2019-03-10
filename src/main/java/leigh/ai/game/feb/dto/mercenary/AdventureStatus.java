package leigh.ai.game.feb.dto.mercenary;

public class AdventureStatus {
	public static final int STATUS_NO_ADVENTURE = 0;
	public static final int STATUS_ON_ADVENTURE = 1;
	public static final int STATUS_ADVENTURE_FINISHED = 2;
	
	private int status;
	private String adventurePlace;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAdventurePlace() {
		return adventurePlace;
	}
	public void setAdventurePlace(String adventurePlace) {
		this.adventurePlace = adventurePlace;
	}
}
