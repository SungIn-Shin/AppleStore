package test.client.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author 성인
 * tcpdump의 패킷을 확인하기위해 테스트용 TCP Client
 */
public class ClientMain {
	//
	public static void main(String[] args) {
		//
		String ip = "192.168.0.128";
		int port = 9999;
		//사용자에게 입력받은 정보를 저장해두고 보낼 스트림
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg = "";
		BufferedReader br = null;
		PrintWriter pw = null;
		Socket socket = null;
		try {
			socket = new Socket(ip, port);
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				System.out.println("보낼 문자를 입력하세요");
				msg = br.readLine();
				pw.println(msg);
				pw.flush();
			}
			
		} catch (UnknownHostException e) {
			//
			e.printStackTrace();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}
		
	}
}
