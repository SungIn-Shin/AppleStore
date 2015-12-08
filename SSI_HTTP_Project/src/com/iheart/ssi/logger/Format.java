package com.iheart.ssi.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.iheart.ssi.util.PropertyLoader;

public class Format {
	
	/**
	 * com.iheart.ssi.logger 의 logger.config에서 주입.
	 */
	PropertyLoader prop = PropertyLoader.getInstance();
	private SimpleDateFormat formatter;
	
	/**
	 * 
	 */
	public Format(){}
	
	
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
		//System.out.println("날짜 : " + formatter.format(new Date()));
		return formatter.format(new Date());
	}
	
}
