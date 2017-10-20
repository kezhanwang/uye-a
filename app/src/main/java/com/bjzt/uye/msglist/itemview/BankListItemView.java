package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.entity.PBankEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.msglist.base.BaseItemView;

/**
 * Created by diaosi on 2017/2/27.
 * 银行列表dialog
 */
public class BankListItemView extends BaseItemView<PBankEntity> implements View.OnClickListener{
    private PBankEntity mEntity;
    private ImageView mImgIcon;
    private TextView mTxtName;
    private ImageView imgSelect;
    private RelativeLayout mRela;
    private IItemListener mListener;

    public BankListItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(PBankEntity loanPBankEntity) {
        this.mEntity = loanPBankEntity;
        //set icon
        String imgUrl = this.mEntity.icon;
        if(!TextUtils.isEmpty(imgUrl)){
            this.mImgIcon.setVisibility(View.VISIBLE);
            PicController.getInstance().showPic(mImgIcon,imgUrl);
        }else{
            this.mImgIcon.setVisibility(View.GONE);
        }
        //set name
        String strName = this.mEntity.open_bank;
        if(!TextUtils.isEmpty(strName)){
            this.mTxtName.setText(strName);
        }else{
            this.mTxtName.setText("");
        }
        //set select img flag
        boolean isSelect = this.mEntity.vIsSelected;
        if(isSelect){
            this.imgSelect.setVisibility(View.VISIBLE);
        }else{
            this.imgSelect.setVisibility(View.GONE);
        }
    }

    @Override
    public PBankEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.banklist_itemview_layout,this,true);
        this.mRela = (RelativeLayout) this.findViewById(R.id.dialog_rela_main);
        this.mRela.setOnClickListener(this);
        this.mImgIcon = (ImageView) this.findViewById(R.id.banklist_imgview_icon);
        this.mTxtName = (TextView) this.findViewById(R.id.banklist_txtview_name);
        this.imgSelect = (ImageView) this.findViewById(R.id.banklist_imgview_select);
    }

    @Override
    public void onClick(View v) {
        if(v == this.mRela){
            if(this.mListener != null){
                this.mListener.onItemClick(this.mEntity,-1);
            }
        }
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
