package com.iheart.slk.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.iheart.slk.util.SLKPUtil;

public class SLKPReceiver extends Thread {
	//
	private SLKPServer server;
	private Socket socket;
	private DataInputStream dis;
	////////////////////////////////////
	private byte[] typeArr, lenArr, echoArr;
	private boolean isStop;
	private boolean userCheck;
	private String id, pwd;
	private String ip;
	
	public SLKPReceiver(Socket socket, SLKPServer server) {
		// 안녕하세요 어우어우~
		try {
			this.server = server;
			this.socket = socket;
			this.dis = new DataInputStream(socket.getInputStream());
			this.typeArr = new byte[2];
			this.lenArr = new byte[2];
			isStop = true;
			userCheck = false;
			id = server.getId();
			pwd = server.getPwd();
			ip = "[IP : "+ socket.getRemoteSocketAddress().toString() +"]";
		} catch (IOException e) {
			//
			System.out.println(ip + "[입력스트림 생성 중 에러 발생. 소켓 폐쇠 또는 셧다운 발생]" + e.getMessage());
			removeAndCloseSocket();
		}
		
	}
	@Override
	public void run() {
		//
		
			try {
				while(isStop){
					// 스트림의 2바이트를 읽어와 typeArr에 담는다. return - 읽어온 바이트 수
					int typeReadCount = dis.read(typeArr, 0, 2);
					// 위에서 읽은 바이트 다음 2바이트를 읽어오 lenArr에 담는다. return - 읽어온 바이트 수
					int lenReadCount = dis.read(lenArr, 0, 2);
					
					// read() 가 정상적으로 이뤄지면 읽어온 바이트를 반환.
					// 소켓연결이 끊기면 -1 반환
					if((typeReadCount == -1) || (lenReadCount == -1)){
						throw new IOException(ip + "[클라이언트의 연걸이 끊겼습니다.]");// 강제로 IOException을 발생시킴
					}
					
					//바이트 개수 체크 Type - 2byte, len - 2byte
					boolean isCorrectByteCount = checkHeaderByteCount(typeReadCount, lenReadCount);
					//읽어온 바이트를 Type(String) len(short)로 구분해서 정확한 데이터가 들어왔는지 체크한다.
					boolean isCorrectValue = checkHeaderValue(typeArr, lenArr);
					
					System.out.println("1 :" + typeReadCount);
					System.out.println("2 :" + lenReadCount); 
					System.out.println("3 :" + isCorrectByteCount); // 맞으면 true
					System.out.println("4 :" + isCorrectValue); // 맞으면 true
					
					String serverMsg = "-> [FROM SERVER ^^]";
					
					if(isCorrectByteCount && isCorrectValue){
						String type = new String(typeArr, 0, 2, "UTF-8");
						short len = SLKPUtil.BytesToShort(lenArr, 1);
						//short len = 5;
						byte[] bodyArr = new byte[len];
						
						int bodyByteCount = dis.read(bodyArr, 0, len);
						String body = new String(bodyArr, 0, bodyByteCount, "UTF-8");
						
						System.out.println("Type ->"+ type);
						System.out.println("Length ->" + len);
						System.out.println("Body ->" + body);
						
						if(type.equals("ab") && !userCheck){ //사용자가 ab를 보내고, 유저 인증상태가 false이면
							// 사용자 인증 절차를 실행하라.
							// 사용자 정보가 맞다면 userCheck 가 true로 바뀐다.
							echoArr = userInfoCheck(body);
							server.broadCastMessage(echoArr);
						} else if(type.equals("ab") && userCheck){ // 이미 인증된 사용자가 다시 인증 요청을 한다면.
							echoArr = SLKPUtil.convertToProtocol("이미 인증된 사용자입니다.", "cd");
							server.broadCastMessage(echoArr);
						}
						else if(type.equals("cd") && userCheck){ // Type이 'cd'이고 사용자 인증이 완료된 경우.
							//[사용자가 보낸 메세지 + 서버가보내는 메세지] 형식으로 사용자에게 보내준다.
							echoArr = SLKPUtil.convertToProtocol(body + serverMsg, "cd");
							server.broadCastMessage(echoArr);
						} 
					} else{ // 클라이언트가 보낸 데이터가 이상하면 그냥 종료시켜라.
						System.out.println(ip +"[클라이언트 패킷이 이상합니다.]");
						removeAndCloseSocket();
					}
				}
			} catch(SocketException e){
				System.out.println(ip + "[클라이언트가 강제 종료를 했습니다.]");
				removeAndCloseSocket();
			} catch(SocketTimeoutException e){
				System.out.println(ip + "[5초 이상 보내는 메세지가 없어서 연결을 끊습니다.]" + "[" + e.getMessage() + "]");
				removeAndCloseSocket();
			} catch (IOException e) {
				//
				System.out.println(ip + "[read() 에서 문제 생김]");
				removeAndCloseSocket();
				e.printStackTrace();
			}
	}
	//
	
	/**
	 * Exception이 났을때 소켓을 Map에서 remove 시키고, 
	 * 소켓을 close()한다. 
	 * 사용자 접속 수 체크까지 한다.
	 */
	private void removeAndCloseSocket() {
		server.removeClient(socket);
		server.getClientSize();
		isStop = false;
	}
	
	
	/**
	 * @param body
	 * 사용자 계정 확인
	 * 파라미터로 받은 body ("id/pwd") 를
	 * split "/" 로 잘라서 아이디와 패스워드를 받아낸다.
	 * 서버에있는 id, pwd 데이터와 사용자가 보낸 데이터가 일치하면
	 * userCheck (boolean) 을 true로 바꾸고 
	 * 클리언트에게 "success" 메세지를 "bc" type 으로 전송한다.
	 */
	private byte[] userInfoCheck(String body) {
		//
		String[] split = body.split("/");
		String userId = split[0];
		String userPwd = split[1];
		System.out.println("사용자 입력 아이디 : " + userId);
		System.out.println("사용자 입력 비번 : " + userPwd);
		
		// 사용자 입력정보와 서버의 계정정보가 일치하면.
		if(id.equals(userId) && pwd.equals(userPwd)){
			userCheck = true;
			return SLKPUtil.convertToProtocol("success", "bc");
		} else {
			userCheck = false;
			return SLKPUtil.convertToProtocol("fail", "bc");
		}
	}
	
	/**
	 * @param typeArr - 2byte의 type, String형태이다.
	 * @param lenArr - 2byte의 length, short 형태이다.
	 * @return header의 값이 올바르면 true 아니면 false
	 * 헤더의 byte 값을 변환하여 데이터를 분석한다.
	 * type - 'ab'(인증), 'cd'(양방향메세지) - 나머지는 들어오면 안되니 false 반환.
	 */
	private boolean checkHeaderValue(byte[] typeArr, byte[] lenArr) {
		//
		boolean flag = true;
		try {
			String type = new String(typeArr, 0, 2, "UTF-8");
			
			//short len = SLKPUtil.BytesToShort(lenArr, 1);
			// type 이 'ab' 와 'cd' 만 허용한다.
			// Client->Server 는 'ab' -인증 타입과, 'cd' - 양방향 전송만 할 수 있다.
			if(type.equals("ab")){
				flag = true;
			} else if(type.equals("cd")){
				flag = true;
			}else {
				flag = false;
			}
			
		} catch (UnsupportedEncodingException e) {
			//
			System.out.println(ip + "[문자 인코딩이 지원되지 않습니다.]" + e.getMessage());
		}
		return flag;
	}
	/**
	 * @param typeReadCount
	 * @param lenReadCount
	 * @return
	 * 헤더의 바이트 개수를 체크한다.
	 * Type - 2Byte, len - 2Byte
	 */
	private boolean checkHeaderByteCount(int typeReadCount, int lenReadCount) {
		//
		boolean flag = true;
		if(typeReadCount == -1 || typeReadCount == 0 || typeReadCount > 2){
			flag = false;
		} else if(lenReadCount == -1 || lenReadCount == 0 || lenReadCount > 2){
			flag = false;
		} 
		return flag;
	}
}
