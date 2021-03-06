package pcutils;
/*
 * udp接收类
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpReceive {
	public InetAddress ip = null;
	public String data;
	public void Receive() throws IOException {
		System.out.println("-->进入接收端");
		DatagramSocket ds = new DatagramSocket(8899);
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		ds.receive(dp);
		
		ip = dp.getAddress();
		data = new String(dp.getData(), 0, dp.getLength());
		ds.close();
		System.out.println("-->ip:"+ip+"\n-->数据:"+data); 
		System.out.println("-->接收成功");

	}

}
