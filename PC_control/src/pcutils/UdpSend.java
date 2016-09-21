package pcutils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/*
 * udp发送类
 */
public class UdpSend {
	
	public void send(String command, InetAddress ip) throws IOException {
		
		DatagramPacket dp = new DatagramPacket(command.getBytes(),
				command.getBytes().length, ip, 8898);
		System.out.println(ip);
	
		DatagramSocket ds = new DatagramSocket();
		ds.send(dp);
		ds.close();
		System.out.println("发送成功:"+command);
	}
}
