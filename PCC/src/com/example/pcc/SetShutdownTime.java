package com.example.pcc;


import utils.PcControlService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class SetShutdownTime extends BaseActivity{
	
	
	private int Hour,Minute;
	private PcControlService GetTime;
	private TimePicker tp ;
	private ImageButton btnSDYep;
	private TextView tv;
	private PcControlService ShutDownCancel;
	private Boolean IsShutDown;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setMyContent(R.layout.activity_shutdown0);
		setTitle("关机");
		GetTime= new PcControlService(getHostInetAddress());
		ShutDownCancel= new PcControlService(getHostInetAddress());
		
		/**
		 * @author Eric
		 * 获取关机确认信息，以选择是否显示顶部弹窗
		 */
		Intent intent = getIntent();
		IsShutDown = intent.getBooleanExtra("IsShutDown", true);		
		Log.d("eric","IsShutDown="+IsShutDown);
		if(IsShutDown)
		{
			setShowInfo(IsShutDown);
			setShowInfoText(R.string.set_shutdown_over);
			setShowInfoPicture(R.drawable.shutdown_cancle);
			setShowInfoPictureListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					ShutDownCancel.pcShutDownCancel();
					Toast.makeText(SetShutdownTime.this, "取消关机成功~", Toast.LENGTH_SHORT).show();
					finish();
				}
			});
		}
		
		
		/**
		 * 设置时间初值为当前时间
		 */
		
		Hour = 00;  
	    Minute = 30 ;
		
		
		/**
		 * 设置按钮的监听，发送时间。
		 */
		btnSDYep=(ImageButton)findViewById(R.id.btn_set);
		btnSDYep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					GetTime.pcShutDown(Hour, Minute);
				Toast.makeText(SetShutdownTime.this, "设置时间成功~", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		
	
		/**
		 * 右上角back按钮的监听
		 */
		setBackButtonListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		//SD设置时间
		tv=(TextView) this.findViewById(R.id.textView1);
	    tp=(TimePicker)this.findViewById(R.id.timePicker1);
		tp.setIs24HourView(true);
		tp.setCurrentHour(0);
		tp.setCurrentMinute(30);
		tp.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				tv.setText("你选择的时间是: " + hourOfDay + "时" + minute + "分");  
				Hour=tp.getCurrentHour();
				Minute=tp.getCurrentMinute();
			}
		});

	}

}
