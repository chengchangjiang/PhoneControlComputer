package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * 用于发送图片
 * @author Ray
 *
 */
public class SendImageService {
	
	private final static int port = 9001;
	private DatagramSocket datagramSocket;
	
	private InetAddress mAddress; 
	public SendImageService(InetAddress address){
		mAddress = address;
	}
	
	public void sendImage(File file){
		try {
			FileInputStream fin = new FileInputStream(file);
			byte imageByte[] = new byte[8192];
			DatagramPacket datagramPacket;
			while((fin.read(imageByte))!=-1){
				fin.hashCode();
				datagramPacket = new DatagramPacket(imageByte, imageByte.length, mAddress, port);
				datagramSocket = new DatagramSocket();
				datagramSocket.send(datagramPacket);
			}
			fin.close();
			String end = ";!";			
			datagramPacket = new DatagramPacket(end.getBytes(),end.getBytes().length, mAddress, port);
			datagramSocket.send(datagramPacket);
			datagramSocket.close();
			
		} catch (FileNotFoundException e) { 
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
