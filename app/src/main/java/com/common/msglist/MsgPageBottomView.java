package com.common.msglist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.common.listener.NoConfusion;


public class MsgPageBottomView extends LinearLayout implements NoConfusion,OnClickListener {
	
	public static final int STATE_LISTVIEW_LOADING = 1;	//loading加载状态
	public static final int STATE_LISTVIEW_NOMORE = 2;	//没有更多消息
	public static final int STATE_LISTVIEW_INIT = 3;	// 初始状态
	public static final int STATE_LISTVIEW_ERROR = 5;	//加载失败
	
	private ProgressBar progressBar;
	private TextView txtView;
	private int mState;
	private LinearLayout linearMain;
	private OnClickListener mListener;
	
	public MsgPageBottomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public MsgPageBottomView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.msgitem_view_bottom_grey,this,true);
		progressBar = (ProgressBar) this.findViewById(R.id.pagination_progress_bar);
		txtView = (TextView) this.findViewById(R.id.msgitem_bottom_view);
		linearMain = (LinearLayout) this.findViewById(R.id.linear_main);
		linearMain.setOnClickListener(this);
		
		updateState(STATE_LISTVIEW_INIT);
	}

	public int getState(){
		return this.mState;
	}
	
	public void updateState(int state){
		this.mState = state;
		if(state == STATE_LISTVIEW_INIT){
			progressBar.setVisibility(View.GONE);
			String str = getContext().getResources().getString(R.string.msgitem_bottom_init);
			txtView.setText(str);
		}else if(state == STATE_LISTVIEW_LOADING){
			progressBar.setVisibility(View.VISIBLE);
			String str = getContext().getResources().getString(R.string.msgitem_bottom_loading);
			txtView.setText(str);
		}else if(state == STATE_LISTVIEW_NOMORE){
			progressBar.setVisibility(View.GONE);
			String str = getContext().getResources().getString(R.string.msgitem_bottom_nomore);
			txtView.setText(str);
		}else if(state == STATE_LISTVIEW_ERROR){
			progressBar.setVisibility(View.GONE);
			String str = getContext().getResources().getString(R.string.msgitem_bottom_error_tips);
			txtView.setText(str);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view == this.linearMain){
			if(mListener != null){
				mListener.onClick(view);
			}
		}
	}
	
	public void setListener(OnClickListener mListener){
		this.mListener = mListener;
	}
}
