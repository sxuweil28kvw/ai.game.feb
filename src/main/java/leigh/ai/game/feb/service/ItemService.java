package leigh.ai.game.feb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.parsers.ItemParser;
import leigh.ai.game.feb.parsers.PersonStatusParser;
import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.status.Item;
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

	public static void ensureItems(Item... items) {
		if(items.length > 5) {
			throw new IllegalArgumentException("道具数不能超过5");
		}
		List<MyItem> toSell = new ArrayList<MyItem>(5);
		List<Item> toBuy = new ArrayList<Item>(items.length);
		List<MyItem> toStore = new ArrayList<MyItem>(5);
		List<MyItem> tmp1 = new ArrayList<MyItem>(PersonStatusService.items.size());
		tmp1.addAll(PersonStatusService.items);
		for(Item item: items) {
			boolean needBuy = true;
			for(int i = 0; i < tmp1.size(); i++) {
				MyItem t = tmp1.get(i);
				if(t.getName().equals(item.getName())) {
					tmp1.remove(i--);
					if(t.getAmountLeft() == item.getMaxAmount()) {
						needBuy = false;
						break;
					} else {
						toSell.add(t);
					}
				}
			}
			if(needBuy) {
				toBuy.add(item);
			}
		}
		toStore.addAll(tmp1);
		if(toStore.size() > 0) {
			MoveService.moveToFacility(FacilityType.bank);
			for(MyItem t: toStore) {
				if(!FacilityService.storeItem(t.getPosition())) {
					throw new RuntimeException("银行仓库不够了！");
				}
			}
		}
		if(toBuy.isEmpty()) {
			return;
		}
		toBuy.sort(new Comparator<Item>() {
			@Override
			public int compare(Item o1, Item o2) {
				if(o1.getBuyFrom().equals(FacilityType.itemshopMember)) {
					if(o2.getBuyFrom().equals(FacilityType.itemshop)) {
						return -1;
					}
				} else if(o2.getBuyFrom().equals(FacilityType.itemshopMember)) {
					return 1;
				}
				return 0;
			}
		});
		MoveService.moveToFacility(toBuy.get(0).getBuyFrom());
		if(toSell.size() > 0) {
			for(MyItem t: toSell) {
				FacilityService.sellItem(t);
			}
		}
		for(int i = 0; i < toBuy.size(); i++) {
			MoveService.moveToFacility(toBuy.get(i).getBuyFrom());
			FacilityService.buyItem(toBuy.get(i).getCode());
		}
		if(logger.isDebugEnabled()) {
			StringBuilder logsb = new StringBuilder("ensureWeapon(");
			for(Item item: items) {
				logsb.append(item).append(',');
			}
			logsb.append(") result=[");
			for(MyItem t: PersonStatusService.items) {
				logsb.append(t.getName()).append('*').append(t.getAmountLeft()).append(',');
			}
			logsb.append(']');
			logger.debug(logsb.toString());
		}
	}

	public static void equip(Item item) {
		for(MyItem t: PersonStatusService.items) {
			if(t.getName().equals(item.getName())) {
				ItemService.equipItem(t);
				return;
			}
		}
	}

}
