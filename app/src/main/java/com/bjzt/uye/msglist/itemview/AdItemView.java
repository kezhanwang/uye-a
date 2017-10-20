package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.entity.PAdEntity;
import com.bjzt.uye.listener.IViewPagerListener;


public class AdItemView extends RelativeLayout implements OnClickListener {
	private final String TAG = "AdItemView";
	
	private ImageView imgView;
	private IViewPagerListener mListener;
	private PAdEntity mEntity;
	
	public AdItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public AdItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public AdItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.ad_item_view_layout, this, true);
		imgView = (ImageView) this.findViewById(R.id.imgview);
		imgView.setOnClickListener(this);
	}
	
	public void setAdInfo(PAdEntity mEntity){
		this.mEntity = mEntity;
		String picUrl = mEntity.logo;
		PicController.getInstance().showPic(imgView,picUrl);
	}
	
	public void setIPagerListener(IViewPagerListener mListener){
		this.mListener = mListener;
	}
	
	public void recyle(){
		
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(mListener != null){
			if(view == imgView){
				mListener.onItemClick(this.mEntity);
			}
		}
	}
}
