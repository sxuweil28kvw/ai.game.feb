package leigh.ai.game.feb.util;

import org.slf4j.Logger;

public class LogUtil {
	public static void debugStackTrace(Logger logger, Throwable e) {
		if(logger.isDebugEnabled()) {
			logger.debug(e.toString());
			StackTraceElement[] stackTraces = e.getStackTrace();
			for(StackTraceElement st: stackTraces) {
				logger.debug(" at " + st.toString());
			}
		}
	}
	public static void infoStackTrace(Logger logger, Throwable e) {
		if(logger.isInfoEnabled()) {
			logger.info(e.toString());
			StackTraceElement[] stackTraces = e.getStackTrace();
			for(StackTraceElement st: stackTraces) {
				logger.info(" at " + st.toString());
			}
		}
	}
	public static void warnStackTrace(Logger logger, Throwable e) {
		if(logger.isWarnEnabled()) {
			logger.warn(e.toString());
			StackTraceElement[] stackTraces = e.getStackTrace();
			for(StackTraceElement st: stackTraces) {
				logger.warn(" at " + st.toString());
			}
		}
	}
	public static void errorStackTrace(Logger logger, Throwable e) {
		logger.error(e.toString());
		StackTraceElement[] stackTraces = e.getStackTrace();
		for(StackTraceElement st: stackTraces) {
			logger.error(" at " + st.toString());
		}
		while(e.getCause() != null) {
			e = e.getCause();
			logger.error("caused by: " + e.toString());
			stackTraces = e.getStackTrace();
			for(StackTraceElement st: stackTraces) {
				logger.error(" at " + st.toString());
			}
		}
	}
}
