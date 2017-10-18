package com.bjzt.uye.adapter;

import com.bjzt.uye.entity.PBankEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.BankListItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.List;
/**
 * Created by diaosi on 2017/2/27
 */
public class BankListAdapter extends BaseListAdapter<PBankEntity> {
    private IItemListener mListener;

    public BankListAdapter(List<PBankEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<PBankEntity> getItemView(PBankEntity loanPBankEntity) {
        BaseItemView itemView = new BankListItemView(Global.getContext());
        if(itemView instanceof BankListItemView){
            BankListItemView cashItemView = (BankListItemView) itemView;
            cashItemView.setIItemListener(mListener);
        }
        return itemView;
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
