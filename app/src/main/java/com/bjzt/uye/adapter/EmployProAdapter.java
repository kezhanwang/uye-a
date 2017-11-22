package com.bjzt.uye.adapter;

import com.bjzt.uye.entity.PEmployProListItemEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.msglist.itemview.EmployProItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.List;

/**
 * Created by billy on 2017/10/25.
 */

public class EmployProAdapter extends BaseListAdapter<PEmployProListItemEntity>{

    public EmployProAdapter(List<PEmployProListItemEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<PEmployProListItemEntity> getItemView(PEmployProListItemEntity pEmployProEntity) {
        BaseItemView<PEmployProListItemEntity> itemView = new EmployProItemView(Global.getContext());
        return itemView;
    }
}
