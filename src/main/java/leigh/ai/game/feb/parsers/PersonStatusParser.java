package leigh.ai.game.feb.parsers;

import java.util.ArrayList;

import leigh.ai.game.feb.service.MyStatus.MyItem;
import leigh.ai.game.feb.service.MyStatus.MyWeapon;
import leigh.ai.game.feb.service.PersonStatusService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonStatusParser {
	private static Logger logger = LoggerFactory.getLogger(PersonStatusParser.class);
	
	public static void parse(String str) {
		try {
			Document doc = Jsoup.parse(str);
			Element bigDiv = doc.getElementById("my_bag");
			try {
				Element weaponTableBody = bigDiv.child(0).child(0).child(0).child(0).child(0).child(0);
				PersonStatusService.weapons = new ArrayList<MyWeapon>(weaponTableBody.children().size());
				if(weaponTableBody.children().size() > 0) {
					for(int i = 0; i < weaponTableBody.children().size(); i++) {
						MyWeapon w = new MyWeapon();
						w.setName(weaponTableBody.child(i).child(0).child(0).child(0).child(0).child(1).html());
						w.setAmountLeft(Integer.parseInt(weaponTableBody.child(i).child(1).html()));
						PersonStatusService.weapons.add(w);
					}
				}
			} catch(IndexOutOfBoundsException e) {
				PersonStatusService.weapons = new ArrayList<MyWeapon>(0);
			}
			try {
				Element itemTableBody = bigDiv.child(0).child(0).child(0).child(0).child(1).child(0);
				PersonStatusService.items = new ArrayList<MyItem>(itemTableBody.children().size());
				if(itemTableBody.children().size() > 0) {
					for(int i = 0; i < itemTableBody.children().size(); i++) {
						MyItem t = new MyItem();
						t.setPosition(itemTableBody.child(i).child(0).attr("onClick").split("wrap=", 2)[1].split("'", 2)[0]);
						t.setName(itemTableBody.child(i).child(0).child(0).child(0).child(0).child(1).attr("title").split("STYLEBLUR3>", 2)[1].split("<", 2)[0]);
						t.setAmountLeft(Integer.parseInt(itemTableBody.child(i).child(1).html()));
						PersonStatusService.items.add(t);
					}
				}
			} catch(IndexOutOfBoundsException e) {
				PersonStatusService.items = new ArrayList<MyItem>();
			}
			if(logger.isDebugEnabled()) {
				StringBuilder sb = new StringBuilder("weapons:");
				for(MyWeapon w: PersonStatusService.weapons) {
					sb.append(w.getName()).append(w.getAmountLeft()).append(",");
				}
				sb.append("; items:");
				for(MyItem t: PersonStatusService.items) {
					sb.append(t.getName()).append(t.getAmountLeft()).append(",");
				}
				logger.debug(sb.toString());
			}
			Element rightTableBody = bigDiv.child(0).child(0).child(0).child(2).child(0).child(0);
			PersonStatusService.halfCard = !rightTableBody.child(0).child(0).hasAttr("style");
			PersonStatusService.memberCard = !rightTableBody.child(1).child(0).hasAttr("style");
			PersonStatusService.goodCard = !rightTableBody.child(2).child(0).hasAttr("style");
			PersonStatusService.justiceCard = !rightTableBody.child(3).child(0).hasAttr("style");
			PersonStatusService.money = Integer.parseInt(rightTableBody.child(5).child(0).html().substring(2));
			PersonStatusService.mahua = Integer.parseInt(rightTableBody.child(6).child(0).html().substring(2));
		} catch(Exception e) {
			e.printStackTrace();
			ParserExceptionHandler.handle(e, str, "解析个人界面失败！");
		}
	}

	public static void parseAfterUseItem(String str) {
		PersonStatusService.HP = Integer.parseInt(str.split("'j_hp','", 2)[1].split("'", 2)[0]);
		PersonStatusService.AP = Integer.parseInt(str.split("'j_ap','", 2)[1].split("'", 2)[0]);
	}

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
}
