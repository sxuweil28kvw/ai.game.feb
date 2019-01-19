package leigh.ai.game.feb.dto.raid;

import leigh.ai.game.feb.dto.skill.Skill;
import leigh.ai.game.feb.service.status.Item;

public class RaidBattleStrategy {
	private int battler;
	private byte turns;
	// 所用武器
	private String[] weapons;
	// 各武器对应的必须耐久量
	private int[] weaponAmountNeeded;
	// 需装备之道具
	private Item item2Equip;
	// 必须持有之道具
	private Item[] items;
	private int[] itemAmountNeeded;
	// 小于或等于此HP值时治疗
	private int healLeHp;
	private int healer;
	private String healItem;
	private int healItemAmount;
	private Skill[] skills;
	public int getBattler() {
		return battler;
	}
	public void setBattler(int battler) {
		this.battler = battler;
	}
	public byte getTurns() {
		return turns;
	}
	public void setTurns(byte turns) {
		this.turns = turns;
	}
	public String[] getWeapons() {
		return weapons;
	}
	public void setWeapons(String[] weapons) {
		this.weapons = weapons;
	}
	public int[] getWeaponAmountNeeded() {
		return weaponAmountNeeded;
	}
	public void setWeaponAmountNeeded(int[] weaponAmountNeeded) {
		this.weaponAmountNeeded = weaponAmountNeeded;
	}
	public Item getItem2Equip() {
		return item2Equip;
	}
	public void setItem2Equip(Item item2Equip) {
		this.item2Equip = item2Equip;
	}
	public Item[] getItems() {
		return items;
	}
	public void setItems(Item[] items) {
		this.items = items;
	}
	public int[] getItemAmountNeeded() {
		return itemAmountNeeded;
	}
	public void setItemAmountNeeded(int[] itemAmountNeeded) {
		this.itemAmountNeeded = itemAmountNeeded;
	}
	public int getHealLeHp() {
		return healLeHp;
	}
	public void setHealLeHp(int healLeHp) {
		this.healLeHp = healLeHp;
	}
	public int getHealer() {
		return healer;
	}
	public void setHealer(int healer) {
		this.healer = healer;
	}
	public String getHealItem() {
		return healItem;
	}
	public void setHealItem(String healItem) {
		this.healItem = healItem;
	}
	public int getHealItemAmount() {
		return healItemAmount;
	}
	public void setHealItemAmount(int healItemAmount) {
		this.healItemAmount = healItemAmount;
	}
	public Skill[] getSkills() {
		return skills;
	}
	public void setSkills(Skill[] skills) {
		this.skills = skills;
	}
}
