package com.example.pcc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.LogRecord;

import utils.PcControlService;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class CameraActivity extends BaseActivity {
	
	private ImageButton mCameraButton;
	private ImageButton mPickButton;
	private String fileName;
	private String pathUrl;
	private String imageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMyContent(R.layout.activity_camera);
		mCameraButton = (ImageButton) findViewById(R.id.camera_button);
		mPickButton = (ImageButton) findViewById(R.id.pick_button);
		setTitle("拍照");
		getPhotopath();
        mPickButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
	                intent.addCategory(Intent.CATEGORY_OPENABLE);  
	                intent.setType("image/*");  
	                intent.putExtra("aspectX",1);  
	                intent.putExtra("aspectY",1);  
	                intent.putExtra("outputX", getWindowManager().getDefaultDisplay().getWidth());  
	                intent.putExtra("outputY", getWindowManager().getDefaultDisplay().getWidth());  
	                intent.putExtra("return-data",true);  
	                startActivityForResult(intent, 11);  
			}
		});
        
        mCameraButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // 跳转至拍照界面
               Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				 // 加载路径
               Uri uri = Uri.fromFile(new File(fileName));
               // 指定存储路径，这样就可以保存原图了
               intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			   startActivityForResult(intent, 1); 
            }
        });
    }
	
	 /**
	  * 回调函数
	  */
	@Override                           
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
    		if(requestCode == 1){
      
           Intent intent = new Intent(CameraActivity.this,PhotoSendActivity.class);
           intent.putExtra("fileName", fileName);
           startActivity(intent);
    	}
    		else if(requestCode == 11){
    			 Uri selectedImage = data.getData();  
    	         String[] filePathColumn = { MediaStore.Images.Media.DATA };  
    	   
    	         Cursor cursor = getContentResolver().query(selectedImage,  
    	                    filePathColumn, null, null, null);  
    	         cursor.moveToFirst();  
    	   
    	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);  
    	         fileName = cursor.getString(columnIndex);  
    	         cursor.close();  
    	         Intent intent = new Intent(CameraActivity.this,PhotoSendActivity.class);
    	         intent.putExtra("fileName", fileName);
    	         startActivity(intent);
    		}
			
		}
    }
	
	
    /**
     * 获取图片存储路径
     * @return
     */
    private void getPhotopath() {
        // 照片全路径
        fileName = "";
        // 文件夹路径
        pathUrl = Environment.getExternalStorageDirectory().getPath();
        imageName = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";   
        fileName = pathUrl + "/"+imageName;
    }
    
   
    
 

}
