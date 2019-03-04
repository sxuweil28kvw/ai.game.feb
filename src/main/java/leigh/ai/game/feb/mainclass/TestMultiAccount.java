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
						new Account("海王满", "kk82liewuxux"),
						new Account("姬宫千歌音", "kk82liewuxux"),
						new Account("黑岩射手", "kk82liewuxux"),
//						new Account("支仓令", "kk82liewuxux"),
				}, 1
//				, 1
		);
		Integer code = MapService.nameLookup.get("塔塞尔港");
		LoginService.login("岛津由乃", "kk82liewuxux");
		MoveService.moveTo(code);
		LoginService.login("海王满", "kk82liewuxux");
		MoveService.moveTo(code);
	}

}
