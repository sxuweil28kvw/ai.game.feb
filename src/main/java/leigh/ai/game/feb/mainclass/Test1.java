package leigh.ai.game.feb.mainclass;

import java.util.ArrayList;
import java.util.List;

import leigh.ai.game.feb.parsers.ItemParser;
import leigh.ai.game.feb.service.ItemService;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.RaidService;
import leigh.ai.game.feb.service.TeamRaidBiz;
import leigh.ai.game.feb.service.TeamService;
import leigh.ai.game.feb.service.status.Item;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.util.HttpUtil;

public class Test1 {
	public static void main(String[] args) {
		LoginService.login("水野蓉子", "kk82liewuxux");
		System.out.println(TeamService.queryMyTeam());
		LoginService.logout();
	}
}
