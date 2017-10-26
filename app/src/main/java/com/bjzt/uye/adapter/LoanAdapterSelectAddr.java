package com.bjzt.uye.adapter;

import android.text.TextUtils;

import com.bjzt.uye.activity.dialog.DialogLocation;
import com.bjzt.uye.entity.PLocItemEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.msglist.itemview.LoanSelectAddressItem;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ningdan on 2016/9/28.
 */
public class LoanAdapterSelectAddr extends BaseListAdapter<PLocItemEntity> {
    private DialogLocation.LoanISelectAddressItemClickListener mSelectAddressItemListener;

    public LoanAdapterSelectAddr(ArrayList<PLocItemEntity> mList){
        super(mList);
    }

    @Override
    protected BaseItemView<PLocItemEntity> getItemView(PLocItemEntity PLocItemEntity) {
        LoanSelectAddressItem itemview=new LoanSelectAddressItem(Global.getContext());
        itemview.setSelectAddressItemListener(mSelectAddressItemListener);
        return itemview;
    }

    public void setmSelectAddressItemListener(DialogLocation.LoanISelectAddressItemClickListener mSelectAddressItemListener){
        this.mSelectAddressItemListener=mSelectAddressItemListener;
    }

    public PLocItemEntity getSelectItem(PLocItemEntity entity){
        PLocItemEntity rEntity = null;
        List<PLocItemEntity> mList = getList();
        if(mList != null){
            for(int i = 0;i < mList.size();i++){
                PLocItemEntity item = mList.get(i);
                if((!TextUtils.isEmpty(item.id) && item.id.equals(entity.id))){
                    rEntity = item;
                    break;
                }
            }
        }
        return rEntity;
    }
}
