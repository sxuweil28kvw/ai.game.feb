package leigh.ai.game.feb.service.status;

import leigh.ai.game.feb.service.FacilityService.FacilityType;

public enum Item {
	小万灵("aaab", "万灵药", 3, FacilityType.itemshop),
	会员万灵("aaah", "万灵药", 15, FacilityType.itemshopMember),
	小伤药("aaaa", "伤药", 3, FacilityType.itemshop),
	小圣水("aaac", "圣水", 3, FacilityType.itemshop),
	会员圣水("aaai", "圣水", 45, FacilityType.itemshopMember),
	铁丝("aaaf", "铁丝", 30, FacilityType.itemshop),
	会员伤药("aaag", "伤药", 30, FacilityType.itemshopMember),
	E杖("eaaa", "回复之杖", 30, FacilityType.itemshop),
	D杖("eaab", "治疗之杖", 20, FacilityType.itemshop),
	C杖("eaac", "痊愈之杖", 15, FacilityType.itemshop),
	天马的羽毛M("baac", "天马的羽毛M", 5, FacilityType.itemshop),
	B杖("eaad", "治愈之杖", 15, FacilityType.itemshopMember),
	;
	private String code;
	private String name;
	private int maxAmount;
	private FacilityType buyFrom;
	private Item(String code, String name, int maxAmount, FacilityType buyFrom) {
		this.code = code;
		this.name = name;
		this.maxAmount = maxAmount;
		this.buyFrom = buyFrom;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public int getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}
	public FacilityType getBuyFrom() {
		return buyFrom;
	}
	public void setBuyFrom(FacilityType buyFrom) {
		this.buyFrom = buyFrom;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
}
