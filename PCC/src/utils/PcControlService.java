package utils;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import android.util.Log;
/**
 * @author lqq
 * 
 * 实现了PcControlServiceImp的接口
 */
public class PcControlService implements PcControlServiceImp{
	
	private SendCommandService mSend;
	private ReceiveCommand mReceive;
	private ReceiveImage mImage;
	private InetAddress mAddress;
	private PhotoSend mPhotoSend;
	/**
	 * 构造函数，实例化一个发送器和接受器
	 * @throws SocketException 
	 */
	public PcControlService(InetAddress inetAddress){
		mSend = new SendCommandService(inetAddress);
		mReceive = new ReceiveCommand();
		mImage = new ReceiveImage();
		mAddress = inetAddress;
		mPhotoSend = new PhotoSend();
	}
	public boolean pcShutDown(int hour, int minute){
		return mSend.sendCommand(CommandConstant.SHUT_DOWN_WITH_TIME + ";" + hour + ":" + minute + ";" +"!");
	}
	/**
	 * 向电脑发送一个取消关机的命令
	 * @return
	 */
	public boolean pcShutDownCancel(){
		return mSend.sendCommand(CommandConstant.SHUT_DOWN_CANCEL + ";" + "!");
	}
	/**
	 * 重新设置时间
	 * @param hour 关机的时
	 * @param minute 关机的分
	 * @return
	 */
	public boolean pcReSetShutDown(int hour, int minute){
		return mSend.sendCommand(CommandConstant.RESET_SHUT_DOWN + ";" + hour + ":" + minute + ";" + "!");
	}
	
	/**
	 * 发送摇一摇
	 */
	public boolean pcControlMusic(String command) {
		return mSend.sendCommand(CommandConstant.CONTROL_MUSIC+";"+command+";"+"!");
	}
	
	public boolean pcStopServer() {
		return mSend.sendCommand(CommandConstant.STOP_SERVER+";"+"!");
	}
	
	
	/**
	 * 判断是主机是否在关机,这是个延时操作，如果用在UI线程会引起程序崩溃，请开另外开线程
	 * 和电脑通讯讯成功会返回true
	 * 没有成功会返回false
	 * @return 返回个boolean
	 */
	public boolean pcIsShutDown(){
		CommandExplain mCommand;
		mSend.sendCommand(CommandConstant.IS_SHUT_DOWN+";"+"!");
		mCommand = new CommandExplain(new String(mReceive.receive().getData()));
		if(CommandConstant.SHUT_DOWN_TRUE.equals(mCommand.getTitle()))
			return true;
		return false;
	}
	
	/**
	 * 开启空调，设置温度
	 */
	public boolean airPowerOn(String temp){
		return mSend.sendCommand(CommandConstant.AIR_POWER_ON + ";" + temp + ";"  + "!");
	}
	
	/**
	 * 关闭空调
	 */
	public boolean airPowerOff(){
		return mSend.sendCommand(CommandConstant.AIR_POWER_OFF + ";" + "!");
	}
	
	/**
	 * 有的话会返回电脑的地址，否则直接一直阻塞
	 */
	public static InetAddress searchPc() {
		ReceiveCommand receiveCommand = new ReceiveCommand();
		SendCommandService.searchPc();
		return receiveCommand.receive().getAddress();
	}
	@Override
	public CommandExplain getCommandExplain() {
		return null;
	}
	/**
	 * 切换为图片模式
	 */
	@Override
	public byte[] pcPictureMode() throws SocketException {
		mSend.sendCommand(CommandConstant.PICTURE_MODE+";"+"!");
		return mImage.receive();
	}
	/**
	 * 发送点击坐标
	 */
	@Override
	public boolean pcTouchXY(float x,float y,float xMax,float yMax,boolean long_flag) {
		// TODO 自动生成的方法存根
		return mSend.sendCommand(CommandConstant.TOUCH_XY+";"+x+";"+y+";"+xMax+";"+yMax+";"+long_flag+";"+"!");
	}
	@Override
	public boolean sendCommand(String command) {
		return mSend.sendCommand(command + ";!");
	}
	@Override
	public int sendImage(final File file) {
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				
				try {
					mSend.sendCommand(CommandConstant.CAMERA_PHOTO+";!");
					Thread.sleep(1000);
					mPhotoSend.send(file, mAddress);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		
		});
		thread.start();
		while(true){
		switch(mPhotoSend.sendState){
		case 1:return mPhotoSend.sendState; 
		case 2:return mPhotoSend.sendState; 
		default:try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		
		}
		}
		
	}
	@Override
	public byte[] pcPptMode() {
		mSend.sendCommand(CommandConstant.PPT_CONTROL+";" + CommandConstant.PPT_FULL_SCREEN + ";" +"!");
		return null;
	}
	
	public byte[] receiveImage(){
		try {
			return mImage.receive();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
