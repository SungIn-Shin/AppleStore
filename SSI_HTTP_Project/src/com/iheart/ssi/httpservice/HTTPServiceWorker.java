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
import com.iheart.ssi.socket.SocketServer;

public class HTTPServiceWorker implements Runnable {
	//
	private DataInputStream httpRequest;
	private DataOutputStream httpResponse;
	private SocketServer server;
	//private Socket socket;
	private String http_conf_path, log_conf_path, web_inf_path, dynamic_log_header;
	
	private HTTPHeaderParser httpParser;
	
	private static final Logger log = Logger.getLogger(SocketServer.class);
	
	/**
	 * 1. userArgs[0] - http.conf 경로
	 * 2. userArgs[1] - log.conf 경로
	 * 3. userArgs[2] - WEB-INF 경로
	 **/
	public HTTPServiceWorker(Socket socket, SocketServer server, String[] userArgs) {
		try {
			dynamic_log_header = "";
			
			setParameter(userArgs);
			
			this.server = server;
			
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

	private void setParameter(String[] userArgs) {
		this.http_conf_path	=	userArgs[0];	//	http.conf파일 	->	HTTPServiceParser
		this.log_conf_path	=	userArgs[1];	//	log.conf파일		->	logger
		this.web_inf_path	=	userArgs[2];	//	WEB-INF폴더 경로	->	HTTPServiceParser
		for(int i = 3 ; i < userArgs.length; i++){
			// 3번째 파라미터부터 dynamic_log_header이다. 몇개가 들어오건 그건 사용자가 알아서할일.
			this.dynamic_log_header += " " + userArgs[i];
		}
	}
	
	@Override
	public void run() {
		byte[] reqArr = new byte[8190]; // 8Kb
		server.read(httpRequest, reqArr);
		byte[] resProtocol = httpParser.parseHTTPHeader(reqArr);
		server.write(httpResponse, resProtocol);// write and flush
	}
}
