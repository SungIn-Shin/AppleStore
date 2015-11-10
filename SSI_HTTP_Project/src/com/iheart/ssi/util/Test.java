package com.iheart.ssi.util;

import com.iheart.ssi.logger.LogFileHandler;
import com.iheart.ssi.logger.Logger;

public class Test {
	private static Logger log = Logger.getLogger(Test.class);
	
	public static void main(String[] args) {
		log.addHandler(new LogFileHandler());
//		log.alert("ggg");
//		log.debug("ggg들어왔나?");
//		
//		String b = "gdasd";
//		try{
//		Integer.parseInt(b);
//		}catch(NumberFormatException e){
//			log.error(e.toString());
//		}
		System.out.println(Thread.activeCount());
	}
}
