package leigh.ai.game.feb.service;

import leigh.ai.game.feb.util.HttpUtil;

public class NpcService {
	public static void gotoTetisi() {
		MoveService.moveTo(1168);
		String jiahana = HttpUtil.get("http://120.132.72.13/febo/move.php?mov=1168");
		if(!jiahana.contains("特缇斯")) {
			MoveService.moveTo(1178);
			String luosidun = HttpUtil.get("http://120.132.72.13/febo/move.php?mov=1179");
			if(!luosidun.contains("特缇斯")) {
				MoveService.moveTo(1118);
				String fuleiliya = HttpUtil.get("http://120.132.72.13/febo/move.php?mov=1119");
				if(!fuleiliya.contains("特缇斯")) {
					MoveService.moveTo(1104);
					String luneisi = HttpUtil.get("http://120.132.72.13/febo/move.php?mov=1102");
					if(!luneisi.contains("特缇斯")) {
						MoveService.moveTo(1152);
					}
				}
			}
		}
	}
}
