package com.common.msglist;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.common.common.MyLog;
import com.common.file.SharePreRefreshTime;
import com.common.listener.NoConfusion;
import com.common.msglist.listener.IRefreshListener;
import com.common.util.TimeUtils;
import com.common.util.Utils;

import java.text.SimpleDateFormat;

/**
 * 
 * Copyright (c) 2013 Nono_Lilith All right reserved.
 * -------------------------------------------------------
 * 
 * @Description: NLPullRefreshView
 * @Author: Nono(陈凯)
 * @CreateDate: 2013--9 下午1:44:17
 * @version 1.0.0
 * 
 */
public class NLPullRefreshView extends LinearLayout implements NoConfusion {
	private static final String TAG = "NLPullRefreshView";
	private float xDistance, yDistance, xLast, yLast;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
	
	enum Status {
		NORMAL, DRAGGING, REFRESHING,
	}

	private Status status = Status.NORMAL;

	private final static float MIN_MOVE_DISTANCE = 5.0f;// 最小移动距离，用于判断是否在下拉，设置为0则touch事件的判断会过于平凡。具体值可以根据自己来设定

	private Scroller scroller;
	private View refreshView;
	private ImageView refreshIndicatorView;
	private int refreshTargetTop = -62;
	private ProgressBar bar;
	private TextView downTextView;
	private ImageView imgLogo;
	
	private IRefreshListener refreshListener;// 刷新监听器

	// 需要用到的文字引用
	private String downCanRefreshText;
	private String releaseCanRefreshText;
	private String txtRefreshSucc;
	private String txtRefreshError;
	
	private String refreshTime ;
	private int lastY;
	private Context mContext;
	private Handler uiHandler;
	private boolean isNoDelayFinish;
	private boolean canPullDown = true;
	
	public NLPullRefreshView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public NLPullRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}
	
	public void setNoDelayFinish(boolean isNoDelayFinish){
		this.isNoDelayFinish = isNoDelayFinish;
	}

	public void setEnablePullDown(boolean canPullDown){
		this.canPullDown = canPullDown;
	}
	
	/**
	 * 初始化
	 * @MethodDescription init
	 * @exception
	 * @since 1.0.0
	 */
	private void init() {
		// TODO Auto-generated method stub
		refreshTargetTop = getPixelByDip(mContext, refreshTargetTop);
		// 滑动对象，
		scroller = new Scroller(mContext);
		// 刷新视图顶端的的view
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		refreshView = li.inflate(R.layout.refresh_top_item, null);
		// 指示器view
		refreshIndicatorView = (ImageView) refreshView.findViewById(R.id.refresh_img_indicator);
		// 刷新bar
		bar = (ProgressBar) refreshView.findViewById(R.id.progress);
		// img logo
		imgLogo = (ImageView) refreshView.findViewById(R.id.img_logo);
		// 下拉显示text
		downTextView = (TextView) refreshView.findViewById(R.id.refresh_hint);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, -refreshTargetTop);
		lp.topMargin = refreshTargetTop;
		lp.gravity = Gravity.CENTER;
		addView(refreshView, lp);
		//文字资源可以归档在资源集中，此处为了方便。
		downCanRefreshText = getContext().getResources().getString(R.string.pull_headerview_txt_down);
		releaseCanRefreshText = getContext().getResources().getString(R.string.pull_headerview_txt_up);
		txtRefreshSucc = getResources().getString(R.string.pull_headerview_refresh_succ);
		txtRefreshError = getResources().getString(R.string.pull_headerview_refresh_error);
		
		SharePreRefreshTime sharePre = new SharePreRefreshTime();
		long time = sharePre.loadTime();
		String defaultTimeStr = "";
		if(time != 0){
			defaultTimeStr = TimeUtils.getTimeStr(time);
		}else{
			defaultTimeStr = getResources().getString(R.string.pull_headerview_refresh_no_refresh);
		}
		refreshTime = defaultTimeStr;//可以从保存文件中取得上次的更新时间
		
		if (refreshTime != null) {
			setRefreshTime(refreshTime);
		}
		initUIHandler();
	}

	private void initUIHandler(){
		uiHandler = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				int what = msg.what;
				if(what == 0x100){
					LayoutParams lp = (LayoutParams) NLPullRefreshView.this.refreshView.getLayoutParams();
					int i = lp.topMargin;
					bar.setVisibility(GONE);
					scroller.startScroll(0, i, 0, refreshTargetTop);
					invalidate();
					status = Status.NORMAL;
				}
			}
		};
	}
	
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		recyle();
	}
	
	public void recyle(){
		if(uiHandler != null){
			uiHandler.removeCallbacksAndMessages(null);
		}
	}
	
	/**
	 * 设置刷新后的内容
	 * 
	 * @MethodDescription setRefreshText
	 * @param time
	 * @exception
	 * @since 1.0.0
	 */
	private void setRefreshText(String time) {
		Log.i(TAG, "------>setRefreshText");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(canPullDown){
			if (status == Status.REFRESHING)
				return false;
			
			int y = (int) event.getRawY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.i(TAG, "MotionEvent.ACTION_DOWN");
				// 记录下y坐标
				lastY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i(TAG, "MotionEvent.ACTION_MOVE");
				// y移动坐标
				int m = y - lastY;
				doMovement(m);
				// 记录下此刻y坐标
				this.lastY = y;
				break;
			case MotionEvent.ACTION_UP:
				Log.i(TAG, "MotionEvent.ACTION_UP");
				fling();
				break;
			}
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		// layout截取touch事件
		if(canPullDown){
			int action = e.getAction();
			int y = (int) e.getRawY();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				lastY = y;
				
				xDistance = yDistance = 0f;  
	            xLast = e.getX();  
	            yLast = e.getY();  
	            break;
			case MotionEvent.ACTION_MOVE:
				// y移动坐标
				int m = y - lastY;
				// 记录下此刻y坐标
				this.lastY = y;
				
				//手势冲突
				final float curX = e.getX();  
	            final float curY = e.getY();             
	            xDistance += Math.abs(curX - xLast);
	            yDistance += Math.abs(curY - yLast);
	            xLast = curX;  
	            yLast = curY;  
	            if(xDistance > yDistance){  
	            	return false;
	            }
	            //end 手势冲突
	            
				if (m > MIN_MOVE_DISTANCE && canScroll()) {
					Log.i(TAG, "canScroll");
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_CANCEL:
				break;
			}
			return false;
		}
		
		return super.onInterceptTouchEvent(e);
	}

	/**
	 * up事件处理
	 */
	private void fling() {
		// TODO Auto-generated method stub
		LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
		if (lp.topMargin > 0) {// 拉到了触发可刷新事件
			status = Status.REFRESHING;
			Log.i(TAG, "fling ----->REFRESHING");
			refresh();
		} else {
			Log.i(TAG, "fling ----->NORMAL");
			returnInitState();
			status = Status.NORMAL;
		}
	}
	
	private void returnInitState() {
		// TODO Auto-generated method stub
		LayoutParams lp = (LayoutParams) this.refreshView.getLayoutParams();
		int i = lp.topMargin;
		Log.i(TAG, "returnInitState top = "+i);
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
	}

	/***
	 * 下拉刷新中，列表向上滑动
	 * 下拉刷新header重置
	 */
	public void onPageScrollUp(){
		if(status == Status.REFRESHING){
			returnInitState();
			status = Status.NORMAL;
		}
	}
	
	/**
	 * 执行刷新
	 * @MethodDescription refresh
	 * @exception
	 * @since 1.0.0
	 */
	private void refresh() {
		// TODO Auto-generated method stub
		Log.i(TAG, " ---> refresh()");
		LayoutParams lp = (LayoutParams) this.refreshView.getLayoutParams();
		int i = lp.topMargin;
		refreshIndicatorView.setVisibility(View.GONE);
		bar.setVisibility(View.VISIBLE);
		String str = "正在刷新中";
		downTextView.setVisibility(View.VISIBLE);
		downTextView.setText(str);
		imgLogo.setVisibility(View.GONE);
		scroller.startScroll(0, i, 0, 0 - i);
		invalidate();
		if (refreshListener != null) {
			refreshListener.onRefresh(this);
		}
	}

	/**
	 * 
	 */
	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {//scroll 动作还未结束
			int i = this.scroller.getCurrY();
			LayoutParams lp = (LayoutParams) this.refreshView.getLayoutParams();
			boolean isFinish = scroller.isFinished();
			int k = 0;
			boolean flag = false;
			if(isFinish && status != Status.REFRESHING){
				k = refreshTargetTop;
				flag = true;
			}else{
				k = Math.max(i, refreshTargetTop);
				flag = false;
			}
			lp.topMargin = k;
			this.refreshView.setLayoutParams(lp);
		}
	}

	/**
	 * 下拉move事件处理
	 * @param moveY
	 */
	private void doMovement(int moveY) {
		status = Status.DRAGGING;
		LayoutParams lp = (LayoutParams) refreshView.getLayoutParams();
		float f1 = lp.topMargin;
		float f2 = moveY * 0.3F;// 以0.3比例拖动
		int i = (int) (f1 + f2);
		// 修改上边距
		lp.topMargin = i;
		// 修改后刷新
		refreshView.setLayoutParams(lp);
		//setLayoutParams进行刷新,invalidate不进行重复刷新操作
//		refreshView.invalidate();
//		timeTextView.setVisibility(View.VISIBLE);
		downTextView.setVisibility(View.VISIBLE);
		refreshIndicatorView.setVisibility(View.VISIBLE);
		bar.setVisibility(View.GONE);
		imgLogo.setVisibility(View.VISIBLE);
		if (lp.topMargin > 0) {
			downTextView.setText(releaseCanRefreshText);
			refreshIndicatorView.setImageResource(R.drawable.refresh_arrow_up);
		} else {
			downTextView.setText(downCanRefreshText);
			refreshIndicatorView.setImageResource(R.drawable.refresh_arrow_down);
		}
	}

	/**
	 * 设置刷新时间
	 * @MethodDescription setRefreshTime 
	 * @param refreshTime 
	 * @exception 
	 * @since  1.0.0
	 */
	public void setRefreshTime(String refreshTime){
//		timeTextView.setText("更新于:"+refreshTime);
	}

	/**
	 * 设置监听
	 * @MethodDescription setRefreshListener 
	 * @param listener 
	 * @exception 
	 * @since  1.0.0
	 */
	public void setRefreshListener(IRefreshListener listener) {
		this.refreshListener = listener;
	}

	/**
	 * 结束刷新事件
	 */
	public void finishRefresh(boolean isSucc,boolean isShowToast) {
		Log.i(TAG, "------->finishRefresh()");
		LayoutParams lp = (LayoutParams) this.refreshView.getLayoutParams();
		int i = lp.topMargin;
		refreshIndicatorView.setVisibility(View.GONE);//下拉箭头显示
		bar.setVisibility(View.GONE);
//		imgLogo.setVisibility(View.VISIBLE);
		if(isSucc){
			downTextView.setText(txtRefreshSucc);
		}else{
			downTextView.setText(txtRefreshError);
			if(isShowToast){
				Utils.toast("刷新失败",false,true);
			}
		}
		int delay = 20;
		if(isNoDelayFinish){
			delay = 50;
			lp = (LayoutParams) NLPullRefreshView.this.refreshView.getLayoutParams();
			i = lp.topMargin;
			bar.setVisibility(GONE);
			scroller.startScroll(0, i, 0, refreshTargetTop);
//			invalidate();
			status = Status.NORMAL;
		}else{
			uiHandler.sendEmptyMessageDelayed(0x100,delay);
		}
	}

	/**
	 * 此方法兼容两种子布局的判断，listview，scrollview 主要作用是判断两个子View是否滚动到了最上面，若是，则表示此次touch
	 * move事件截取然后让layout来处理，来移动下拉视图，反之则不然
	 * 
	 * @MethodDescription canScroll
	 * @return
	 * @exception
	 * @since 1.0.0
	 */
	private boolean canScroll() {
		// TODO Auto-generated method stub
		View childView;
		if (getChildCount() > 1) {
			childView = this.getChildAt(1);
			if (childView instanceof ListView) {
				if(((ListView) childView).getChildAt(0) != null){
					int top = ((ListView) childView).getChildAt(0).getTop();
					int pad = ((ListView) childView).getListPaddingTop();
					if ((Math.abs(top - pad)) < 3 && ((ListView) childView).getFirstVisiblePosition() == 0) {
						return true;
					} else {
						return false;
					}
				}else{
					return false;
				}
			} else if (childView instanceof ScrollView) {
				if (((ScrollView) childView).getScrollY() == 0) {
					return true;
				} else {
					return false;
				}
			}

		}
		return false;
	}

	public static int getPixelByDip(Context c, int pix) {
		float f1 = c.getResources().getDisplayMetrics().density;
		float f2 = pix;
		return (int) (f1 * f2 + 0.5F);
	}
}
