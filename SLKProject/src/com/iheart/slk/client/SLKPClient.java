package com.iheart.slk.client;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.iheart.slk.util.SLKPUtil;

public class SLKPClient {
	public static void main(String[] args) throws InterruptedException {
		BufferedReader br = null;
		DataInputStream dis = null ;
		DataOutputStream dos = null;
		Socket socket = null;
		
		try {
			socket = new Socket();
			// Socket연결 자체에 대한 TimeOut 
			// 5000밀리세컨 동안 연결이 되지 않으면 타임아웃 발생시킴.
			socket.connect(new InetSocketAddress("127.0.0.1", 9999), 5000);
			//socket.connect(new InetSocketAddress("192.168.0.178", 9999), 5000);
			socket.setSoTimeout(5000);
			br = new BufferedReader(new InputStreamReader(System.in));
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			
			while (true) {
				//
				System.out.println("엔터치세요!");
				br.readLine();
				// 서버로 전송
				if(socket.isConnected()){
					new SocketException();
				}
				// Type 잘못보내기
//				dos.write(SLKPUtil.convertToProtocol("test/test", "bb"));
//				dos.flush();

				
				dos.write(SLKPUtil.convertToProtocol("test/test", "ab"));
				dos.flush();

				Thread.sleep(2000);
				
				// 서버 데이터 읽기
				readMessage(dis);

				Thread.sleep(2000);

				dos.write(SLKPUtil.convertToProtocol("대화하자 헤헤헤헤", "cd"));
				dos.flush();

				readMessage(dis);
				
				Thread.sleep(3000);
			}
		} catch(SocketTimeoutException st){
			System.out.println("[SocketTimeoutException] 서버 응답 없음... 소켓 연결을 끊습니다." + st.getMessage());
			closer(socket, dis, dos);
		} catch (IOException e) {
			//
			System.out.println("[IOException] " + e.getMessage());
			closer(socket, dis, dos);
		} finally{
			System.out.println("[Finally] 할라믄 다시시작하세요.");
			closer(socket, dis, dos);
		}
	}

	/**
	 * @param socket
	 * 소켓 연결 끊기
	 */
	private static void closer(Socket socket, DataInputStream dis, DataOutputStream dos) {
		try {
			dos.close();
			dis.close();
			socket.close();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}

	private static void readMessage(DataInputStream dis) throws IOException{
		byte[] typeArr = new byte[2];
		byte[] lenArr = new byte[2];
		
			// 스트림의 2바이트를 읽어와 typeArr에 담는다. return - 읽어온 바이트 수
			int typeReadCount = dis.read(typeArr, 0, 2);
			// 위에서 읽은 바이트 다음 2바이트를 읽어오 lenArr에 담는다. return - 읽어온 바이트 수
			int lenReadCount = dis.read(lenArr, 0, 2);
			
			// 어느시점에 강제종료할지모르니...두개다 체크
			if((typeReadCount == -1) || (lenReadCount == -1)){
				throw new IOException("[서버에서 강제로 연결을 종료하였습니다.]");// 강제로 IOException을 발생시킴
			}
			
			String type = new String(typeArr, 0, 2, "UTF-8");
			short len = SLKPUtil.BytesToShort(lenArr, 1);

			byte[] bodyArr = new byte[len];
			int bodyByteCount = dis.read(bodyArr, 0, len);

			String body = new String(bodyArr, 0, bodyByteCount, "UTF-8");
			System.out.println("Type ->" + type);
			System.out.println("Length ->" + len);
			System.out.println("Body ->" + body);
	}
}
