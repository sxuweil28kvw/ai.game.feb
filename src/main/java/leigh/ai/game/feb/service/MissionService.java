package leigh.ai.game.feb.service;

import leigh.ai.game.feb.parsers.MoveParser;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.util.HttpUtil;

public class MissionService {
	public static void newbie() {
		MoveService.moveTo(1102);
		HttpUtil.get("npc.php?npcid=117");
		HttpUtil.get("npc.php?npcid=117&act=NQ16_1");
		HttpUtil.get("npc.php?npcid=236");
		HttpUtil.get("npc.php?npcid=236&act=NQ0016_2");
		PersonStatusService.update();
		
		//拉克村
		MoveService.moveTo(1107);
		//买铁剑
		FacilityService.buyWeapon("1101");
		
		//鲁内斯城
		MoveService.moveTo(1102);
		HttpUtil.get("npc.php?npcid=236");
		//交铁剑
		HttpUtil.get("npc.php?npcid=236&act=NQ0016_3");
		
		//沙洲哨站
		MoveService.moveTo(1142);
		//弗鲁迪
		HttpUtil.get("npc.php?npcid=116");
		//兄弟任务
		HttpUtil.get("npc.php?npcid=116&act=Q4_ON");
		//买钢枪
		FacilityService.buyWeapon("2111");
		
		//鲁内斯城
		MoveService.moveTo(1102);
		HttpUtil.get("npc.php?npcid=236");
		//交钢枪
		HttpUtil.get("npc.php?npcid=236&act=NQ0016_4");
		
		//边境缪兰
		MoveService.moveTo(1113);
		//弗朗茨
		HttpUtil.get("npc.php?npcid=104");
		//兄弟任务
		HttpUtil.get("npc.php?npcid=104&act=Q4_1");
		
		//塔港
		MoveService.moveTo(1161);
		//弗雷塔泽
		HttpUtil.get("npc.php?npcid=142A");
		//完成新手任务
		HttpUtil.get("npc.php?npcid=142A&act=NQ0016_5");
		PersonStatusService.update();
		
		//沙洲哨站
		MoveService.moveTo(1142);
		//弗鲁迪
		HttpUtil.get("npc.php?npcid=116");
		//完成兄弟任务
		HttpUtil.get("npc.php?npcid=116&act=Q4_2");
		BagService.update();
	}
	//一转
	public static void yizhuan() {
		w1:
		while(true) {
			BattleService.readyForBattle();
			MoveService.moveTo(1125);
			BattleInfo info = BattleService.fight(BattleService.searchUntilEnemy());
			for(String s: info.getOtherInfo()) {
				if(s.contains("一个任务被触发")) {
					break w1;
				}
			}
		}
		
		MoveService.moveTo(1102);
		HttpUtil.get("npc.php?npcid=117");
		HttpUtil.get("npc.php?npcid=117&act=Q5_1");
		
		w2:
		while(true) {
			BattleService.readyForBattle();
			MoveService.moveTo(1125);
			BattleInfo info = BattleService.fight(BattleService.searchUntilEnemy());
			for(String s: info.getOtherInfo()) {
				if(s.startsWith("得到了新的信息")) {
					break w2;
				}
			}
		}
		
		MoveService.moveTo(1102);
		HttpUtil.get("npc.php?npcid=117");
		HttpUtil.get("npc.php?npcid=117&act=Q5_3");
		
		w3:
			while(true) {
				BattleService.readyForBattle();
				MoveService.moveTo(1125);
				BattleInfo info = BattleService.fight(BattleService.searchUntilEnemy());
				for(String s: info.getOtherInfo()) {
					if(s.startsWith("找到一个奇怪的木雕")) {
						break w3;
					}
				}
			}
		
		MoveService.moveTo(1102);
		HttpUtil.get("npc.php?npcid=117");
		HttpUtil.get("npc.php?npcid=117&act=Q5_5");
		//瓦内萨和摩达
		MoveService.moveTo(1119);
		HttpUtil.get("npc.php?npcid=106");
		HttpUtil.get("npc.php?npcid=106&act=Q5_6");
		HttpUtil.get("npc.php?npcid=105");
		HttpUtil.get("npc.php?npcid=105&act=Q5_7");
		//接进塔任务
		MoveService.moveTo(1120);
		HttpUtil.get("npc.php?npcid=133");
		HttpUtil.get("npc.php?npcid=133&act=Q0_ON");
		
		MoveService.moveTo(1113);
		HttpUtil.get("npc.php?npcid=103");
		HttpUtil.get("npc.php?npcid=103&act=Q0_1");
		
		w:
			while(true) {
				BattleService.readyForBattle();
				MoveService.moveTo(1114);
				BattleInfo info = BattleService.fight(BattleService.searchUntilEnemy());
				for(String s: info.getOtherInfo()) {
					if(s.startsWith("杀死一个魔物完成")) {
						break w;
					}
				}
			}
		
		MoveService.moveTo(1113);
		HttpUtil.get("npc.php?npcid=103");
		HttpUtil.get("npc.php?npcid=103&act=Q0_3");
		//塔娜和希尼亚斯
		MoveService.moveTo(1120);
		HttpUtil.get("npc.php?npcid=133");
		HttpUtil.get("npc.php?npcid=133&act=Q0_4");
		HttpUtil.get("npc.php?npcid=111");
		HttpUtil.get("npc.php?npcid=111&act=Q0_5");
		//凯尔，关于这木雕
		MoveService.moveTo(1102);
		HttpUtil.get("npc.php?npcid=117");
		HttpUtil.get("npc.php?npcid=117&act=Q5_8");
		//魔殿，西蕾尼
		MoveService.moveTo(1188);
		HttpUtil.get("npc.php?npcid=132");
		HttpUtil.get("npc.php?npcid=132&act=Q5_9");
		//玛丽卡
		MoveService.moveTo(1168);
		String jiahana = HttpUtil.get("move.php?mov=1156");
		MoveParser.parseMove(jiahana);
		HttpUtil.get("npc.php?npcid=122");
		HttpUtil.get("npc.php?npcid=122&act=Q5_10");
		//找特缇斯……
		if(!jiahana.contains("特缇斯")) {
			MoveService.moveTo(1178);
			String luosidun = HttpUtil.get("move.php?mov=1179");
			MoveParser.parseMove(luosidun);
			if(!luosidun.contains("特缇斯")) {
				MoveService.moveTo(1118);
				String fuleiliya = HttpUtil.get("move.php?mov=1119");
				MoveParser.parseMove(fuleiliya);
				if(!fuleiliya.contains("特缇斯")) {
					MoveService.moveTo(1104);
					String luneisi = HttpUtil.get("move.php?mov=1102");
					MoveParser.parseMove(luneisi);
					if(!luneisi.contains("特缇斯")) {
						MoveService.moveTo(1152);
					}
				}
			}
		}
		HttpUtil.get("npc.php?npcid=121");
		HttpUtil.get("npc.php?npcid=121&act=Q5_11");
		//拉切尔
		MoveService.moveTo(1180);
		HttpUtil.get("npc.php?npcid=125");
		HttpUtil.get("npc.php?npcid=125&act=Q5_12");
		MoveService.moveTo(1194);
	}
}
