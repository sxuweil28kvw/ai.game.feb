package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MercenaryService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.multiAccount.Account;

public class Test1 {
	public static void main(String[] args) {
		LoginService.login("岛津由乃", "kk82liewuxux");
		System.out.println(PersonStatusService.mahua);
		MercenaryService.mercenary3333();
	}
}
