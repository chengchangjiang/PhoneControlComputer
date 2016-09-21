package pccontrol;

/**
 * ����ִ���ࣺͨ�����յ���ָ��ִ����Ӧ�ķ�����
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
	 * ����յ�SearchPc��ip����ReplyPing
	 */
	public void SearchPc(InetAddress ip) throws IOException {
		// TODO �Զ����ɵķ������
		udpSend.send("ReplyPing;!", ip);
	}
	/**
	 * �����Ƿ����ڹػ�
	 */
	public void IsShutDown(String command,InetAddress ip) throws IOException {
		// TODO �Զ����ɵķ������
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					udpSend.send(command, ip);
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				
			}
		});
		thread.start();
		udpSend.send(command, ip);
	}
	/**
	 * ִ�йػ�
	 */
	public void ShutDown() throws Exception {
		Runtime.getRuntime().exec("shutdown -s -f -t 10");
	}
	/**
	 * ִ��ȡ���ػ�
	 */
	public void ShutDownCancel() throws Exception {
		Runtime.getRuntime().exec("shutdown -a");
	}
	/**
	 * ִ�ж�ʱ�ػ�
	 */
	public void ShutDownWithTime(ArrayList<String> command) throws Exception {
		int hour = 0, minute = 0;
		hour = Integer.parseInt(command.get(1));
		minute = Integer.parseInt(command.get(2));
		System.out.println("hour = " + hour + "\n" + "minute = " + minute);
		int time = 3600 * hour +60 * minute;
		Runtime.getRuntime().exec("shutdown -s -f -t " + time);
		//JOptionPane.showMessageDialog(null, "���Խ���" + hour +"Сʱ" +minute+"�ֺ�ػ�", "��ʾ", 1);
		
	}
	/**
	 * ִ�����ùػ�ʱ��
	 */
	public void ShutDownReset(ArrayList<String> command) throws Exception {
		Runtime.getRuntime().exec("shutdown -a");
		this.ShutDownWithTime(command);
	}
	/*
	 * ִ��ҡһҡ�и輴ģ���и��ݼ�
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
		
		
		System.out.println("-->ִ�гɹ�");
		
		
	}
	/*
	 * ִ�йر�PC�����
	 */
	public void PowerOff()throws Exception{
		System.out.println("-->ִ�гɹ�");
		System.exit(0);
	}
	/**
	 * �л���ͼƬģʽ �� ��android�˷���ͼƬ
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
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					} 
				
				System.out.println("-->ͼƬ���ͽ���");
			
				}
		});
		thread.start();
		
	}
//	public void CutPage() throws AWTException {
//		// TODO �Զ����ɵķ������
//		Robot robot = new Robot();
//		robot.keyPress(KeyEvent.VK_ALT);
//		robot.keyPress(KeyEvent.VK_F4);
//		robot.keyRelease(KeyEvent.VK_F4);
//		robot.keyRelease(KeyEvent.VK_ALT);
//	}
	/**
	 * ��������ֵ��ģ����Ӧ�ĵ���¼�
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
	 * ���������Y����
	 */
	private float YExplain(float phone_y, float yMax) {
		// TODO �Զ����ɵķ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		float pc_y = (float)screenSize.getHeight();
		return pc_y*(phone_y/yMax);
	}
	/**
	 * ���������X����
	 */
	private float XExplain(float phone_x,float xMax) {
		// TODO �Զ����ɵķ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		float pc_x = (float)screenSize.getWidth();
		return pc_x*(phone_x/xMax); 
		
	}
	/**
	 * PPT���Ʒ���
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
			System.out.println("-->ppt ����ִ��ʧ��");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		try {
			udpImageSend.send(ss.snapshot(), ip);
		} catch (FileNotFoundException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	/**
	 * ��PPT
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
				// TODO �Զ����ɵ� catch ��
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
	 * ����Ƭ
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
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}
			
				}
		});
		thread.start();
		
		
	}
}
