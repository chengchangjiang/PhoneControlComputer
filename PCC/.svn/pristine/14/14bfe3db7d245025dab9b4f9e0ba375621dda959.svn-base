package com.example.pcc;

import java.io.File;
import utils.PcControlService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoSendActivity extends BaseActivity{
	private ImageButton mSendButton;
    private ImageView mImage;
    private PcControlService mPcService;
    private String fileName;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO 自动生成的方法存根
			super.onCreate(savedInstanceState);
			setMyContent(R.layout.activity_photosend);
			Intent intent =this.getIntent();
			fileName =intent.getExtras().getString("fileName");
			setTitle("拍照");
			mSendButton = (ImageButton) findViewById(R.id.send_button);
	        mImage = (ImageView) findViewById(R.id.image_show);
	        showPhoto(mImage, fileName);
	        mSendButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					showProgressDialog(R.string.photo_update_ing);
					Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							Message msg = new Message();
							mPcService = new PcControlService(getHostInetAddress());
							switch(mPcService.sendImage(new File(fileName))){
							case 1:msg.arg1 =1;break;
							default:msg.arg1 =0;break;
							}
							mHandler.sendMessage(msg);
						}
					});
					thread.start();
					
				}
			});
	        setShowInfoPictureListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					setShowInfo(false);
				}
			});
		}
		/**
		 * 异步消息处理
		 */
		Handler mHandler = new Handler(){  
	        @Override  
	        public void handleMessage(Message msg){  
	        	switch(msg.arg1){
	        	case 2:DisplayToast("照片上传中……"); break;
	        	case 1:{
	        			setShowInfo(true);
	        			setShowInfoText(R.string.photo_update_success);
	        			closeProgressDialog();
	        			}
	        			break;
	        	
	        	case 0:{
        				setShowInfo(true);
        				setShowInfoText(R.string.photo_update_fail);
        				closeProgressDialog();
        				} break;
	        	default: break;
	        	}
	        }
		};
		  
	    /**
	     * Toast提示方法
	     * @param str
	     */
	    public void DisplayToast(String str){
			Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 220);
			toast.show();
		}
	    /**
		 * 显示照片
		 * @param photo
		 * @param picturePath
		 */
	    private void showPhoto(ImageView photo,String picturePath){  
	        
	        // 缩放图片, width, height 按相同比例缩放图片  
	        BitmapFactory.Options options = new BitmapFactory.Options();  
	        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片  
	        options.inJustDecodeBounds = true;  
	        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);  
	        int scale = (int)( options.outWidth / (float)300);  
	        if(scale <= 0)  
	            scale = 1;  
	        options.inSampleSize = scale;  
	        options.inJustDecodeBounds = false;  
	        bitmap = BitmapFactory.decodeFile(picturePath, options);  
	          
	        photo.setImageBitmap(bitmap);  
	        photo.setMaxHeight(350);  
	    }  
}
