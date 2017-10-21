package com.bjzt.uye.adapter;

import com.bjzt.uye.entity.VQAItemEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.QAListItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.List;

/**
 * Created by billy on 2017/10/21.
 */
public class QAItemAdapter extends BaseListAdapter<VQAItemEntity>{
    private IItemListener mListener;

    public QAItemAdapter(List<VQAItemEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<VQAItemEntity> getItemView(VQAItemEntity vqaItemEntity) {
        QAListItemView baseItemView = new QAListItemView(Global.getContext());
        baseItemView.setIItemListener(this.mListener);
        return baseItemView;
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
