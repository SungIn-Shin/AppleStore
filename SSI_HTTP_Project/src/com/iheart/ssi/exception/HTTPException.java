package com.iheart.ssi.exception;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class HTTPException extends Exception {
	//
	public static final Map<String, String> errorCode = new HashMap<>();
//	private static final PropertyLoader loader = PropertyLoader.getInstance();
	public HTTPException() {
		//4xx code
		errorCode.put("400", "400 Bad Request");
		errorCode.put("404", "404 Not Found");
		errorCode.put("405", "405 Method Not Allowed");
		errorCode.put("414", "414 Request-URI Too Long");
		//5xx code
		errorCode.put("503", "503 Service Unavailable");
		errorCode.put("505", "505 HTTP Version Not Supported");
	}
	
	public String getErrorCode(String code) {
		return errorCode.get(code);
	}

	@Override
	public String getMessage() {
		//
		return super.getMessage();
	}
	//
}
