package leigh.ai.game.feb.service;

import java.util.List;

import leigh.ai.game.feb.parsers.PersonStatusParser;
import leigh.ai.game.feb.service.MyStatus.MyItem;
import leigh.ai.game.feb.service.MyStatus.MyWeapon;
import leigh.ai.game.feb.util.HttpUtil;

public class PersonStatusService {
	public static String myjob;
	public static int HP;
	public static int maxHP;
	public static int AP;
	public static int currentLocation;
	public static List<MyWeapon> weapons;
	public static List<MyItem> items;
	public static boolean memberCard;
	public static boolean halfCard;
	public static boolean goodCard;
	public static boolean justiceCard;
	public static int money;
	public static int mahua;
	public static int bankTotalSlots;
	public static List<MyItem> bankItems;
	public static void update() {
		String response = HttpUtil.get("equip.php");
		PersonStatusParser.parse(response);
	}
}
