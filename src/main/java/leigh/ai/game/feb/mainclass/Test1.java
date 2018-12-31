package leigh.ai.game.feb.mainclass;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import leigh.ai.game.feb.dto.bag.GivenItem;
import leigh.ai.game.feb.service.BagService;
import leigh.ai.game.feb.service.LoginService;

public class Test1 {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		LoginService.login("藤堂志摩子", "kk82liewuxux");
		for(GivenItem e: BagService.givenItems) {
			System.out.println(e.getType() + "\t" + e.getName() + "\t" + e.getGivenBy() + "\t"
					+ e.getAmount() + "\t" + e.getTime());
		}
	}
}
