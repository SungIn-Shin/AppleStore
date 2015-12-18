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
import java.util.concurrent.TimeUnit;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

import com.iheart.ssi.httpservice.HTTPServiceWorker;

public class SocketServer_Linux extends SocketMain implements Daemon {
	//
	private ExecutorService threadPool;
	private ServerSocket serverSocket;
	private Socket socket;
	private boolean flag;
	private String[] userArgs;
	private Properties prop;
	
	private String status = "";
	private Thread startThread;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.daemon.Daemon#init(org.apache.commons.daemon.
	 * DaemonContext) 초기화 메서드이다. 1. userArgs[0] - http.conf 2. userArgs[1] -
	 * log.conf 3. userArgs[2] - WEB-INF Path - WEB_HOME 4. 4번째 파라미터부터 사용자 설정
	 * log Header이다. 개수제한 없음.
	 */
	@Override
	public void init(DaemonContext context) throws DaemonInitException, Exception {
		//
		System.out.println("init...");
		String[] args = context.getArguments();
		if (args != null) {
			for (String arg : args) {
				System.out.println(arg);
			}
		}
		status = "INITED";
		System.out.println("init OK.");
		System.out.println();
		
		prop = new Properties();
		try {
			this.userArgs = context.getArguments();
			prop.load(new BufferedReader(new InputStreamReader(new FileInputStream(userArgs[0]), "UTF-8")));
			flag = true;
			serverSocket = open(serverSocket, Integer.parseInt(prop.getProperty("port")));
			threadPool = Executors.newFixedThreadPool(5);
			
			startThread = new Thread(){
				@Override
				public void run() {
					while (flag) {
						socket = accept(serverSocket);
						HTTPServiceWorker workerThread = new HTTPServiceWorker(socket, new SocketServer_Linux(),userArgs);
						threadPool.execute(workerThread);
					}
				}
			};startThread.start();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.daemon.Daemon#start() 프로세스를 start했을때 메서드이다.
	 */
	@Override
	public void start() throws Exception {
		//
		status = "STARTED";
		System.out.println("status: " + status);
		System.out.println("start...");
		System.out.println("start OK.");
		System.out.println();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.daemon.Daemon#stop() Daemon이 stop()이 호출되면 실행되는
	 * 메서드이다. 종료되는 시점에 작업을 마무리 하도록 처리한다.
	 */
	@Override
	public void stop() throws Exception {
		//
		
		boolean isFinish = threadPool.awaitTermination(5000, TimeUnit.MILLISECONDS);
		if(isFinish){
			threadPool.shutdown();
			System.out.println("안정적으로 Thread 종료 Stop!!!!!!!!!!");
		} else{
			System.out.println("처리 다 끝내지 않고 Thread 종료 Stop!!!!!!!!!!");
		}
		flag = false;
	}

	@Override
	public void destroy() {
		//
		System.out.println("status: " + status);
		System.out.println("destroy...");
		status = "DESTROIED";
		System.out.println("destroy OK.");
		System.out.println();
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
			e.printStackTrace();
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serverSocket;
	}

}
