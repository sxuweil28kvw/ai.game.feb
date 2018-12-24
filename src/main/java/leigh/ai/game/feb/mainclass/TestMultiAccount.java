package leigh.ai.game.feb.mainclass;

import leigh.ai.game.feb.business.TeamRaidBiz;

public class TestMultiAccount {

	public static void main(String[] args) {
		args = new String[] {
				"1111", "张飞", "7777777",
				"看门罗汉", "7777777", "司祭",
				"降龙罗汉", "7777777", "司祭",
				"伏虎罗汉", "7777777", "司祭",
				"沉思罗汉", "7777777", "司祭",
		};
		TeamRaidBiz.helpLaofan(args);
	}

}
