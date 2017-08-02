package leigh.ai.game.feb.service.mercenary;

public class Mercenary {
	private int id;
	private String name;
	private MercenaryStatus status = MercenaryStatus.rest;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MercenaryStatus getStatus() {
		return status;
	}
	public void setStatus(MercenaryStatus status) {
		this.status = status;
	}
	public String toString() {
		return id + ":" + name + ":" + status;
	}
}
