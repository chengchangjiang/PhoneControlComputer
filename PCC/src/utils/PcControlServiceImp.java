package utils;

import java.io.File;
import java.net.SocketException;



public interface PcControlServiceImp {
	/**
	 * 向电脑发送一条命令使电脑关机
	 * @param hour 关机的时
	 * @param minute 关机的分
	 * @return
	 */
	public boolean pcShutDown(int hour, int minute);
	/**
	 * 向电脑发送一个取消关机的命令
	 * @return
	 */
	public boolean pcShutDownCancel();
	/**
	 * 重新设置时间
	 * @param hour 关机的时
	 * @param minute 关机的分
	 * @return
	 */
	public boolean pcReSetShutDown(int hour, int minute);
	
	/**
	 * 发送摇一摇命令
	 */
	public boolean pcControlMusic(String command);
	/**
	 * 关机
	 * @return
	 */
	public boolean pcStopServer();
	/**
	 * 判断是主机是否在关机,这是延延时操作，如果用在UI线程会引起程序崩溃，请开另外个线
	 * 和电脑�?�讯成功会返回一个true
	 * 没有成功会返回false
	 * @return 返回个boolean
	 */
	public boolean pcIsShutDown();
	/**
	 * 
	 * @return
	 */
	public CommandExplain getCommandExplain();
	public byte[]  pcPictureMode() throws SocketException;
	public boolean pcTouchXY(float x,float y,float xMax,float yMax,boolean long_flag);
	/**
	 * 用于ppt模式
	 */
	public byte[] pcPptMode();
	
	/**
	 * 用于发送只需要一条的命令，可以直接调用
	 */
	public boolean sendCommand(String command);
	/**
	 * 用于发送图片
	 */
	public int sendImage(File file);
}
