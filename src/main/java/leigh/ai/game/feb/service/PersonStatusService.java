package leigh.ai.game.feb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leigh.ai.game.feb.parsers.PersonStatusParser;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.service.status.MyStatus.MyWeapon;
import leigh.ai.game.feb.service.status.PersonStatus;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

public class PersonStatusService {
	public static String myjob = null;
	public static int userId;
	public static byte level;
	public static int HP;
	public static int maxHP;
	public static int AP;
	public static int currentLocation;
	public static List<MyWeapon> weapons = new ArrayList<MyWeapon>(5);
	public static List<MyItem> items = new ArrayList<MyItem>(5);
	public static boolean memberCard;
	public static boolean halfCard;
	public static boolean goodCard;
	public static boolean justiceCard;
	public static int money;
	public static int mahua;
	public static int bankTotalSlots;
	public static List<MyItem> bankItems;
	public static int bagLimit;
	public static int bagFree;
	public static int[] resources;
	public static Map<String, PersonStatus> status = new HashMap<String, PersonStatus>();
	public static void update() {
		String response = HttpUtil.get("equip.php");
		PersonStatusParser.parse(response);
	}
	public static void equipItem(MyItem t) {
		HttpUtil.get("equip.php");
		HttpUtil.get("equip_sw.php?goto=show&type=its&wrap=" + t.getPosition());
		FakeSleepUtil.sleep(1, 2);
		HttpUtil.get("equip_ep.php?type=its&wrap=" + t.getPosition());
		update();
	}
	public static void equipWeapon(MyWeapon myWeapon) {
		String position = myWeapon.getPosition();
		HttpUtil.get("equip_sw.php?goto=show&type=wep&wrap=" + position);
		HttpUtil.get("equip_ep.php?type=wep&wrap=" + position);
		update();
	}
}
