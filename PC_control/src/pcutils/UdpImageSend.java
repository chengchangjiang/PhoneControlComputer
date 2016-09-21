package pcutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpImageSend {
	private static int port = 9000;
	private DatagramPacket datagramPacket;
	private DatagramSocket datagramSocket;
	byte b[] = new byte[8192];
	private FileInputStream in;
	/**
	 * ͼƬת��Ϊ�ļ�����Ƭ������
	 * @Author ccj
	 * 
	 */
	public void send(String path, InetAddress ip) throws FileNotFoundException{
	
		try {
			
			in = new FileInputStream(path);
			int n = -1;
			while((n=in.read(b))!=-1){
				in.hashCode();
				datagramPacket = new DatagramPacket(b,b.length,ip,port);
				datagramSocket = new DatagramSocket();
				datagramSocket.send(datagramPacket);
			
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		datagramSocket.close();
		String end = ";!";
		datagramPacket = new DatagramPacket(end.getBytes(),end.getBytes().length,ip,port);
		try {
			datagramSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {
			datagramSocket.send(datagramPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		datagramSocket.close();
		System.out.println("-->����ͼƬ����");
	}
}
