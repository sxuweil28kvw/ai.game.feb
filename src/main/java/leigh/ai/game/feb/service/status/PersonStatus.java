package leigh.ai.game.feb.service.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leigh.ai.game.feb.service.multiAccount.Account;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.service.status.MyStatus.MyWeapon;

public class PersonStatus {
	private String myjob;
	private Integer userId;
	private byte level;
	private int HP;
	private int maxHP;
	private int AP;
	private int currentLocation;
	private List<MyWeapon> weapons;
	private List<MyItem> items;
	private boolean memberCard;
	private boolean halfCard;
	private boolean goodCard;
	private boolean justiceCard;
	private int money;
	private int mahua;
	private int bankTotalSlots;
	private List<MyItem> bankItems;
	private int bagLimit;
	private int bagFree;
	private int[] resources;
	private int raidMapPosition;
	private Account account;
	private Map<String, String> weaponClass = new HashMap<String, String>(4);
	public String getMyjob() {
		return myjob;
	}
	public PersonStatus setMyjob(String myjob) {
		this.myjob = myjob;
		return this;
	}
	public Integer getUserId() {
		return userId;
	}
	public PersonStatus setUserId(Integer userId) {
		this.userId = userId;
		return this;
	}
	public byte getLevel() {
		return level;
	}
	public PersonStatus setLevel(byte level) {
		this.level = level;
		return this;
	}
	public int getHP() {
		return HP;
	}
	public PersonStatus setHP(int hP) {
		HP = hP;
		return this;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public PersonStatus setMaxHP(int maxHP) {
		this.maxHP = maxHP;
		return this;
	}
	public int getAP() {
		return AP;
	}
	public PersonStatus setAP(int aP) {
		AP = aP;
		return this;
	}
	public int getCurrentLocation() {
		return currentLocation;
	}
	public PersonStatus setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
		return this;
	}
	public List<MyWeapon> getWeapons() {
		return weapons;
	}
	public PersonStatus setWeapons(List<MyWeapon> weapons) {
		this.weapons = weapons;
		return this;
	}
	public List<MyItem> getItems() {
		return items;
	}
	public PersonStatus setItems(List<MyItem> items) {
		this.items = items;
		return this;
	}
	public boolean isMemberCard() {
		return memberCard;
	}
	public PersonStatus setMemberCard(boolean memberCard) {
		this.memberCard = memberCard;
		return this;
	}
	public boolean isHalfCard() {
		return halfCard;
	}
	public PersonStatus setHalfCard(boolean halfCard) {
		this.halfCard = halfCard;
		return this;
	}
	public boolean isGoodCard() {
		return goodCard;
	}
	public PersonStatus setGoodCard(boolean goodCard) {
		this.goodCard = goodCard;
		return this;
	}
	public boolean isJusticeCard() {
		return justiceCard;
	}
	public PersonStatus setJusticeCard(boolean justiceCard) {
		this.justiceCard = justiceCard;
		return this;
	}
	public int getMoney() {
		return money;
	}
	public PersonStatus setMoney(int money) {
		this.money = money;
		return this;
	}
	public int getMahua() {
		return mahua;
	}
	public PersonStatus setMahua(int mahua) {
		this.mahua = mahua;
		return this;
	}
	public int getBankTotalSlots() {
		return bankTotalSlots;
	}
	public PersonStatus setBankTotalSlots(int bankTotalSlots) {
		this.bankTotalSlots = bankTotalSlots;
		return this;
	}
	public List<MyItem> getBankItems() {
		return bankItems;
	}
	public PersonStatus setBankItems(List<MyItem> bankItems) {
		this.bankItems = bankItems;
		return this;
	}
	public int getBagLimit() {
		return bagLimit;
	}
	public PersonStatus setBagLimit(int bagLimit) {
		this.bagLimit = bagLimit;
		return this;
	}
	public int getBagFree() {
		return bagFree;
	}
	public PersonStatus setBagFree(int bagFree) {
		this.bagFree = bagFree;
		return this;
	}
	public int[] getResources() {
		return resources;
	}
	public PersonStatus setResources(int[] resources) {
		this.resources = resources;
		return this;
	}
	public int getRaidMapPosition() {
		return raidMapPosition;
	}
	public PersonStatus setRaidMapPosition(int raidMapPosition) {
		this.raidMapPosition = raidMapPosition;
		return this;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Map<String, String> getWeaponClass() {
		return weaponClass;
	}
	public void setWeaponClass(Map<String, String> weaponClass) {
		this.weaponClass = weaponClass;
	}
}
