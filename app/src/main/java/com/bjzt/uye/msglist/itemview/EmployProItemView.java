package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.view.LayoutInflater;

import com.bjzt.uye.R;
import com.bjzt.uye.entity.PEmployProEntity;
import com.bjzt.uye.entity.PEmployProListItemEntity;
import com.common.msglist.base.BaseItemView;

import butterknife.ButterKnife;

/**
 * 就业进展列表
 * Created by billy on 2017/10/25
 */
public class EmployProItemView extends BaseItemView<PEmployProListItemEntity>{
    private PEmployProListItemEntity mEntity;

    public EmployProItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(PEmployProListItemEntity pEmployProEntity) {
        this.mEntity = pEmployProEntity;
    }

    @Override
    public PEmployProListItemEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.employ_itemview_layout,this,true);

        ButterKnife.bind(this);
    }
}
