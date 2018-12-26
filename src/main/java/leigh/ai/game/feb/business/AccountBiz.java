package leigh.ai.game.feb.business;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import leigh.ai.game.feb.parsers.ItemParser;
import leigh.ai.game.feb.service.BattleService;
import leigh.ai.game.feb.service.FacilityService;
import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.JobService;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MissionService;
import leigh.ai.game.feb.service.MoveService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;
import leigh.ai.game.feb.util.UnicodeReader;

public class AccountBiz {
	public static void batchNewAccount(String file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new UnicodeReader(new FileInputStream(file), "utf8"));
			String line = br.readLine();
			while(line != null) {
				if(line.trim().equals("")) {
					continue;
				}
				String[] spl = line.split("\t");
				newAccount(spl[0], spl[1], spl[2], spl[3], spl[4]);
				line = br.readLine();
			}
		} catch(FileNotFoundException e) {
			System.out.println("指定的账号文件不存在！可能路径写法错误？");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("账号文件读取出错！");
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}
	public static void newAccount(String u, String p, String job, String abe, String answers) {
		u = u.trim();
		LoginService.deleteAccount(u, p);
		LoginService.register(u, p, job, abe, answers);
		FakeSleepUtil.sleep(5, 8);
		PersonStatusService.level = 1;
		LoginService.login(u, p);
		HttpUtil.get("newbit.php");
		while(PersonStatusService.maxHP < 25) {
			LoginService.logout();
			LoginService.deleteAccount(u, p);
			LoginService.register(u, p, job, abe, answers);
			LoginService.login(u, p);
			HttpUtil.get("newbit.php");
		}
		HttpUtil.get("useitem_co.php?goto=useitem&wrap=E");
		ItemParser.itemsAfterUse("useitem.php");
		MissionService.newbie();
		MoveService.moveToFacility(FacilityType.weaponshopE);
		FacilityService.buyWeapon(JobService.eWeaponCode());
		lowerJobLevelUp();
		MissionService.yizhuan();
		System.out.println(LoginService.username + "培养完成！");
		LoginService.logout();
	}

	private static void lowerJobLevelUp() {
		BattleService.battleToLevel(12, 1103);
		BattleService.battleToLevel(20, 1126);
	}
	public static void yizhuan(String u, String p) {
		LoginService.login(u, p);
		MissionService.yizhuan();
	}
	
}
