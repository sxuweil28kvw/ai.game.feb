package leigh.ai.game.feb.dto.raid;

public class RaidBattleStrategy {
	private int battler;
	private byte turns;
	// 所用武器
	private String[] weapons;
	// 各武器对应的必须耐久量
	private int[] weaponAmountNeeded;
	// 需装备之道具
	private String item2Equip;
	// 必须持有之道具
	private String[] items;
	private int[] itemAmountNeeded;
	// 小于或等于此HP值时治疗
	private int healLeHp;
	private int healer;
	private String healItem;
	private int healItemAmount;
	private String[] skills;
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
	public String getItem2Equip() {
		return item2Equip;
	}
	public void setItem2Equip(String item2Equip) {
		this.item2Equip = item2Equip;
	}
	public String[] getItems() {
		return items;
	}
	public void setItems(String[] items) {
		this.items = items;
	}
	public String[] getSkills() {
		return skills;
	}
	public void setSkills(String[] skills) {
		this.skills = skills;
	}
}
