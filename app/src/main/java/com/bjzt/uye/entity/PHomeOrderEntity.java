package com.bjzt.uye.entity;

import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.MutiBaseListAdapter;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/19.
 */

public class PHomeOrderEntity implements Serializable,BaseItemListener{
    public String compensation;
    public int count;
    public int paid_compensation;

    @Override
    public int getType() {
        return MutiBaseListAdapter.TYPE_HOME_ORDERIINFO;
    }
}
