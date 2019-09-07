package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MapService;
import leigh.ai.game.feb.service.MoveService;
import leigh.ai.game.feb.service.multiAccount.Account;

public class TestMultiAccount {

	public static void main(String[] args) {
		TeamRaidBiz.ruin(
				new Account[] {
						new Account("岛津由乃", "kk82liewuxux"),
						new Account("鸟居江利子", "kk82liewuxux"),
						new Account("小笠原祥子", "kk82liewuxux"),
						new Account("支仓令", "kk82liewuxux"),
//						new Account("佐藤圣", "kk82liewuxux"),
				}, 1
//				, 1
		);
		Integer code = MapService.nameLookup.get("塔塞尔港");
		LoginService.login("岛津由乃", "kk82liewuxux");
		MoveService.moveTo(code);
//		LoginService.login("细川可南子", "kk82liewuxux");
//		MoveService.moveTo(code);
	}

}
