package test.server.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
	//
	private Socket socket = null;
	private ServerSocket serverSocket = null;
	private BufferedReader br = null;
	private String msg = null;
	
	public ServerMain(){
	//
		try {			
			serverSocket = new ServerSocket(9999);
			System.out.println("Wait Client!!");
			worker();
		} catch (IOException e) {
			// 
			e.printStackTrace();
		}
	}
	
	public void worker(){
		//
		while(true) {
			//
			try {
				socket = serverSocket.accept();
				
				if(socket.isConnected()){
					System.out.println("Connect to Client");
				}
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				while((msg = br.readLine())!= null){
					System.out.println("From Client Msg : " + msg);
				}
				
			} catch (IOException e) {
		
				//
				e.printStackTrace();
			}
		}			
	}
	
	public static void main(String[] args) {
		new ServerMain();
	}
}
