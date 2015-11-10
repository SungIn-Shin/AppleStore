package com.iheart.ssi.httpservice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.iheart.ssi.httpparser.HTTPHeaderController;
import com.iheart.ssi.httpparser.HTTPHeaderControllerImpl;
import com.iheart.ssi.logger.Logger;
import com.iheart.ssi.socket.SocketServer;

public class HTTPServiceWorker extends Thread {
	//
	private DataInputStream httpRequest;
	private DataOutputStream httpResponse;
	private SocketServer server;
	private Socket socket;
	private HTTPHeaderController headerParser;
	private Map<String, String> headerMap;
	
	
	//Logger
	private Logger log = Logger.getLogger(HTTPServiceWorker.class);
	public HTTPServiceWorker(Socket socket, SocketServer server) {
		// 
		this.socket = socket;
		this.server = server;
		try {
			httpRequest = new DataInputStream(socket.getInputStream());
			httpResponse = new DataOutputStream(socket.getOutputStream());
			headerParser = new HTTPHeaderControllerImpl();
			headerMap = new HashMap<>();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		//
		while(true){
			
			headerMap = headerParser.parseHTTPHeader(httpRequest);
		}
	}
	//
}
