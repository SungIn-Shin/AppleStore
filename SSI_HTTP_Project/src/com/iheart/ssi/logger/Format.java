package com.iheart.ssi.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.iheart.ssi.util.PropertyLoader;

public class Format {
	
	/**
	 * com.iheart.ssi.logger 의 logger.config에서 주입.
	 */
	private PropertyLoader prop = PropertyLoader.getInstance();
	private SimpleDateFormat formatter;
	private LogLevel level;
	private String userIP;
	private String className;
	
	/**
	 * 
	 */
	public Format(){
		
	}
	
	/**
	 * Log 작성되는 메세지 Format
	 * @param level
	 * @param userIP 
	 */
	public Format(LogLevel level, String userIP, String className){
		this.level = level;
		this.userIP = userIP;
		this.className = className;
	}
	
	// *HH:24:MI.SS.MS * [Log Header] [유동 Header]
	public String getLogMsgFormat(){
		//
		StringBuffer sb = new StringBuffer(); // Thread-safe
		sb.append(getLogTimePattern() + " ");
		sb.append("[").append(level.getName()).append("]");
		sb.append("[").append(userIP).append("]");
		sb.append(" " + className + " ");
		return sb.toString();
	}
	
	/**
	 * @return 로그 시간 형식
	 */
	public String getLogTimePattern(){
		//
		formatter = new SimpleDateFormat(prop.getProperty("log_time_pattern"));
		return formatter.format(new Date());
	}
	
	/**
	 * @return 로그 파일 날짜 형식
	 */
	public String getLogFilePattern(){
		formatter = new SimpleDateFormat(prop.getProperty("file_name_pattern"));
		return formatter.format(new Date());
	}
	
}
