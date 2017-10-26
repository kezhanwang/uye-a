package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.view.LayoutInflater;

import com.bjzt.uye.R;
import com.bjzt.uye.entity.PEmployProEntity;
import com.common.msglist.base.BaseItemView;

import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/25.
 */

public class EmployProItemView extends BaseItemView<PEmployProEntity>{
    private PEmployProEntity mEntity;

    public EmployProItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(PEmployProEntity pEmployProEntity) {
        this.mEntity = pEmployProEntity;
    }

    @Override
    public PEmployProEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.employ_itemview_layout,this,true);

        ButterKnife.bind(this);
    }
}
