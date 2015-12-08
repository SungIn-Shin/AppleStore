package com.iheart.ssi.httpservice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.iheart.ssi.httpparser.HTTPHeaderParser;
import com.iheart.ssi.httpparser.HTTPHeaderParserImpl;
import com.iheart.ssi.socket.SocketServer;

public class HTTPServiceWorker implements Runnable {
	//
	private DataInputStream httpRequest;
	private DataOutputStream httpResponse;
	private SocketServer server;
	private HTTPHeaderParser httpParser;
	
	public HTTPServiceWorker(Socket socket, SocketServer server) {
		// 
		this.server = server;
		try {
			httpRequest = new DataInputStream(socket.getInputStream());
			httpResponse = new DataOutputStream(socket.getOutputStream());
			httpParser = new HTTPHeaderParserImpl();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		//
		byte[] reqArr = new byte[8190]; // 8Kb
		server.read(httpRequest, reqArr);
		byte[] resProtocol = httpParser.parseHTTPHeader(reqArr);
		server.write(httpResponse, resProtocol);// write and flush
	}
	//
}
