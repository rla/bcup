package ee.pri.bcup.client.common.log;

import java.util.HashMap;

/**
 * Very simple and primitive log implementation. Does not depend on log4j.
 * Writes everything to standard output.
 */
public class Logger {
	private static final HashMap<Class<?>, Logger> loggers;
	private static boolean debugEnabled;
	
	static {
		loggers = new HashMap<Class<?>, Logger>();
	}
	
	private Class<?> clazz;
	
	private Logger(Class<?> clazz) {
		this.clazz = clazz;
	}

	public static Logger getLogger(Class<?> clazz) {
		if (!loggers.containsKey(clazz)) {
			loggers.put(clazz, new Logger(clazz));
		}
		
		return loggers.get(clazz);
	}

	public static boolean isDebugEnabled() {
		return debugEnabled;
	}

	public static void setDebugEnabled(boolean debugEnabled) {
		Logger.debugEnabled = debugEnabled;
	}
	
	public void info(String message) {
		System.out.println("INFO: " + clazz.getSimpleName() + " : " + message);
	}
	
	public void warn(String message) {
		System.out.println("WARN: " + clazz.getSimpleName() + " : " + message);
	}
	
	public void error(String message) {
		System.out.println("ERROR: " + clazz.getSimpleName() + " : " + message);
	}
	
	public void debug(String message) {
		if (debugEnabled) {
			System.out.println("DEBUG: " + clazz.getSimpleName() + " : " + message);
		}
	}
}
