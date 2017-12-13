package com.bjzt.uye.adapter;

import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.QAItemFooterView;
import com.bjzt.uye.msglist.itemview.QAItemView;
import com.bjzt.uye.views.component.TipsSubHeaderView;
import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.MutiBaseListAdapter;
import java.util.List;

/**
 * Created by billy on 2017/10/21.
 */
public class QAAdapter extends MutiBaseListAdapter{
    private IItemListener mListener;

    public QAAdapter(List<BaseItemListener> mList) {
        super(mList,MutiBaseListAdapter.ADAPTER_TYPE_QA);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    protected BaseItemView<BaseItemListener> getItemView(BaseItemListener baseItemListener) {
        int mType = baseItemListener.getType();
        BaseItemView baseItemView = null;
        if(mType == MutiBaseListAdapter.TYPE_QA_ITEM){
            QAItemView qaItemView = new QAItemView(Global.getContext());
            qaItemView.setIItemListener(mListener);
            baseItemView = qaItemView;
        }else if(mType == MutiBaseListAdapter.TYPE_QA_HEADER){
            TipsSubHeaderView itemView = new TipsSubHeaderView(Global.getContext());
            itemView.updateType(TipsSubHeaderView.TYPE_QA);
            baseItemView = itemView;
        }else if(mType == MutiBaseListAdapter.TYPE_QA_BTN){
            QAItemFooterView footerView = new QAItemFooterView(Global.getContext());
            footerView.setListener(mListener);
            baseItemView = footerView;
        }
        return baseItemView;
    }
}
