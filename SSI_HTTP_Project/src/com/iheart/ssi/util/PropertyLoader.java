package com.iheart.ssi.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertyLoader {
	private Properties properties;
	private static final String PROP_FILEPATH = "D:\\Git\\AppleStore\\SSI_HTTP_Project\\src\\com\\iheart\\ssi\\prop\\ssi.properties";
	private static final PropertyLoader loader = new PropertyLoader();
	
	private PropertyLoader(){
		try {
			properties = new Properties();
			properties.load(new BufferedReader(new InputStreamReader(new FileInputStream(PROP_FILEPATH), "UTF-8")));
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
