package com.iheart.ssi.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
	private Properties properties;
	private static final String PROP_FILEPATH = "D:\\Git\\AppleStore\\SSI_HTTP_Project\\src\\com\\iheart\\ssi\\logger\\logger.properties";
	private static final PropertyLoader loader = new PropertyLoader();
	
	private PropertyLoader(){
		try {
			properties = new Properties();
			properties.load(new FileInputStream(PROP_FILEPATH));
		} catch (FileNotFoundException e) {
			//
			e.printStackTrace();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}
	
	public static PropertyLoader getInstance(){
		//
		return loader;
	}
	
	/**
	 * @param key
	 * @return key값에 해당하는 프로퍼티 value를 가져온다.
	 */
	public String getProperty(String key){
		return properties.getProperty(key);
	}
	
	
}
