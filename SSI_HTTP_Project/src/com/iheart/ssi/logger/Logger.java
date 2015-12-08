package com.iheart.ssi.logger;

import com.iheart.ssi.util.PropertyLoader;

public class Logger {
	//
	private volatile static Logger logger;
	
	private LogHandler handler;
	private Format format;
	private String className;
	////////////////////////////////////////////////////////////////////////////////////
	// ssi.properties를 읽어올 객체
	public static final PropertyLoader loader = PropertyLoader.getInstance();
	// 표시할 LogLevel
	// ssi.properties의 VISIBLE_LOG_LEVEL=? 에서 가져온다.
	public static final int VISIBLE_LOG_LEVEL = Integer.parseInt(loader.getProperty("VISIBLE_LOG_LEVEL"));
	// 유동헤더
	public static final String DYNAMIC_HEADER = loader.getProperty("DYNAMIC_HEADER");
	
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
					logger.addHandler(new LogFileHandler()); //강제주입
				}
			}
		}
		return logger;
	}
	
	public void write(LogLevel level, String logMsg){
		//
		if(level.getValue() >= VISIBLE_LOG_LEVEL){
			format = new Format();
			StringBuffer logHeader = new StringBuffer(); // Thread-safe
			logHeader.append(format.getLogTimePattern() + " "); 
			logHeader.append("[").append(level.getName()).append("]");
			logHeader.append("[").append(DYNAMIC_HEADER).append("]");
			logHeader.append(" " + className + " ");
			handler.append(logHeader.toString()+logMsg);
		}
	}
	
	public void addHandler(LogHandler handler) {
		this.handler = handler;
	}
	
}
