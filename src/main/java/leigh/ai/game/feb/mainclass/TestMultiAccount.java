package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;

public class TestMultiAccount {

	public static void main(String[] args) {
		args = new String[] {
				"01", "藤堂志摩子", "kk82liewuxux",
				"艾莎", "13953539", "剑豪",
				"艾雅", "13953539", "魔法骑士",
		};
		TeamRaidBiz.helpLaofan(args);
	}

}
