package leigh.ai.game.feb.mainclass;

import java.util.ArrayList;
import java.util.List;

import leigh.ai.game.feb.service.ItemService;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.RaidService;
import leigh.ai.game.feb.service.TeamRaidBiz;
import leigh.ai.game.feb.service.status.Item;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.util.HttpUtil;

public class Test1 {
	public static void main(String[] args) {
		args = new String[] {
				"1", "张飞", "7777777",
				"波罗的海神尼", "7777777",
		};
//		TeamRaidBiz.helpLaofan(args);
		LoginService.login("波罗的海神尼", "7777777");
		
		String laofanSaid = HttpUtil.get("npc.php?npcid=090");
		
		if(!laofanSaid.contains("我加入你")) {
			System.out.println("{}老范对话错误，请检查任务线");
			System.exit(1);
		}
		if(PersonStatusService.items.size() == 5) {
			for(MyItem t: PersonStatusService.items) {
				if(t.getName().equals(Item.小圣水.getName())
						|| t.getName().equals(Item.小伤药.getName())
						|| t.getName().equals(Item.小万灵.getName())
						|| t.getName().equals(Item.E杖.getName())
						|| t.getName().equals(Item.D杖.getName())
						|| t.getName().equals(Item.C杖.getName())) {
					ItemService.throwItem(t);
				}
			}
		}
		
		List<MyItem> formerItems = new ArrayList<MyItem>(5);
		formerItems.addAll(PersonStatusService.items);
		String joinLaofan = HttpUtil.get("npc.php?npcid=090&act=Q5_14");
		if(joinLaofan.contains("道具已满")) {
			System.out.println("{}老范对话错误，请检查任务线");
			System.exit(1);
		}
		PersonStatusService.update();
		for(MyItem t: PersonStatusService.items) {
			boolean formerExists = false;
			for(MyItem former: formerItems) {
				if(former.getName().equals(t.getName())) {
					formerExists = true;
					break;
				}
			}
			if(!formerExists) {
				ItemService.useItem(t);
				RaidService.exit();
				break;
			}
		}
	}
}
