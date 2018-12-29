package leigh.ai.game.feb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import leigh.ai.game.feb.parsers.MoveParser;
import leigh.ai.game.feb.parsers.NpcParser;
import leigh.ai.game.feb.service.battle.BattleInfo;
import leigh.ai.game.feb.util.HttpUtil;

public class MissionService {
	private static final Logger logger = LoggerFactory.getLogger(MissionService.class);
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
						MoveService.moveTo(1143);
						String shazhoushaozhan = HttpUtil.get("move.php?mov=1142");
						if(!shazhoushaozhan.contains("特缇斯")) {
							MoveService.moveTo(1152);
						}
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
	
	public static void halfCard() {
		MoveService.moveTo(1102);
		String yifuliemuSaid = NpcParser.parse(HttpUtil.get("npc.php?npcid=115"));
		if(!yifuliemuSaid.contains("我想去协助将军")) {
			logger.error("没有接到半价卡任务！");
			return;
		}
		HttpUtil.get("npc.php?npcid=115&act=Q1_ON");
		//你自愿加入魔殿志愿军，首先你得去魔殿向塞思将军报道
		
		MoveService.moveTo(1188);
		String saisiSaid = NpcParser.parse(HttpUtil.get("npc.php?npcid=102"));
		if(!saisiSaid.contains("援军")) {
			logger.error("塞思没有提到援军！");
			return;
		}
		saisiSaid = HttpUtil.get("npc.php?npcid=102&act=Q1_1");
		//在魔殿杀死至少10只魔物，当然，如果你愿意，你可以杀死更多，应该可以获得更好的奖励<br>已杀死：0/10或50
		
		BattleService.killSomeEnemies(50, 1188);
		
	}
	
	public static void memberCard() {
		MoveService.moveTo(1198);//贸易港
		String leinakeSaid = NpcParser.parse(HttpUtil.get("npc.php?npcid=127"));
		if(!leinakeSaid.contains("庞大的债务")) {
			logger.error("没有领到会员卡任务！请检查前置条件");
			return;
		}
		HttpUtil.get("npc.php?npcid=127&act=Q7_ON");
		MoveService.moveTo(1180);//罗斯顿王宫
		String laqieerSaid = NpcParser.parse(HttpUtil.get("npc.php?npcid=125"));
		if(!laqieerSaid.contains("雷纳克的债务")) {
			logger.error("拉切尔对话中没有雷纳克的债务选项！");
			return;
		}
		HttpUtil.get("npc.php?npcid=125&act=Q7_1");
		
		MoveService.moveTo(1198);
		leinakeSaid = NpcParser.parse(HttpUtil.get("npc.php?npcid=127"));
		if(!leinakeSaid.contains("讨债失败")) {
			logger.error("雷纳克对话中没有讨债失败选项！");
			return;
		}
		HttpUtil.get("npc.php?npcid=127&act=Q7_2");
		
		MoveService.moveTo(1107);//拉克村
		String meimiSaid = NpcParser.parse(HttpUtil.get("npc.php?npcid=108"));
		if(!meimiSaid.contains("科曼的行踪")) {
			logger.error("梅米对话中没有科曼的行踪选项！");
			return;
		}
		HttpUtil.get("npc.php?npcid=108&act=Q7_4");
		
	}
}
