package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;
import leigh.ai.game.feb.service.multiAccount.Account;

public class TeamMaking {

	public static void main(String[] args) {
		TeamRaidBiz.team(new Account[] {
				new Account("鸟居江利子", "kk82liewuxux"),
				new Account("岛津由乃", "kk82liewuxux"),
				new Account("侵犯了小丽奈", "kk82liewuxux"),
				new Account("支仓令", "kk82liewuxux"),
				new Account("海王满", "kk82liewuxux"),
		}, 0, true);
//		TeamRaidBiz.team(new Account[] {
//				new Account("许褚", "7777777"),
//				new Account("刘禅", "7777777"),
//				new Account("Ccwwaa", "7777777"),
//				new Account("Ccwwab", "7777777"),
//				new Account("Ccwwac", "7777777"),
//		}, 0, true);
	}

}
