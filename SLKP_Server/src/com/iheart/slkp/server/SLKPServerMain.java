package com.iheart.slkp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class SLKPServerMain {
	//
	private ServerSocket serverSocket;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;

	private String id, pwd;

	public SLKPServerMain() {
		//
		id = "test";
		pwd = "test";
		try {

			serverSocket = new ServerSocket(9999);
			System.out.println("서버시작");
			process();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void process() {
		byte[] typeArr = new byte[2];
		byte[] lenArr = new byte[2];
		byte[] messageArr = null;
		String type = "";
		int len = 0;
		String message = "";
		while (true) {
			try {
				socket = serverSocket.accept();
				System.out.println("소켓 접속함 : " + socket.getInetAddress());
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

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

				if (type.equals("AB")) {
					// 인증 프로토콜
					messageArr = new byte[len + 1];
					for (int i = 0; i < len; i++) {
						messageArr[i] = dis.readByte();
					}
					message = new String(messageArr);
					String[] split = message.split("/");
					String userId = split[0].trim();
					String userPwd = split[1].trim();

					if (userId.equals(id) && userPwd.equals(pwd)) {
						type = "BC";
						message = "success";
						ByteBuffer bb = SLKPUtil.makeOfProtocol(message, type);

						dos.write(bb.array());
						dos.close();
					} else {
						type = "BC";
						message = "fail";
						ByteBuffer bb = SLKPUtil.makeOfProtocol(message, type);

						dos.write(bb.array());
						dos.close();
					}

				} else { // type ="CD"
					//
					messageArr = new byte[len + 1];
					for (int i = 0; i < len; i++) {
						messageArr[i] = dis.readByte();
					}
					message = new String(messageArr);
					System.out.println("Message : " + message);
				}
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
	}

	public static void main(String[] args) {
		//
		new SLKPServerMain();
	}
}
