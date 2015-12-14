package com.iheart.ssi.httpparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

import com.iheart.ssi.exception.HTTP400Exception;
import com.iheart.ssi.exception.HTTP404Exception;
import com.iheart.ssi.exception.HTTP405Exception;
import com.iheart.ssi.exception.HTTP414Exception;
import com.iheart.ssi.exception.HTTP505Exception;

public class HTTPHeaderParserImpl implements HTTPHeaderParser {
	
	private Properties loader;

	private String web_inf_path;
	
	private String http_version; // HTTP/1.1
	
	private String char_set;
	
	private static final String GET="GET";
	
	private static final String POST="POST";
	
	public HTTPHeaderParserImpl(String web_inf_path, String http_conf_path) {
		//
		try {
			this.web_inf_path=web_inf_path;
			loader = new Properties();
			loader.load(new BufferedReader(new InputStreamReader(new FileInputStream(http_conf_path), "UTF-8")));
			http_version = loader.getProperty("http_version");
			char_set = loader.getProperty("char_set");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();  
		}
	}

	@Override
	public byte[] parseHTTPHeader(byte[] reqArr) {
		//
		String reqData = "";
		try {
			// byte[8190] 사이즈로 들어오기 때문에 trim()으로 공백을 제거한다.
			// Window환경에서 접근 시 ISO-8859-1 charset으로 받는다.
			reqData = new String(reqArr, char_set).trim();
			// ISO-8859-1 -> UTF-8 로 Decodeing한다.
			reqData = URLDecoder.decode(reqData, char_set);
			
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		System.out.println("======"+this.getClass().getName()+"======");
		System.out.println(reqData);
		System.out.println("======REQUEST=====DATA==========");
		
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("HTTP_VERSION", http_version);
		responseMap.put("STATUS", "200 OK");
		responseMap.put("Content-Type", "text/html; charset=UTF-8");
		try {
			//
			String header = "";
			String body = "";
			// 1. 전체 HTTP Protocol을 "\r\n"으로 잘라낸다.
			String[] http_split = reqData.split("\r\n");
			
			// 2. Header와 Body의 구분선인 비어있는 개행라인의 Index를 담기위한 변수를 선언한다.
			// HTTP Protocol의 첫번째 라인 (index = 0)은 무조건 상태문이다.
			// loop를 돌다가 isEmpty()가 true인 Index값을 emptyIndex값에 담아둔다.
			// 그말은 즉, emptyIndex + 1 부터는 Body값이라는 의미이다.
			int emptyIndex = 0; 
			int length = http_split.length;
			for(int i = 0; i < length; i++){
				
				if(!http_split[i].isEmpty()){
					if(emptyIndex == 0){ 
						header+=http_split[i]+"\r\n";
					}else{
						body += http_split[emptyIndex+1];
					}
				} else{
					emptyIndex = i;
				}
			}
			
			// 3. 위에서 구분한 Header와 Body에서 Header를 세분화 시켜서 잘라낸다. 
			// 헤더 = 상태문 + 본문 으로 되어 있기 때문에
			// 상태문과 본문을 분리한다.
			// ==============================
			// |	헤더의 상태문(Status_Line)	|
			// |	헤더 본문(Field: FieldValue	|
			// |	비어있는 라인("\r\n")		|
			// |	진짜 바디(<HTML></HTML>)	|
			// ==============================
			
			// 상태문 : POST /index.html HTTP/1.1    -> StringTokenizer를 사용하여 공백으로 구분
			// RequestMethod -> POST 
			// RequestFilePath -> /index.html
			// HTTP Version -> HTTP/1.1
			
			// 헤더 본문 :	Content-Type: text/html     -> split(": ")로 구분
			// 			Content-length: 19
			// header를 \r\n으로 구분하여 잘라낸다.
			String[] h_split = header.split("\r\n");
			// 상태문을 저장할 변수
			String h_statusLine = "";
			// filed를 관리하는 Map
			Map<String, String> fieldMap = new HashMap<>();
			
			int len = h_split.length;
			for(int i = 0; i < len; i++){
				if(i == 0){ // 첫번째 라인은 무조건 상태문(Status_Line)이다.
					h_statusLine = h_split[i];
				} else{
					String[] h_body = h_split[i].split(": "); // Header_Body를 key: value로 구분해서 map에 담는다.
					if(h_body.length != 2){ // Content-Type: text/html -> length : 2 그이상 나올 수 없다.
						//
						throw new HTTP400Exception();
					} else{
						fieldMap.put(h_body[0], h_body[1]);
					}
				}
			}
			
			StringTokenizer st = new StringTokenizer(h_statusLine);
			
			// RequestMethod
			String requestMethod = st.nextToken(); // GET, POST
			// RequestURI   /index.html?hello=안녕&name=이름
			if(!st.hasMoreTokens()){
				throw new NoSuchElementException();
			}
			String requestURI = st.nextToken(); // /index.html
			
			// 테스트하기위해 임의의 수로 지정했음. 
			if(requestURI.length() > 100){ 
				throw new HTTP414Exception();
			}
			String requestFilePath = ""; //요청 파일 Path
			String requestParam =""; // 요청 파라미터
			if(requestURI.contains("?")){
				// URI에서 0번째 인덱스에서 부터 가장 가까운 ?의 index를 반환
				int index = requestURI.indexOf("?");
				// /index.html?hello=ged&name=djks 의 0번째 인덱스부터 ?의 index 전까지 잘라서 반환
				requestFilePath = requestURI.substring(0, index);
				// URI의 ?가 위치하는 인덱스  다음 인덱스 부터 읽어서 반환
				requestParam = requestURI;
			} else {
				requestFilePath = requestURI;
			}
			
			// HTTP Version
			String requestHttpVersion = st.nextToken();
			
			/* 4. 이제 데이터들이 분리가 되었다.
			 *	ex)
			 *	RequestMethod :POST
				RequestFilePath :/index.html
				RequestParam : ex) name=성인&age=19 ...
				HTTPVersion :HTTP/1.1
				Field Data -> Content-length: 19
				Field Data -> Content-Type: text
				
				Body Message : This is body message
				
				자!!!이제  RequestMethod를 기준으로 데이터를 처리한다.
			 * */
			
			// HTTP VERSION 확인
			// HTTP/1.1(지원하는 버전)이 아니면
			if (!requestHttpVersion.equals(http_version)) {
				throw new HTTP505Exception();
			}
			
			// 클라이언트가 요청한 파일
			// 요청한 파일의 존재 유무를 검사한다. 없으면 404 ERROR
			int fileSize = 0;
			byte[] fileArr = null;
			// web_home = WEB-INF폴더의 경로
			// requestFilePath = /index.html 등등 요청 파일 경로
			System.out.println(web_inf_path + requestFilePath);
			File file = new File(web_inf_path + requestFilePath); 
			System.out.println(file.isFile());
			
			if(!file.exists()){
				//
				throw new HTTP404Exception();
			} else{
				//
				fileSize = (int) file.length();
				fileArr = new byte[fileSize];
				FileInputStream fis = new FileInputStream(file);
				fis.read(fileArr);
				fis.close();
			}
			
			if(requestMethod.equals(GET)){
				// GET URI에 파라미터 분석
				if(!requestParam.equals("")){
					Map<String, String> param = paramParser(requestParam);
					Iterator<String> iter = param.keySet().iterator();
					while(iter.hasNext()){
						String key = iter.next();
						System.out.println("KEY : " + key + " , " + "VALUE : " + param.get(key));
					}
				}
				// RequestMethod = 'GET' 일때 처리
				// 1. byte[]로 읽은 파일을 String으로 변환한다.
				String fileContent = new String(fileArr, "UTF-8");
				// 2. 클라이언트가 요청한 field의 Content-Type으로 responseMap에 담는다.
				// 3. 파일의 길이를 Content-length : fileSize로 담는다.
				responseMap.put("Content-length", String.valueOf(fileSize));
				// 4. 읽어온 파일의 내용을 'BODY' 로 담는다.
				responseMap.put("BODY", fileContent);
				// 5. Map을 return한다.
				return makeProtocol(responseMap);
			} else if(requestMethod.equals(POST)){
				// RequestMethod = 'POST' 일때 처리
				// 1. Protocol에서 읽은 Body를 분석   name=성인&age=25&sex=male
				// &를 기준으로 key=value를 구분하고
				// = 기준으로 key와 value를 구분해낸다.
				// POST방식이면 BODY가 있어야 정상 Protocol이다.
				StringBuffer sb = new StringBuffer();
				if(!body.equals(null) || !body.equals("")){
					Map<String, String> bodyParamMap = paramParser(body);
					Iterator<String> iter = bodyParamMap.keySet().iterator();
					while(iter.hasNext()){
						String key = iter.next();
						sb.append(key + " -> " + bodyParamMap.get(key));
						sb.append("\r\n");
					}
				}
				// 2. 파일의 길이를 Content-length : POST방식으로 넘어온 파라미터를 담는다.
				// POST일때는 text/plain으로 텍스트만 뿌려준다.
				responseMap.put("Content-Type", "text/plain; charset=UTF-8");
				responseMap.put("Content-length", String.valueOf(sb.toString().getBytes().length));
				// 3. 읽어온 파일의 내용을 'BODY' 로 담는다.
				responseMap.put("BODY", sb.toString());
				return makeProtocol(responseMap);
			} else { // GET, POST가 아닌 다른 값.
				//
				throw new HTTP405Exception();
			}
		} catch (HTTP400Exception e) {
			responseMap.put("STATUS", e.getMessage());
			responseMap.put("BODY", loader.getProperty("400"));
		} catch (HTTP404Exception e) {
			responseMap.put("STATUS", e.getMessage());
			responseMap.put("BODY", loader.getProperty("404"));
		} catch (HTTP505Exception e) {
			responseMap.put("STATUS", e.getMessage());
			responseMap.put("BODY", loader.getProperty("505"));
		} catch (FileNotFoundException e) { // 404 error
			responseMap.put("STATUS", e.getMessage());
			responseMap.put("BODY", loader.getProperty("404"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (HTTP405Exception e) {
			responseMap.put("STATUS", e.getMessage());
			responseMap.put("BODY", loader.getProperty("405"));
		} catch (HTTP414Exception e) {
			responseMap.put("STATUS", e.getMessage());
			responseMap.put("BODY", loader.getProperty("414"));
		} catch(NoSuchElementException e){
			responseMap.put("STATUS", "400 Bad Request");
			responseMap.put("BODY", loader.getProperty("400"));
		}
		
		return makeProtocol(responseMap);
	}

	
	/**
	 * @param body
	 * @return
	 * 클라이언트가 요청한 Body의 파라미터 값을 구분하여 Map<>에 담아 반환한다.
	 * @throws HTTP400Exception 
	 */
	private Map<String, String> paramParser(String body) throws HTTP400Exception{
		//
		Map<String, String> map = null;
		try {
			String[] split01 = body.split("&");
			map = new HashMap<>();
			for(String e : split01){
				String[] split02 = e.split("=");
				map.put(split02[0], split02[1]);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// POST 방식에서 Body에 파라미터가 넘어올때 형식이 맞지 않으면 나는 Exception
			// ex) name=iheart&age=15&zzzzzzzzz    << = index에러가 난다.
			throw new HTTP400Exception();
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.iheart.ssi.httpparser.HTTPHeaderController#createHTTPProtocol(java.
	 * util.Map)
	 * ======================================================================================
	 * LinkedHashMap<String, String> 으로 사용을 하기 때문에 map에 put한 시점과순서가 일치하다. 
	 * HTTP 프로토콜에서 가장 상위에 있는 Status Line에 들어가는 데이터가 HTTP의 버전, 상태값이
	 * 들어가기 때문에 Map에도 순서대로 넣어서 출력한다. ex) HTTP/1.1 200 OK\r\n LinkedHashMap에는 여러
	 * 데이터가 key:value로 맵핑이 되어있는데 body를 제외하고는 다 Header에 들어갈 값들이기 때문에 key: value로
	 * 넣어주고
	 * body와 Header를 구분하기위해 key값이 body일때 \r\n로 개행을 시켜서 프로토콜을 완성시키고 byte[]로 반환한다.
	 * =========================================================================
	 * =============
	 */
	
	private byte[] makeProtocol(Map<String, String> resData) { // statusLine, HTTP/1.1 200 OK
		//
		StringBuffer sb = new StringBuffer();
		Iterator<String> iter = resData.keySet().iterator();
		String version = iter.next(); // HTTP/1.1 ...
		String status = iter.next(); // 200 OK ...
		// status_line setting
		// ex) HTTP/1.1 200 OK\r\n -> status line
		sb.append(resData.get(version)).append(" ").append(resData.get(status)).append("\r\n");
		
		while (iter.hasNext()) {
			String key = iter.next();
			if (resData.get("BODY") != null && key.equals("BODY")) {
				sb.append("\r\n");
				sb.append(resData.get(key));
			} else if(resData.get("BODY") == null && key.equals("BODY")){
				sb.append("\r\n");
			} else {
				sb.append(key).append(": ").append(resData.get(key)).append("\r\n");
			}
		}
//		System.out.println("================================================");
//		System.out.println(sb.toString());
//		System.out.println("================================================");
		return sb.toString().getBytes();
	}
}
