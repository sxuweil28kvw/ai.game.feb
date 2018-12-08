package leigh.ai.game.feb.service.status;

import java.util.HashMap;
import java.util.Map;

public class MyStatus {
	public static class MyWeapon {
		private String name;
		private int amountLeft;
		private String position;
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
	}
	public static class MyItem {
		public static final Map<String, String> lookup = new HashMap<String, String>();
		static {
			for(Item i: Item.values()) {
				lookup.put(i.getCode(), i.getName());
			}
		}
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
