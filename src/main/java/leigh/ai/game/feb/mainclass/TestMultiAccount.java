package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;
import leigh.ai.game.feb.service.multiAccount.Account;

public class TestMultiAccount {

	public static void main(String[] args) {
		TeamRaidBiz.ruin(new Account[] {
				new Account("岛津由乃", "kk82liewuxux"),
				new Account("侵犯了小丽奈", "kk82liewuxux"),
				new Account("水野蓉子", "kk82liewuxux"),
				new Account("土萌萤", "kk82liewuxux"),
				new Account("细川可南子", "kk82liewuxux"),
		}, 1);
	}

}
