package com.iheart.ssi.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.iheart.ssi.exception.SocketPortNumberException;

public class SocketMain {
	//
	/**
	 * @param serverSocket
	 * @return
	 */
	public Socket accept(ServerSocket serverSocket){
		Socket socket = null;
		try {
			socket = serverSocket.accept();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
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
}