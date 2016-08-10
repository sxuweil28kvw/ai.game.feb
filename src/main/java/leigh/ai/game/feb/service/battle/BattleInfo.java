package leigh.ai.game.feb.service.battle;

public class BattleInfo {
	private BattleResult result;
	private String[] otherInfo;
	public BattleResult getResult() {
		return result;
	}
	public void setResult(BattleResult result) {
		this.result = result;
	}
	public String[] getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String[] otherInfo) {
		this.otherInfo = otherInfo;
	}
}