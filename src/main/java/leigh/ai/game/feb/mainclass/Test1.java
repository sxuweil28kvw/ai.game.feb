package leigh.ai.game.feb.mainclass;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import leigh.ai.game.feb.business.MercenaryBiz;

public class Test1 {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
//		LoginService.login("爱琴海神尼", "7777777");
//		LoginService.logout();
		
//		ObjectMapper om = new ObjectMapper(new YAMLFactory());
//		RaidBattleStrategy strat = om.readValue(new File("f:/f/g/feb/test1.yml"), RaidBattleStrategy.class);
//		System.out.println(strat.getBattler());
		
		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵少女.yml");
		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵红楼.yml");
		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵意甲.yml");
		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵德军.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵长者.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵jedi.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵主号.yml");
	}
}
