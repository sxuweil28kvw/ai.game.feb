package leigh.ai.game.feb.business;

import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MercenaryService;

public class MercenaryBiz {
	public static void pick(String u, String p, int mahuaLeft) {
		LoginService.login(u, p);
		MercenaryService.mercenaryBatch(mahuaLeft);
	}
}
