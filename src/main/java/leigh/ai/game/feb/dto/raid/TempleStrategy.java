package leigh.ai.game.feb.dto.raid;

import java.util.List;
import java.util.Map;

import leigh.ai.game.feb.service.multiAccount.Account;

public class TempleStrategy {
	private List<Account> accounts;
	private Integer healer;
	private Map<Integer, Map<Integer, BattleStrategy>> mapBattles;
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	public Integer getHealer() {
		return healer;
	}
	public void setHealer(Integer healer) {
		this.healer = healer;
	}
	public Map<Integer, Map<Integer, BattleStrategy>> getMapBattles() {
		return mapBattles;
	}
	public void setMapBattles(Map<Integer, Map<Integer, BattleStrategy>> mapBattles) {
		this.mapBattles = mapBattles;
	}
}
