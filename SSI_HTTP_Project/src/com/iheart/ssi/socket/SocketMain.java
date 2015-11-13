package com.iheart.ssi.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;

import com.iheart.ssi.exception.NotSupportCharsetException;
import com.iheart.ssi.exception.SocketPortNumberException;
import com.iheart.ssi.logger.Logger;

public class SocketMain {
	//
	private static final Logger logger = Logger.getLogger(SocketMain.class);
	private static final String UTF_8 = "UTF-8";
	private static final String euc_kr = "EUC-KR";
	/**
	 * @param serverSocket
	 * @return
	 * @throws IOException 
	 */
	public Socket accept(ServerSocket serverSocket) throws IOException{		
		//
		return serverSocket.accept();
	}
	
	/**
	 * @param serverSocket
	 * @param port
	 * @return
	 */
	public ServerSocket open(ServerSocket serverSocket, int port){
		//
		if(port < 0 || port > 65535){
			throw new SocketPortNumberException("입력 Port : ["+ port + "] -> Port의 범위는 0~65535 까지 입니다.");
		}
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
		return serverSocket;
	}
	
	
	/**
	 * @param input : Stream
	 * @param off : 넣을 배열의 위치
	 * @param len : 읽어올 길이
	 * @return
	 * @throws IOException 
	 */
	public byte[] read(InputStream input, byte[] b , int off, int len) throws IOException, IndexOutOfBoundsException, SocketTimeoutException, NullPointerException{
		//
		int readCount = 0;
		
			if(off > len){
				throw new IndexOutOfBoundsException("[off값이 len보다 큽니다. 다시 설정하세요.]");
			}
			
			if(b == null){
				throw new NullPointerException("[byte array가 Null입니다. 확인해주세요.]");
			}
			
			readCount = input.read(b, off, len);
			
			if(readCount == -1){
				throw new SocketTimeoutException("[Socket의 연결이 끊겨서 read할 수 없습니다.]");
			}
		
		return b;
	}
	
	
	
	/**
	 * @param input
	 * @param arrSize
	 * @return 
	 * 브라우저에서는 Windows 기준으로 ISO-8859-1 Encoding을 해서 URI에 담아서 넘긴다.
	 * 이 값을 UTF-8로 Decode하는 작업을 해주어야 한글이 깨지지 않는다.
	 * @throws NotSupportCharsetException 
	 */
	public String read(InputStream input, int arrSize, String charSet, String encode) throws NotSupportCharsetException{
		//
		if(!encode.equals(UTF_8) && !encode.equals(euc_kr)){
			String	msg = "[지원하지 않는 encode type입니다.] ->" + encode + "\r\n";
					msg+= "[지원 인코딩 타입] -> " + UTF_8 + ", " + euc_kr;
			throw new NotSupportCharsetException(msg);
		}
		
		String str = "";
		String result= "";
		try {
			byte[] reqArr = new byte[arrSize];
			
			int readCount = input.read(reqArr);
			
			if(readCount == -1){
				logger.info("[클라이언트 접속 종료.]");
			}
			// Window 기본 Encoding - ISO-8859-1
			str = new String(reqArr, charSet);
			// ISO-8859-1 을 UTF-8로 Decode
			result = URLDecoder.decode(str, encode);
			
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
		
		return result.trim();
	}
	
	
	/**
	 * @param br
	 * @return
	 */
	public String readLine(BufferedReader br){
		//
		String readLine = "";
		String temp = "";
		try {
			temp = br.readLine();
			while(temp != null){
				//
				readLine = temp;
			}
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
		return readLine;
	}
	
	/**
	 * @param socket
	 */
	public void close(Socket socket){
		try {
			socket.close();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
	}
	
	
	public void write(OutputStream output, byte[] writeArr){
		
		try {
			output.write(writeArr);
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}