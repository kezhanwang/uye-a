package com.bjzt.uye.views.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

    @BindView(R.id.rela_main)
    RelativeLayout mRelaMain;
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
    public static final int TYPE_RIGHT_TXT = 1; //登录样式，左侧返回，右侧注册
    public static final int TYPE_MYINFO = 2;
    public static final int TYPE_RIGHT_TXT_ONLY = 3;    //只是右侧的txt
    public static final int TYPE_PIC_SCANNE = 4;	//左面是箭头,右边是imgIcon,图片浏览类型
    public static final int TYPE_ABOUT_ABLUME = 5;
    public static final int TYPE_IMAGE_RIGHT_ABLUME = 6;

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
        mImgRight.setVisibility(View.GONE);
        relaLeft.setOnClickListener(this);
        relaRight.setOnClickListener(this);

        switch(this.mType){
            case TYPE_ABOUT:
                mTxtTitle.setVisibility(View.VISIBLE);
                relaLeft.setVisibility(View.VISIBLE);
                break;
            case TYPE_RIGHT_TXT:
            case TYPE_RIGHT_TXT_ONLY:
                mTxtTitle.setVisibility(View.VISIBLE);
                relaRight.setVisibility(View.VISIBLE);
                if(this.mType == TYPE_RIGHT_TXT){
                    relaLeft.setVisibility(View.VISIBLE);
                }
                break;
            case TYPE_PIC_SCANNE:
            case TYPE_IMAGE_RIGHT_ABLUME:
                relaLeft.setVisibility(View.VISIBLE);
                mTxtTitle.setVisibility(View.VISIBLE);
                relaRight.setVisibility(View.VISIBLE);
                mImgRight.setVisibility(View.VISIBLE);
                int c = getResources().getColor(R.color.common_black);
                mRelaMain.setBackgroundDrawable(new ColorDrawable(c));
                Drawable d = getResources().getDrawable(R.drawable.loan_header_rela_bg_ablum);
                relaLeft.setBackgroundDrawable(d);
                d = getResources().getDrawable(R.drawable.loan_header_rela_bg_ablum);
                relaRight.setBackgroundDrawable(d);
                int cWhtite = getResources().getColor(R.color.common_white);
                mTxtTitle.setTextColor(cWhtite);
                d = getResources().getDrawable(R.drawable.super_back);
                imgBack.setImageDrawable(d);
                break;
        }
    }

    public void setRightClickAble(boolean isClickAble){
        Drawable d = null;
        if(isClickAble){
            d = getResources().getDrawable(R.drawable.more_setting_bg_selector);
        }
        this.relaRight.setBackgroundDrawable(d);
    }

    //设置右侧
    public void setRightImage(int resId){
        this.mImgRight.setImageResource(resId);
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
