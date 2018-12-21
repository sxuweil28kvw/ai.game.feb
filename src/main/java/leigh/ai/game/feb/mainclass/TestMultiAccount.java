package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.service.TeamRaidBiz;

public class TestMultiAccount {

	public static void main(String[] args) {
		args = new String[] {
				"1111", "张飞", "7777777",
				"布袋罗汉", "7777777", "司祭",
				"芭蕉罗汉", "7777777", "司祭",
				"长眉罗汉", "7777777", "司祭",
				"甄姬", "7777777", "铁骑士"
		};
		TeamRaidBiz.helpLaofan(args);
	}

}
