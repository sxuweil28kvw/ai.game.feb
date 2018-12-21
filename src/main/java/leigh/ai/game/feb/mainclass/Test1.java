package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;
import leigh.ai.game.feb.service.multiAccount.Account;

public class Test1 {
	public static void main(String[] args) {
		TeamRaidBiz.team(new Account[] {
				new Account("侵犯了小丽奈", "kk82liewuxux"),
				new Account("岛津由乃", "kk82liewuxux"),
				new Account("海王满", "kk82liewuxux"),
				new Account("小笠原祥子", "kk82liewuxux"),
				new Account("天王遥", "kk82liewuxux"),
		}, 0, true);
	}
}
