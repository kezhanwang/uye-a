package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/11/2.
 */

public class OrgDetailBtnArea extends RelativeLayout implements View.OnClickListener,NoConfusion{

    @BindView(R.id.rela_phone)
    RelativeLayout mRelaPhone;
    @BindView(R.id.rela_fav)
    RelativeLayout mRelaFav;
    @BindView(R.id.rela_btn_sign)
    RelativeLayout mRelaSign;

    private IItemListener mListener;

    public static final int SRC_PHONE = 1;
    public static final int SRC_FAV = 2;
    public static final int SRC_SIGN = 3;

    public OrgDetailBtnArea(Context context) {
        super(context);
        init();
    }

    public OrgDetailBtnArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.org_btn_area_layout,this,true);
        ButterKnife.bind(this);

        mRelaPhone.setOnClickListener(this);
        mRelaFav.setOnClickListener(this);
        mRelaSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.mRelaPhone){
                this.mListener.onItemClick(null,SRC_PHONE);
            }else if(v == this.mRelaFav){
                this.mListener.onItemClick(null,SRC_FAV);
            }else{
                this.mListener.onItemClick(null,SRC_SIGN);
            }
        }
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
