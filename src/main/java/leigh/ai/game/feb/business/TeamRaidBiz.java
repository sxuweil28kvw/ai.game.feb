package leigh.ai.game.feb.business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import leigh.ai.game.feb.dto.raid.HelpLaofanParam;
import leigh.ai.game.feb.dto.team.ReceivingTeamInviteException;
import leigh.ai.game.feb.dto.team.Team;
import leigh.ai.game.feb.service.BattleService;
import leigh.ai.game.feb.service.FacilityService;
import leigh.ai.game.feb.service.FacilityService.FacilityType;
import leigh.ai.game.feb.service.ItemService;
import leigh.ai.game.feb.service.JobService;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MoveService;
import leigh.ai.game.feb.service.MultiAccountService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.RaidService;
import leigh.ai.game.feb.service.TeamService;
import leigh.ai.game.feb.service.multiAccount.Account;
import leigh.ai.game.feb.service.raid.RaidMapType;
import leigh.ai.game.feb.service.raid.RaidStopReason;
import leigh.ai.game.feb.service.status.Item;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.service.status.PersonStatus;
import leigh.ai.game.feb.util.HttpUtil;

public class TeamRaidBiz {
	private static final Logger logger = LoggerFactory.getLogger(TeamRaidBiz.class);
	
	public static void helpLaofan(Account[] accounts, Boolean[] isZhenshi, String[] upperJobs) {
		MultiAccountService.login(accounts);
		team(accounts, 0, false);
		
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
				MultiAccountService.activate(battlePerson);
				do {
					RaidService.exit();
					MoveService.enterTower();
					RaidService.moveNoBattleUntil(-2, firstEnemyPosition);
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
				case noWeapon:
					int location = PersonStatusService.currentLocation, position = RaidService.myPosition;
					RaidService.exit();
					MoveService.moveTo(1113);
					RaidService.repairAllWeapons();
					RaidService.ensureHolywaterOutside();
					BattleService.buyMedicine();
					MoveService.enterTower();
					RaidService.moveNoBattleUntil(location, position);
					if(RaidService.myPosition > position) {
						for(int i = 0; i < MultiAccountService.status.size(); i++) {
							if(i == battlePerson) {
								continue;
							}
							PersonStatus ps = MultiAccountService.status.get(i);
							if(ps.getRaidMapPosition() == position) {
								MultiAccountService.activate(i);
								if(PersonStatusService.AP < 50) {
									RaidService.addAp();
								}
								MultiAccountService.askForHelp();
								PersonStatusService.AP -= 50;
								MultiAccountService.activate(battlePerson);
							}
						}
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
			if(i == battlePerson) {
				continue;
			}
			MultiAccountService.activate(i);
			RaidService.moveNoBattleUntil(6);
			String laofanSaid = HttpUtil.get("npc.php?npcid=090");
			if(laofanSaid.contains("真相是什么")) {
				HttpUtil.get("npc.php?npcid=090&act=Q5_13");
			} else {
				logger.warn("{}老范对话错误，请检查任务线", accounts[i].getU());
			}
		}
		
		for(int i = 1; i < accounts.length; i++) {
			if(!isZhenshi[i - 1]) {
				continue;
			}
			MultiAccountService.activate(i);
			RaidService.moveNoBattleUntil(6);
			String laofanSaid = HttpUtil.get("npc.php?npcid=090");
			
			if(!laofanSaid.contains("我加入你")) {
				logger.warn("{}老范对话错误，请检查任务线", accounts[i].getU());
				continue;
			}
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
					ItemService.useJobChangeItem(t, upperJobs[i - 1]);
					RaidService.exit();
					break;
				}
			}
		}
		
		MultiAccountService.activate(battlePerson);
		//TODO:杀死老范完成疑团任务
		
		
		for(int i = 0; i < accounts.length; i++) {
			MultiAccountService.activate(i);
			LoginService.logout();
		}
	}

//	private static boolean[] parseIsZhenshi(String string) {
//		boolean[] result = new boolean[string.length()];
//		for(int i = 0; i < string.length(); i++) {
//			result[i] = string.charAt(i) == '1';
//		}
//		return result;
//	}
	
	/***********
	 * 
	 * team结成的仪式。
	 * 
	 * @param accounts
	 * @param leaderIndex
	 * @param login 是否需要先multi登录
	 * 
	 * @return 队长视角的Team。
	 */
	public static Team team(Account[] accounts, int leaderIndex, boolean login) {
		if(login) {
			MultiAccountService.login(accounts);
		}
		for(int i = 0; i < accounts.length; i++) {
			MultiAccountService.activate(i);
			Team myTeam = null;
			try {
				myTeam = TeamService.queryMyTeam();
			} catch(ReceivingTeamInviteException e) {
				TeamService.refuseInviting();
			}
			if(myTeam != null) {
				if(PersonStatusService.currentLocation < 0) {
					RaidService.exit();
				}
				TeamService.quitTeam(myTeam);
			}
		}
		
		MultiAccountService.activate(leaderIndex);
		String teamName = TeamService.createTeam();
		for(int i = 0; i < accounts.length; i++) {
			if(i == leaderIndex) {
				continue;
			}
			TeamService.invite(teamName, accounts[i].getU());
		}
		for(int i = 0; i < accounts.length; i++) {
			if(i == leaderIndex) {
				continue;
			}
			MultiAccountService.activate(i);
			TeamService.acceptInvite(teamName);
		}
		MultiAccountService.activate(leaderIndex);
		Team team = TeamService.queryMyTeam();
		logger.info(team.toString());
		return team;
	}
	
	public static void ruin(Account[] accounts, int healerIndex) {
		team(accounts, healerIndex, true);
		int battlePerson = 0;
		MultiAccountService.activate(0);
		ItemService.ensureItems(Item.铁丝, Item.天马的羽毛M, Item.会员圣水);
		MoveService.moveToFacility(FacilityType.weaponShops());
		RaidService.repairAllWeapons();
		ItemService.equip(Item.天马的羽毛M);
		MultiAccountService.activate(healerIndex);
		FacilityService.drawCash(36000);
		Item[] BStaffs = new Item[5];
		Arrays.fill(BStaffs, Item.B杖);
		ItemService.ensureItems(BStaffs);
		FacilityService.drawCash(36000);
		for(int i = 0; i < accounts.length; i++) {
			MultiAccountService.activate(i);
			MoveService.enterRuin();
		}
		
		List<RaidMapType> ruinMap = RaidService.raidMap.get(-9);
		
		int firstEnemyPosition = RaidService.firstEnemyPosition(-9);
		while(firstEnemyPosition >= 0 && firstEnemyPosition < 12) {
			int whoEngagedFirstEnemy = -1;
			for(int i = 0; i < accounts.length; i++) {
				MultiAccountService.activate(i);
				RaidService.moveNoBattleUntil(-9, firstEnemyPosition);
				if(whoEngagedFirstEnemy != battlePerson && RaidService.myPosition == firstEnemyPosition) {
					whoEngagedFirstEnemy = i;
				}
			}
			switch(ruinMap.get(firstEnemyPosition)) {
			case door:
				MultiAccountService.activate(0);
				ItemService.equip(Item.铁丝);
				RaidService.openDoor();
				ItemService.equip(Item.天马的羽毛M);
				break;
			case enemy:
			case stopingEnemy:
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
				
				RaidStopReason rsr = RaidService.ruinBattle();
				while(rsr != null) {
					switch(rsr) {
					case noAp:
					case noHeal:
						MultiAccountService.activate(healerIndex);
						MultiAccountService.healMate(PersonStatusService.items.get(0), battlePerson);
						MultiAccountService.activate(battlePerson);
						PersonStatusService.HP = PersonStatusService.maxHP;
						PersonStatusService.AP = 100;
						break;
					case noWeapon:
						int location = PersonStatusService.currentLocation;
						int position = RaidService.myPosition;
						RaidService.exit();
						RaidService.repairAllWeapons();
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
				break;
			default:
				logger.warn("ERROR! firstEnemyPosition={}, type={}",
						firstEnemyPosition, ruinMap.get(firstEnemyPosition).name());
				break;
			}
			firstEnemyPosition = RaidService.firstEnemyPosition(-9);
		}
	}

	public static void dai1zhuan(String ymlFile) {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		HelpLaofanParam param = null;
		try {
			param = mapper.readValue(new File(ymlFile), HelpLaofanParam.class);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		helpLaofan(param.getAccounts().toArray(new Account[0]),
				param.getIsZhenshi().toArray(new Boolean[0]),
				param.getUpperJobs().toArray(new String[0]));
	}
}