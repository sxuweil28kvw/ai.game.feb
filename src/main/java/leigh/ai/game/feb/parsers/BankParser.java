package leigh.ai.game.feb.parsers;

import java.util.ArrayList;

import leigh.ai.game.feb.service.MyStatus.MyItem;
import leigh.ai.game.feb.service.PersonStatusService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class BankParser {
	/****************
	 * 解析打开银行的第一个请求的返回值
	 * @param str
	 */
	public static void parseBankOpening(String str) {
		try {
			PersonStatusService.bankTotalSlots = Integer.parseInt(str.split("现有物品柜:", 2)[1].split("<", 2)[0]);
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, str, "解析打开银行第一个请求返回值失败！");
		}
	}

	public static void parseBankStorage(String str) {
		PersonStatusService.bankItems = new ArrayList<MyItem>();
		Document doc = Jsoup.parse(str);
		Element table = doc.body().child(0);
		if(table.children().size() == 0) {
			return;
		}
		for(int i = 0; i < table.child(0).children().size(); i++) {
			Element tr = table.child(0).child(i);
			for(int j = 0; j < tr.children().size(); j += 2) {
				MyItem t = new MyItem();
				t.setName(tr.child(j).child(0).child(0).child(0).child(1).attr("title").split("STYLEBLUR3>", 2)[1].split("<", 2)[0]);
				t.setAmountLeft(Integer.parseInt(tr.child(j + 1).html()));
				PersonStatusService.bankItems.add(t);
			}
		}
	}
}
