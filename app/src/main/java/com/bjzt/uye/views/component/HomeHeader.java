package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/19.
 */

public class HomeHeader extends RelativeLayout implements NoConfusion, View.OnClickListener{

    @BindView(R.id.rela_loc)
    RelativeLayout mRelaLoc;
    @BindView(R.id.txt_loc)
    TextView mTxtLoc;
    @BindView(R.id.linear_content)
    LinearLayout mLinearContent;
    @BindView(R.id.img_sysmsg)
    ImageView imgSysMsg;

    public static final int TAG_LOC = 1;
    public static final int TAG_CONTENTS = 2;
    public static final int TAG_SYSMSG = 3;

    private IItemListener mListener;

    public HomeHeader(Context context) {
        super(context);
        init();
    }

    public HomeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.home_header_layout,this,true);
        ButterKnife.bind(this);

        mRelaLoc.setOnClickListener(this);
        mLinearContent.setOnClickListener(this);
        imgSysMsg.setOnClickListener(this);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View view) {
        if(this.mListener != null){
            if(view == this.mRelaLoc){
                this.mListener.onItemClick(this,TAG_LOC);
            }else if(view == this.mLinearContent){
                this.mListener.onItemClick(this,TAG_CONTENTS);
            }else if(view == this.imgSysMsg){
                this.mListener.onItemClick(this,TAG_SYSMSG);
            }
        }
    }

    public void setLocTxt(String strInfo){
        mTxtLoc.setText(strInfo);
    }
}
