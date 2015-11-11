package com.iheart.ssi.httpparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.iheart.ssi.exception.HTTP404Exception;
import com.iheart.ssi.exception.HTTP405Exception;
import com.iheart.ssi.exception.HTTP505Exception;
import com.iheart.ssi.logger.Logger;
import com.iheart.ssi.util.PropertyLoader;

public class HTTPHeaderControllerImpl implements HTTPHeaderController {
	//
	private static final Logger log = Logger.getLogger(HTTPHeaderControllerImpl.class);
	private String file_path;
	private String HTTP_VERSION;
	PropertyLoader loader = PropertyLoader.getInstance(); // 프로퍼티를 읽어오는 객체

	public HTTPHeaderControllerImpl() {
		file_path = loader.getProperty("file_path"); // 프로퍼티에 정의한 file_path를 불러온다.
		HTTP_VERSION = loader.getProperty("HTTP_VERSION"); // HTTP/1.1
	}

	@Override
	public Map<String, String> parseHTTPHeader(String reqData) {
		//
		Map<String, String> resMap = new LinkedHashMap<>();
		resMap.put("version", HTTP_VERSION);
		resMap.put("status", "200 OK");

		// GET으로 요청한 파일 이름
		String fileName = "";
		// 요청한 파일의 크기
		int fileSize = 0;
		// HTTP Version 1.1 , 1.0 ...
		String httpVersion = "";
		//
		StringTokenizer token = new StringTokenizer(reqData);
		boolean hasMoreTokens = token.hasMoreTokens();
		// GET /index.html HTTP1.1
		String requestMethod = token.nextToken();
		if (hasMoreTokens && requestMethod.equals("GET")) { // GET
			fileName = token.nextToken(); // /index.html
			// 이후에 요청하는 파일의 확장자 마다 Content-Type이 바뀔 예정
			httpVersion = token.nextToken();
			resMap.put("Content-Type", "text/html; charset=UTF-8");
			try {
				//
				// HTTP/1.1(지원하는 버전)이 아니면
				if (!httpVersion.equals(HTTP_VERSION)) {
					throw new HTTP505Exception();
				}
				File file = new File(file_path + fileName);
				// 파일이 없으면 HTTP404Exception
				if (!file.exists()) {
					throw new HTTP404Exception();
				}

				fileSize = (int) file.length();
				byte[] fileArr = new byte[fileSize];
				FileInputStream fis = new FileInputStream(file);
				fis.read(fileArr);
				String body = new String(fileArr, "UTF-8");
				resMap.put("Content-length", fileSize + "");
				resMap.put("body", body);
				
				return resMap;
			} catch (HTTP404Exception e) {
				// 404 Not Found
				resMap.put("status", e.getMessage());
				resMap.put("body", loader.getProperty("404"));
			} catch (HTTP505Exception e) {
				// 505 Error
				resMap.put("status", e.getMessage());
				resMap.put("body", loader.getProperty("505"));
			} catch (FileNotFoundException e) {
				// File Not Found 404 Error
				resMap.put("status", "404 Not Found");
				resMap.put("body", loader.getProperty("404"));

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (requestMethod.equals("POST")) {
			fileName = token.nextToken(); // /index.html
			// 이후에 요청하는 파일의 확장자 마다 Content-Type이 바뀔 예정
			httpVersion = token.nextToken();
			resMap.put("Content-Type", "text/html; charset=UTF-8");
			try {
				//
				// HTTP/1.1(지원하는 버전)이 아니면
				if (!httpVersion.equals(HTTP_VERSION)) {
					throw new HTTP505Exception();
				}
				File file = new File(file_path + fileName);
				// 파일이 없으면 HTTP404Exception
				if (!file.exists()) {
					throw new HTTP404Exception();
				}

				fileSize = (int) file.length();
				byte[] fileArr = new byte[fileSize];
				FileInputStream fis = new FileInputStream(file);
				fis.read(fileArr);
				String body = new String(fileArr, "UTF-8");
				resMap.put("Content-length", fileSize + "");
				resMap.put("body", body);
				return resMap;
			} catch (HTTP404Exception e) {
				// 404 Not Found
				resMap.put("status", e.getMessage());
				resMap.put("body", loader.getProperty("404"));
			} catch (HTTP505Exception e) {
				// 505 Error
				resMap.put("status", e.getMessage());
				resMap.put("body", loader.getProperty("505"));
			} catch (FileNotFoundException e) {
				// File Not Found 404 Error
				resMap.put("status", "404 Not Found");
				resMap.put("body", loader.getProperty("404"));

			} catch (IOException e) {
				e.printStackTrace();
			}
			return resMap;
		} else { // 잘못된 요청 GET,POST 가 아닐때
			//
			try {
				throw new HTTP405Exception();
			} catch (HTTP405Exception e) {
				resMap.put("status", e.getMessage());
				resMap.put("body", loader.getProperty("405"));
			}
		}

		return resMap;
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
			if (resData.get("body") != null && key.equals("body")) {
				sb.append("\r\n");
				sb.append(resData.get(key));
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
	 StringBuffer sb = new StringBuffer();
	 sb.append("POST /index.html HTTP/1.1\r\n");
	 sb.append("Content-Type: text/html; charset=UTF-8\r\n");
	 sb.append("\r\n");
	
	 HTTPHeaderControllerImpl p = new HTTPHeaderControllerImpl();
	 System.out.println(p.createHTTPProtocol(p.parseHTTPHeader(sb.toString())));
	 }
}
