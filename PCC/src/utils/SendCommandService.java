package utils;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;


public class SendCommandService {

	private static final int mPort = 8899;
	private static final String BROADCAST_ADDRESS = "255.255.255.255";
	private InetAddress mInetAddress;
	public SendCommandService(InetAddress inetAddress) {
		mInetAddress = inetAddress;
	}
	
	/**
	 * 设置发�?�的Ip地址
	 * @param mInetAddress
	 */
	public void setInetAddress(InetAddress mInetAddress){
		this.mInetAddress = mInetAddress;
	}
	/**
	 * 新开线程发命令
	 * @param mCommand 这个命令必须是按照格式封装好的命令
	 * @return 成功返会true
	 */
	public boolean sendCommand(String mCommand){
		try {
			byte[]mCommandBuff = mCommand.getBytes();
			DatagramPacket sendPacket = 
					new DatagramPacket(mCommandBuff , mCommandBuff.length, 
					mInetAddress, mPort);
			DatagramSocket sendSocket;
			sendSocket = new DatagramSocket();
			Thread mThread = new Thread(new SendCommandRunbale(sendPacket, sendSocket));
			mThread.start();
		} catch (SocketException e) {
			logdialog("sendCommand");
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 有电脑发送数据
	 * @return
	 */
	static public boolean searchPc(){
		boolean flag = true;
		try {
			byte[]mCommandBuff = (CommandConstant.SEARCH_PC + ";!").getBytes();
			InetAddress tempAddress = InetAddress.getByName(BROADCAST_ADDRESS);
			DatagramPacket sendPacket = 
					new DatagramPacket(mCommandBuff , mCommandBuff.length, 
					tempAddress, mPort);
			DatagramSocket sendSocket = new DatagramSocket();
//			sendSocket.setBroadcast(true);
			Thread mThread = new Thread(new SearchPcRunable(sendPacket, sendSocket));
			mThread.start();
		} catch (UnknownHostException e1) {
			logdialog("searchPc  unknownHost");
			flag = false;
		} catch (SocketException e) {
			logdialog("searchPc socket");
			flag = false;
		}
		return flag;
	}
	
	static private class SendCommandRunbale implements Runnable{
		protected DatagramPacket mDp;
		protected DatagramSocket mDs;
		public SendCommandRunbale(DatagramPacket dp, DatagramSocket ds) {
			// TODO Auto-generated constructor stub
			this.mDp = dp;
			this.mDs = ds;
		}
		
		@Override
		public void run() {
			try {
				mDs.send(mDp);
			} catch (IOException e) {
				logdialog("sendCommandRunable sendbug");
			}
			mDs.close();
		}
		
	}
	
	static private class SearchPcRunable extends SendCommandRunbale{
		
		public SearchPcRunable(DatagramPacket dp, DatagramSocket ds) {
			super(dp, ds);
		}
		@Override
		public void run() {
			try {
				for(int i =0; i < 100; i++){
					mDs.send(mDp);
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.print("---------------searchPcRunable");
				}
			} catch (IOException e) {
				logdialog("SearchPcrunable send");
				e.printStackTrace();
			}
			mDs.close();
		}
		
	}
	

	private static void logdialog(String dialog){
		Log.d("CatchException", "SendCommandServic:" + "dialog");
	}
}
