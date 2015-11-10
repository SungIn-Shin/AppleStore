package com.iheart.ssi.socket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.iheart.ssi.httpservice.HTTPServiceWorker;

public class SocketServer extends SocketMain{
	//
	private ServerSocket serverSocket;
	private Socket socket;
	private ExecutorService threadPool;
	
	public SocketServer(){
		//
//		threadPool = Executors.
		
		serverSocket = open(serverSocket, 9999);
		process();
	}
	
	public void process(){
		//
		while(true){
			socket = accept(serverSocket);
			HTTPServiceWorker workerThread = new HTTPServiceWorker(socket, this);
			workerThread.start();
		}
	}
	
	public static void main(String[] args) {
		new SocketServer();
	}
}
