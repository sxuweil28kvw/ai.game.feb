package leigh.ai.game.feb.service;

public class MyStatus {
	public static class MyWeapon {
		private String name;
		private int amountLeft;
		private int amountTotal;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAmountLeft() {
			return amountLeft;
		}
		public void setAmountLeft(int amountLeft) {
			this.amountLeft = amountLeft;
		}
		public int getAmountTotal() {
			return amountTotal;
		}
		public void setAmountTotal(int amountTotal) {
			this.amountTotal = amountTotal;
		}
	}
	public static class MyItem {
		private String position;
		private String name;
		private int amountLeft;
		private int amountTotal;
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAmountLeft() {
			return amountLeft;
		}
		public void setAmountLeft(int amountLeft) {
			this.amountLeft = amountLeft;
		}
		public int getAmountTotal() {
			return amountTotal;
		}
		public void setAmountTotal(int amountTotal) {
			this.amountTotal = amountTotal;
		}
	}
}
