package view;

import com.example.pcc.R;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.TextView;

/**
 * 带进度条的对话框
 * 可以设置定时关闭
 * 在结束后播放一个动画
 * @author Lqq
 *
 */
public class CustomProgressDialog extends Dialog {


	/**
	 * 不自动关闭时间
	 */
	private final static long DO_NOT_AUTO_CLOSE = 0;
	/**
	 * 动画播放时间
	 */
	private final static long FINISH_DELAY_TIME = 800;
	private String mFinishString;
	private TextView mTipTextView;
	private Context mContext;
	/**
	 * 自动关闭时间
	 */
	private long mAutoCloseTime;
	private long mFinishDelayTime;
	
	public CustomProgressDialog(Context context){
		this(context, R.style.custom_dialog);
	}
	
	private CustomProgressDialog(Context context, int theme) {
		super(context, theme);
//		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		this.setContentView(R.layout.dialog_circle_process_tip);
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
	
		mContext = context;
		initData();
	}
	
	private void initData(){
		mAutoCloseTime = DO_NOT_AUTO_CLOSE;
		mFinishDelayTime = FINISH_DELAY_TIME;
		Resources re = mContext.getResources();
		mTipTextView =(TextView)findViewById(R.id.TextView);
		mTipTextView.setText(re.getString(R.string.loding));
		mFinishString = mContext.getResources().getString(R.string.finish);
	}
	/**
	 * 设置正在运行时的提示文字
	 * @param tip
	 */
	public void setTipText(String tip){
		mTipTextView.setText(tip);
	}
	/**
	 * 设置结束的时候提示的文字
	 * @param tip
	 */
	public void setFinishTipText(String tip){
		this.mFinishString = tip;
	}
	
	/**
	 * 关闭
	 */
	public void close(){
		this.setContentView(R.layout.dialog_circle_tip);
		mFinishString = "完成";
		((TextView)findViewById(R.id.TextView)).setText(mFinishString);
		new Thread(new SleepThreadRunable(this, mFinishDelayTime)).start();
	}
	/**
	 * 设置完成时动画延时的时间，单位是1000 == 1s
	 * 默认为800S
	 * @param finishDelayTime
	 */
	public void setFinishDelay(long finishDelayTime){
		mFinishDelayTime = finishDelayTime;
	}
	
	/**
	 * 设置关闭的时间
	 * @param closeTime
	 */
	public void setCloseTime(long closeTime){
		mAutoCloseTime = closeTime;
	}
	
	
	
	@Override
	public void show() {
		super.show();
//		if(mAutoCloseTime != DO_NOT_AUTO_CLOSE){
//			//如果设置了自动关闭就开启一个线程在设定的时间执行关闭方法
//			new Thread(new CloseViewRunable(this, mAutoCloseTime)).start();
//		}else{
//			
//		}
	}
	
	
	private class SleepThreadRunable implements Runnable {

		private CustomProgressDialog mDialog;
		private long mFinishDelayTime;
		public SleepThreadRunable(CustomProgressDialog dialog, long time) {
			mDialog = dialog;
			mFinishDelayTime = time;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(mFinishDelayTime);
			} catch (InterruptedException e) {
				Log.d("lqqLog", "SleepThreadRunable");
			}
			mDialog.dismiss();
		}
		
	}
	
	/**
	 * 定义一个用于自动关闭的功能
	 * @author Lqq
	 *
	 */
	private class CloseViewRunable implements Runnable {

		private CustomProgressDialog mDialog;
		private long mCloseTime;
		public CloseViewRunable(CustomProgressDialog dialog, long time) {
			mDialog = dialog;
			mCloseTime = time;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(mCloseTime);
				mDialog.close();
			} catch (InterruptedException e) {
				Log.d("LqqLog", "CloseViewRunable");
			}
		}
		
	}
	
}
