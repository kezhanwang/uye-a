package com.bjzt.uye.adapter;

import com.bjzt.uye.entity.PAgencyEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.SearchItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.List;

/**
 * Created by billy on 2017/10/20.
 */

public class SearchAdapter extends BaseListAdapter<PAgencyEntity>{
    private IItemListener mListener;

    public SearchAdapter(List<PAgencyEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<PAgencyEntity> getItemView(PAgencyEntity pAgencyEntity) {
        SearchItemView itemView = new SearchItemView(Global.getContext());
        itemView.setIItemListener(this.mListener);
        return itemView;
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    public void clear(){
        List mList = getList();
        if(mList != null){
            mList.clear();
        }
        notifyDataSetChanged();
    }
}
