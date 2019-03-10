package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;
import leigh.ai.game.feb.service.multiAccount.Account;

public class TeamMaking {

	public static void main(String[] args) {
		TeamRaidBiz.team(new Account[] {
				new Account("鸟居江利子", "kk82liewuxux"),
				new Account("岛津由乃", "kk82liewuxux"),
				new Account("佐藤圣", "kk82liewuxux"),
				new Account("藤堂志摩子", "kk82liewuxux"),
				new Account("二条乃梨子", "kk82liewuxux"),
		}, 0, true);
//		TeamRaidBiz.team(new Account[] {
//				new Account("细川可南子", "kk82liewuxux"),
//				new Account("水野蓉子", "kk82liewuxux"),
//				new Account("天王遥", "kk82liewuxux"),
//				new Account("支仓令", "kk82liewuxux"),
//				new Account("佐藤圣", "kk82liewuxux"),
//		}, 0, true);
	}

}
