package com.iheart.ssi.main;

import com.iheart.ssi.socket.SocketServer;
import com.iheart.ssi.util.PropertyLoader;

public class StartMain {
	public static void main(String[] args) {
		//
		PropertyLoader loader = PropertyLoader.getInstance();
		loader.setConfig(args[0], args[1], args[2]);
		
		SocketServer server = new SocketServer();
		server.process();
	}
}
