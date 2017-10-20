package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.entity.PAgencyEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.msglist.base.BaseItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/20.
 */

public class SearchItemView extends BaseItemView<PAgencyEntity> implements View.OnClickListener{
    private PAgencyEntity mEntity;

    @BindView(R.id.rela_main)
    RelativeLayout mRelaMain;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.txtname)
    TextView mTxtName;
    @BindView(R.id.txt_cat)
    TextView mTxtCat;
    @BindView(R.id.txt_contents)
    TextView mTxtContent;
    @BindView(R.id.txt_dis)
    TextView mTxtDis;
    @BindView(R.id.btn_ok)
    Button btnOk;

    private IItemListener mListener;

    public static final int SRC_ITEM = 1;
    public static final int SRC_BTN_OK = 2;

    public SearchItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(PAgencyEntity pAgencyEntity) {
        this.mEntity = pAgencyEntity;
        //init icon
        String url = this.mEntity.logo;
        if(!TextUtils.isEmpty(url)){
            PicController.getInstance().showPic(imgLogo,url);
        }
        //init name
        String name = this.mEntity.org_name;
        if(!TextUtils.isEmpty(name)){
            mTxtName.setText(name);
        }else{
            mTxtName.setText("");
        }
        //init cat
        String strCat = this.mEntity.category;
        if(!TextUtils.isEmpty(strCat)){
            mTxtCat.setText(strCat);
        }else{
            mTxtCat.setText("");
        }
        //init content
        String strContent = this.mEntity.popular;
        if(!TextUtils.isEmpty(strContent)){
            mTxtContent.setText(strContent);
        }else{
            mTxtContent.setText("");
        }
        //init dis
        String strDis = this.mEntity.distance;
        if(!TextUtils.isEmpty(strDis)){
            mTxtDis.setText(strDis);
        }else{
            mTxtDis.setText("");
        }
    }

    @Override
    public PAgencyEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.search_itemview_layout,this,true);
        ButterKnife.bind(this);

        mRelaMain.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.mRelaMain){
                this.mListener.onItemClick(this.mEntity,SRC_ITEM);
            }else if(v == this.btnOk){
                this.mListener.onItemClick(this.mEntity,SRC_BTN_OK);
            }
        }
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
