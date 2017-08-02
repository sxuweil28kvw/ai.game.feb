package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MoveService;

public class TempMove {
	public static void main(String[] args) {
		LoginService.login("细川可南子", "kk82liewuxux");
		MoveService.moveTo(2034);
	}
}
