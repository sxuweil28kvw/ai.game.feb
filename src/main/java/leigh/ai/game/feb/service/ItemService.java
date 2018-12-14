package leigh.ai.game.feb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.parsers.ItemParser;
import leigh.ai.game.feb.parsers.PersonStatusParser;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;

public class ItemService {
	private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
	
	public static void useItem(String itemPosition) {
		String personStat = HttpUtil.get("useitem_co.php?goto=useitem&wrap=" + itemPosition);
		PersonStatusParser.parseAfterUseItem(personStat);
		String items = HttpUtil.get("useitem.php");
		ItemParser.itemsAfterUse(items);
	}

	public static void useItem(MyItem t) {
		String personStat = HttpUtil.get("useitem_co.php?goto=useitem&wrap=" + t.getPosition());
		logger.debug("使用物品：" + t.getName());
		PersonStatusParser.parseAfterUseItem(personStat);
		String items = HttpUtil.get("useitem.php");
		ItemParser.itemsAfterUse(items);
	}

	public static void equipItem(MyItem t) {
		HttpUtil.get("equip.php");
		HttpUtil.get("equip_sw.php?goto=show&type=its&wrap=" + t.getPosition());
		FakeSleepUtil.sleep(1, 2);
		HttpUtil.get("equip_ep.php?type=its&wrap=" + t.getPosition());
		PersonStatusService.update();
	}

	public static void throwItem(MyItem t) {
		HttpUtil.get("equip.php");
		HttpUtil.get("equip_sw.php?goto=show&type=its&wrap=" + t.getPosition());
		HttpUtil.get("equip_del.php?type=its&wrap=" + t.getPosition());
		PersonStatusService.update();
	}

	public static void useJobChangeItem(MyItem t, String upperJob) {
		String s1 = HttpUtil.get("useitem_co.php?goto=useitem&wrap=" + t.getPosition());
		String uriJobChange = ItemParser.parseJobChangeUri(s1, upperJob);
		HttpUtil.get(uriJobChange);
		HttpUtil.get(uriJobChange.replace("jobup_sw", "jobup_st"));
		ItemParser.itemsAfterUse(HttpUtil.get("useitem.php"));
		LoginService.logout();
		LoginService.login();
		if(PersonStatusService.myjob.equals(upperJob)) {
			logger.info("成功转职为{}", upperJob);
		} else {
			logger.warn("转职失败，转职目标为{}，目前职业为{}", upperJob, PersonStatusService.myjob);
		}
	}
}
