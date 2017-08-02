package leigh.ai.game.feb.service.status;

import java.util.List;

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
	public String getMyjob() {
		return myjob;
	}
	public void setMyjob(String myjob) {
		this.myjob = myjob;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public byte getLevel() {
		return level;
	}
	public void setLevel(byte level) {
		this.level = level;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int hP) {
		HP = hP;
	}
	public int getMaxHP() {
		return maxHP;
	}
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}
	public int getAP() {
		return AP;
	}
	public void setAP(int aP) {
		AP = aP;
	}
	public int getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
	}
	public List<MyWeapon> getWeapons() {
		return weapons;
	}
	public void setWeapons(List<MyWeapon> weapons) {
		this.weapons = weapons;
	}
	public List<MyItem> getItems() {
		return items;
	}
	public void setItems(List<MyItem> items) {
		this.items = items;
	}
	public boolean isMemberCard() {
		return memberCard;
	}
	public void setMemberCard(boolean memberCard) {
		this.memberCard = memberCard;
	}
	public boolean isHalfCard() {
		return halfCard;
	}
	public void setHalfCard(boolean halfCard) {
		this.halfCard = halfCard;
	}
	public boolean isGoodCard() {
		return goodCard;
	}
	public void setGoodCard(boolean goodCard) {
		this.goodCard = goodCard;
	}
	public boolean isJusticeCard() {
		return justiceCard;
	}
	public void setJusticeCard(boolean justiceCard) {
		this.justiceCard = justiceCard;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getMahua() {
		return mahua;
	}
	public void setMahua(int mahua) {
		this.mahua = mahua;
	}
	public int getBankTotalSlots() {
		return bankTotalSlots;
	}
	public void setBankTotalSlots(int bankTotalSlots) {
		this.bankTotalSlots = bankTotalSlots;
	}
	public List<MyItem> getBankItems() {
		return bankItems;
	}
	public void setBankItems(List<MyItem> bankItems) {
		this.bankItems = bankItems;
	}
}
