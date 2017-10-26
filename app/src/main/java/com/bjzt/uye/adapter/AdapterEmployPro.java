package com.bjzt.uye.adapter;

import com.bjzt.uye.entity.PEmployProEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.msglist.itemview.EmployProItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.List;

/**
 * Created by billy on 2017/10/25.
 */

public class AdapterEmployPro extends BaseListAdapter<PEmployProEntity>{

    public AdapterEmployPro(List<PEmployProEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<PEmployProEntity> getItemView(PEmployProEntity pEmployProEntity) {
        BaseItemView<PEmployProEntity> itemView = new EmployProItemView(Global.getContext());
        return itemView;
    }
}
