package com.example.pcc;

import java.net.InetAddress;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import utils.PcControlService;
import view.CustomProgressDialog;

public class BaseActivity extends Activity {
	
	private static final int CLOSE_PROGRESS_DIALOG = 1;
	private static final int SHOW_PROGRESS_DIALOG = 2;
	private static final long DELAY_TIME = 20000;
	
	static private InetAddress mInetAddress;
	protected Context mContext;
	private CustomProgressDialog mDialog;
	private LayoutInflater mInflater;
	private RelativeLayout mShowInfoLayout;
	private FrameLayout mMainLayout;
	private ImageButton mTitleRightButton;
	private ImageButton mInfoRightButton;	
	private ImageButton mBackButton;
	private TextView mInfoTextView;
	private TextView mTitleView;
	private OnClickListener mBackListener;
	private OnClickListener mTitleRightButtonListener;
	private OnClickListener mInfoRightButtonListener;
	
	private Handler mBaseHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			
			switch(msg.what){
			case CLOSE_PROGRESS_DIALOG:
				mDialog.close();
				break;
			case SHOW_PROGRESS_DIALOG:
				mDialog = new CustomProgressDialog(mContext);
				mDialog.setTipText(mContext.getResources().getString(msg.arg1));
				mDialog.setCloseTime(DELAY_TIME);
				mDialog.show();
				break;
			default:
				break;
			};
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}
	
	protected void setMyContent(int layoutResID){
		View view = mInflater.inflate(layoutResID, null);
		mMainLayout.addView(view);
	}

	/**
	 * 搜索局域网下的主机，并且赋值给mInterfaceAddress
	 */
	protected void searchPc(){
		showProgressDialog();
		Thread th = new Thread(new SearchPcRunable(mDialog));
		th.start();
	}
	/**
	 * 检查是否获取到主机地址
	 * @return 如果有主机地址返回true
	 */
	protected boolean checkHost(){
		return mInetAddress == null;
	}
	
	/**
	 * 实现一个runable，会执行搜索主机的操作，一个延时20S的操作
	 * 1.在20s内没有搜索到主机会显示失败
	 * 2.在20s内搜索到主机会自动显示完成并且关闭对话框
	 * @author Lqq
	 *
	 */
	protected class SearchPcRunable implements Runnable{
		
		private CustomProgressDialog mDialog;
		
		public SearchPcRunable(CustomProgressDialog dialog) {
			mDialog = dialog;
		}
		@Override
		public void run() {
			mInetAddress = PcControlService.searchPc();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			toDoGetHostAddress();
			closeProgressDialog();
		}
	}
	/**
	 * 用于初始化所有数据
	 */
	private void initData(){
		mContext = this;
		mInflater = LayoutInflater.from(mContext);
		if(mInetAddress == null){
			searchPc();
		}
		initMyView();
	}
	/**
	 * 用于初始化控件
	 */
	private void initMyView(){
		setContentView(R.layout.activity_base);
		mBackButton = (ImageButton)findViewById(R.id.bakc_button);
		mTitleView = (TextView)findViewById(R.id.title_textView);
		mTitleRightButton = (ImageButton)findViewById(R.id.right_button);
		mShowInfoLayout = (RelativeLayout)findViewById(R.id.showinfo_relativeLayout);
		mInfoTextView = (TextView)findViewById(R.id.info_textView);
		mInfoRightButton = (ImageButton)findViewById(R.id.info_right_imageButton);
		mMainLayout = (FrameLayout)findViewById(R.id.main_frameLayout);
		

		mTitleRightButton.setClickable(true);
		mBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((Activity)mContext).finish();				
			}
		});
	}
	
	
	protected InetAddress getHostInetAddress(){
		return mInetAddress;
	}
	
	protected void showProgressDialog(){
		showProgressDialog(R.string.loding);
	}
	
	protected void showProgressDialog(int tip){
		Message msg = new Message();
		msg.what = SHOW_PROGRESS_DIALOG;
		msg.arg1 = tip;
		mBaseHandler.sendMessage(msg);
	}

	protected void closeProgressDialog(){
		mBaseHandler.sendEmptyMessage(CLOSE_PROGRESS_DIALOG);
	}
	/**
	 * 这个函数会在得到主机地址后被调用
	 */
	protected void toDoGetHostAddress(){
		
	}
	
	/**
	 * 设置返回按钮监听
	 * @param l
	 */
	protected void setBackButtonListener(OnClickListener l){
		mBackButton.setOnClickListener(l);
	}
	/**
	 * 用于是否显示提示信息
	 * @param isShowInfo
	 */
	protected void setShowInfo(boolean isShowInfo){
		if(isShowInfo){
			mShowInfoLayout.setVisibility(View.VISIBLE);
		}else{
			mShowInfoLayout.setVisibility(View.GONE);
		}
	}
	/**
	 * 设置提示信息文字
	 * @param r
	 */
	protected void setShowInfoText(int r){
		if(mInfoTextView.getVisibility() != View.VISIBLE){
			mInfoTextView.setVisibility(View.VISIBLE);
		}
		mInfoTextView.setText(r);
	}
	/**
	 * 用于设置右上角图片的监听事件
	 * @param l
	 */
	protected void setShowInfoPictureListener(OnClickListener l){
		mInfoRightButton.setOnClickListener(l);
	}
	/**
	 * 设置提示信息右边图片
	 * @param r
	 */
	protected void setShowInfoPicture(int r){
		if(mInfoRightButton.getVisibility() != View.VISIBLE){
			mInfoRightButton.setVisibility(View.VISIBLE);
		}
		mInfoRightButton.setImageResource(r);
	}
	/**
	 * 
	 * @param l
	 */
	protected void setRightButtonListener(OnClickListener l){
		mTitleRightButtonListener = l;
	}
	/**
	 * 设置标题文字
	 */
	protected void setTitle(String s){
		if(mTitleView.getVisibility() != View.VISIBLE){
			mTitleView.setVisibility(View.VISIBLE);
		}
		mTitleView.setText(s);
	}
	protected void setTitleRightPictureListener(OnClickListener l){
		mTitleRightButtonListener = l;
	}
	protected void setTitleRightPicture(int r){
		if(mTitleRightButton.getVisibility() != View.VISIBLE){
			mTitleRightButton.setVisibility(View.VISIBLE);
		}
		mTitleRightButton.setImageResource(r);
	}
}
