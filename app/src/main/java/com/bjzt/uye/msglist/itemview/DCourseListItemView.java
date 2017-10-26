package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.entity.PCourseEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.msglist.base.BaseItemView;

/**
 * Created by diaosi on 2017/2/27.
 * 银行列表dialog
 */
public class DCourseListItemView extends BaseItemView<PCourseEntity> implements View.OnClickListener{
    private PCourseEntity mEntity;
    private TextView mTxtName;
    private ImageView imgSelect;
    private RelativeLayout mRela;
    private IItemListener mListener;

    public DCourseListItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(PCourseEntity loanPBankEntity) {
        this.mEntity = loanPBankEntity;
        //set name
        String strName = this.mEntity.c_name;
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
    public PCourseEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.dialog_course_itemview_layout,this,true);
        this.mRela = (RelativeLayout) this.findViewById(R.id.dialog_rela_main);
        this.mRela.setOnClickListener(this);
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
