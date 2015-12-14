package com.iheart.ssi.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class PropertyLoader {
	private Properties properties;
//	private static final String PROP_FILEPATH = System.getProperty("user.dir") + "\\ssi.properties";
	//private static final String PROP_FILEPATH ="ssi.properties"; //절대경로로 바꿀거임.
	private static PropertyLoader loader;
	
	
	private PropertyLoader(){
			properties = new Properties();
			try {
				properties.load(new BufferedReader(new InputStreamReader(new FileInputStream("D:\\Git\\AppleStore\\SSI_HTTP_Project\\ssi.properties"), "UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void setConfig(String propPath, String logPath, String webPath){
		
		try {
			properties.load(new BufferedReader(new InputStreamReader(new FileInputStream(propPath), "UTF-8")));
			properties.setProperty("LOG_HOME", logPath);
			properties.setProperty("WEB_HOME", webPath);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println(propPath + "파일을 찾을 수 없습니다. " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized PropertyLoader getInstance(){
		//
		if(loader == null){
			loader = new PropertyLoader();
		}
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
