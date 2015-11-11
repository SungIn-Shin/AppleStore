package com.iheart.ssi.httpservice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedHashMap;
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
	private HTTPHeaderController headerController;
	
	
	//Logger
	private static final Logger log = Logger.getLogger(HTTPServiceWorker.class);
	
	public HTTPServiceWorker(Socket socket, SocketServer server) {
		// 
		this.socket = socket;
		this.server = server;
		try {
			httpRequest = new DataInputStream(socket.getInputStream());
			httpResponse = new DataOutputStream(socket.getOutputStream());
			headerController = new HTTPHeaderControllerImpl();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		//
		Map<String, String> resMap = new LinkedHashMap<String, String>();
		String reqHeader = server.read(httpRequest, 8190);
		System.out.println("================================================");
		System.out.println("===================Request Header===============");
		System.out.println(reqHeader);
		System.out.println("===================Request Header===============");
		System.out.println("================================================");
		resMap = headerController.parseHTTPHeader(reqHeader);
		byte[] responsHeader = headerController.createHTTPProtocol(resMap);
		server.write(httpResponse, responsHeader);// write and flush
	}
	//
}
