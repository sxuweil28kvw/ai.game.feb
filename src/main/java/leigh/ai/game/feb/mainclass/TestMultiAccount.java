package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;
import leigh.ai.game.feb.service.multiAccount.Account;

public class TestMultiAccount {

	public static void main(String[] args) {
		TeamRaidBiz.ruin(new Account[] {
				new Account("岛津由乃", "kk82liewuxux"),
				new Account("鸟居江利子", "kk82liewuxux"),
				new Account("支仓令", "kk82liewuxux"),
				new Account("菱川六花", "kk82liewuxux"),
				new Account("小笠原祥子", "kk82liewuxux"),
		}, 1, 1);
	}

}
