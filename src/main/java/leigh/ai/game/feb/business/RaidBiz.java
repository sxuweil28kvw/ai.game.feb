package leigh.ai.game.feb.business;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.parsers.NpcParser;
import leigh.ai.game.feb.service.BagService;
import leigh.ai.game.feb.service.BattleService;
import leigh.ai.game.feb.service.FacilityService;
import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.ItemService;
import leigh.ai.game.feb.service.JobService;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MoveService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.RaidService;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.service.battle.BattleResult;
import leigh.ai.game.feb.service.raid.RaidMapType;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.service.status.MyStatus.MyWeapon;
import leigh.ai.game.feb.util.FakeSleepUtil;
import leigh.ai.game.feb.util.HttpUtil;
import leigh.ai.game.feb.util.UnicodeReader;

public class RaidBiz {
	private static final Logger logger = LoggerFactory.getLogger(RaidBiz.class);

	public static void guya(String[] args) {
		String u = "飞飞企鹅";
		String p = "kk82liewuxux";
		if(args.length > 0) {
			u = args[0];
			p = args[1];
		}
		LoginService.login(u, p);
		if(PersonStatusService.bagFree < 10) {
			System.out.println("资源快满了！");
			System.exit(0);
		}
		RaidService.readyForGuya();
		MoveService.moveTo(1114);
		while(true) {
			if(!MoveService.enterTower()) {
				System.err.println("进塔错误：没有队伍、或队伍已经有一个塔进度、或没完成进塔任务。");
				System.exit(1);
			}
			RaidService.move();
			w:
			while(RaidService.myPosition <= 11) {
				switch(RaidService.raidMap.get(PersonStatusService.currentLocation).get(RaidService.myPosition)) {
				case enemy:
				case stopingEnemy:
					if(!RaidService.selfHeal()) {
						break w;
					}
					if(PersonStatusService.AP < 10) {
						if(!RaidService.addAp()) {
							break w;
						}
					}
					HttpUtil.get("pve.php");
					BattleInfo battleInfo = RaidService.battle(5);
					while(!battleInfo.getResult().equals(BattleResult.win)) {
						PersonStatusService.update();
						if(PersonStatusService.weapons.get(0).getAmountLeft() == 0) {
							break w;
						}
						if(!RaidService.selfHeal()) {
							break w;
						}
						if(PersonStatusService.AP < 10) {
							if(!RaidService.addAp()) {
								break w;
							}
						}
						HttpUtil.get("pve.php");
						battleInfo = RaidService.battle(5);
					}
					PersonStatusService.update();
					if(PersonStatusService.weapons.get(0).getAmountLeft() == 0) {
						break w;
					}
					if(!RaidService.selfHeal()) {
						break w;
					}
					if(PersonStatusService.AP < 10) {
						if(!RaidService.addAp()) {
							break w;
						}
					}
				default:
					break;
				}
				RaidService.move();
			}
			RaidService.exit();
			BagService.update();
			if(PersonStatusService.bagFree < 10) {
				System.out.println("资源快满了！");
				System.exit(0);
			}
			RaidService.readyForGuya();
		}
	}
	
	public static void longpi(String[] args) {
		String u1 = args[0];
		String p1 = args[1];
		String u2 = args[2];
		String p2 = args[3];
		int maxTimes = -1;
		if(args.length > 4) {
			maxTimes = Integer.parseInt(args[4]);
		}
		LoginService.login(u1, p1);
		if(PersonStatusService.currentLocation < 0) {
			RaidService.exit();
		}
		LoginService.logout();
		System.out.println(u2 + "进塔卡进度");
		LoginService.login(u2, p2);
		MoveService.moveTo(1114);
		MoveService.enterTower();
		LoginService.logout();
		LoginService.username = u1;
		LoginService.password = p1;
		LoginService.login();
		/*
		for(int i = 0; i < PersonStatusService.weapons.size(); i++) {
			MyWeapon w = PersonStatusService.weapons.get(i);
			if(w.getName().equals("勇者之剑")) {
				if(w.getAmountLeft() < WeaponType.勇者之剑.getQuantity()) {
					FacilityService.repairWeapon(w);
				}
				continue;
			}
			WeaponType wt = WeaponType.valueOf(w.getName());
			if(wt == null) {
				if(!FacilityService.saveWeaponToBank(w)) {
					System.out.println("身上携带有不明武器，不知如何处理！");
				}
				i--;
				continue;
			} else if(wt.getLevel().charAt(0) < 'B') {
				FacilityService.sellWeapon(w);
				i--;
				continue;
			}
		}
		if(PersonStatusService.weapons.size() < 5) {
			MoveService.movePath(MapService.findFacility(PersonStatusService.currentLocation, new FacilityType[] {FacilityType.weaponshopB}));
			for(int i = PersonStatusService.weapons.size(); i < 5; i++) {
				FacilityService.buyWeapon("1232");
			}
		}
		*/
		BattleService.selfHeal(true);
		ensureTiesi();
		ensureHolywater();
		ensureMedicine();
		
		if(PersonStatusService.bagFree < 20) {
			System.out.println("资源快满了！");
			System.exit(0);
		}
		int times = 0;
		while(true) {
			MoveService.moveTo(1114);
			MoveService.enterTower();
			
			RaidService.move();
			boolean pause = false;
			w:
			while(PersonStatusService.currentLocation != -4) {
				switch(RaidService.raidMap.get(PersonStatusService.currentLocation).get(RaidService.myPosition)) {
				case enemy:
				case stopingEnemy:
					if(RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation) &&
							RaidService.deadEnemies.get(PersonStatusService.currentLocation).contains(RaidService.myPosition)) {
						break;
					}
					HttpUtil.get("pve.php");
					int battleTurns = 5;
					if(PersonStatusService.currentLocation == -2 && RaidService.myPosition == 17) {
						battleTurns = 3;
					}
					BattleInfo battleInfo = RaidService.battle(battleTurns);
					while(!battleInfo.getResult().equals(BattleResult.win)) {
						if(!RaidService.ensureWeapon()) {
							pause = true;
							break w;
						}
						if(battleInfo.getResult().equals(BattleResult.lose)) {
							if(!RaidService.selfHeal()) {
								pause = true;
								break w;
							}
						} else if(PersonStatusService.HP < 10) {
							if(!RaidService.selfHeal()) {
								pause = true;
								break w;
							}
						}
						if(PersonStatusService.AP < 10) {
							if(!RaidService.addAp()) {
								pause = true;
								break w;
							}
						}
						HttpUtil.get("pve.php");
						battleInfo = RaidService.battle(battleTurns);
						FakeSleepUtil.sleep(3, 4);
					}
					if(!RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation)) {
						Set<Integer> value = new HashSet<Integer>();
						value.add(RaidService.myPosition);
						RaidService.deadEnemies.put(PersonStatusService.currentLocation, value);
					} else {
						RaidService.deadEnemies.get(PersonStatusService.currentLocation).add(RaidService.myPosition);
					}
					if(!RaidService.ensureWeapon()) {
						pause = true;
						break w;
					}
					if(PersonStatusService.HP < 10 && !RaidService.selfHeal()) {
						pause = true;
						break w;
					}
					if(PersonStatusService.AP < 10) {
						if(!RaidService.addAp()) {
							pause = true;
							break w;
						}
					}
					
					break;
				case door:
					if(RaidService.deadEnemies.get(PersonStatusService.currentLocation).contains(RaidService.myPosition)) {
						break;
					}
					RaidService.openDoor();
					break;
				default:
					break;
				}
				RaidService.move();
			}
			if(!pause) {
				for(int i = 0; i < 4; i++) {
					RaidService.move();
					RaidService.openChest(PersonStatusService.userId, u1);
				}
				RaidService.exit();
				LoginService.logout();
				System.out.println(u2 + "重新进塔卡进度");
				LoginService.login(u2, p2);
				RaidService.exit();
				MoveService.enterTower();
				LoginService.logout();
				LoginService.login(u1, p1);
				RaidService.deadEnemies.clear();
			} else {
				RaidService.exit();
			}
			if(PersonStatusService.bagFree < 20) {
				System.out.println("资源快满了！");
				System.exit(0);
			}
			prepare();
			times++;
			if(maxTimes > 0 && times >= maxTimes) {
				logger.info("已刷龙皮{}次，结束！", times);
				break;
			}
		}
	}

	public static void moyan(String[] args) {
		String u1 = args[0];
		String p1 = args[1];
		String u2 = args[2];
		String p2 = args[3];
		LoginService.login(u1, p1);
		if(PersonStatusService.currentLocation < 0) {
			RaidService.exit();
		}
		LoginService.logout();
		System.out.println(u2 + "进塔卡进度");
		LoginService.login(u2, p2);
		MoveService.moveTo(1114);
		MoveService.enterTower();
		LoginService.logout();
		LoginService.username = u1;
		LoginService.password = p1;
		LoginService.login();
		BattleService.selfHeal(true);
		prepare();
		
		if(PersonStatusService.bagFree < 20) {
			System.out.println("资源快满了！");
			System.exit(0);
		}
		while(true) {
			MoveService.moveTo(1114);
			MoveService.enterTower();
			
			RaidService.move();
			boolean pause = false;
			w:
			while(PersonStatusService.currentLocation != -3) {
				switch(RaidService.raidMap.get(PersonStatusService.currentLocation).get(RaidService.myPosition)) {
				case enemy:
				case stopingEnemy:
					if(RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation) &&
							RaidService.deadEnemies.get(PersonStatusService.currentLocation).contains(RaidService.myPosition)) {
						break;
					}
					HttpUtil.get("pve.php");
					int battleTurns = 5;
					if(PersonStatusService.currentLocation == -2 && RaidService.myPosition == 17) {
						battleTurns = 3;
					}
					BattleInfo battleInfo = RaidService.battle(battleTurns);
					while(!battleInfo.getResult().equals(BattleResult.win)) {
						if(!RaidService.ensureWeapon()) {
							pause = true;
							break w;
						}
						if(battleInfo.getResult().equals(BattleResult.lose)) {
							if(!RaidService.selfHeal()) {
								pause = true;
								break w;
							}
						} else if(PersonStatusService.HP < 10) {
							if(!RaidService.selfHeal()) {
								pause = true;
								break w;
							}
						}
						if(PersonStatusService.AP < 10) {
							if(!RaidService.addAp()) {
								pause = true;
								break w;
							}
						}
						HttpUtil.get("pve.php");
						battleInfo = RaidService.battle(battleTurns);
					}
					if(!RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation)) {
						Set<Integer> value = new HashSet<Integer>();
						value.add(RaidService.myPosition);
						RaidService.deadEnemies.put(PersonStatusService.currentLocation, value);
					} else {
						RaidService.deadEnemies.get(PersonStatusService.currentLocation).add(RaidService.myPosition);
					}
					if(!RaidService.ensureWeapon()) {
						pause = true;
						break w;
					}
					if(PersonStatusService.HP < 10 && !RaidService.selfHeal()) {
						pause = true;
						break w;
					}
					if(PersonStatusService.AP < 10) {
						if(!RaidService.addAp()) {
							pause = true;
							break w;
						}
					}
					
					break;
				case door:
					if(RaidService.deadEnemies.get(PersonStatusService.currentLocation).contains(RaidService.myPosition)) {
						break;
					}
					RaidService.openDoor();
					break;
				default:
					break;
				}
				RaidService.move();
			}
			if(!pause) {
				BattleInfo battleInfo = RaidService.battle(5);
				while(!battleInfo.getResult().equals(BattleResult.win)) {
					if(!RaidService.ensureWeapon()) {
						pause = true;
					}
					if(battleInfo.getResult().equals(BattleResult.lose)) {
						if(!RaidService.selfHeal()) {
							pause = true;
						}
					} else if(PersonStatusService.HP < 10) {
						if(!RaidService.selfHeal()) {
							pause = true;
						}
					}
					if(PersonStatusService.AP < 10) {
						if(!RaidService.addAp()) {
							pause = true;
						}
					}
					HttpUtil.get("pve.php");
					battleInfo = RaidService.battle(5);
					FakeSleepUtil.sleep(1, 2);
				}
				RaidService.exit();
				if(!pause) {
					LoginService.logout();
					System.out.println(u2 + "重新进塔卡进度");
					LoginService.login(u2, p2);
					RaidService.exit();
					MoveService.enterTower();
					LoginService.logout();
					LoginService.login(u1, p1);
					RaidService.deadEnemies.clear();
				}
			} else {
				RaidService.exit();
			}
			if(PersonStatusService.bagFree < 20) {
				System.out.println("资源快满了！");
				System.exit(0);
			}
			prepare();
		}
	}
	
	public static void ta6(String propertyFile) {
		Properties prop = new Properties();
		try {
			BufferedReader br = new BufferedReader(new UnicodeReader(new FileInputStream(propertyFile), "utf8"));
			String line = br.readLine();
			while(line != null) {
				if(line.trim().equals("") || line.startsWith("#")) {
					continue;
				}
				String[] spl = line.split("=", 2);
				prop.put(spl[0], spl[1]);
				
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		String uAss = prop.getProperty("刺客");
		String pAss = prop.getProperty("密码" + uAss);
		String uVlk = prop.getProperty("圣女");
		String pVlk = prop.getProperty("密码" + uVlk);
		
		ta6(uAss, pAss, uVlk, pVlk);
	}
	
	public static void ta6Once(String propertyFile) {
		Properties prop = new Properties();
		try {
			BufferedReader br = new BufferedReader(new UnicodeReader(new FileInputStream(propertyFile), "utf8"));
			String line = br.readLine();
			while(line != null) {
				if(line.trim().equals("") || line.startsWith("#")) {
					continue;
				}
				String[] spl = line.split("=", 2);
				prop.put(spl[0], spl[1]);
				
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		String uAss = prop.getProperty("刺客");
		String pAss = prop.getProperty("密码" + uAss);
		String uVlk = prop.getProperty("圣女");
		String pVlk = prop.getProperty("密码" + uVlk);
		
		LoginService.login(uAss, pAss);
		if(PersonStatusService.currentLocation < 0) {
			RaidService.exit();
		}
		LoginService.logout();
		System.out.println(uVlk + "进塔卡进度");
		LoginService.login(uVlk, pVlk);
		
		if(PersonStatusService.bagFree < 70) {
			System.out.println("圣女资源快满了！");
			LoginService.logout();
			System.exit(0);
		}
		
		prepare();
		
		MoveService.moveTo(1114);
		MoveService.enterTower();
		LoginService.logout();
		
		LoginService.login(uAss, pAss);
		
		if(PersonStatusService.bagFree < 15) {
			System.out.println("刺客资源快满了！");
			LoginService.logout();
			System.exit(0);
		}
		BattleService.selfHeal(true);
		prepare();
		
		MoveService.moveTo(1114);
		MoveService.enterTower();
		
		toTa5(uAss);
		
		RaidService.ta5();
		BattleInfo battleInfo = RaidService.battle(5);
		while(!battleInfo.getResult().equals(BattleResult.win)) {
			if(!RaidService.ensureWeapon()) {
				reEnterTower();
				toTa5(uAss);
			}
			if(PersonStatusService.AP < 10) {
				if(!RaidService.addAp()) {
					reEnterTower();
					toTa5(uAss);
				}
			}
			battleInfo = RaidService.battle(5);
			
		}
		//上6楼
		RaidService.move();
		LoginService.logout();
		
		LoginService.login(uVlk, pVlk);
		//坐电梯到5楼；圣女打前4楼的逻辑尚未实现
		MoveService.enterTower(5);
		
		battleTa6();
		
		LoginService.logout();
		
		LoginService.login(uAss, pAss);
		while(RaidService.myPosition < 24) {
			RaidService.move();
			if(RaidService.raidMap.get(PersonStatusService.currentLocation).get(RaidService.myPosition)
					.equals(RaidMapType.chest)) {
				RaidService.openChest(PersonStatusService.userId, uAss);
			}
		}
		LoginService.logout();
		logger.info("塔6一次完毕！");
	}
	

	private static void battleTa6() {
		BattleInfo battleInfo = null;
		w6:
			while(RaidService.myPosition < 22) {
				RaidService.move();
				RaidMapType mapType = RaidService.raidMap.get(PersonStatusService.currentLocation).get(RaidService.myPosition);
				if(mapType.equals(RaidMapType.enemy) || mapType.equals(RaidMapType.stopingEnemy)) {
					if(RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation) &&
							RaidService.deadEnemies.get(PersonStatusService.currentLocation).contains(RaidService.myPosition)) {
					} else {
						battleInfo = RaidService.battle(5);
						while(!battleInfo.getResult().equals(BattleResult.win)) {
							if(!RaidService.ensureWeapon()) {
								RaidService.exit();
								prepare();
								MoveService.moveTo(1114);
								MoveService.enterTower(5);
								continue w6;
							}
							if(battleInfo.getResult().equals(BattleResult.lose)) {
								if(!RaidService.selfHeal()) {
									RaidService.exit();
									prepare();
									MoveService.moveTo(1114);
									MoveService.enterTower(5);
									continue w6;
								}
							} else if(PersonStatusService.HP < 35) {
								if(!RaidService.selfHeal()) {
									RaidService.exit();
									prepare();
									MoveService.moveTo(1114);
									MoveService.enterTower(5);
									continue w6;
								}
							}
							if(PersonStatusService.AP < 10) {
								if(!RaidService.addAp()) {
									RaidService.exit();
									prepare();
									MoveService.moveTo(1114);
									MoveService.enterTower(5);
									continue w6;
								}
							}
							battleInfo = RaidService.battle(5);
						}
						RaidService.addDeadPosition();
						
						if(!RaidService.ensureWeapon()) {
							RaidService.exit();
							prepare();
							MoveService.moveTo(1114);
							MoveService.enterTower(5);
							continue w6;
						}
						
						if(PersonStatusService.HP < 35) {
							if(!RaidService.selfHeal()) {
								RaidService.exit();
								prepare();
								MoveService.moveTo(1114);
								MoveService.enterTower(5);
								continue w6;
							}
						}
						if(PersonStatusService.AP < 10) {
							if(!RaidService.addAp()) {
								RaidService.exit();
								prepare();
								MoveService.moveTo(1114);
								MoveService.enterTower(5);
								continue w6;
							}
						}
					}
				}
			}
	}

	/************
	 * 打到塔5；
	 * 必须先进塔。
	 */
	private static void toTa5(String userNameAssassin) {
		RaidService.move();
		w:
		while(PersonStatusService.currentLocation != -5) {
			switch(RaidService.raidMap.get(PersonStatusService.currentLocation).get(RaidService.myPosition)) {
			case enemy:
			case stopingEnemy:
				if(RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation) &&
						RaidService.deadEnemies.get(PersonStatusService.currentLocation).contains(RaidService.myPosition)) {
					break;
				}
				HttpUtil.get("pve.php");
				int battleTurns = RaidService.determinTurns();
				
				BattleInfo battleInfo = RaidService.battle(battleTurns);
				while(!battleInfo.getResult().equals(BattleResult.win)) {
					if(!RaidService.ensureWeapon()) {
						reEnterTower();
						continue w;
					}
					if(battleInfo.getResult().equals(BattleResult.lose)) {
						if(!RaidService.selfHeal()) {
							reEnterTower();
							continue w;
						}
					} else if(PersonStatusService.HP < 10) {
						if(!RaidService.selfHeal()) {
							reEnterTower();
							continue w;
						}
					}
					if(PersonStatusService.AP < 10) {
						if(!RaidService.addAp()) {
							reEnterTower();
							continue w;
						}
					}
					HttpUtil.get("pve.php");
					battleInfo = RaidService.battle(battleTurns);
				}
				RaidService.addDeadPosition();
				if(!RaidService.ensureWeapon()) {
					reEnterTower();
					continue w;
				}
				if(PersonStatusService.HP < 10 && !RaidService.selfHeal()) {
					reEnterTower();
					continue w;
				}
				if(PersonStatusService.AP < 10) {
					if(!RaidService.addAp()) {
						reEnterTower();
						continue w;
					}
				}
				
				break;
			case door:
				if(RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation) &&
						RaidService.deadEnemies.get(PersonStatusService.currentLocation).contains(RaidService.myPosition)) {
					break;
				}
				RaidService.openDoor();
				break;
			case shop:
				RaidService.repairAllWeapons();
				break;
			case chest:
				//不开前三层箱子……
				if(PersonStatusService.currentLocation >= -3) {
					break;
				}
				if(RaidService.deadEnemies.containsKey(PersonStatusService.currentLocation) &&
						RaidService.deadEnemies.get(PersonStatusService.currentLocation).contains(RaidService.myPosition)) {
					break;
				}
				RaidService.openChest(PersonStatusService.userId, userNameAssassin);
				break;
			default:
				break;
			}
			RaidService.move();
		}
	}
	
	private static void reEnterTower() {
		RaidService.exit();
		prepare();
		MoveService.moveTo(1114);
		MoveService.enterTower();
	}

	private static void ensureMedicine() {
		List<MyItem> medicines = new LinkedList<MyItem>();
		for(MyItem t: PersonStatusService.items) {
			if(t.getName().equals("万灵药")) {
				medicines.add(t);
			}
		}
		for(int i = 0; i < medicines.size(); i++) {
			MyItem t = medicines.get(i);
			if(t.getAmountLeft() < 15) {
				FacilityService.sellItem(t);
			}
		}
		medicines.clear();
		for(MyItem t: PersonStatusService.items) {
			if(t.getName().equals("万灵药")) {
				medicines.add(t);
			}
		}
		if(medicines.size() < 2) {
			for(int i = medicines.size(); i < 2; i++) {
				MoveService.moveToFacility(FacilityType.itemshopMember);
				FacilityService.buyItem("aaah");
			}
		}
	}

	private static void ensureHolywater() {
		MyItem holywater = null;
		for(MyItem t: PersonStatusService.items) {
			if(t.getName().equals("圣水") || t.getName().equals("烧酒") || t.getName().equals("卡博雷酒")) {
				holywater = t;
				break;
			}
		}
		if(holywater == null) {
			MoveService.moveToFacility(FacilityType.itemshopMember);
			FacilityService.buyItem("aaai");
		} else if(holywater.getAmountLeft() < 5) {
			FacilityService.sellItem(holywater);
			MoveService.moveToFacility(FacilityType.itemshopMember);
			FacilityService.buyItem("aaai");
		}
	}
	
	private static void ensureTiesi() {
		MyItem tiesi = null;
		for(MyItem t: PersonStatusService.items) {
			if(t.getName().equals("铁丝")) {
				tiesi = t;
				break;
			}
		}
		if(tiesi == null) {
			MoveService.moveToFacility(FacilityType.itemshop);
			FacilityService.buyItem("aaaf");
			for(MyItem t: PersonStatusService.items) {
				if(t.getName().equals("铁丝")) {
					tiesi = t;
					break;
				}
			}
		}
		if(!tiesi.getPosition().equals("E")) {
			ItemService.equipItem(tiesi);
		}
		if(tiesi.getAmountLeft() < 15) {
			FacilityService.sellItem(tiesi);
			MoveService.moveToFacility(FacilityType.itemshop);
			FacilityService.buyItem("aaaf");
		}
	}

	private static void prepare() {
		for(MyWeapon w: PersonStatusService.weapons) {
			FacilityService.repairWeapon(w);
		}
		BattleService.selfHeal(true);
		if(PersonStatusService.myjob.equals("刺客") || PersonStatusService.myjob.equals("密探")) {
			BattleService.addAp(true);
			ensureTiesi();
			ensureHolywater();
			ensureMedicine();
		} else if(JobService.canUseStaff()) {
			RaidService.buy5Staff();
		}
		FacilityService.drawCash(35000);
	}

	public static void ta5(String u, String p) {
		LoginService.username = u;
		LoginService.password = p;
		LoginService.login();
		RaidService.ta5();
	}

	public static void ta6(String uAss, String pAss, String uVlk, String pVlk) {
		LoginService.login(uAss, pAss);
		if(PersonStatusService.currentLocation < 0) {
			RaidService.exit();
		}
		LoginService.logout();
		System.out.println(uVlk + "进塔卡进度");
		LoginService.login(uVlk, pVlk);
		
		if(PersonStatusService.bagFree < 70) {
			System.out.println("圣女资源快满了！");
			LoginService.logout();
			System.exit(0);
		}
		
		prepare();
		
		MoveService.moveTo(1114);
		MoveService.enterTower();
		LoginService.logout();
		
		LoginService.login(uAss, pAss);
		
		if(PersonStatusService.bagFree < 15) {
			System.out.println("刺客资源快满了！");
			LoginService.logout();
			System.exit(0);
		}
		BattleService.selfHeal(true);
		prepare();
		
		MoveService.moveTo(1114);
		MoveService.enterTower();
		
		toTa5(uAss);
		
		RaidService.ta5();
		BattleInfo battleInfo = RaidService.battle(5);
		while(!battleInfo.getResult().equals(BattleResult.win)) {
			if(!RaidService.ensureWeapon()) {
				reEnterTower();
				toTa5(uAss);
			}
			if(PersonStatusService.AP < 10) {
				if(!RaidService.addAp()) {
					reEnterTower();
					toTa5(uAss);
				}
			}
			battleInfo = RaidService.battle(5);
		}
		//上6楼
		RaidService.move();
		LoginService.logout();
		
		LoginService.login(uVlk, pVlk);
		//坐电梯到5楼；圣女打前4楼的逻辑尚未实现
		MoveService.enterTower(5);
		
		battleTa6();
		
		LoginService.logout();
		
		LoginService.login(uAss, pAss);
		while(RaidService.myPosition < 24) {
			RaidService.move();
			if(RaidService.raidMap.get(PersonStatusService.currentLocation).get(RaidService.myPosition)
					.equals(RaidMapType.chest)) {
				RaidService.openChest(PersonStatusService.userId, uAss);
			}
		}
		logger.info("塔6第一轮完毕！");
		
		int ta6Times = 1;
		
		String selenaSaid = NpcParser.parse(HttpUtil.get("npc.php?npcid=302"));
		List<String> summoners = NpcParser.parseSelenaSummoners(selenaSaid);
		while(summoners.size() > 0) {
			if(!RaidService.ensureWeapon()) {
				RaidService.exit();
				prepare();
				MoveService.moveTo(1114);
				MoveService.enterTower(5);
				RaidService.move();
				RaidService.moveNoBattleUntil(24);
			}
			
			HttpUtil.get(summoners.get(0));
			battleInfo = RaidService.battle(5);
			while(!battleInfo.getResult().equals(BattleResult.win)) {
				if(!RaidService.ensureWeapon()) {
					RaidService.exit();
					prepare();
					MoveService.moveTo(1114);
					MoveService.enterTower(5);
					RaidService.move();
					RaidService.moveNoBattleUntil(24);
				}
				if(battleInfo.getResult().equals(BattleResult.lose)) {
					if(!RaidService.selfHeal()) {
						RaidService.exit();
						prepare();
						MoveService.moveTo(1114);
						MoveService.enterTower(5);
						RaidService.move();
						RaidService.moveNoBattleUntil(24);
					}
				} else if(PersonStatusService.HP < 35) {
					if(!RaidService.selfHeal()) {
						RaidService.exit();
						prepare();
						MoveService.moveTo(1114);
						MoveService.enterTower(5);
						RaidService.move();
						RaidService.moveNoBattleUntil(24);
					}
				}
				if(PersonStatusService.AP < 10) {
					if(!RaidService.addAp()) {
						RaidService.exit();
						prepare();
						MoveService.moveTo(1114);
						MoveService.enterTower(5);
						RaidService.move();
						RaidService.moveNoBattleUntil(24);
					}
				}
				battleInfo = RaidService.battle(5);
			}
			
//			RaidService.myPosition = 0;
			RaidService.recallTa6Monsters();
			
			LoginService.logout();
			LoginService.login(uVlk, pVlk);
			
			if(!RaidService.ensureWeapon()) {
				RaidService.exit();
				prepare();
				MoveService.moveTo(1114);
				MoveService.enterTower(5);
				RaidService.move();
			}
			if(battleInfo.getResult().equals(BattleResult.lose)) {
				if(!RaidService.selfHeal()) {
					RaidService.exit();
					prepare();
					MoveService.moveTo(1114);
					MoveService.enterTower(5);
					RaidService.move();
				}
			} else if(PersonStatusService.HP < 35) {
				if(!RaidService.selfHeal()) {
					RaidService.exit();
					prepare();
					MoveService.moveTo(1114);
					MoveService.enterTower(5);
					RaidService.move();
				}
			}
			if(PersonStatusService.AP < 10) {
				if(!RaidService.addAp()) {
					RaidService.exit();
					prepare();
					MoveService.moveTo(1114);
					MoveService.enterTower(5);
					RaidService.move();
				}
			}
			
			battleTa6();

			logger.info("塔6第" + (++ta6Times) + "轮完毕");
			if(ta6Times >= 5) {
				break;
			}
			
			LoginService.logout();
			LoginService.login(uAss, pAss);
			RaidService.moveNoBattleUntil(24);
			selenaSaid = NpcParser.parse(HttpUtil.get("npc.php?npcid=302"));
			summoners = NpcParser.parseSelenaSummoners(selenaSaid);
		}
	}
}
