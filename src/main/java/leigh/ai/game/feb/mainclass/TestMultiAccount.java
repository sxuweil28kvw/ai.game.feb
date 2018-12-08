package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.service.MoveService;
import leigh.ai.game.feb.service.MultiAccountService;
import leigh.ai.game.feb.service.RaidService;
import leigh.ai.game.feb.service.multiAccount.Account;

public class TestMultiAccount {

	public static void main(String[] args) {
		MultiAccountService.login(new Account("吕布", "7777777"),
				new Account("张飞", "7777777"));
		MultiAccountService.activate(0);
		MoveService.enterTower();
		MultiAccountService.activate(1);
		MoveService.enterTower();
		MultiAccountService.activate(0);
		RaidService.move();
		MultiAccountService.activate(1);
		RaidService.move();
	}

}
