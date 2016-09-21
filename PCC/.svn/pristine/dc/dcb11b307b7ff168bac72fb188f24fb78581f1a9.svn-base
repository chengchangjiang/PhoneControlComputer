package com.example.pcc;

import java.net.InetAddress;
import java.net.UnknownHostException;

import utils.CommandConstant;
import utils.PcControlService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PPTFullScreenActivity extends Activity{
	private InetAddress ip;
	private PcControlService mService;
	private Bitmap mBitmap;
	private ImageView mImageView;
	static final int UPDATA_BITMAP = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ppt_fullscreen);
		
		mImageView = (ImageView)findViewById(R.id.ppt_picture);
		Intent intent =this.getIntent();
		try {
			ip =InetAddress.getByName(intent.getExtras().getString("ip"));
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//开启线程接收图片
        Thread thread = new Thread(new RecivePictureRunable(mService));
        thread.start();
	}
	private Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);

            switch (msg.what){
                case UPDATA_BITMAP:
                	mImageView.setImageBitmap(mBitmap);
                	Log.d("LQQTEST", "更新成功");
                    break;
                default:
                    break;
            }

        }
    };
	/**
     * 接收图片线程
     * @author Ray
     */
    private class RecivePictureRunable implements Runnable{
    	private PcControlService mService;
    	public RecivePictureRunable(PcControlService service) {
    		mService = service;
		}
		@Override
		public void run() {
			
			byte imageByte[] = mService.pcPptMode();
			while(true){
				imageByte = mService.receiveImage();
				if(imageByte != null){

					mBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
					mHandler.sendEmptyMessage(UPDATA_BITMAP);
					Log.d("LQQTEST", "接收图片成功");
				}
			}
		}
    	
    }
    /**
     * 用于发送一条命令的按钮监听
     * @author Ray
     *
     */
    private class SendCommandListener implements OnClickListener{

    	private PcControlService mService;
    	private String mCommand;
    	public SendCommandListener(PcControlService service, String command) {
    		mService = service;
    		mCommand = command;
		}
		@Override
		public void onClick(View v) {
			Thread  thread = new Thread(new Runnable() {
				@Override
				public void run() {
					mService.sendCommand(CommandConstant.PPT_CONTROL + ";" +mCommand );
				}
			});
			thread.start();
		}
    	
    }
}
