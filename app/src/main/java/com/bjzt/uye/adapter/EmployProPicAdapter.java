package com.bjzt.uye.adapter;

import com.bjzt.uye.global.Global;
import com.bjzt.uye.msglist.itemview.EmployProPicItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import java.util.List;

/**
 * Created by billy on 2017/11/22.
 */

public class EmployProPicAdapter extends BaseListAdapter<String>{

    public EmployProPicAdapter(List<String> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<String> getItemView(String s) {
        BaseItemView itemView = new EmployProPicItemView(Global.getContext());
        return itemView;
    }
}
