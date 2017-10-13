package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.IHeaderListener;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/12.
 */

public class YHeaderView extends RelativeLayout implements NoConfusion,View.OnClickListener{

    @BindView(R.id.rela_left)
    RelativeLayout relaLeft;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.title)
    TextView mTxtTitle;
    @BindView(R.id.rela_right)
    RelativeLayout relaRight;
    @BindView(R.id.img_header_right)
    ImageView mImgRight;
    @BindView(R.id.textView_header_right)
    TextView mTxtRight;

    private int mType;

    public static final int TYPE_ABOUT = 0;
    public static final int TYPE_RIGHT_TXT = 1;
    public static final int TYPE_MYINFO = 2;

    private IHeaderListener mListener;

    public YHeaderView(Context context) {
        super(context);
        init();
    }

    public YHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.headerview_layout,this,true);
        ButterKnife.bind(this);
    }

    public void updateType(int mType){
        this.mType = mType;
        relaLeft.setVisibility(View.GONE);
        relaRight.setVisibility(View.GONE);
        mTxtTitle.setVisibility(View.GONE);

        relaLeft.setOnClickListener(this);
        relaRight.setOnClickListener(this);

        switch(this.mType){
            case TYPE_ABOUT:
                mTxtTitle.setVisibility(View.VISIBLE);
                relaLeft.setVisibility(View.VISIBLE);
                break;
            case TYPE_RIGHT_TXT:
                relaRight.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setTitle(String title){
        mTxtTitle.setText(title);
    }

    public void setRightTxt(String str){
        mTxtRight.setText(str);
    }

    public void setRightImg(){

    }

    public void setIListener(IHeaderListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View view) {
        if(mListener != null){
            if(view == this.relaLeft){
                this.mListener.onLeftClick();
            }else if(view == this.relaRight){
                this.mListener.onRightClick();
            }
        }
    }
}
