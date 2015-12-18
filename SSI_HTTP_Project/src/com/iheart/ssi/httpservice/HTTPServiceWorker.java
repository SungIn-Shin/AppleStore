package com.iheart.ssi.httpservice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.iheart.ssi.httpparser.HTTPHeaderParser;
import com.iheart.ssi.httpparser.HTTPHeaderParserImpl;
import com.iheart.ssi.logger.LogFileHandler;
import com.iheart.ssi.logger.LogLevel;
import com.iheart.ssi.logger.Logger;
import com.iheart.ssi.socket.SocketServer_Linux;
import com.iheart.ssi.socket.SocketServer_Windows;

public class HTTPServiceWorker implements Runnable {
	//
	private DataInputStream httpRequest;
	private DataOutputStream httpResponse;
	private SocketServer_Linux linuxServer;
	private SocketServer_Windows windowServer;
	//private Socket socket;
	private String http_conf_path, log_conf_path, web_inf_path, dynamic_log_header, os_type;
	
	private HTTPHeaderParser httpParser;
	
	private static final Logger log = Logger.getLogger(HTTPServiceWorker.class);
	
	/**
	 * 1. userArgs[0] - http.conf 경로
	 * 2. userArgs[1] - log.conf 경로
	 * 3. userArgs[2] - WEB-INF 경로
	 * 4. userArgs[3] - OS type
	 * 5. 5번째 부터 사용자 지정 로그
	 **/
	public HTTPServiceWorker(Socket socket, SocketServer_Linux server, String[] userArgs) {
		try {
			dynamic_log_header = "";
			
			setParameter(userArgs);
			
			this.linuxServer = server;
			
			log.setDynamicLogHeader(dynamic_log_header);
			log.setProperty(log_conf_path);
			log.addHandler(new LogFileHandler(log_conf_path));
			log.write(LogLevel.INFO, "Connect!");
			
			httpRequest = new DataInputStream(socket.getInputStream());
			httpResponse = new DataOutputStream(socket.getOutputStream());
			httpParser = new HTTPHeaderParserImpl(web_inf_path, http_conf_path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public HTTPServiceWorker(Socket socket, SocketServer_Windows server, String[] userArgs) {
		try {
			dynamic_log_header = "";
			
			setParameter(userArgs);
			
			this.windowServer = server;
			
			log.setDynamicLogHeader(dynamic_log_header);
			log.setProperty(log_conf_path);
			log.addHandler(new LogFileHandler(log_conf_path));
			log.write(LogLevel.INFO, "Connect!");
			
			httpRequest = new DataInputStream(socket.getInputStream());
			httpResponse = new DataOutputStream(socket.getOutputStream());
			httpParser = new HTTPHeaderParserImpl(web_inf_path, http_conf_path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		byte[] reqArr = new byte[8190]; // 8Kb
		if(windowServer != null){
			windowServer.read(httpRequest, reqArr);
			byte[] resProtocol = httpParser.parseHTTPHeader(reqArr);
			windowServer.write(httpResponse, resProtocol);// write and flush
		} else if(linuxServer != null){
			linuxServer.read(httpRequest, reqArr);
			byte[] resProtocol = httpParser.parseHTTPHeader(reqArr);
			linuxServer.write(httpResponse, resProtocol);// write and flush
		}
	}
	
	private void setParameter(String[] userArgs) {
		this.http_conf_path	=	userArgs[0];	//	http.conf파일 	->	HTTPServiceParser
		this.log_conf_path	=	userArgs[1];	//	log.conf파일		->	logger
		this.web_inf_path	=	userArgs[2];	//	WEB-INF폴더 경로	->	HTTPServiceParser
		this.os_type = userArgs[3];				//	OS타입			->	Windows | Linux
		System.out.println(os_type);
		for(int i = 4 ; i < userArgs.length; i++){
			// 4번째 파라미터부터 dynamic_log_header이다. 몇개가 들어오건 그건 사용자가 알아서할일.
			this.dynamic_log_header += " " + userArgs[i];
		}
	}
}
