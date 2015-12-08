package com.iheart.ssi.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.iheart.ssi.logger.LogLevel;
import com.iheart.ssi.logger.Logger;

public class SocketMain {
	//
	private static final Logger logger = Logger.getLogger(SocketMain.class);
	
	public int read(InputStream input, byte[] reqArr){
		int readCount = 0;
		try {
			readCount = input.read(reqArr);
		} catch(SocketTimeoutException e){
			logger.write(LogLevel.ERROR, "[SocketTimeOutException] -> " + e.getMessage());
		} catch(SocketException e){
			logger.write(LogLevel.ERROR, "[클라이언트 접속 종료]");
		} catch (IOException e) {
			logger.write(LogLevel.ERROR, e.getMessage());
		}
		return readCount;
	}
	
	/**
	 * @param socket
	 */
	public void close(Socket socket){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	
	/**
	 * @param output
	 * @param writeArr
	 * 웹브라우저 사용시 사용하는 write
	 */
	public void write(OutputStream output, byte[] writeArr){
		try {
			output.write(writeArr);
			output.flush();
			output.close();
		} catch(SocketException e){
			logger.write(LogLevel.ERROR, "[소켓 연결 종료로 인한 write Error]");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param output
	 * @param writeArr
	 * SocketClient 전용
	 * Stream을 닫으면 읽어오지 못함
	 */
	public void writeConsol(OutputStream output, byte[] writeArr){
		try {
			output.write(writeArr);
			output.flush();
		} catch(SocketException e){
			logger.write(LogLevel.ERROR, "[소켓 연결 종료로 인한 write Error]");
		} catch (IOException e) {
			logger.write(LogLevel.ERROR, "[소켓 연결 종료로 인한 write Error]");
		}
	}
}