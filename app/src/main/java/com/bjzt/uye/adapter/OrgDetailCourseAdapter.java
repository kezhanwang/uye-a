package com.bjzt.uye.adapter;

import android.widget.BaseAdapter;

import com.bjzt.uye.entity.PCourseEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.OrgDetailCourseItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;

import java.util.List;

/**
 * Created by billy on 2017/11/2.
 */

public class OrgDetailCourseAdapter extends BaseListAdapter<PCourseEntity> {
    private IItemListener mListener;

    public OrgDetailCourseAdapter(List<PCourseEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<PCourseEntity> getItemView(PCourseEntity pCourseEntity) {
        OrgDetailCourseItemView itemView = new OrgDetailCourseItemView(Global.getContext());
        itemView.setIItemListener(mListener);
        return itemView;
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
