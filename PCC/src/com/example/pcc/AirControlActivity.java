package com.example.pcc;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import utils.PcControlService;

public class AirControlActivity extends BaseActivity {
	private Button btnAirOn;
	private Button btnAirOff;
	private EditText textTemp;
	private PcControlService mPcService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air);
		
		Init();
		
		btnAirOn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				mPcService.airPowerOn(textTemp.getText().toString());
			}
		});
		
		btnAirOff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				mPcService.airPowerOff();
			}
		});
	}
	
	private void Init(){
		btnAirOff = (Button)findViewById(R.id.btn_AirOff);
		btnAirOn = (Button)findViewById(R.id.btn_AirOn);
		textTemp = (EditText)findViewById(R.id.text_Temp);
		
		mPcService = new PcControlService(getHostInetAddress());
	}
}
