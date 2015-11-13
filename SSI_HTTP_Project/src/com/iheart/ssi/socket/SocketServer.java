package com.iheart.ssi.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.iheart.ssi.httpservice.HTTPServiceWorker;
import com.iheart.ssi.logger.Logger;

public class SocketServer extends SocketMain {
	//
	private ServerSocket serverSocket;
	private Socket socket;
	private static final Logger log = Logger.getLogger(SocketServer.class);

	public SocketServer() {
		//
		// threadPool = Executors.
		serverSocket = open(serverSocket, 9999);
		process();
	}

	public void process() {
		//
		while (true) {
			try {
//				socket = accept(serverSocket);
				socket = serverSocket.accept();
				log.alert(socket.getRemoteSocketAddress() + "접속");
				HTTPServiceWorker workerThread = new HTTPServiceWorker(socket, this);
				workerThread.start();
			} catch (IOException e) {
				//
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new SocketServer();
	}
}
