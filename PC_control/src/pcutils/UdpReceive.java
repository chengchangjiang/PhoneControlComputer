package pcutils;
/*
 * udp������
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpReceive {
	public InetAddress ip = null;
	public String data;
	public void Receive() throws IOException {
		System.out.println("-->������ն�");
		DatagramSocket ds = new DatagramSocket(8899);
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		ds.receive(dp);
		
		ip = dp.getAddress();
		data = new String(dp.getData(), 0, dp.getLength());
		ds.close();
		System.out.println("-->ip:"+ip+"\n-->����:"+data); 
		System.out.println("-->���ճɹ�");

	}

}
