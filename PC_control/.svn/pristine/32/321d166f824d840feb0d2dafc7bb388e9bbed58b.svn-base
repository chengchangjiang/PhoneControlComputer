package pcutils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PhotoReceive {
	private static int port = 10010;
	
	
	public void receive() throws IOException{
		
		ServerSocket mServer = new ServerSocket(port);
		while(true){
			Socket socket = mServer.accept();//循环等待客户端连接
			new Thread(new FileReceiveThread(socket)).start();
		}
	}
}
