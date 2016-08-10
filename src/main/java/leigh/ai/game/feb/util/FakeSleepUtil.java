package leigh.ai.game.feb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FakeSleepUtil {
	private static Logger logger = LoggerFactory.getLogger(FakeSleepUtil.class);
	public static void sleep(int maxSeconds) {
		try {
			int milli = (int)(Math.random() * 1000 * maxSeconds);
			if(logger.isDebugEnabled()) {
				logger.debug("fake sleep " + milli + " milliseconds");
			}
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void sleep(int minSeconds, int maxSeconds) {
		try {
			int milli = (int)(Math.random() * 1000 * (maxSeconds - minSeconds)) + 1000 * minSeconds;
			if(logger.isDebugEnabled()) {
				logger.debug("fake sleep " + milli + " milliseconds");
			}
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
