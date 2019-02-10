package leigh.ai.game.feb.service;

import leigh.ai.game.feb.util.HttpUtil;

public class WeaponService {
	public static boolean repairBySkill(String position) {
		String viewWeaponResponse = HttpUtil.get("equip_sw.php?goto=show&type=wep&wrap=" + position);
		if(!viewWeaponResponse.contains("修复")) {
			return false;
		}
		String prepareResponse = HttpUtil.get("equip_reg.php?type=wep&wrap=" + position);
		if(!prepareResponse.contains("修复")) {
			return false;
		}
		String repairResponse = HttpUtil.get("equip_reg.php?goto=reg&type=wep&wrap=" + position);
		return repairResponse.contains("修复完成");
	}
}
