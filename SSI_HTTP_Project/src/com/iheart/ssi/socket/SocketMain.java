package com.iheart.ssi.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.iheart.ssi.exception.SocketPortNumberException;
import com.iheart.ssi.logger.Logger;

public class SocketMain {
	//
	private static final Logger logger = Logger.getLogger(SocketMain.class);
	/**
	 * @param serverSocket
	 * @return
	 * @throws IOException 
	 */
	public Socket accept(ServerSocket serverSocket) throws IOException{		
		Socket socket = null;
		socket = serverSocket.accept();
		return socket;
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
	 */
	public byte[] read(InputStream input, byte[] b , int off, int len){
		//
		int readCount = 0;
		
		if(off > len){
			throw new IndexOutOfBoundsException("[off값이 len보다 큽니다. 다시 설정하세요.]");
		}
		
		if(b == null){
			throw new NullPointerException("[배열이 Null입니다. 확인해주세요.]");
		}
		
		try {
			//
			readCount = input.read(b, off, len);
			
			if(readCount == -1){
				throw new SocketTimeoutException("[Socket의 연결이 끊겨서 read할 수 없습니다.]");
			}
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
		return b;
	}
	
	public String read(InputStream input, int arrSize){
		byte[] reqArr = new byte[arrSize];
		String str = "";
		try {
			int readCount = input.read(reqArr);
			
			if(readCount == -1){
				logger.error("[클라이언트 강제 접속 종료.]");
			}
			
			if(readCount > reqArr.length){ 
				// Exception 발생
			}
			str = new String(reqArr, "UTF-8");
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
		
		return str;
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