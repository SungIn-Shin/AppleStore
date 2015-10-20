package com.iheart.slk.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.iheart.slk.util.SLKPUtil;

public class SLKPClient {
	private BufferedReader br;
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket socket;
	
	
	public static void main(String[] args) throws InterruptedException {
		new SLKPClient();
	}

	
	public SLKPClient(){
		try {
			
			socket = new Socket();
			// Socket연결 자체에 대한 TimeOut 
			// 5000밀리세컨 동안 연결이 되지 않으면 타임아웃 발생시킴.
			//socket.connect(new InetSocketAddress("127.0.0.1", 9999), 5000);
			socket.connect(new InetSocketAddress("192.168.0.178", 9999), 5000);
			socket.setSoTimeout(5000);
			br = new BufferedReader(new InputStreamReader(System.in));
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			process();
		} catch(SocketTimeoutException e){
			String msg = "[SocketTimeoutException] 연결 전에 timeout발생. 연결지연현상.";
			exceptionMessage(e, msg);
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}
	
	public void process(){
		try {
			
			while (true) {
				//
				System.out.println("엔터치세요!");
				br.readLine();
				// 서버로 전송
				// Type 잘못보내기
//				dos.write(SLKPUtil.convertToProtocol("test/test", "bb"));
//				dos.flush();

				
				dos.write(SLKPUtil.convertToProtocol("test/test", "ab"));
				dos.flush();

//				Thread.sleep(2000);
				
				// 서버 데이터 읽기
				readMessage(dis);

				Thread.sleep(2000);
//
				dos.write(SLKPUtil.convertToProtocol("대화하자 헤헤헤헤", "cd"));
				dos.flush();
				
				dos.write(SLKPUtil.convertToProtocol("대화하자 헤헤헤헤", "cd"));
				dos.flush();
				
				dos.write(SLKPUtil.convertToProtocol("대화하자 헤헤헤헤", "cd"));
				dos.flush();
				
				dos.write(SLKPUtil.convertToProtocol("대화하자 헤헤헤헤", "cd"));
				dos.flush();
//				
//				Thread.sleep(3000);
//				
//				readMessage(dis);
				
			}
		} catch(SocketException e){
			System.out.println("[SocketException read() error] 서버와 소켓 연결이 끊겼습니다.");
			System.out.println("[ERROR MESSAGE] "+e.getMessage());
			closer(socket, dis, dos);
		} catch (IOException e) {
			// DataInputStream 에서 read()에서 에러가 나거나, 
			// DataOutputStream 에서 Socket이 닫겨있을때 recv failed error가 난다.
			System.out.println("[IOException] 111");
			System.out.println("[ERROR MESSAGE] "+e.getMessage());
			closer(socket, dis, dos);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally{
			System.out.println("[Finally] 클라이언트를 다시시작하세요.");
		}
	}
	

	private void readMessage(DataInputStream dis) {
		byte[] typeArr = new byte[2];
		byte[] lenArr = new byte[2];
		
			try {
				// 스트림의 2바이트를 읽어와 typeArr에 담는다. return - 읽어온 바이트 수
				int typeReadCount = dis.read(typeArr, 0, 2);
				// 위에서 읽은 바이트 다음 2바이트를 읽어오 lenArr에 담는다. return - 읽어온 바이트 수
				int lenReadCount = dis.read(lenArr, 0, 2);
				
				if(typeReadCount == -1){ throw new SocketException("서버와 연결이 끊겼습니다."); }
				if(lenReadCount == -1){ throw new SocketException("서버와 연결이 끊겼습니다."); }
				
				String type = new String(typeArr, 0, 2, "UTF-8");
				short len = SLKPUtil.BytesToShort(lenArr, 1);

				byte[] bodyArr = new byte[len];
				int bodyByteCount = dis.read(bodyArr, 0, len);
				
				String body = new String(bodyArr, 0, bodyByteCount, "UTF-8");
//				System.out.println("Type ->" + type);
//				System.out.println("Length ->" + len);
				System.out.println("Body ->" + body);
			} catch(SocketException e){
				String msg = "[SocketException read() error] 서버로부터 데이터를 읽어올 수 없습니다. Socket 연결이 끊겼겠지요?"; 
				exceptionMessage(e, msg);
			} catch(SocketTimeoutException e ) {
				String msg = "[SocketTimeoutException] 서버 응답 없음... 소켓 연결을 끊습니다.";
				exceptionMessage(e, msg);
			} catch (UnsupportedEncodingException e) {
				String msg = "[UnsupportedEncodingException] 인코딩에 문제가 있습니다.";
				exceptionMessage(e, msg);
			} catch (IOException e) {
				//
				String msg = "[IOException] IOException 222";
				exceptionMessage(e, msg);
			}
	}
	
	private void exceptionMessage(Exception e, String msg){
		System.out.println(msg);
		System.out.println("[ERROR MESSAGE] "+e.getMessage());
		closer(socket, dis, dos);
	}
	
	/**
	 * @param socket
	 * @param dis
	 * @param dos
	 * 연결을 끊는다.
	 */
	private void closer(Socket socket, DataInputStream dis, DataOutputStream dos) {
		try {
			if(!socket.isClosed()){ // 소켓이 close되지 않은 상태이면 밑을 실행.
				if(dos != null){
					System.out.println("DataOutputStream Close()");
					dos.close();
				} //
				if(dis != null){ 
					System.out.println("DataInputStream Close()");
					dis.close();
				} // 
				if(socket != null){
					System.out.println("Socket Close()");
					socket.close(); 
				} //
				
			}
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}
}
