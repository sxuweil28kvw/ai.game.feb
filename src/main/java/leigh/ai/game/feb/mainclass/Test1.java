package leigh.ai.game.feb.mainclass;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import leigh.ai.game.feb.business.MercenaryBiz;
import leigh.ai.game.feb.business.TagangBiz;
import leigh.ai.game.feb.dto.skill.Skill;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.SkillService;
import leigh.ai.game.feb.service.TagangMissionService;

public class Test1 {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		LoginService.login("小笠原祥子", "kk82liewuxux");
		for(int i = 0; i < 37; i++) {
			SkillService.equip(Skill.修复, 1);
			SkillService.equip(Skill.潜行, 1);
		}
//		SkillService.equip(Arrays.asList(Skill.偷窃, Skill.奥义L1), false);
//		MissionService.halfCard();
//		MissionService.memberCard();
//		LoginService.logout();
		
//		Thread.sleep(1000 * 60 * 97);
//		TagangBiz.takeTagang(new String[] {"f:/f/g/feb/accountAll.txt"});
		
//		ObjectMapper om = new ObjectMapper(new YAMLFactory());
//		RaidBattleStrategy strat = om.readValue(new File("f:/f/g/feb/test1.yml"), RaidBattleStrategy.class);
//		System.out.println(strat.getBattler());
		
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵意甲.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵袁术.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵红楼.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵德军.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵长者.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵jedi.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵少女.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵星宿.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵主号.yml");
	}
}
