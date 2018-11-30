package leigh.ai.game.feb.business;

import leigh.ai.game.feb.service.BattleService;
import leigh.ai.game.feb.service.FacilityService;
import leigh.ai.game.feb.service.JobService;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MapService;
import leigh.ai.game.feb.service.MoveService;
import leigh.ai.game.feb.service.PersonStatusService;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.service.battle.BattleResult;
import leigh.ai.game.feb.service.status.MyStatus.MyItem;
import leigh.ai.game.feb.util.FakeSleepUtil;

public class ShuaBiz {
	private static int bankSlots;
	private static int shards;
	public static void guozi(String u, String p, String locationName) {
		shua(u, p, locationName, "魔石的碎片", 5);
	}
	public static void jiapian(String u, String p, String locationName) {
		shua(u, p, locationName, "护甲片", 50);
	}
	private static void shua(String u, String p, String locationName, String item, int fullShards) {
		int battleLocation = 0;
		try {
			battleLocation = MapService.nameLookup.get(locationName);
		} catch(Exception e) {
			System.out.println("不认识的地点：" + locationName + "。请参照地图正确写出地点。");
			System.exit(0);
		}
		LoginService.username = u;
		LoginService.password = p;
		readyForBattle();
		if(PersonStatusService.HP < PersonStatusService.maxHP) {
			BattleService.selfHeal(true);
		}
		shards = FacilityService.storeFullShards(item, fullShards);
		bankSlots = FacilityService.queryBankSlots();
		System.out.println("仓库空位数：" + bankSlots);
		while(bankSlots > 0) {
			MoveService.moveTo(battleLocation);
			while(shards < fullShards) {
				BattleInfo info = BattleService.fight(BattleService.searchUntilEnemy());
				for(String s: info.getOtherInfo()) {
					if(s.contains(item)) {
						shards++;
					}
				}
				PersonStatusService.update();
				if(PersonStatusService.weapons.get(0).getAmountLeft() < 1) {
					if(PersonStatusService.money < 2000) {
						FacilityService.drawCash(42000);
					}
					FacilityService.repairWeapon(PersonStatusService.weapons.get(0));
				}
				if(info.getResult().equals(BattleResult.lose)) {
					BattleService.selfHeal();
				}
				double healPercent = JobService.canUseStaff() ? 0.7 : 0.4;
				if(PersonStatusService.HP < PersonStatusService.maxHP * healPercent) {
					BattleService.selfHeal();
				}
				if(PersonStatusService.AP < 10) {
					BattleService.addAp();
				}
				MoveService.moveTo(battleLocation);
			}
			FacilityService.storeFullShards(item, fullShards);
			bankSlots--;
			shards = 0;
			System.out.println("存" + item + "！仓库空位数：" + bankSlots);
		}
		LoginService.logout();
	}
	
	public static void readyForBattle() {
		LoginService.login();
		if(PersonStatusService.money < 1000) {
			FacilityService.drawCash(19000);
		}
		if(PersonStatusService.weapons.get(0).getAmountLeft() < 10) {
			FacilityService.repairWeapon(0);
		}
		boolean haveMedicine = false;
		boolean haveHolywater = false;
		for(int i = 0; i < PersonStatusService.items.size(); i++) {
			MyItem t = PersonStatusService.items.get(i);
			if(t.getName().equals("伤药") || t.getName().equals("万灵药")) {
				haveMedicine = true;
			} else if(t.getName().equals("回复之杖") && JobService.canUseStaff()) {
				haveMedicine = true;
			}
			if(t.getName().equals("圣水") || t.getName().equals("烧酒") || t.getName().equals("卡博雷酒")) {
				haveHolywater = true;
			}
			
		}
		if(!haveMedicine) {
			BattleService.buyMedicine();
		}
		if(!haveHolywater) {
			BattleService.buyHolywater();
		}
		if(PersonStatusService.HP < PersonStatusService.maxHP * 0.4) {
			BattleService.selfHeal();
		}
	}
}
