package com.bjzt.uye.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.entity.PUpgradeEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.util.DeviceUtil;

public class DialogUpgrade extends Dialog implements OnClickListener {

	private TextView txtTime;
	private TextView txtSize;
	private TextView txtContents;
	private TextView txtBtnCancle;
	private TextView txtBtnOk;
	private IItemListener mListener;
	private View mViewDivide;

	public static final int SRC_CANCLE = 1;
	public static final int SRC_OK = 2;

	public DialogUpgrade(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		init();
	}

	public DialogUpgrade(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		Window window = getWindow();
		window.setGravity(Gravity.CENTER);
//		window.setWindowAnimations(R.style.popwin_animation);
		setCancelable(false);
		setCanceledOnTouchOutside(false);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mView = li.inflate(R.layout.update_dialog_item, null);
		
		this.txtTime = (TextView) mView.findViewById(R.id.update_time);
		this.txtSize = (TextView) mView.findViewById(R.id.update_size);
		this.txtContents = (TextView) mView.findViewById(R.id.update_tips);

		this.txtBtnCancle = (TextView) mView.findViewById(R.id.update_cancel);
		this.txtBtnCancle.setOnClickListener(this);
		
		this.txtBtnOk = (TextView) mView.findViewById(R.id.update_confirm);
		this.txtBtnOk.setOnClickListener(this);

		mViewDivide = (View)mView.findViewById(R.id.update_divider);
		
		int mWidth = DeviceUtil.mWidth;
		int lrMargin = (int) getContext().getResources().getDimension(R.dimen.common_margin);
		mWidth -= (lrMargin*2);
		LayoutParams llp = new LayoutParams(mWidth, LayoutParams.WRAP_CONTENT);
		this.setContentView(mView,llp);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
	}
	
	public void setInfo(PUpgradeEntity pEntity){
		if(pEntity != null){
			if(pEntity.forceUpdate){
				mViewDivide.setVisibility(View.GONE);
				txtBtnCancle.setVisibility(View.GONE);
			} else {
				mViewDivide.setVisibility(View.VISIBLE);
				txtBtnCancle.setVisibility(View.VISIBLE);
			}

			String strTime = "更新时间:" + pEntity.created_time;
			String strSize = "安装包大小:" + pEntity.size;
			String strTips = "新版本特性:" + pEntity.desp;
			txtTime.setText(strTime);
			txtSize.setText(strSize);
			txtContents.setText(strTips);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(mListener != null){
			if(view == this.txtBtnCancle){
				mListener.onItemClick(null,SRC_CANCLE);
			}else if(view == this.txtBtnOk){
				mListener.onItemClick(null,SRC_OK);
			}
		}
	}

	public void setIItemListener(IItemListener btnListener){
		this.mListener = btnListener;
	}
}
