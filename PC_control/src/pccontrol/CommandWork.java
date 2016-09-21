package pccontrol;

/**
 * 命令执行类：通过接收到的指令执行相应的方法。
 */
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.rmi.CORBA.Util;

import pcutils.FileReceiveThread;
import pcutils.PhotoReceive;
import pcutils.UdpImageSend;
import pcutils.UdpSend;

import com.ice.jni.registry.NoSuchKeyException;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryException;
import com.ice.jni.registry.RegistryKey;

public class CommandWork {
	UdpSend udpSend = new UdpSend();
	UdpImageSend udpImageSend = new UdpImageSend();
	ScreenSnapshot ss = new ScreenSnapshot();
	PhotoReceive mPhotoReceive = new PhotoReceive();
	static int i =0;
	/**
	 * 向接收到SearchPc的ip发送ReplyPing
	 */
	public void SearchPc(InetAddress ip) throws IOException {
		// TODO 自动生成的方法存根
		udpSend.send("ReplyPing;!", ip);
	}
	/**
	 * 发送是否正在关机
	 */
	public void IsShutDown(String command,InetAddress ip) throws IOException {
		// TODO 自动生成的方法存根
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					udpSend.send(command, ip);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
		});
		thread.start();
		udpSend.send(command, ip);
	}
	/**
	 * 执行关机
	 */
	public void ShutDown() throws Exception {
		Runtime.getRuntime().exec("shutdown -s -f -t 10");
	}
	/**
	 * 执行取消关机
	 */
	public void ShutDownCancel() throws Exception {
		Runtime.getRuntime().exec("shutdown -a");
	}
	/**
	 * 执行定时关机
	 */
	public void ShutDownWithTime(ArrayList<String> command) throws Exception {
		int hour = 0, minute = 0;
		hour = Integer.parseInt(command.get(1));
		minute = Integer.parseInt(command.get(2));
		System.out.println("hour = " + hour + "\n" + "minute = " + minute);
		int time = 3600 * hour +60 * minute;
		Runtime.getRuntime().exec("shutdown -s -f -t " + time);
		//JOptionPane.showMessageDialog(null, "电脑将在" + hour +"小时" +minute+"分后关机", "提示", 1);
		
	}
	/**
	 * 执行重置关机时间
	 */
	public void ShutDownReset(ArrayList<String> command) throws Exception {
		Runtime.getRuntime().exec("shutdown -a");
		this.ShutDownWithTime(command);
	}
	/*
	 * 执行摇一摇切歌即模拟切歌快捷键
	 */
	public void ControlMusic(String command) throws Exception{
		Robot robot = new Robot();
		if(command.equals(CommandConstant.PLAY_MUSIC)
				||command.equals(CommandConstant.PAUSE_MUSIC)){
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		}
		else if(command.equals(CommandConstant.STOP_MUSIC)){
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		}
		else if(command.equals(CommandConstant.SHAKE_MUSIC)
				||command.equals(CommandConstant.NEXT_MUSIC)){
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_RIGHT);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		}
		else if(command.equals(CommandConstant.LAST_MUSIC)){
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_LEFT);
			robot.keyRelease(KeyEvent.VK_LEFT);
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
		}
		
		
		System.out.println("-->执行成功");
		
		
	}
	/*
	 * 执行关闭PC服务端
	 */
	public void PowerOff()throws Exception{
		System.out.println("-->执行成功");
		System.exit(0);
	}
	/**
	 * 切换到图片模式 即 向android端发送图片
	 */
	public void PictureMode(InetAddress ip) throws IOException{
 
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
							i++;
				try {
						System.out.println("-->"+i);
						udpImageSend.send(ss.snapshot(), ip);
					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} 
				
				System.out.println("-->图片发送结束");
			
				}
		});
		thread.start();
		
	}
//	public void CutPage() throws AWTException {
//		// TODO 自动生成的方法存根
//		Robot robot = new Robot();
//		robot.keyPress(KeyEvent.VK_ALT);
//		robot.keyPress(KeyEvent.VK_F4);
//		robot.keyRelease(KeyEvent.VK_F4);
//		robot.keyRelease(KeyEvent.VK_ALT);
//	}
	/**
	 * 计算坐标值，模拟相应的点击事件
	 * @throws AWTException 
	 */
	public void TouchXY(float x,float y,float xMax,float yMax,boolean long_flag) throws AWTException{
		int move_x = (int)XExplain(x,xMax);
		int move_y = (int)YExplain(y,yMax);
		Robot robot = new Robot();
		if(long_flag){
			robot.mouseMove(move_x, move_y);
			robot.mousePress(KeyEvent.BUTTON1_MASK);
			robot.mouseRelease(KeyEvent.BUTTON1_MASK);
			robot.mousePress(KeyEvent.BUTTON1_MASK);
			robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		}else{
			robot.mouseMove(move_x, move_y);
			robot.mousePress(KeyEvent.BUTTON1_MASK);
			robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		}
		
	}
	/**
	 * 解析点击的Y坐标
	 */
	private float YExplain(float phone_y, float yMax) {
		// TODO 自动生成的方法存根
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		float pc_y = (float)screenSize.getHeight();
		return pc_y*(phone_y/yMax);
	}
	/**
	 * 解析点击的X坐标
	 */
	private float XExplain(float phone_x,float xMax) {
		// TODO 自动生成的方法存根
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		float pc_x = (float)screenSize.getWidth();
		return pc_x*(phone_x/xMax); 
		
	}
	/**
	 * PPT控制方法
	 * @throws AWTException 
	 */
	public void PptControl(String command,InetAddress ip) throws AWTException{
		Robot robot = new Robot();
		if(command.equals(CommandConstant.PPT_FULL_SCREEN)){
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_SHIFT);
		}
		else if(command.equals(CommandConstant.PPT_PAGE_UP)){
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_UP);
			robot.keyRelease(KeyEvent.VK_UP);
		}
		else if(command.equals(CommandConstant.PPT_PAGE_DOWN)){
			robot.keyPress(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_F5);
			robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_DOWN);
		}else if(command.equals(CommandConstant.PPT_STOP_FULL)){
			robot.keyPress(KeyEvent.VK_ESCAPE);
			robot.keyRelease(KeyEvent.VK_ESCAPE);
		}else 
			System.out.println("-->ppt 命令执行失败");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		try {
			udpImageSend.send(ss.snapshot(), ip);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	/**
	 * 打开PPT
	 * @return
	 * @throws RegistryException 
	 * @throws NoSuchKeyException 
	 */
	public void OpenSoftWare(String command) throws NoSuchKeyException, RegistryException{
		Runtime runtime = Runtime.getRuntime();
		String path=GetPath(command);
		if(!path.isEmpty() ){
			try {
				runtime.exec(path);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	public String GetPath(String softname) throws NoSuchKeyException, RegistryException{
		RegistryKey registryKey = Registry.HKEY_LOCAL_MACHINE.openSubKey("SOFTWARE");
		RegistryKey subKey = registryKey.openSubKey("Microsoft").openSubKey("Windows").openSubKey("CurrentVersion")
				    .openSubKey("App Paths").openSubKey(softname+".exe");
		String appPath = subKey.getStringValue("Path");
		if(!appPath.isEmpty()){
		     appPath = appPath + "\\"+softname+".exe";
		     System.out.println(appPath);
		     return appPath;
		    }
		return appPath;
	}
	/**
	 * 打开照片
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void OpenCameraPhoto() throws FileNotFoundException, IOException{
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				try {
						mPhotoReceive.receive();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
			
				}
		});
		thread.start();
		
		
	}
}
