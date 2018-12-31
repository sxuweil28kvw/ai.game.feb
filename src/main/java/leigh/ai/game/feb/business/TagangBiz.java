package leigh.ai.game.feb.business;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.TagangMissionService;
import leigh.ai.game.feb.service.WabaoService;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.UnicodeReader;

public class TagangBiz {
	private static final Logger logger = LoggerFactory.getLogger(TagangBiz.class);

	public static void tagang(String[] args) {
		if(args == null || args.length == 0) {
			System.out.println("请指定账号文件路径。");
			System.exit(1);
		}
		List<String[]> accounts = new LinkedList<String[]>();
		List<String> banana = new LinkedList<String>();
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
				banana.add(account[0] + ": " + TagangMissionService.bananaMissionStatus);
				LoginService.logout();
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(account[0] + "做塔港时异常");
			}
		}
		for(String b: banana) {
			System.out.println(b);
		}
		System.out.println("程序执行完毕。");
	}
	
	public static void takeTagang(String[] args) {
		if(args == null || args.length == 0) {
			System.out.println("请指定账号文件路径。");
			System.exit(1);
		}
		List<String[]> accounts = new LinkedList<String[]>();
		List<String> banana = new LinkedList<String>();
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
		System.out.println("接塔港任务完毕。");
	}

	public static void onlyMowu(String[] args) {
		if(args == null || args.length == 0) {
			System.out.println("请指定账号文件路径。");
			System.exit(1);
		}
		List<String[]> accounts = new LinkedList<String[]>();
		LinkedList<Integer> mahua = new LinkedList<Integer>();
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
			logger.error("指定的账号文件不存在！可能路径写法错误？");
			return;
		} catch (IOException e) {
			logger.error("账号文件读取出错！");
			return;
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
				TagangMissionService.doOnlyMowu();
				mahua.add(PersonStatusService.mahua);
				LoginService.logout();
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(account[0] + "做塔港时异常");
			}
		}
		for(String[] account: accounts) {
			logger.info("{}现有{}麻花.", account[0], mahua.removeFirst());
		}
	}

	public static void tagangDesert(String[] args) {
		if(args == null || args.length == 0) {
			System.out.println("请指定账号文件路径。");
			System.exit(1);
		}
		List<String[]> accounts = new LinkedList<String[]>();
		List<String> bananaStatus = new LinkedList<String>();
		List<String> desertResult = new LinkedList<String>();
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
				System.out.println(account[0] + " 做塔港任务。");
				LoginService.login(account[0], account[1]);
				TagangMissionService.doMissions();
				bananaStatus.add(account[0] + ": " + TagangMissionService.bananaMissionStatus);
				LoginService.logout();
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(account[0] + "做塔港时异常");
			}
		}
		for(String[] account: accounts) {
			try {
				System.out.println(account[0] + " 去沙漠挖宝。");
				LoginService.login(account[0], account[1]);
				desertResult.add(account[0] + WabaoService.desert());
				LoginService.logout();
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(account[0] + "沙漠挖宝异常");
			}
		}
		for(String d: desertResult) {
			System.out.println(d);
		}
		for(String b: bananaStatus) {
			System.out.println(b);
		}
		System.out.println("程序执行完毕。");
	}

	public static void tagangShrine(String[] args) {
		if(args == null || args.length == 0) {
			System.out.println("请指定账号文件路径。");
			System.exit(1);
		}
		List<String[]> accounts = new LinkedList<String[]>();
		List<String> bananaStatus = new LinkedList<String>();
		List<String> shendianResult = new LinkedList<String>();
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
				System.out.println(account[0] + " 做塔港任务。");
				LoginService.login(account[0], account[1]);
				TagangMissionService.doMissions();
				bananaStatus.add(account[0] + ": " + TagangMissionService.bananaMissionStatus);
				LoginService.logout();
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(account[0] + "做塔港时异常");
			}
		}
		for(String[] account: accounts) {
			try {
				System.out.println(account[0] + " 去神殿挖宝。");
				LoginService.login(account[0], account[1]);
				shendianResult.add(account[0] + WabaoService.shrine());
				LoginService.logout();
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println(account[0] + "神殿挖宝异常");
			}
		}
		for(String d: shendianResult) {
			System.out.println(d);
		}
		for(String b: bananaStatus) {
			System.out.println(b);
		}
		System.out.println("程序执行完毕。");
	}
}
