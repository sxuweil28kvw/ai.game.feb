package leigh.ai.game.feb.mainclass;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import leigh.ai.game.feb.business.TeamRaidBiz;
import leigh.ai.game.feb.dto.raid.HelpLaofanParam;
import leigh.ai.game.feb.service.BattleService;
import leigh.ai.game.feb.service.ItemService;
import leigh.ai.game.feb.service.LoginService;
import leigh.ai.game.feb.service.MercenaryService;
import leigh.ai.game.feb.service.multiAccount.Account;
import leigh.ai.game.feb.service.status.Item;

public class Test1 {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		LoginService.login("藤堂志摩子", "kk82liewuxux");
		MercenaryService.mercenary3333();
	}
}
