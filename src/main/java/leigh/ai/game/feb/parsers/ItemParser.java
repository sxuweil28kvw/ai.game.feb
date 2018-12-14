package leigh.ai.game.feb.parsers;

import java.util.ArrayList;

import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;

public class ItemParser {

	/**********************
	 * 
	 * 解析useitem.php的返回结果。
	 * 
	 * @param items
	 */
	public static void itemsAfterUse(String items) {
		String[] spl = items.split("onmouseover=\"itsinfo\\('");
		PersonStatusService.items = new ArrayList<MyItem>(spl.length - 1);
		for(int i = 1; i < spl.length; i++) {
			MyItem t = new MyItem();
			String[] splspl = spl[i].split("剩余", 2);
			t.setName(splspl[0]);
			t.setAmountLeft(Integer.parseInt(splspl[1].split("'", 2)[0]));
			t.setPosition(spl[i].split("wrap=", 2)[1].split("'", 2)[0]);
			PersonStatusService.items.add(t);
		}
	}

	public static String parseJobChangeUri(String src, String upperJob) {
		try {
			String tmp1 = src.split("转职为", 2)[1].split(upperJob, 2)[0];
			String[] spl1 = tmp1.split("chatturn\\('");
			return spl1[spl1.length - 1].split("'", 2)[0];
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, src, "解析使用转职道具界面失败！");
			return null;
		}
	}

}
