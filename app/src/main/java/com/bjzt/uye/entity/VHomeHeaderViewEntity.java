package com.bjzt.uye.entity;

import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.MutiBaseListAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/20.
 */

public class VHomeHeaderViewEntity implements Serializable,BaseItemListener{
    public ArrayList<PAdEntity> mList;
    public String strOrderCnt;


    @Override
    public int getType() {
        return MutiBaseListAdapter.TYPE_HOME_HEADERVIEW;
    }
}
