package com.iheart.ssi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.iheart.ssi.logger.Logger;

public class Test {
	private static final Logger LOG = Logger.getLogger(Test.class);

	public static void main(String[] args) {
//		String path = "name=성인&age=19&sex=male";
//		
//		String[] split01 = path.split("&");
//		Map<String, String> map = new HashMap<>();
//		for(String e : split01){
//			String[] split02 = e.split("=");
//			map.put(split02[0], split02[1]);
//		}
//		
//		Iterator<String> iter = map.keySet().iterator();
//		while(iter.hasNext()){
//			String key = iter.next();
//			System.out.println("Key : " + key + ", Value : " + map.get(key));
//		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("POST /index.html HTTP/1.1\r\n");
		sb.append("Content-Type: text\r\n");
		sb.append("Content-length: 19\r\n");
		sb.append("\r\n");
		sb.append("This is body message");
		
		
		String http = sb.toString();
		String header="";
		String body="";
		
		String[] s = http.split("\r\n");
		
		
		/**
		 * 
		 * **/
		// Header와 Body의 구분선인 비어있는 Line의 Index를 담기위한 변수선언.
		// for loop를 돌다가 isEmpty() 가 true인 index값을 emptyIndex값테 저장한다.
		// emptyIndex + 1 부터는 Body이다.
		int emptyIndex = 0; 
		for(int i = 0; i < s.length;i++){
			
			if(!s[i].isEmpty()){ // 배열의 값이 있을때
				if(emptyIndex == 0){ // emptyIndex값이 0이면 헤더이고, 
					header+=s[i];
				}else{
					body += s[emptyIndex+1]; // 아니면 body이다.
				}
			} else{ // 배열의 값이 empty일때 index를 emptyIndex에 대입한다.
				emptyIndex = i;
			}
		}
		
		System.out.println(header);
		System.out.println(body);

		
		

	}
}
