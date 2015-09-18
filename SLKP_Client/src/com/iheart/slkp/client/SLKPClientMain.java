package com.iheart.slkp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * @author 성인
 *  --2Byte---2Byte----------가변길이------
 * |		|		|					|
 * |Type	|Length	|	Message			|
 * |		|		|					|
 *  ------------------------------------
 */
public class SLKPClientMain {
	//
	
	private DataInputStream dis;	
	private DataOutputStream dos;
	private Socket socket;
	private String ip;
	private int port;

	public SLKPClientMain() {
		//
		ip = "127.0.0.1";
		port = 9999;
		try {
			socket = new Socket(ip, port);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}

	// 사용자 인증 프로토콜 전송 메서드 AB Type 사용
	public void userAuthentication(String userInfo) {
		//
		String type = "AB";		
		ByteBuffer bb = SLKPUtil.makeOfProtocol(userInfo, type);
				/** 여기까지 발신 **/
				try {
					dos.write(bb.array());
					System.out.println("발신완료");
					readMessage();
				} catch (IOException e) {
					//
					e.printStackTrace();
				} finally {
					try {
						dis.close();
					} catch (IOException e) {
						//
						
						e.printStackTrace();
					}
				}

				
	}

	// 서버에게 메세지를 보내는 함수 : Type -> CD
	public void sendMessage(String msg) {
		//
		String type = "CD";
		
		ByteBuffer bb = SLKPUtil.makeOfProtocol(msg, type);
		
		/** 여기까지 발신 **/
		try {
			dos.write(bb.array());
			System.out.println("발신완료");
		} catch (IOException e) {
			// 
			e.printStackTrace();
		}

	}
	
	public void readMessage() throws IOException{
		byte[] typeArr = new byte[2];
		byte[] lenArr = new byte[2];
		byte[] messageArr = null;
		String type = "";
		int len = 0;
		String message = "";
		
		// 앞에서 4바이트 (Header)만 읽어들인다
		for (int i = 0; i < 2; i++) {
			typeArr[i] = dis.readByte();
		}

		for (int i = 0; i < 2; i++) {
			lenArr[i] = dis.readByte();
		}

		type = new String(typeArr);
		System.out.println("Type : " + type);

		len = SLKPUtil.byteArrayToShort(lenArr, 0);
		System.out.println("Length : " + len);
		
		if(type.equals("BC")){
			messageArr = new byte[len + 1];
			for (int i = 0; i < len; i++) {
				messageArr[i] = dis.readByte();
			}
			message = new String(messageArr).trim();
			System.out.println("서버응답 : " + message);
			if(message.equals("fail")){
				System.out.println("서버와의 연결이 끊겼습니다.");
				socket.close();
				System.out.println("소켓 연결을 끊었나요? " + socket.isClosed());
			}
		}
	}


	public static void main(String[] args) {
		SLKPClientMain client = new SLKPClientMain();
		//client.sendMessage("야이야이t");
		client.userAuthentication("test/test");
	}
}
