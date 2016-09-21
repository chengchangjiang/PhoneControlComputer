package pcutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import pccontrol.PhotoOpen;

public class FileReceiveThread implements Runnable{
	private Socket socket;
	private static String filePath = "";
	private PhotoOpen mOpen;
	
	FileReceiveThread(Socket socket){
		mOpen = new PhotoOpen();
		this.socket = socket;
	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		String ip = socket.getInetAddress().getHostAddress();
		try {
			System.out.println("-->开启新线程接受来自客户端："+ip+ "的文件");
			InputStream mInStream = socket.getInputStream();
			String fileName = getClientFileName(mInStream);
			if(fileName.isEmpty()){
				System.out.println("-->文件获取失败");
				socket.close();
			}
			writeOutInfo(socket,"FileSendNow");    //告诉客户端,开始传送数据吧
			filePath = "d:/PccPhoto";
			File file = new File(filePath);
			file.mkdir();
			filePath = filePath+"/"+fileName;
			FileOutputStream  mFileStream = new FileOutputStream(new File (filePath));
			byte[] bufferFile = new byte[1024*1024];
			int lengthFile = 0;
			while(true){
				lengthFile = mInStream.read(bufferFile);
				if(lengthFile != -1){
					mFileStream.write(bufferFile, 0, lengthFile);
				}
				else {
					break;
				}
					
			}
			
			writeOutInfo(socket,"FileSendSucess");    //文件接收成功后给客户端反馈一个信息  
			System.out.println("-->照片上传成功");

			mOpen.open(filePath);
			mFileStream.close();
			socket.close();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}
	private String getClientFileName(InputStream mInStream) throws Exception {
		// TODO 自动生成的方法存根
		byte[] bufferName = new byte[1024];
		int lengthInfo = 0;
		lengthInfo = mInStream.read(bufferName);
		String fileName = new String(bufferName,0,lengthInfo);
		    
		return fileName;
	}

	public void writeOutInfo(Socket sock,String infoStr)throws Exception//将信息反馈给服务端  
	{  
	    OutputStream sockOut = sock.getOutputStream();  
	    sockOut.write(infoStr.getBytes());  
	} 
}