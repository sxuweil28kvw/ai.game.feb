package leigh.ai.game.feb.service.status;

public enum Item {
	小万灵("aaab", "万灵药"),
	会员万灵("aaah", "万灵药"),
	小伤药("aaaa", "伤药"),
	小圣水("aaac", "圣水"),
	会员圣水("aaai", "圣水"),
	铁丝("aaaf", "铁丝"),
	会员伤药("aaag", "伤药"),
	E杖("eaaa", "回复之杖"),
	D杖("eaab", "治疗之杖"),
	C杖("eaac", "痊愈之杖"),
	;
	private String code;
	private String name;
	private Item(String code, String name) {
		this.code = code;
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
}
