package leigh.ai.game.feb.service;

public class JobService {
	public static boolean canUseStaff() {
		return canUseStaff(PersonStatusService.myjob);
	}
	public static boolean canUseStaff(String job) {
		if(job.equals("神官骑士")
				|| job.equals("圣女")
				|| job.equals("司祭")
				|| job.equals("贤者")
				|| job.equals("德鲁依")
				|| job.equals("女武神")
				|| job.equals("大司祭")
				|| job.equals("大贤者")
				|| job.equals("咒术师")
				) {
			return true;
		}
		return false;
	}
	public static boolean canFly() {
		return canFly(PersonStatusService.myjob);
	}
	private static boolean canFly(String job) {
		if(job == null) {
			return false;
		}
		if(job.equals("蛇龙骑士")
				|| job.equals("翼龙骑士")
				|| job.equals("龙骑统帅")
				|| job.equals("圣龙骑士")
				|| job.equals("隼骑士")
				|| job.equals("圣天马骑士")) {
			return true;
		}
		return false;
	}
	/*************
	 * 
	 * @return 职业所能使用的E武的代码
	 */
	public static String eWeaponCode() {
		String job = PersonStatusService.myjob;
		if(job.equals("蛇龙骑士")
				|| job.equals("龙骑士")
				|| job.equals("天马骑士")
				|| job.equals("翼龙骑士")
				|| job.equals("龙骑统帅")
				|| job.equals("圣龙骑士")
				|| job.equals("轻骑士")
				|| job.equals("圣骑士")
				|| job.equals("白银骑士")
				|| job.equals("黄金骑士")
				|| job.equals("重骑士")
				|| job.equals("隼骑士")
				|| job.equals("圣天马骑士")) {
			return "2101";
		} else if(job.equals("剑士")
				|| job.equals("佣兵")
				|| job.equals("盗贼")
				|| job.equals("剑豪")
				|| job.equals("刺客")
				|| job.equals("密探")
				|| job.equals("剑圣")) {
			return "1101";
		} else if(job.equals("战士")
				|| job.equals("勇者")
				|| job.equals("英雄")
				|| job.equals("将军")
				|| job.equals("元帅")
				|| job.equals("海盗")
				|| job.equals("狂战士")
				|| job.equals("斗士")
				|| job.equals("武姬")
				|| job.equals("斧骑士")
				|| job.equals("铁骑士")
				|| job.equals("大师骑士")
				|| job.equals("霸者")
				|| job.equals("猛士")) {
			return "3101";
		} else if(job.equals("弓箭手")
				|| job.equals("狙击手")
				|| job.equals("神射手")
				|| job.equals("自由骑士")
				|| job.equals("丛林骑士")) {
			return "4101";
		} else if(job.equals("魔法师")
				|| job.equals("贤者")
				|| job.equals("大贤者")
				|| job.equals("魔法骑士")
				|| job.equals("摘星者")) {
			return "5101";
		} else if(job.equals("神官骑士")
				|| job.equals("圣女")
				|| job.equals("女武神")
				|| job.equals("司祭")
				|| job.equals("大司祭")
				|| job.equals("修道士")) {
			return "6101";
		} else if(job.equals("黑暗法师")
				|| job.equals("德鲁依")
				|| job.equals("咒术师")) {
			return "7101";
		}
		return null;
	}
}
