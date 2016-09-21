package com.example.pcc;

import java.io.ByteArrayOutputStream;
import java.net.SocketException;

import utils.PcControlService;
import utils.SendCommandService;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Desktop extends BaseActivity{
	
	private ImageView deskpic = null;
	private PcControlService mPcService ;
	private Bitmap bmp;
	private ProgressBar mProgressBar;
	private TextView mTextView;
	private Message mMessage = new Message();
	private float touch_x;//点击点的横坐标
	private float touch_y;//点击点的纵坐标
	private float xMax;//屏幕总宽
	private float yMax;//屏幕总长
	private WindowManager mWindowManager;
	int i =0;
	final static int PICTURE_RECEIVE_SUCCESS = 1;
	final static int PICTURE_RECEIVE_FAILED = 0;
	private Thread thread;
	private CheckRunableIsRun mRunnable ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_desktop);
			mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
			mTextView = (TextView)findViewById(R.id.tv);
			mProgressBar.setVisibility(View.VISIBLE);
			mTextView.setVisibility(View.VISIBLE);
			mWindowManager= this.getWindowManager();
			mPcService = new PcControlService(getHostInetAddress());
			mRunnable = new CheckRunableIsRun();
			thread = new Thread(mRunnable);
			thread.start();
//			Thread thread = new Thread(new PictureRunnable());
//			thread.start();
		}
		/**
		 * 捕捉异步消息，并执行
		 */
		Handler mHandler = new Handler(){  
            @Override  
            public void handleMessage(Message msg){    
            	if(msg.what == PICTURE_RECEIVE_SUCCESS){
            		mProgressBar.setVisibility(View.GONE);
					mTextView.setVisibility(View.GONE);
            		deskpic = (ImageView) findViewById(R.id.desk_pic);
            		xMax = mWindowManager.getDefaultDisplay().getWidth();
            		yMax = mWindowManager.getDefaultDisplay().getHeight();
            		bmp = FitTheScreenSizeImage(bmp,(int)xMax,(int)yMax);
            		
					deskpic.setImageBitmap(bmp);
					
					deskpic.setOnTouchListener(new OnTouchListener() {
						private long down_time;
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							
							switch(event.getAction()){
							case MotionEvent.ACTION_DOWN:
								touch_x = event.getX();
								touch_y = event.getY();
								down_time = System.currentTimeMillis();
								mPcService.pcTouchXY(touch_x,touch_y,xMax,yMax,false);
								System.out.println("=====>按下时间:"+down_time);
								break;
							case MotionEvent.ACTION_UP:
								System.out.println("=====>时间差:"+(System.currentTimeMillis()-down_time));
								if(System.currentTimeMillis()-down_time > 1000){
									mPcService.pcTouchXY(touch_x,touch_y,xMax,yMax,true);
									System.out.println("=====>发送长按命令");
								}
								break;
							default:break;
							}
							return true;
						}
					});
            	}
            	else{
            		
            		mProgressBar.setVisibility(View.GONE);
            		mTextView.setText("显示图片失败");
            	}
            		
            }
            
		};
        /**
         * 使图片充满屏幕 
         */
        
		public static Bitmap FitTheScreenSizeImage(Bitmap m,int ScreenWidth, int ScreenHeight)
		{
		        float width  = (float)ScreenWidth/m.getWidth();
		        float height = (float)ScreenHeight/m.getHeight();
		        Matrix matrix = new Matrix();
		        matrix.postScale(width,height);
		        return Bitmap.createBitmap(m, 0, 0, m.getWidth(), m.getHeight(), matrix, true);
		 }
		@Override
		protected void onResume(){
			
			
			super.onResume();
		}
		@Override
		protected void onDestroy() {
			// TODO 自动生成的方法存根
				
			super.onDestroy();

		}
		@Override
		protected void onPause() {
			// TODO 自动生成的方法存根
			
			mRunnable.stop();
			super.onPause();
		}
		class PictureRunnable implements Runnable{
				private int index;
				public boolean flag;
				public PictureRunnable(){
					index = 0;
					flag = true;
				}
				public void stop(){
					flag = false;
				}
				@Override
				public void run() {
					while(flag){
						index++;
						mMessage = new Message();			
						if(mPcService == null){
							mPcService = new PcControlService(getHostInetAddress());
						}
						byte[] imageByte = null;
						try {
							imageByte = mPcService.pcPictureMode();
						} catch (SocketException e) {
							e.printStackTrace();
						}
						ByteArrayOutputStream outPut = new ByteArrayOutputStream();
						if(imageByte == null){
							mMessage.what = PICTURE_RECEIVE_FAILED;
							mHandler.sendMessage(mMessage);
						}
						else if(imageByte.length!=0){  
				        	
				        	bmp = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
				        	
				        	if(bmp == null){
				        		break;
				        	}else{
				        		bmp.compress(CompressFormat.JPEG, 50, outPut);
					        	mMessage.what=PICTURE_RECEIVE_SUCCESS;
					        	mHandler.sendMessage(mMessage);
					        	
				        	}
				        }
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}
				
				public int getRunnableIndex(){
					return index;
				}
		}
		

		class CheckRunableIsRun implements Runnable{
			private PictureRunnable mRunnable;
			private Thread mThread;
			private int mOldIndex;
			private boolean flag ;
			public CheckRunableIsRun(){
				mRunnable = new PictureRunnable();
				mThread = new Thread(mRunnable);
				mThread.start();
				mOldIndex = mRunnable.getRunnableIndex();
				flag = true;
			}
			public void stop(){
				flag = false;
				mRunnable.stop();
			}
			@Override
			public void run() {
				
				while(flag){
					try {
						Thread.sleep(1500);
					
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(mRunnable.getRunnableIndex() == mOldIndex){
						SendCommandService service = new SendCommandService(getHostInetAddress());
						service.sendCommand("EndSymbol"+";!");
					}else{
						mOldIndex = mRunnable.getRunnableIndex();
					}
				}
			}
		}
}
		

