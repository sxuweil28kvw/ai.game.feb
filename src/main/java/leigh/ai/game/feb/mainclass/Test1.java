package leigh.ai.game.feb.mainclass;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import leigh.ai.game.feb.business.MercenaryBiz;

public class Test1 {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
//		LoginService.login("长者二零一九", "kk82liewuxux");
//		MissionService.halfCard();
		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵少女.yml");
//		MercenaryBiz.batchTraining("f:/f/g/feb/批量练兵主号.yml");
	}
}
