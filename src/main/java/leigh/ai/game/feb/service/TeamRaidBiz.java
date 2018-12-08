package leigh.ai.game.feb.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.service.multiAccount.Account;
import leigh.ai.game.feb.service.raid.RaidStopReason;
import leigh.ai.game.feb.service.status.Item;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.util.HttpUtil;

public class TeamRaidBiz {
	private static final Logger logger = LoggerFactory.getLogger(TeamRaidBiz.class);
	
	public static void helpLaofan(String[] args) {
		boolean[] isZhenshi = parseIsZhenshi(args[0]);
		Account[] accounts = new Account[isZhenshi.length + 1];
		Account dahao = new Account(args[1], args[2]);
		accounts[0] = dahao;
		for(int i = 3; i < args.length; i += 2) {
			Account xiaohao = new Account(args[i], args[i + 1]);
			accounts[i / 2] = xiaohao;
		}
		MultiAccountService.login(accounts);
		
		int battlePerson = 0;
		
		for(int i = 0; i < accounts.length; i++) {
			MultiAccountService.activate(i);
			MoveService.moveTo(1113);
			if(i == 0) {
				RaidService.repairAllWeapons();
			}
			RaidService.ensureHolywaterOutside();
		}
		for(int i = 0; i < accounts.length; i++) {
			MultiAccountService.activate(i);
			MoveService.enterTower();
		}
		
		int firstEnemyPosition = RaidService.firstEnemyPosition(-1);
		while(firstEnemyPosition >= 0) {
			int whoEngagedFirstEnemy = -1;
			for(int i = 0; i < accounts.length; i++) {
				MultiAccountService.activate(i);
				RaidService.moveNoBattleUntil(-1, firstEnemyPosition);
				if(whoEngagedFirstEnemy != battlePerson && RaidService.myPosition == firstEnemyPosition) {
					whoEngagedFirstEnemy = i;
				}
			}
			
			if(whoEngagedFirstEnemy == -1) {
				MultiAccountService.activate(battlePerson);
				do {
					RaidService.exit();
					MoveService.enterTower();
					while(RaidService.myPosition < firstEnemyPosition) {
						RaidService.move();
					}
				} while (RaidService.myPosition > firstEnemyPosition);
			} else if(whoEngagedFirstEnemy != battlePerson) {
				MultiAccountService.activate(whoEngagedFirstEnemy);
				if(PersonStatusService.AP < 50) {
					RaidService.addAp();
				}
				MultiAccountService.askForHelp();
				PersonStatusService.AP -= 50;
				MultiAccountService.activate(battlePerson);
			} else {
				MultiAccountService.activate(battlePerson);
				if(PersonStatusService.AP < 50) {
					RaidService.addAp();
				}
				MultiAccountService.askForHelp();
				PersonStatusService.AP -= 50;
			}
			
			RaidStopReason rsr = RaidService.raidBattle();
			while(rsr != null) {
				switch(rsr) {
				case noAp:
				case noHeal:
					int location = PersonStatusService.currentLocation, position = RaidService.myPosition;
					RaidService.exit();
					RaidService.repairAllWeapons();
					if(!JobService.canUseStaff()) {
						RaidService.ensureHolywaterOutside();
					}
					BattleService.buyMedicine();
					MoveService.enterTower();
					RaidService.moveNoBattleUntil(location, position);
					for(int i = 1; i < accounts.length; i++) {
						if(MultiAccountService.status.get(i).getRaidMapPosition() == position) {
							MultiAccountService.activate(i);
							if(PersonStatusService.AP < 50) {
								RaidService.addAp();
							}
							MultiAccountService.askForHelp();
							PersonStatusService.AP -= 50;
							MultiAccountService.activate(battlePerson);
							break;
						}
					}
					break;
				default:
					break;
				}
				rsr = RaidService.raidBattle();
			}
			RaidService.addDeadPosition();
			firstEnemyPosition = RaidService.firstEnemyPosition(-1);
		}
		for(int i = 0; i < accounts.length; i++) {
			MultiAccountService.activate(i);
			RaidService.moveNoBattleUntil(-2, 0);
		}
		
		firstEnemyPosition = RaidService.firstEnemyPosition(-2);
		while(firstEnemyPosition < 6) {
			int whoEngagedFirstEnemy = -1;
			for(int i = 0; i < accounts.length; i++) {
				MultiAccountService.activate(i);
				while(RaidService.myPosition < firstEnemyPosition) {
					RaidService.move();
				}
				if(whoEngagedFirstEnemy == -1 && RaidService.myPosition == firstEnemyPosition) {
					whoEngagedFirstEnemy = i;
				}
			}
			
			if(whoEngagedFirstEnemy == -1) {
				MultiAccountService.activate(0);
				do {
					RaidService.exit();
					MoveService.enterTower();
					while(RaidService.myPosition < firstEnemyPosition) {
						RaidService.move();
					}
				} while (RaidService.myPosition > firstEnemyPosition);
			} else if(whoEngagedFirstEnemy > 0) {
				MultiAccountService.activate(whoEngagedFirstEnemy);
				if(PersonStatusService.AP < 50) {
					RaidService.addAp();
				}
				MultiAccountService.askForHelp();
				PersonStatusService.AP -= 50;
				MultiAccountService.activate(0);
			} else {
				MultiAccountService.activate(0);
			}
			
			RaidStopReason rsr = RaidService.raidBattle();
			while(rsr != null) {
				switch(rsr) {
				case noAp:
				case noHeal:
				case noWeapon:
					int location = PersonStatusService.currentLocation, position = RaidService.myPosition;
					RaidService.exit();
					RaidService.repairAllWeapons();
					RaidService.ensureHolywaterOutside();
					BattleService.buyMedicine();
					MoveService.enterTower();
					RaidService.moveNoBattleUntil(location, position);
					if(RaidService.myPosition > position) {
						
					}
					break;
				default:
					break;
				}
				rsr = RaidService.raidBattle();
			}
			RaidService.addDeadPosition();
			firstEnemyPosition = RaidService.firstEnemyPosition(-2);
		}
		
		for(int i = 0; i < accounts.length; i++) {
			MultiAccountService.activate(i);
			RaidService.moveNoBattleUntil(6);
			String laofanSaid = HttpUtil.get("npc.php?npcid=090");
			if(laofanSaid.contains("真相是什么")) {
				HttpUtil.get("npc.php?npcid=090&act=Q5_13");
			}
		}
		
		for(int i = 1; i < accounts.length; i++) {
			if(!isZhenshi[i - 1]) {
				continue;
			}
			MultiAccountService.activate(i);
			RaidService.moveNoBattleUntil(6);
			String laofanSaid = HttpUtil.get("npc.php?npcid=090");
			if(laofanSaid.contains("我加入你")) {
				if(PersonStatusService.items.size() == 5) {
					for(MyItem t: PersonStatusService.items) {
						if(t.getName().equals(Item.小圣水.getName())
								|| t.getName().equals(Item.小伤药.getName())
								|| t.getName().equals(Item.小万灵.getName())
								|| t.getName().equals(Item.E杖.getName())
								|| t.getName().equals(Item.D杖.getName())
								|| t.getName().equals(Item.C杖.getName())) {
							ItemService.throwItem(t);
						}
					}
				}
				
				List<MyItem> formerItems = new ArrayList<MyItem>(5);
				formerItems.addAll(PersonStatusService.items);
				String joinLaofan = HttpUtil.get("npc.php?npcid=090&act=Q5_14");
				if(joinLaofan.contains("道具已满")) {
					logger.error("{}道具已满", accounts[i].getU());
					continue;
				}
				PersonStatusService.update();
				for(MyItem t: PersonStatusService.items) {
					boolean formerExists = false;
					for(MyItem former: formerItems) {
						if(former.getName().equals(t.getName())) {
							formerExists = true;
							break;
						}
					}
					if(!formerExists) {
						ItemService.useItem(t);
						RaidService.exit();
						break;
					}
				}
			}
		}
		
		MultiAccountService.activate(battlePerson);
		
		for(int i = 0; i < accounts.length; i++) {
			MultiAccountService.activate(i);
			LoginService.logout();
		}
	}

	private static boolean[] parseIsZhenshi(String string) {
		boolean[] result = new boolean[string.length()];
		for(int i = 0; i < string.length(); i++) {
			result[i] = string.charAt(i) == '1';
		}
		return result;
	}
}
