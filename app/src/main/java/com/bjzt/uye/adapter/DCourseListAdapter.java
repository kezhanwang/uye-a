package com.bjzt.uye.adapter;

import com.bjzt.uye.entity.PCourseEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.DCourseListItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;

import java.util.List;

/**
 * Created by billy on 2017/10/19.
 */

public class DCourseListAdapter extends BaseListAdapter<PCourseEntity> {
    public IItemListener mListener;

    public DCourseListAdapter(List<PCourseEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<PCourseEntity> getItemView(PCourseEntity pCourseEntity) {
        DCourseListItemView baseItemView = new DCourseListItemView(Global.getContext());
        baseItemView.setIItemListener(this.mListener);
        return baseItemView;
    }

    public void setListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
