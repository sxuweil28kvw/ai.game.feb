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
				|| job.equals("德鲁依")) {
			return true;
		}
		return false;
	}
}
