package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;

public class TestMultiAccount {

	public static void main(String[] args) {
		args = new String[] {
				"1111", "许褚", "7777777",
				"Ccwwad", "7777777", "蛇龙骑士",
				"Ccwwae", "7777777", "蛇龙骑士",
				"Ccwwaf", "7777777", "蛇龙骑士",
				"Ccwwag", "7777777", "蛇龙骑士",
		};
		TeamRaidBiz.helpLaofan(args);
	}

}
