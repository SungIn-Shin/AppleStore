package com.iheart.ssi.logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Logger {
	//
	private volatile static Logger logger;
	
	private LogHandler handler;
	private Format format;
	private String className;
	private String dynamic_log_header;
	private Properties loader;
	// 표시할 LogLevel
	
	private <T> Logger(Class<T> clazz){
		this.className = clazz.getName();
		format = new Format();
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
	
	public void write(LogLevel level, String logMsg){
		//
		int visible_log_level = Integer.parseInt(loader.getProperty("visible_log_level").trim());
		String logging_time = format.getLogTimePattern(loader.getProperty("log_time_pattern"));
		
		if(level.getValue() >= visible_log_level){
			format = new Format();
			StringBuffer logHeader = new StringBuffer(); // Thread-safe
			logHeader.append("[").append(logging_time).append("]");
			logHeader.append(" [").append(level.getName()).append("]");
			// 유동헤더 설정 부분
			logHeader.append("[");
			logHeader.append(className);
			logHeader.append(dynamic_log_header);
			logHeader.append("]");
			
			handler.append(logHeader.toString()+"\t"+logMsg);
		}
	}
	
	public void setProperty(String log_conf_path){
		loader = new Properties();
		try {
			loader.load(new BufferedReader(new InputStreamReader(new FileInputStream(log_conf_path), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addHandler(LogHandler handler) {
		this.handler = handler;
	}

	public void setDynamicLogHeader(String dynamic_log_header) {
		this.dynamic_log_header = dynamic_log_header;
	}
	
}
