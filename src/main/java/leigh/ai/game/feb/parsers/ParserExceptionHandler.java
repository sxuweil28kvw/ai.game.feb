package leigh.ai.game.feb.parsers;

import leigh.ai.game.feb.util.LogUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(ParserExceptionHandler.class);
	public static void handle(Exception e, String source, String msg) {
		LogUtil.errorStackTrace(logger, new Exception(msg + source, e));
		System.out.println(msg);
		System.exit(1);
	}

}
