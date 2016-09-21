package com.example.pcc;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity{
	private SharedPreferences preferences;                                 //判断是否首次登陆
	private Editor editor; 
	private final int SPLASH_DISPLAY_LENGHT = 2000; // 延迟一秒  
	public boolean IsFirst=false;
	
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
        WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏  
        setContentView(R.layout.activity_splash);  
        
        
        preferences = getSharedPreferences("phone", 0);  
        //判断是不是首次登录，  
        if (preferences.getBoolean("firststart", true)) {  
         editor = preferences.edit();
         IsFirst=true;
         //将登录标志位设置为false，下次登录时不在显示首次登录界面  
         editor.putBoolean("firststart", false);  
         editor.commit();  
        }
  
        new Handler().postDelayed(new Runnable() {  
            public void run() {  
            	
            	if(IsFirst==false)            
            	{
            		Log.v("eric","第二次");
            		Intent mainIntent = new Intent(Splash.this,MainActivity.class);  
            		Splash.this.startActivity(mainIntent);  
            		Splash.this.finish();  
            	}
            	else                                             //若为第一次载入，进入引导信息;
            	{
            		Intent mainIntent = new Intent(Splash.this,FirstLoginActivity.class);  
            		Log.v("eric","第一次");
            		Splash.this.startActivity(mainIntent);  
            		Splash.this.finish();  
            	}
            }  
  
        }, SPLASH_DISPLAY_LENGHT); 
    }

}
