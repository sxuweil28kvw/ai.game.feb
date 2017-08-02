package leigh.ai.game.feb.business;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MapService;
import leigh.ai.game.feb.service.MoveService;
import leigh.ai.game.feb.util.UnicodeReader;

public class MoveBiz {

	public static void allMoveToDesert(String[] args) {
		if(args == null || args.length == 0) {
			System.out.println("请指定账号文件路径。");
			System.exit(1);
		}
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
				System.out.println(spl[0] + " 开始向沙漠移动。");
				LoginService.login(spl[0], spl[1]);
				MoveService.moveTo(2188);
				LoginService.logout();
				line = br.readLine();
			}
		} catch(FileNotFoundException e) {
			System.out.println("指定的账号文件不存在！可能路径写法错误？");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("账号文件读取出错！");
		} finally {
			System.out.println("全部移动完毕。");
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}
	public static void allMoveTo(String[] args) {
		if(args == null || args.length == 0) {
			System.out.println("请指定账号文件路径。");
			System.exit(1);
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new UnicodeReader(new FileInputStream(args[1]), "utf8"));
			String line = br.readLine();
			while(line != null) {
				if(line.trim().equals("")) {
					continue;
				}
				String[] spl = line.split("=", 2);
				if(spl.length < 2) {
					continue;
				}
				System.out.println(spl[0] + " 开始向" + args[0] + "移动。");
				LoginService.login(spl[0], spl[1]);
				MoveService.moveTo(MapService.nameLookup.get(args[0]));
				LoginService.logout();
				line = br.readLine();
			}
		} catch(FileNotFoundException e) {
			System.out.println("指定的账号文件不存在！可能路径写法错误？");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("账号文件读取出错！");
		} finally {
			System.out.println("全部移动完毕。");
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
