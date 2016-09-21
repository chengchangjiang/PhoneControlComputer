package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class PhotoSend {
	private static int port = 10010;
	public int sendState ;
	
	public void send(File file,InetAddress ip) throws IOException{
		Socket socket = new Socket(ip,port);
		FileInputStream mFileStream = new FileInputStream(file);
		OutputStream mOutStream = socket.getOutputStream();
		sendState = 0;
		String fileName = file.getName();//先发照片名，让服务端知道
		mOutStream.write(fileName.getBytes());
		String serverInfo = serverInfoBack(socket);//反馈的信息：服务端是否获取照片名成功
		if(serverInfo.equals("FileSendNow")){
			byte[] bufferFile = new byte[1024];
			int lengthFile = 0;
			while(true){
				lengthFile = mFileStream.read(bufferFile);
				if(lengthFile != -1){
					mOutStream.write(bufferFile,0,lengthFile);
				}
				else {
					break;
				}
			}
		}
		else{
			System.out.println("-->服务器返回信�?:"+serverInfo);
		}
		
		socket.shutdownOutput();//告诉服务端照片数据已写完
		String serverInfo1 = serverInfoBack(socket);
		System.out.println("-->"+serverInfo1);
		if(serverInfo1.equals("FileSendSucess")){
			sendState = 1;
		}else{
			sendState = 2;
		}
		mFileStream.close();
		socket.close();
		
	}
	public String serverInfoBack(Socket socket) throws IOException {
		InputStream mInStream = socket.getInputStream();
		byte[] bufferIn = new byte[1024];
		int lengthIn = mInStream.read(bufferIn);
		String info = new String(bufferIn,0,lengthIn);
		return info;
	}
}
