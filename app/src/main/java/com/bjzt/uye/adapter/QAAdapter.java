package com.bjzt.uye.adapter;

import com.bjzt.uye.entity.PQACfgItemEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.QAItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.List;

/**
 * Created by billy on 2017/10/21.
 */
public class QAAdapter extends BaseListAdapter<PQACfgItemEntity>{
    private IItemListener mListener;

    public QAAdapter(List<PQACfgItemEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<PQACfgItemEntity> getItemView(PQACfgItemEntity pqaCfgItemEntity) {
        QAItemView itemView = new QAItemView(Global.getContext());
        itemView.setIItemListener(this.mListener);
        return itemView;
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
