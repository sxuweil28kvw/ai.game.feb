package leigh.ai.game.feb.service.multiAccount;

public class Account {
	private String u;
	private String p;
	public Account(String u, String p) {
		super();
		this.u = u;
		this.p = p;
	}
	public String getU() {
		return u;
	}
	public void setU(String u) {
		this.u = u;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
}
