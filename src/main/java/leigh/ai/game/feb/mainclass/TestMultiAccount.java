package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;
import leigh.ai.game.feb.service.multiAccount.Account;

public class TestMultiAccount {

	public static void main(String[] args) {
		TeamRaidBiz.ruin(new Account[] {
				new Account("岛津由乃", "kk82liewuxux"),
				new Account("侵犯了小丽奈", "kk82liewuxux"),
				new Account("佐藤圣", "kk82liewuxux"),
				new Account("菱川六花", "kk82liewuxux"),
//				new Account("藤堂志摩子", "kk82liewuxux"),
		}, 1);
	}

}
