package leigh.ai.game.feb.parsers;

import java.util.ArrayList;

import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.service.status.MyStatus.MyWeapon;
import leigh.ai.game.feb.service.MapService;
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
			Element bigDiv = doc.getElementById("equipsw");
			try {
				Element itemTbody = bigDiv.child(0).child(0).child(0).child(0).child(0).child(0);
				if(itemTbody.children().size() > 0) {
					PersonStatusService.weapons = new ArrayList<MyWeapon>(5);
					PersonStatusService.items = new ArrayList<MyItem>(5);
					for(int i = 0; i < itemTbody.children().size(); i++) {
						String onclick = itemTbody.child(i).child(0).attr("onClick");
						if(onclick == null || onclick.equals("")) {
							continue;
						}
						if(onclick.contains("type=wep")) {
							MyWeapon w = new MyWeapon();
							w.setPosition(onclick.split("wrap=", 2)[1].split("'", 2)[0]);
							w.setName(itemTbody.child(i).child(0).child(0).child(0).child(0).child(1).html());
							w.setAmountLeft(Integer.parseInt(itemTbody.child(i).child(1).html()));
							PersonStatusService.weapons.add(w);
						} else {
							MyItem t = new MyItem();
							t.setPosition(onclick.split("wrap=", 2)[1].split("'", 2)[0]);
							t.setName(itemTbody.child(i).child(0).child(0).child(0).child(0).child(1).attr("title").split("STYLEBLUR3>", 2)[1].split("<", 2)[0]);
							t.setAmountLeft(Integer.parseInt(itemTbody.child(i).child(1).html()));
							PersonStatusService.items.add(t);
						}
					}
				}
			} catch(IndexOutOfBoundsException e) {
				PersonStatusService.weapons = new ArrayList<MyWeapon>(0);
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
			Element weaponClassTableBody = doc.body().child(0).child(0).child(1).child(3).child(0).child(0).child(0).child(0).child(0).child(0);
			String[] weaponCategory = {weaponClassTableBody.child(0).child(0).text(), weaponClassTableBody.child(0).child(3).text(),
					weaponClassTableBody.child(1).child(0).text(), weaponClassTableBody.child(1).child(3).text()};
			String[] weaponClass = {weaponClassTableBody.child(0).child(2).text(), weaponClassTableBody.child(0).child(5).text(),
					weaponClassTableBody.child(1).child(2).text(), weaponClassTableBody.child(1).child(5).text()};
			PersonStatusService.weaponClass.clear();
			for(int i = 0; i < 4; i++) {
				if(!weaponClass[i].equals("--")) {
					PersonStatusService.weaponClass.put(weaponCategory[i], weaponClass[i]);
				}
			}
//			if(logger.isDebugEnabled()) {
//				logger.debug("weaponClass:{}", PersonStatusService.weaponClass);
//			}
			
			Element cardsTable = doc.body().child(0).child(0).child(1).child(3).child(0).child(0).child(10).child(0).child(0);
			PersonStatusService.halfCard = !cardsTable.child(0).child(1).child(0).hasAttr("style");
			PersonStatusService.memberCard = !cardsTable.child(0).child(1).child(1).hasAttr("style");
			PersonStatusService.goodCard = !cardsTable.child(0).child(2).child(0).hasAttr("style");
			PersonStatusService.justiceCard = !cardsTable.child(0).child(2).child(1).hasAttr("style");
			
			Element moneyTable = doc.body().child(0).child(0).child(1).child(3).child(0).child(0).child(8).child(0).child(0);
			PersonStatusService.money = Integer.parseInt(moneyTable.child(0).child(0).child(0).html().substring(2));
			PersonStatusService.mahua = Integer.parseInt(moneyTable.child(0).child(0).child(1).html().substring(2));
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, str, "解析个人界面失败！");
		}
	}

	public static void parseAfterUseItem(String str) {
		PersonStatusService.HP = Integer.parseInt(str.split("'j_hp','", 2)[1].split("'", 2)[0]);
		PersonStatusService.AP = Integer.parseInt(str.split("'j_ap','", 2)[1].split("'", 2)[0]);
	}

	/*******************
	 * 
	 * 解析"move.php?display=1"的结果
	 * 
	 * @param str
	 */
	public static void afterMove(String str) {
		try {
			PersonStatusService.HP = Integer.parseInt(str.split("'j_hp','", 2)[1].split("'", 2)[0]);
			PersonStatusService.maxHP = Integer.parseInt(str.split("'j_mhp','", 2)[1].split("'", 2)[0]);
			PersonStatusService.AP = Integer.parseInt(str.split("'j_ap','", 2)[1].split("'", 2)[0]);
			
			Document doc = Jsoup.parse(str);
			Element table1 = doc.body().child(2);
			Element table2 = table1.child(0).child(0).child(0).child(0);
			Element table3 = table2.child(0).child(0).child(0).child(0);
			Element table4 = table3.child(0).child(1).child(1).child(0);
			String location = table4.child(0).child(0).child(0).html();
			PersonStatusService.currentLocation = MapService.nameLookup.get(location);
		} catch(Exception e) {
			ParserExceptionHandler.handle(e, str, "解析move.php结果失败！");
		}
	}
}
