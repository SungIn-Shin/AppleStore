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
		String path = "name=성인&age=19&sex=male";
		
		String[] split01 = path.split("&");
		Map<String, String> map = new HashMap<>();
		for(String e : split01){
			String[] split02 = e.split("=");
			map.put(split02[0], split02[1]);
		}
		
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()){
			String key = iter.next();
			System.out.println("Key : " + key + ", Value : " + map.get(key));
		}
//		
//		StringBuffer sb = new StringBuffer();
//		sb.append("POST /index.html HTTP/1.1\r\n");
//		sb.append("Content-Type: text\r\n");
//		sb.append("Content-length: 19\r\n");
//		sb.append("\r\n");
//		sb.append("This is body message");
//		
//		
//		String http = sb.toString();
//		String header="";
//		String body="";
//		
//		String[] s = http.split("\r\n");
//		
//		// Header와 Body의 구분선인 비어있는 Line의 Index를 담기위한 변수선언.
//		// for loop를 돌다가 isEmpty() 가 true인 index값을 emptyIndex값테 저장한다.
//		// emptyIndex + 1 부터는 Body이다.
//		int emptyIndex = 0; 
//		for(int i = 0; i < s.length;i++){
//			
//			if(!s[i].isEmpty()){ // 배열의 값이 있을때
//				if(emptyIndex == 0){ // emptyIndex값이 0이면 헤더이고, 
//					header+=s[i]+"\r\n";
//				}else{
//					body += s[emptyIndex+1]; // 비어있는 Index + 1 번째 인덱스 부터는 Body이다.
//				}
//			} else{ // 배열의 값이 empty일때 index를 emptyIndex에 대입한다.
//				emptyIndex = i;
//			}
//		}
//		//위에서 구분한 Header와 Body를 또 다시 
//		//    status_line 과 -> StringTokenizer로 구분
//		//    header_body 의 key:value로 구분한다.
//		// h_ : header를 의미함.
//		String[] h_split = header.split("\r\n");
//		String h_statusLine = "";
//		Map<String, String> fieldMap = new HashMap<>(); // filed를 관리하는 Map
//		
//		int len = h_split.length;
//		for(int i = 0; i < len; i++){
//			if(i == 0){ // 첫번째 라인은 무조건 상태문(Status_Line)이다.
//				h_statusLine = h_split[i];
//			} else{
//				String[] h_body = h_split[i].split(": "); // Header_Body를 key: value로 구분해서 map에 담는다.
//				fieldMap.put(h_body[0], h_body[1]);
//			}
//		}
//		
//		// POST /index.html HTTP/1.1 과 같이 생긴 Status_Line을 
//		// 공백으로 구분해서
//		// requestMethod -> POST or GET
//		// filePath -> /index.html 
//		// httpVersion -> HTTP/1.1 or HTTP/1.0 등등...
//		// 으로 나눈다.
//		StringTokenizer st = new StringTokenizer(h_statusLine);
//		
//		String requestMethod = st.nextToken();
//		String filePath = st.nextToken();
//		String httpVersion = st.nextToken();
//		
//		System.out.println("RequestMethod :"+requestMethod);
//		System.out.println("RequestFilePath :"+filePath);
//		System.out.println("HTTPVersion :" + httpVersion);
//		
//		Iterator<String> iter = fieldMap.keySet().iterator();
//		while(iter.hasNext()){
//			String key = iter.next();
//			System.out.println("Field Data -> " + key + ": " + fieldMap.get(key));
//		}
//		System.out.println();
//		System.out.println("Body Message : "+ body);
	}
}
