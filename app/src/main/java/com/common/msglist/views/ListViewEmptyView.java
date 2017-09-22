package com.common.msglist.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.common.listener.NoConfusion;

/**
 * listView为空时显示的View
 * @author yas
 *
 */
public class ListViewEmptyView extends RelativeLayout implements NoConfusion, OnClickListener {
	public static final int TYPE_COURSE=1;	//关注课程为空

	private ImageView imageView_emptyTop;
	private ImageView image_empty;
	private TextView text_empty;
	private Button btn_empty;
	private Context mContext;
	public ListViewEmptyView(Context context) {
		super(context);
		mContext=context;
		// TODO Auto-generated constructor stub
		init();
	}
	public ListViewEmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		// TODO Auto-generated constructor stub
		init();
	}
	public ListViewEmptyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext=context;
		// TODO Auto-generated constructor stub
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		LayoutInflater li=(LayoutInflater) Global.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.list_empty, this,true);
		imageView_emptyTop=(ImageView) this.findViewById(R.id.imageView_emptyTop);
		image_empty=(ImageView) this.findViewById(R.id.image_empty);
		text_empty=(TextView) this.findViewById(R.id.text_empty);
		btn_empty=(Button) this.findViewById(R.id.btn_empty);
		btn_empty.setOnClickListener(this);
	}

	//设置类型
	public void setType(int type){
		Drawable drawable=null;
		String str=null;
		image_empty.setImageDrawable(drawable);
		text_empty.setText(str);
	}

	@Override
	public void onClick(View view) {

	}
}
