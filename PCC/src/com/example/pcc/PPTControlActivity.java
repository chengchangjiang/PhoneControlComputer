package com.example.pcc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import utils.CommandConstant;
import utils.PcControlService;

public class PPTControlActivity extends BaseActivity {
    static final String TAG = "LQQTEST";
    static final int UPDATA_BITMAP = 1;
    private Bitmap mBitmap;
    private ImageView mImageView;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private PcControlService mService;
    private boolean ReceiveFlag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContent(R.layout.activity_ppt);

        initData();
        setBackButtonListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
			    	finish();
			   }
		});
    }
    private void initData(){
        Point mPoint = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(mPoint);
        mService = new PcControlService(getHostInetAddress());
        
        mNextButton = (ImageButton)findViewById(R.id.next_ppt_button);
        mPreviousButton = (ImageButton)findViewById(R.id.previous_ppt_button);
        mImageView =  (ImageView)findViewById(R.id.printf_ppt_imageview);
        
        mImageView.setOnTouchListener(new JudgeGuesture(100));
        mPreviousButton.setOnClickListener(new SendCommandListener(mService, CommandConstant.PPT_PAGE_UP));
        mNextButton.setOnClickListener(new SendCommandListener(mService, CommandConstant.PPT_PAGE_DOWN));
        
        ReceiveFlag = true;
        Rect rect = new Rect();
        this.getWindowManager().getDefaultDisplay().getRectSize(rect);
        
        setTitle("PPT控制");
//        //开启线程接收图片
//        Thread thread = new Thread(new RecivePictureRunable(mService));
//        thread.start();
        sendPptCommand(CommandConstant.PPT_FULL_SCREEN);
        
        
        new Thread(new RecivePictureRunable(mService)).start();
        
    }
    

    

    private Handler mHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);

            switch (msg.what){
                case UPDATA_BITMAP:
                	mImageView.setImageBitmap(mBitmap);
                	break;
                default:
                    break;
            }

        }
    };
    private class JudgeGuesture implements OnTouchListener{

        private float mDownX;
        private float mDownY;
        private float mMinMovePix;
        public  JudgeGuesture(float minMovePix){
            this.mMinMovePix = minMovePix;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mDownX = event.getX();
                    mDownY = event.getY();
                    Log.d(TAG, "x  "  + mDownX + " y  " +  mDownY);
                    break;
                case MotionEvent.ACTION_UP:
                    float offsetX = event.getX() - mDownX;
                    float offsetY = event.getY() - mDownY;
                    Log.d(TAG, "offsetX:"  + offsetX + "offsetY:" +  event.getY());
                    
                    if(Math.abs(offsetX) <= mMinMovePix && Math.abs(offsetY) <= mMinMovePix){
                        break;
                    }
                    if(Math.abs(offsetX) > Math.abs(offsetY)){
                        if(offsetX > 0){
                        	sendPptCommand(CommandConstant.PPT_PAGE_UP);
                            
                        }else{
                        	sendPptCommand(CommandConstant.PPT_PAGE_DOWN);
                        }
                    }else{
                        if(offsetY > 0){
                        	sendPptCommand(CommandConstant.PPT_PAGE_UP);
                        }else{
                        	sendPptCommand(CommandConstant.PPT_PAGE_DOWN);
                        }
                    }
                case MotionEvent.ACTION_MOVE:

                    break;
                default:
                    break;
            }
            return true;
        }
    }
    
    /**
     * 接收图片线程
     * @author Ray
     */
    private class RecivePictureRunable implements Runnable{
    	private PcControlService mService;
    	public RecivePictureRunable(PcControlService service) {
    		mService = new PcControlService(getHostInetAddress());
		}
		@Override
		public void run() {

			
			byte imageByte[];
			Log.d("LQQTEST", "开始接受图片");
			while(ReceiveFlag){
				imageByte = mService.receiveImage();
				Log.d("LQQTEST", "开始接受图片1");
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
					mService.sendCommand(CommandConstant.PPT_CONTROL + ";" +mCommand +";!");
				}
			});
			thread.start();
			new Thread(new RecivePictureRunable(mService)).start();
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

    }

    private void sendPptCommand(String mCommand){
    	mService.sendCommand(CommandConstant.PPT_CONTROL + ";" + mCommand +";!");
    	
    }
    
    
    @Override
    protected void onDestroy() {
    	ReceiveFlag = false;
    	mService.sendCommand(CommandConstant.PPT_CONTROL + ";" + CommandConstant.PPT_STOP_FULL +";!");
    	super.onDestroy();
    }
    
    
    
}
