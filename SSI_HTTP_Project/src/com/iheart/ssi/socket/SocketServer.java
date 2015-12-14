package com.iheart.ssi.socket;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.iheart.ssi.httpservice.HTTPServiceWorker;

public class SocketServer extends SocketMain{
	//
	private ExecutorService threadPool;
	private ServerSocket serverSocket;
	private Socket socket;
	private boolean flag;
	private String[] userArgs;
	private Properties prop;
	
	
	public static void main(String[] args) {
		// Server를 시작하면서 사용자로부터 argument를 입력받는다.
		SocketServer server = new SocketServer(args);
		server.process();
	}
	
	/**
	 * 1. userArgs[0] - http.conf 
	 * 2. userArgs[1] - log.conf
	 * 3. userArgs[2] - WEB-INF Path - WEB_HOME
	 **/
	public SocketServer(String[] userArgs) {
		//
		prop = new Properties();
		try {
			prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(userArgs[0]), "UTF-8")));
			this.userArgs = userArgs;
			flag = true;
			serverSocket = open(serverSocket, Integer.parseInt(prop.getProperty("port")));
			threadPool = Executors.newFixedThreadPool(5);
			process();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	


	public void process() {
		//
		while (flag) {
			socket = accept(serverSocket);
			// socket.setSoTimeout(5000);
			HTTPServiceWorker workerThread = new HTTPServiceWorker(socket, this, userArgs);
			threadPool.execute(workerThread);
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
			//log.write(LogLevel.ERROR, "accetp에서 SocketTimeoutException 발생");
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
			//log.write(LogLevel.ERROR, e.getMessage());
		} catch (IOException e) {
			//log.write(LogLevel.ERROR, e.getMessage());
		}
		return serverSocket;
	}
}
