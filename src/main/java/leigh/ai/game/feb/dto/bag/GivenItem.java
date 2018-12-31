package leigh.ai.game.feb.dto.bag;

public class GivenItem {
	private String type;
	private String name;
	private String givenBy;
	private int amount;
	private String time;
	private String acceptUri;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGivenBy() {
		return givenBy;
	}
	public void setGivenBy(String givenBy) {
		this.givenBy = givenBy;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAcceptUri() {
		return acceptUri;
	}
	public void setAcceptUri(String acceptUri) {
		this.acceptUri = acceptUri;
	}
}
