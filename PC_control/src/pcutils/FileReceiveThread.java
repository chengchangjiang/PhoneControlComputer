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
		// TODO �Զ����ɵķ������
		String ip = socket.getInetAddress().getHostAddress();
		try {
			System.out.println("-->�������߳̽������Կͻ��ˣ�"+ip+ "���ļ�");
			InputStream mInStream = socket.getInputStream();
			String fileName = getClientFileName(mInStream);
			if(fileName.isEmpty()){
				System.out.println("-->�ļ���ȡʧ��");
				socket.close();
			}
			writeOutInfo(socket,"FileSendNow");    //���߿ͻ���,��ʼ�������ݰ�
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
			
			writeOutInfo(socket,"FileSendSucess");    //�ļ����ճɹ�����ͻ��˷���һ����Ϣ  
			System.out.println("-->��Ƭ�ϴ��ɹ�");

			mOpen.open(filePath);
			mFileStream.close();
			socket.close();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
	}
	private String getClientFileName(InputStream mInStream) throws Exception {
		// TODO �Զ����ɵķ������
		byte[] bufferName = new byte[1024];
		int lengthInfo = 0;
		lengthInfo = mInStream.read(bufferName);
		String fileName = new String(bufferName,0,lengthInfo);
		    
		return fileName;
	}

	public void writeOutInfo(Socket sock,String infoStr)throws Exception//����Ϣ�����������  
	{  
	    OutputStream sockOut = sock.getOutputStream();  
	    sockOut.write(infoStr.getBytes());  
	} 
}