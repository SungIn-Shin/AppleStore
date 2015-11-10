package com.iheart.ssi.logger;

import java.util.logging.Level;

public class Logger {
	//
	private volatile static Logger logger;
	
	private Level level;
	private LogHandler handler;
	private Format format;
	private String userIP = "127.0.0.1";
	private String className;
	
	private <T> Logger(Class<T> clazz){
		this.className = clazz.getName();
	}
	
	/**
	 * @param <T>
	 * @return
	 * 
	 * Multi-Thread에서 Logger는 단 1개의 유일한 객체만 생성되어야한다.
	 * 
	 * synchronized를 getInstance()에 걸어줄 수 있지만, 속도의 문제가 생길 수 있다.
	 * 이 문제를 해결하기 위해 두가지 방법이 있는데, 
	 * 1. 인스턴스를 필요할 때 생성하지 않고 처음부터 만들어버린다.
	 * private static Logger logger = new Logger(); // JVM에서 유일한 인스턴스를 생성시킨다.
	 * 
	 * 2. DCL을 써서 getInstance()에서 동기화 되는 부분을 줄인다. 
	 * DCL (Double-Checking Locking)을 사용하여 getInstance()에서 동기화 되는 부분을 줄인다.
	 * 
	 * 
	 */
	public static <T> Logger getLogger(Class<T> clazz){
		if(logger == null){
			synchronized (Logger.class) {
				if(logger == null){
					logger = new Logger(clazz);
				}
			}
		}
		return logger;
	}
	
	/**
	 * @param args
	 * TestMain
	 */
	public static void main(String[] args) {
//		Logger logger = Logger.getLogger();
//		LogHandler handler = new LogFileHandler();
//		logger.addHandler(handler);
//		
//		logger.debug("헤헤 이건 디버그.");
//		logger.emerg("헤헤 이건 EMERG");
	}
	
	public void emerg(String logMsg){
		//
		format = new Format(LogLevel.EMERG, userIP, className);
		handler.append(format.getLogMsgFormat() + logMsg);
	}
	
	public void alert(String logMsg){
		//
		format = new Format(LogLevel.ALERT, userIP, className);
		handler.append(format.getLogMsgFormat() + logMsg);
	}
	
	public void crit(String logMsg){
		//
		format = new Format(LogLevel.CRIT, userIP, className);
		handler.append(format.getLogMsgFormat() + logMsg);
	}
	
	public void error(String logMsg){
		//
		format = new Format(LogLevel.ERROR, userIP, className);
		handler.append(format.getLogMsgFormat() + logMsg);
	}
	
	public void warn(String logMsg){
		//
		format = new Format(LogLevel.WARN, userIP, className);
		handler.append(format.getLogMsgFormat() + logMsg);
	}
	
	public void notice(String logMsg){
		//
		format = new Format(LogLevel.NOTICE, userIP, className);
		handler.append(format.getLogMsgFormat() + logMsg);
	}
	
	public void info(String logMsg){
		//
		format = new Format(LogLevel.INFO, userIP, className);
		handler.append(format.getLogMsgFormat() + logMsg);
	}
	
	public void debug(String logMsg){
		//
		format = new Format(LogLevel.DEBUG, userIP, className);
		handler.append(format.getLogMsgFormat() + logMsg);
	}
	
	
	//getter, setter
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}


	public void addHandler(LogHandler handler) {
		this.handler = handler;
	}
	
}
