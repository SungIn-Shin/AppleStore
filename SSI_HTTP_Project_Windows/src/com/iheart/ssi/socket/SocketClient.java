package com.iheart.ssi.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.iheart.ssi.util.PropertyLoader;

public class SocketClient extends SocketMain {
	//
	private Socket socket;
	PropertyLoader loader = PropertyLoader.getInstance();
	private String ip;
	private int port;

	public SocketClient() {
		//
		ip = loader.getProperty("SERVER_IP");
		port = Integer.parseInt(loader.getProperty("SERVER_PORT"));
		process();
	}

	public void process() {
		//
		BufferedReader br = null;
		try {
			while (true) {
				br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("GET or POST를 입력하세요");
				String sendMsg = br.readLine();
				if(sendMsg.equals("GET")){
					sendProtocol(testGETProtocol());
				} else if(sendMsg.equals("POST")){
					sendProtocol(testPOSTProtocol());
				} else {
					System.out.println("다시 입력하세요.");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendProtocol(String sendProtocol) {

		try {
			DataInputStream dis = null;
			DataOutputStream dos = null;

			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port));
			socket.setSoTimeout(1000 * 60); //readTimeOut
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());

			
			byte[] writeArr = sendProtocol.getBytes();
			writeConsol(dos, writeArr);

			Thread.sleep(2000);
			byte[] readArr = new byte[8190];
			dis.read(readArr);
			String readData = new String(readArr).trim();

			System.out.println("========SERVER -> CLIENT=========");
			System.out.println(readData);
			System.out.println("========SERVER -> CLIENT=========");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String testGETProtocol(){
		StringBuffer sb = new StringBuffer();
		// URI를 길게 넣었을때 테스트 - length Limit 100
		// sb.append("GET
		// /post.jsp?name=승찬&age=14URI가100이넘어가면414ERROR를발생시키게했습니다.그래서이테스트는에러가날것입니다.실제Server는100보다길죠^^이건테스트니까이렇게사용합니다.
		// HTTP/1.1\r\n");
		// sb.append("Content-Type: text/html; charset=UTF-8\r\n");
		// sb.append("\r\n");
		// 정상 Protocol
		sb.append("GET /post.jsp?name=승찬&age=14 HTTP/1.1\r\n");
		sb.append("Content-Type: text/html; charset=UTF-8\r\n");
		sb.append("\r\n");
		System.out.println("========GET CLIENT -> SERVER=========");
		System.out.println(sb.toString());
		System.out.println("========GET CLIENT -> SERVER=========");
		return sb.toString();
	}
	
	public String testPOSTProtocol(){
		StringBuffer sb = new StringBuffer();
//		 sb.append("POST /post.jsp HTTP/1.1\r\n");
//		 sb.append("Content-Type: text/html; charset=UTF-8\r\n");
//		 sb.append("\r\n");
//		 sb.append("name=성인&age=19");
//		 sb.append("&zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
		 //정상적인 요청
		 sb.append("POST /post.jsp HTTP/1.1\r\n");
		 sb.append("Content-Type: text/html; charset=UTF-8\r\n");
		 sb.append("\r\n");
		 sb.append("name=성인&age=19");
		System.out.println("========POST CLIENT -> SERVER=========");
		System.out.println(sb.toString());
		System.out.println("========POST CLIENT -> SERVER=========");
		return sb.toString();
	}


	public static void main(String[] args) {
		new SocketClient();
	}
}
