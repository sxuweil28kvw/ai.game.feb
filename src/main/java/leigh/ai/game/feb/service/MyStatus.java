package leigh.ai.game.feb.service;

import java.util.HashMap;
import java.util.Map;

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
		public static final Map<String, String> lookup = new HashMap<String, String>();
		static {
			lookup.put("aaaa", "伤药");
			lookup.put("aaab", "万灵药");
			lookup.put("aaac", "圣水");
			lookup.put("aaag", "伤药");
			lookup.put("aaah", "万灵药");
			lookup.put("aaai", "圣水");
			lookup.put("eaaa", "回复之杖");
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
