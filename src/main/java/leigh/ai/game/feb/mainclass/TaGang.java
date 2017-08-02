package leigh.ai.game.feb.mainclass;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.TagangMissionService;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.UnicodeReader;

public class TaGang {

	public static void main(String[] args) {
		if(args == null || args.length == 0) {
			System.out.println("请指定账号文件路径。");
			System.exit(1);
		}
		List<String[]> accounts = new LinkedList<String[]>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new UnicodeReader(new FileInputStream(args[0]), "utf8"));
			String line = br.readLine();
			while(line != null) {
				if(line.trim().equals("")) {
					continue;
				}
				String[] spl = line.split("=", 2);
				if(spl.length < 2) {
					continue;
				}
				accounts.add(spl);
				System.out.println(spl[0] + " 领取塔港任务。");
				LoginService.login(spl[0], spl[1]);
				TagangMissionService.takeMission();
				LoginService.logout();
				FakeSleepUtil.sleep(2, 4);
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
		for(String[] account: accounts) {
			try {
				System.out.println(account[0] + " 领取塔港任务。");
				LoginService.login(account[0], account[1]);
				TagangMissionService.doMissions();
				LoginService.logout();
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(account[0] + "做塔港时异常");
			}
		}
		System.out.println("程序执行完毕。");
	}
}
