package com.iheart.ssi.httpparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import com.iheart.ssi.exception.HTTP400Exception;
import com.iheart.ssi.exception.HTTP404Exception;
import com.iheart.ssi.exception.HTTP405Exception;
import com.iheart.ssi.exception.HTTP414Exception;
import com.iheart.ssi.exception.HTTP505Exception;
import com.iheart.ssi.logger.Logger;
import com.iheart.ssi.util.PropertyLoader;

public class HTTPHeaderControllerImpl implements HTTPHeaderController {
	//
	private static final Logger log = Logger.getLogger(HTTPHeaderControllerImpl.class);
	
	private static PropertyLoader loader = PropertyLoader.getInstance(); // 프로퍼티를 읽어오는 객체
	
	private static final String FILE_HOME = loader.getProperty("FILE_HOME"); // 프로퍼티에 정의한 file_path를 불러온다.;
	
	private static final String HTTP_VERSION = loader.getProperty("HTTP_VERSION"); // HTTP/1.1
	
	private static final String GET="GET";
	
	private static final String POST="POST";
	
	public HTTPHeaderControllerImpl() {
	}

	@Override
	public Map<String, String> parseHTTPHeader(String reqData) {
		//
		//
		Map<String, String> responseMap = new LinkedHashMap<>();
		responseMap.put("HTTP_VERSION", HTTP_VERSION);
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
			// |	헤더 본문(Field)			|
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
			System.out.println("METHOD : " + requestMethod);
			// RequestURI   /index.html?hello=ged&name=djks
			if(!st.hasMoreTokens()){
				throw new NoSuchElementException();
			}
			String requestURI = st.nextToken(); // /index.html
			
			if(requestURI.length() >100){
				throw new HTTP414Exception();
			}
			String requestFilePath = "";
			String requestParam ="";
			if(requestURI.contains("?")){
				// URI에서 0번째 인덱스에서 부터 가장 가까운 ?의 index를 반환
				int index = requestURI.indexOf("?");
				// /index.html?hello=ged&name=djks 의 0번째 인덱스부터 ?의 index 전까지 잘라서 반환
				requestFilePath = requestURI.substring(0, index);
				// URI의 ?가 위치하는 인덱스  다음 인덱스 부터 읽어서 반환
				requestParam = requestURI.substring(index+1);
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
			if (!requestHttpVersion.equals(HTTP_VERSION)) {
				throw new HTTP505Exception();
			}
			
			// 클라이언트가 요청한 파일
			// 요청한 파일의 존재 유무를 검사한다. 없으면 404 ERROR
			int fileSize = 0;
			byte[] fileArr = null;
			File file = new File(FILE_HOME + requestFilePath); 
			
			if(!file.exists()){
				//
				throw new HTTP404Exception();
			} else{
				//
				fileSize = (int) file.length();
				fileArr = new byte[fileSize];
				@SuppressWarnings("resource")
				FileInputStream fis = new FileInputStream(file);
				fis.read(fileArr);
			}
			
			if(requestMethod.equals(GET)){
				// GET URI에 파라미터 분석
				if(!requestParam.equals("")){
					Map<String, String> param = paramParser(requestParam);
					Iterator<String> iter = param.keySet().iterator();
					while(iter.hasNext()){
						String key = iter.next();
						log.debug("It's GET METHOD LOGGING");
						log.debug("KEY : " + key + " , " + "VALUE : " + param.get(key));
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
				return responseMap;
			} else if(requestMethod.equals(POST)){
				// RequestMethod = 'POST' 일때 처리
				// 1. byte[]로 읽은 파일을 String으로 변환한다.
				String fileContent = new String(fileArr, "UTF-8");
				// 2. 파일의 길이를 Content-length : fileSize로 담는다.
				responseMap.put("Content-length", String.valueOf(fileSize));
				// 3. 읽어온 파일의 내용을 'BODY' 로 담는다.
				responseMap.put("BODY", fileContent);
				// 4. Protocol에서 읽은 Body를 분석   name=성인&age=25&sex=male
				// &를 기준으로 key=value를 구분하고
				// = 기준으로 key와 value를 구분해낸다.
				// POST방식이면 BODY가 있어야 정상 Protocol이다.
				if(!body.equals(null) || !body.equals("")){
					Map<String, String> bodyParamMap = paramParser(body);
					Iterator<String> iter = bodyParamMap.keySet().iterator();
					while(iter.hasNext()){
						String key = iter.next();
						log.debug("It's POST METHOD LOGGING");
						log.debug("KEY : " + key + " , " + "VALUE : "+bodyParamMap.get(key));
					}
				}
				return responseMap;
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
		
		return responseMap;
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
	 * 
	 * body와 Header를 구분하기위해 key값이 body일때 \r\n로 개행을 시켜서 프로토콜을 완성시키고 byte[]로 반환한다.
	 * =========================================================================
	 * =============
	 */
	@Override
	public byte[] createHTTPProtocol(Map<String, String> resData) { // statusLine, HTTP/1.1 200 OK
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
		System.out.println("================================================");
		System.out.println("===================Response Protocol============");
		System.out.println(sb.toString());
		System.out.println("===================Response Protocol============");
		System.out.println("================================================");
		
		
		return sb.toString().getBytes();
	}
	
	
	 public static void main(String[] args) {
	 /**
	  * POST Method Test Case
	  * **/
	 StringBuffer sb = new StringBuffer();
	 sb.append("POST /post.jsp HTTP/1.1\r\n");
	 sb.append("Content-Type: text/html; charset=UTF-8\r\n");
	 sb.append("\r\n");
	 sb.append("name=성인&age=19");
	 sb.append("&zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");	 
	 /**
	  * GET Method Test Case
	  * **/
//	 StringBuffer sb = new StringBuffer();
//	 sb.append("GET /post.jsp?name=승찬&age=14 HTTP/1.1\r\n");
//	 sb.append("Content-Type: text/html; charset=UTF-8\r\n");
//	 sb.append("\r\n");
	 HTTPHeaderControllerImpl p = new HTTPHeaderControllerImpl();
	 System.out.println(p.createHTTPProtocol(p.parseHTTPHeader(sb.toString())));
	 }
}
