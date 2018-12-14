package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.service.TeamRaidBiz;

public class TestMultiAccount {

	public static void main(String[] args) {
		args = new String[] {
				"1111", "张飞", "7777777",
				"日本海神尼", "7777777",
				"里海神尼", "7777777",
				"濑户内海神尼", "7777777",
				"许褚", "7777777",
		};
		TeamRaidBiz.helpLaofan(args);
	}

}
