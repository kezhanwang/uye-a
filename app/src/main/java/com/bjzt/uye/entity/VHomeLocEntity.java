package com.bjzt.uye.entity;

import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.MutiBaseListAdapter;

/**
 * Created by billy on 2017/10/20.
 */

public class VHomeLocEntity implements BaseItemListener{
    public String strLoc;

    @Override
    public int getType() {
        return MutiBaseListAdapter.TYPE_HOME_LOC;
    }
}