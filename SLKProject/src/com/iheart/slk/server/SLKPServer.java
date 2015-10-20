package com.iheart.slk.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SLKPServer {
	//
	private ServerSocket server;
	private Socket socket;
	private Map<Socket, DataOutputStream> clients;
	private DataOutputStream dos;
	
	private String id, pwd;
	
	public SLKPServer(String startTime){
		//
		try {
			server = new ServerSocket(9999);
			clients = new HashMap<>();
			this.id = "test";
			this.pwd = "test";
			System.out.println("[서버 시작 시간]  " +startTime);
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
		process();
	}
	
	private void process() {
		// 
		while(true){
			try {
				System.out.println("소캣 연결 대기중...");
				socket = server.accept(); // 소켓이 연결될때까지 blocking
				socket.setSoTimeout(10 * 1000);
				dos = new DataOutputStream(socket.getOutputStream());
				System.out.println("소켓 연결!!");
				System.out.println("Time Out " + "[" + socket.getSoTimeout() + "]");
				System.out.println("연결 정보 [IP] " + socket.getRemoteSocketAddress());
				System.out.println("연결 정보 [Port] " + socket.getPort());
				System.out.println("========================================");
				
				//접속한 사용자 관리.
				clients.put(socket, dos);
				getClientSize();
				SLKPReceiver receiver = new SLKPReceiver(socket, this);
				receiver.start();
				System.out.println("["+ receiver.getName()+"]" +"생성");
			} catch (IOException e) {
				// 서버가 종료되면 안됨. 다시 살림.
				System.out.println("서버가 종료되는거같아서 다시 살립니다~");
				String startTime = getNowTime();
				new SLKPServer(startTime);
			}
		}
	}
	//
	
	
	/**
	 * @param sendMessage
	 * 모든 list 사용자에게 메세지 전송.
	 */
	public void broadCastMessage(byte[] sendMessage){
		Iterator<Socket> it = clients.keySet().iterator();
		try {
			
			while(it.hasNext()){
				Socket client = it.next();
				DataOutputStream dos = clients.get(client);
				dos.write(sendMessage);
			}
			
		} catch (IOException e) {
			//
			System.out.println("[DataOutputStream 으로 write하는데 에러가나는듯함. 일단 체크.]");
			e.printStackTrace();
		} 
	}
	
	public void removeClient(Socket socket){
		
		clients.remove(socket);
		
		try {
			
			socket.close();
			System.out.println("start");
			Thread.sleep(1000);
			System.out.println("end");
			
		} catch (IOException e) {
			//
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getClientSize(){
		System.out.println("[접속자 수]  " + "["+ clients.size() + "명]");
	}

	public String getId() {
		return id;
	}

	public String getPwd() {
		return pwd;
	}


	public static void main(String[] args) {
		//
		String startTime = getNowTime();
		
		new SLKPServer(startTime);
	}

	private static String getNowTime() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR);
		int minute = cal.get(Calendar.MINUTE);
		String startTime = year +"년-" + month + "월-" + day +"일" + " " + hour +"시" +minute +"분";
		return startTime;
	}
}
