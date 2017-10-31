package com.bjzt.uye.adapter;

import com.bjzt.uye.entity.PExperiEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.ExperiItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.List;

/**
 * Created by billy on 2017/10/31.
 */

public class ExperiListAdapter extends BaseListAdapter<PExperiEntity>{
    private IItemListener mListener;

    public ExperiListAdapter(List<PExperiEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<PExperiEntity> getItemView(PExperiEntity pExperiEntity) {
        ExperiItemView itemView = new ExperiItemView(Global.getContext());
        itemView.setIItemListener(this.mListener);
        return itemView;
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
