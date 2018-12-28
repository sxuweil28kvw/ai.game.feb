package leigh.ai.game.feb.dto.raid;

import java.util.List;

import leigh.ai.game.feb.service.multiAccount.Account;

public class HelpLaofanParam {
	private List<Account> accounts;
	private List<Boolean> isZhenshi;
	private List<String> upperJobs;
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	public List<Boolean> getIsZhenshi() {
		return isZhenshi;
	}
	public void setIsZhenshi(List<Boolean> isZhenshi) {
		this.isZhenshi = isZhenshi;
	}
	public List<String> getUpperJobs() {
		return upperJobs;
	}
	public void setUpperJobs(List<String> upperJobs) {
		this.upperJobs = upperJobs;
	}
}
