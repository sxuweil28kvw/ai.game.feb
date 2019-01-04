package leigh.ai.game.feb.mainclass;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import leigh.ai.game.feb.business.MercenaryBiz;
import leigh.ai.game.feb.dto.mercenary.BatchEndTrainingParam;
import leigh.ai.game.feb.service.MercenaryService;

public class Test1 {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
//		LoginService.login("藤堂志摩子", "kk82liewuxux");
		MercenaryBiz.batchEndTraining("f:/f/g/feb/批量练兵.yml");
//		Thread.sleep(1000 * 60 * 14);
		
	}
}
