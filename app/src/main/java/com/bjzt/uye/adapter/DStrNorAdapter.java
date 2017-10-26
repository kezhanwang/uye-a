package com.bjzt.uye.adapter;

import com.bjzt.uye.entity.BDialogStrEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.DStrNorListItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.List;

/**
 * Created by billy on 2017/10/26.
 */

public class DStrNorAdapter extends BaseListAdapter<BDialogStrEntity>{
    private IItemListener mListener;

    public DStrNorAdapter(List<BDialogStrEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<BDialogStrEntity> getItemView(BDialogStrEntity bDialogStrEntity) {
        DStrNorListItemView itemView = new DStrNorListItemView(Global.getContext());
        itemView.setIItemListener(mListener);
        return itemView;
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
