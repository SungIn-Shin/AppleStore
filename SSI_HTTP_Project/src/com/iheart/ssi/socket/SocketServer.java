package com.iheart.ssi.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.iheart.ssi.httpservice.HTTPServiceWorker;
import com.iheart.ssi.logger.LogLevel;
import com.iheart.ssi.logger.Logger;

public class SocketServer extends SocketMain{
	//
	private ExecutorService threadPool;
	private ServerSocket serverSocket;
	private Socket socket;
	private static final Logger log = Logger.getLogger(SocketServer.class);
	private boolean flag;
	public SocketServer() {
		//
		flag = true;
		serverSocket = open(serverSocket, 9999);
		threadPool = Executors.newFixedThreadPool(5);
		process();
	}

	public void process() {
		//
		try{
			while (flag) {
				socket = accept(serverSocket);
				// socket.setSoTimeout(5000);
				log.write(LogLevel.INFO, "[" + socket.getRemoteSocketAddress() + "]" + "접속");
				log.write(LogLevel.EMERG, "[" + socket.getRemoteSocketAddress() + "]" + "접속");
				log.write(LogLevel.DEBUG, "[" + socket.getRemoteSocketAddress() + "]" + "접속");
				HTTPServiceWorker workerThread = new HTTPServiceWorker(socket, this);
				threadPool.execute(workerThread);
			}
		} finally{
			
		}
	}

	/**
	 * @param serverSocket
	 * @return
	 * @throws IOException
	 */
	private Socket accept(ServerSocket serverSocket) {
		//
		Socket socket = null;
		try {
			socket = serverSocket.accept();
		} catch (SocketTimeoutException e) {
			log.write(LogLevel.DEBUG, "accetp에서 SocketTimeoutException 발생");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return socket;
	}

	/**
	 * @param serverSocket
	 * @param port
	 * @return
	 */
	private ServerSocket open(ServerSocket serverSocket, int port) {
		//
		try {
			if (port < 1001 || port > 65535) {
				throw new IllegalArgumentException("입력 Port : [" + port + "] -> Port의 범위는 1001~65535 까지 입니다.");
			}
			serverSocket = new ServerSocket(port);
		} catch (IllegalArgumentException e) {
			log.write(LogLevel.ALERT, e.getMessage());
		} catch (IOException e) {
			log.write(LogLevel.ALERT, e.getMessage());
		}
		return serverSocket;
	}
}
