package com.example.pcc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class FirstLoginActivity extends Activity implements OnGestureListener{
	private ViewFlipper flipper;
	private int num =1;
	private GestureDetector detector;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstlogin);
        initView();
    }
	private void initView() {
		flipper  = (ViewFlipper) findViewById(R.id.viewFlipper1);
		detector = new GestureDetector(this);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if(e1.getX()-e2.getX()>5) {
			flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
			if(num < 3) {
				flipper.showNext();
				num++;
			}else if(num == 3) {
				toOtherActivity();
			}
			return true;
		}else if(e1.getX()-e2.getX()<-5) {
			flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));
			if(num >1) {
				flipper.showPrevious();
				num--;
			}
			return true;
		}
		return true;
	}
	private void toOtherActivity() {
//		FirstLoginActivity.this.finish();
		Intent intent = new Intent(FirstLoginActivity.this,MainActivity.class);  
	    startActivity(intent);  
	    finish();
		/*
		Intent intent = new Intent(FirstLoginActivity.this,MainActivity.class);
		FirstLoginActivity.this.startActivity(intent);
		*/
	}
}