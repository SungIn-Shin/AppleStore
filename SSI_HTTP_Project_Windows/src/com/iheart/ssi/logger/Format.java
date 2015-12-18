package com.iheart.ssi.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {
	
	/**
	 * com.iheart.ssi.logger 의 logger.config에서 주입.
	 */
	private SimpleDateFormat formatter;
	/**
	 * 
	 */
	public Format(){}
	
	/**
	 * @return 로그 시간 형식
	 */
	public String getLogTimePattern(String logTimePattern){
		//
		formatter = new SimpleDateFormat(logTimePattern);
		return formatter.format(new Date());
	}
	
	/**
	 * @return 로그 파일 날짜 형식
	 */
	public String getLogFilePattern(String fileNamePattern){
		formatter = new SimpleDateFormat(fileNamePattern);
		return formatter.format(new Date());
	}
	
}