package pccontrol;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import pcutils.UdpReceive;

public class PCC {

	private JFrame frmMobilePhoneControl;
	private JTextField textField;
	private boolean flag = false;//是否正在关机
	private float touch_x;//点击点的横坐标
	private float touch_y;//点击点的纵坐标
	private float xMax;//屏幕总宽
	private float yMax;//屏幕总长
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PCC window = new PCC();
					window.frmMobilePhoneControl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PCC() {
		JOptionPane.showMessageDialog(null, "成功开启电脑端", "提示", 1);
		try {
			initialize();
			
		} catch (AWTException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws AWTException 
	 */
	private void initialize() throws AWTException {
		frmMobilePhoneControl = new JFrame();
		frmMobilePhoneControl.setTitle("Mobile Phone Control Computer v1.0");
		frmMobilePhoneControl.getContentPane().setLayout(null);
		frmMobilePhoneControl.setBounds(100, 100, 450, 300);
		frmMobilePhoneControl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnNewButton = new JButton("New button");
		frmMobilePhoneControl.getContentPane().add(btnNewButton,
				BorderLayout.WEST);
		JButton button_1 = new JButton("\u7ED3\u675F");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});
		button_1.setBounds(170, 191, 93, 23);
		frmMobilePhoneControl.getContentPane().add(button_1);
		
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(120, 43, 186, 80);
		frmMobilePhoneControl.getContentPane().add(textField);
		textField.setColumns(10);

		JTextPane textPane = new JTextPane();
		textPane.setText("\u63A5\u6536\u5230\u7684\u6307\u4EE4");
		textPane.setBounds(121, 0, 0, 34);
		frmMobilePhoneControl.getContentPane().add(textPane);

		JTextPane textPane_1 = new JTextPane();
		textPane_1.setEnabled(false);
		textPane_1.setEditable(false);
		textPane_1.setText("\u63A5\u6536\u5230\u7684\u6307\u4EE4");
		textPane_1.setBounds(170, 10, 93, 23);
		frmMobilePhoneControl.getContentPane().add(textPane_1);
		while(true){
			
			UdpReceive udpReceive = new UdpReceive();
			CommandWork commandWork = new CommandWork();
			ReadCommand readCommand = new ReadCommand();
			
				try {
					udpReceive.Receive();
					
				} catch (IOException e2) {
					// TODO 自动生成的 catch 块
					System.out.println("-->接收异常");
				}
				try {
					readCommand.Read(udpReceive.data);
				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					System.out.println("-->解读命令异常");
				}
				try {
					if (readCommand.command == null)
						System.out.println("-->指令错误");
					else if (readCommand.command.get(0).equals(
							CommandConstant.SEARCH_PC)) {
						// System.out.println("ReplyPing;!");
						commandWork.SearchPc(udpReceive.ip);

					} else if (readCommand.command.get(0).equals(
							CommandConstant.SHUT_DOWN)) {
						commandWork.ShutDown();
						flag = true;
						
					} else if (readCommand.command.get(0).equals(
							CommandConstant.SHUT_DOWN_WITH_TIME)) {
						commandWork.ShutDownWithTime(readCommand.command);
						flag = true;
						
					} else if (readCommand.command.get(0).equals(
							CommandConstant.SHUT_DOWN_CANCEL)) {
						commandWork.ShutDownCancel();
						flag = false;
					} else if (readCommand.command.get(0).equals(
							CommandConstant.RESET_SHUT_DOWN)) {
						commandWork.ShutDownReset(readCommand.command);
						flag = true;
					} else if (readCommand.command.get(0).equals(
							CommandConstant.IS_SHUT_DOWN)) {
						String command = null;
						if (flag == true) {
							command = CommandConstant.SHUT_DOWN_TRUE + ";"
									+ "!";
						} else {
							command = CommandConstant.SHUT_DOWN_FALSE + ";"
									+ "!";
						}
						commandWork.IsShutDown(command, udpReceive.ip);
					} else if(readCommand.command.get(0).equals(CommandConstant.CONTROL_MUSIC)){
						System.out.println("-->"+readCommand.command.get(1));
						commandWork.ControlMusic(readCommand.command.get(1));
					}else if(readCommand.command.get(0).equals(CommandConstant.STOP_SERVER)){
						commandWork.PowerOff();
					}
					else if(readCommand.command.get(0).equals(CommandConstant.PICTURE_MODE)){
						commandWork.PictureMode(udpReceive.ip);
					}
					else if(readCommand.command.get(0).equals(CommandConstant.TOUCH_XY)){
						
						touch_x =Float.parseFloat( readCommand.command.get(1));
						touch_y =Float.parseFloat( readCommand.command.get(2));
						xMax =Float.parseFloat( readCommand.command.get(3));
						yMax =Float.parseFloat( readCommand.command.get(4));
						boolean long_flag =Boolean.parseBoolean( readCommand.command.get(5));
						commandWork.TouchXY(touch_x,touch_y, xMax, yMax,long_flag);
						//commandWork.PictureMode(udpReceive.ip);
					}
					else if(readCommand.command.get(0).equals(CommandConstant.PPT_CONTROL)){
						commandWork.PptControl(readCommand.command.get(1),udpReceive.ip);
					}
//					else if(readCommand.command.get(0).equals(CommandConstant.CUT_PAGE)){
//						commandWork.CutPage();
//					}
					else if(readCommand.command.get(0).equals(CommandConstant.CAMERA_PHOTO)){
						commandWork.OpenCameraPhoto();
					}else if(readCommand.command.get(0).equals("EndSymbol")){
						String end = ";!";
						DatagramPacket datagramPacket = new DatagramPacket(end.getBytes(),
								end.getBytes().length,udpReceive.ip,9000);
						try {
							DatagramSocket datagramSocket = new DatagramSocket();
							datagramSocket.send(datagramPacket);
							datagramSocket.close();
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
					
					else{
						System.out.println("-->指令错误");
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					System.out.println("-->执行异常");
				}
			}
	}
	
}
