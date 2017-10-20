package com.bjzt.uye.views.component;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/13
 */
public class MyInfoHeaderView extends RelativeLayout implements NoConfusion, View.OnClickListener{

    @BindView(R.id.rela_main)
    RelativeLayout relaMain;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.txt_name)
    TextView mTxtNickName;
    @BindView(R.id.txt_loc)
    TextView mTxtLoc;

    private IItemListener mListener;

    public MyInfoHeaderView(Context context) {
        super(context);
        init();
    }

    public MyInfoHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.myinfo_headerview_layout,this,true);

        ButterKnife.bind(this);

        relaMain.setOnClickListener(this);
    }

    public void setIListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View view) {
        if(this.mListener != null){
            if(view == this.relaMain){
                mListener.onItemClick(null,-1);
            }
        }
    }

    public void setInfo(String faceUrl,String nickName,String strLoc){
        if(!TextUtils.isEmpty(faceUrl)){
//            PicController.getInstance().showPic(imgLogo,faceUrl);
        }
        //init nickName
        if(!TextUtils.isEmpty(nickName)){
            mTxtNickName.setText(nickName);
        }
        //init strLoc
        if(!TextUtils.isEmpty(strLoc)){
            mTxtLoc.setText(strLoc);
        }else{
            mTxtLoc.setText("");
        }
    }
}
