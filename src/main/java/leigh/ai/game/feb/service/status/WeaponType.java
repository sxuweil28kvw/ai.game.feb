package leigh.ai.game.feb.service.status;

public enum WeaponType {
	铁剑("剑", "E", 45, 5, 90, 0, 5),
	细身剑("剑", "E", 30, 3, 100, 5, 2),
	铁大剑("剑", "E", 40, 10, 70, 0, 14),
	钢剑("剑", "D", 30, 8, 75, 0, 10),
	钢大剑("剑", "D", 30, 13, 60, 0, 16),
	杀手剑("剑", "C", 20, 9, 75, 30, 7),
	勇者之剑("剑", "B", 30, 9, 75, 0, 12),
	银剑("剑", "A", 20, 13, 80, 0, 8),
	银大剑("剑", "A", 20, 16, 60, 0, 15),
	
	铁枪("枪", "E", 45, 7, 80, 0, 8),
	细身枪("枪", "E", 30, 4, 85, 5, 4),
	投枪("枪", "E", 20, 6, 65, 0, 11),
	钢枪("枪", "D", 30, 10, 70, 0, 13),
	杀手枪("枪", "C", 20, 10, 70, 30, 9),
	重投枪("枪", "C", 18, 9, 60, 0, 12),
	长枪("枪", "B", 15, 12, 70, 5, 10),
	勇者之枪("枪", "B", 30, 10, 70, 0, 14),
	银枪("枪", "A", 20, 14, 75, 0, 10),
	
	铁斧("斧", "E", 45, 8, 75, 0, 10),
	投斧("斧", "E", 20, 7, 60, 0, 12	),
	钢斧("斧", "D", 30, 11, 65, 0, 15),
	杀手斧("斧", "C", 20, 11, 65, 30, 11),
	重投斧("斧", "A", 15, 13, 65, 0, 14),
	银斧("斧", "A", 20, 15, 70, 0, 12),
	
	铁弓("弓", "E", 45, 6, 85, 0, 5),
	钢弓("弓", "D", 30, 9, 70, 0, 9),
	短弓("弓", "D", 22, 5, 85, 10, 3),
	杀手弓("弓", "C", 20, 9, 75, 30, 7),
	银弓("弓", "A", 20, 13, 75, 0, 6),
	;
	private String type;
	private String level;
	private int quantity;
	private int power;
	private int accuracy;
	private int critical;
	private int weight;
	private WeaponType(String type, String level, int quantity, int power,
			int accuracy, int critical, int weight) {
		this.type = type;
		this.level = level;
		this.quantity = quantity;
		this.power = power;
		this.accuracy = accuracy;
		this.critical = critical;
		this.weight = weight;
	}
	public String getType() {
		return type;
	}
	public String getLevel() {
		return level;
	}
	public int getQuantity() {
		return quantity;
	}
	public int getPower() {
		return power;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public int getCritical() {
		return critical;
	}
	public int getWeight() {
		return weight;
	}
}
