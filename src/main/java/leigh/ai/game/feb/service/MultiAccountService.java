package leigh.ai.game.feb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.parsers.ItemParser;
import leigh.ai.game.feb.parsers.RaidParser;
import leigh.ai.game.feb.service.multiAccount.Account;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.service.status.PersonStatus;
import leigh.ai.game.feb.util.HttpUtil;

public class MultiAccountService {
	private static final Logger logger = LoggerFactory.getLogger(MultiAccountService.class);
	public static List<CloseableHttpClient> hcs;
	public static List<PersonStatus> status;
	public static int currentPerson;
	public static void login(Account... accounts) {
		hcs = new ArrayList<CloseableHttpClient>(accounts.length);
		status = new ArrayList<PersonStatus>(accounts.length);
		RequestConfig rc = RequestConfig.custom()
				.setConnectTimeout(10000)
				.setSocketTimeout(10000)
				.build();
		for(int i = 0; i < accounts.length; i++) {
			Account acc = accounts[i];
			CloseableHttpClient hc = HttpClients.custom()
				.setUserAgent("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 10.0; WOW64; Trident/7.0; Touch; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; Tablet PC 2.0; LCJB)")
				.setDefaultCookieStore(new BasicCookieStore())
				.setDefaultRequestConfig(rc)
				.build();
			hcs.add(hc);
			HttpUtil.setHc(hc);
			LoginService.login(acc.getU(), acc.getP());
			storeStatus(i);
		}
		currentPerson = accounts.length - 1;
		HttpUtil.setHc(hcs.get(currentPerson));
	}
	private static void storeStatus(int i) {
		PersonStatus status = new PersonStatus();
		status.setAP(PersonStatusService.AP)
			.setBagFree(PersonStatusService.bagFree)
			.setBagLimit(PersonStatusService.bagLimit)
			.setBankItems(PersonStatusService.bankItems)
			.setBankTotalSlots(PersonStatusService.bankTotalSlots)
			.setCurrentLocation(PersonStatusService.currentLocation)
			.setGoodCard(PersonStatusService.goodCard)
			.setHalfCard(PersonStatusService.halfCard)
			.setHP(PersonStatusService.HP)
			.setItems(PersonStatusService.items)
			.setJusticeCard(PersonStatusService.justiceCard)
			.setLevel(PersonStatusService.level)
			.setMahua(PersonStatusService.mahua)
			.setMaxHP(PersonStatusService.maxHP)
			.setMemberCard(PersonStatusService.memberCard)
			.setMoney(PersonStatusService.money)
			.setMyjob(PersonStatusService.myjob)
			.setRaidMapPosition(RaidService.myPosition)
			.setUserId(PersonStatusService.userId)
			.setWeapons(PersonStatusService.weapons)
			;
		int[] resources = new int[BagService.resourceNameList.length];
		for(Integer resourceId: BagService.resourceAmount.keySet()) {
			resources[resourceId] = BagService.resourceAmount.get(resourceId);
		}
		status.setResources(resources);
		status.getWeaponClass().clear();
		status.getWeaponClass().putAll(PersonStatusService.weaponClass);
		if(MultiAccountService.status.size() <= i) {
			MultiAccountService.status.add(status);
		} else {
			MultiAccountService.status.set(i, status);
		}
		status.setAccount(new Account(LoginService.username, LoginService.password));
	}
	public static void activate(int i) {
		if(i == currentPerson) {
			return;
		}
		storeStatus(currentPerson);
		HttpUtil.setHc(hcs.get(i));
		loadStatus(i);
		currentPerson = i;
		logger.debug("换 {} 行动", LoginService.username);
	}
	private static void loadStatus(int i) {
		PersonStatus ps = status.get(i);
		PersonStatusService.AP = ps.getAP();
		PersonStatusService.bagFree = ps.getBagFree();
		PersonStatusService.bagLimit = ps.getBagLimit();
		PersonStatusService.bankItems = ps.getBankItems();
		PersonStatusService.bankTotalSlots = ps.getBankTotalSlots();
		PersonStatusService.currentLocation = ps.getCurrentLocation();
		PersonStatusService.goodCard = ps.isGoodCard();
		PersonStatusService.halfCard = ps.isHalfCard();
		PersonStatusService.HP = ps.getHP();
		PersonStatusService.items = ps.getItems();
		PersonStatusService.justiceCard = ps.isJusticeCard();
		PersonStatusService.level = ps.getLevel();
		PersonStatusService.mahua = ps.getMahua();
		PersonStatusService.maxHP = ps.getMaxHP();
		PersonStatusService.memberCard = ps.isMemberCard();
		PersonStatusService.money = ps.getMoney();
		PersonStatusService.myjob = ps.getMyjob();
		RaidService.myPosition = ps.getRaidMapPosition();
		PersonStatusService.userId = ps.getUserId();
		PersonStatusService.weapons = ps.getWeapons();
		BagService.resourceAmount.clear();
		for(int j = 0; j < ps.getResources().length; j++) {
			if(ps.getResources()[j] > 0) {
				BagService.resourceAmount.put(j, ps.getResources()[j]);
			}
		}
		PersonStatusService.weaponClass.clear();
		PersonStatusService.weaponClass.putAll(ps.getWeaponClass());
		LoginService.username = ps.getAccount().getU();
		LoginService.password = ps.getAccount().getP();
	}
	public static void askForHelp() {
		int position = RaidService.myPosition;
		int currentPerson = MultiAccountService.currentPerson;
		String askForHelp = HttpUtil.get("raid_help.php");
		Set<String> mates = RaidParser.parseAskForHelp(askForHelp);
		for(int i = 0; i < MultiAccountService.status.size(); i++) {
			if(i == currentPerson) {
				continue;
			}
			PersonStatus ps = MultiAccountService.status.get(i);
			if(mates.contains(ps.getAccount().getU()) && ps.getRaidMapPosition() != position) {
				MultiAccountService.activate(i);
				RaidService.forceMove();
				RaidService.myPosition = position;
			}
		}
		MultiAccountService.activate(currentPerson);
	}
	public static void healMate(MyItem staff, int battlePerson) {
		int userId = status.get(battlePerson).getUserId();
		String username = status.get(battlePerson).getAccount().getU();
		String tmp1 = HttpUtil.get("useitem_heal.php?goto=useitem&wrap=" + staff.getPosition());
		if(!tmp1.contains(username)) {
			logger.error("无法治疗{}！", username);
//			PersonStatusService.up date();
			return;
		}
		HttpUtil.get("useitem_heal.php?goto=teamuse&wrap=" + staff.getPosition() + "&maintext=" + userId);
		ItemParser.itemsAfterUse(HttpUtil.get("useitem.php"));
		logger.debug("应该使用{}治疗了{}", staff.getName(), username);
	}
}
