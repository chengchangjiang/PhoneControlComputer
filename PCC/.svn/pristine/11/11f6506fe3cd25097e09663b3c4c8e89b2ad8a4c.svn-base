package utils;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
/**
 * 这个类用于接收消�?
 * @author lqq
 *
 */
public class ReceiveCommand {
	
	/**
	 * 端口
	 */
	private final static int RECEIEVE_PORT = 8898;
	/**
	 * 包的长度
	 */
	private final static int BYTE_LENGTH = 1024;
	/**
	 * �?启接�?
	 * @return 返回接收成功的dp�?
	 */
	public DatagramPacket receive(){
		byte[] mByte = new byte[BYTE_LENGTH];
		DatagramPacket dp = new DatagramPacket(mByte, mByte.length);
		try {
			DatagramSocket ds = new DatagramSocket(RECEIEVE_PORT);
			ds.receive(dp);
			ds.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		 catch (IOException e) {
				e.printStackTrace();
		}
		return dp;
	}
	
	

}
