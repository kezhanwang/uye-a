package com.bjzt.uye.entity;

import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.MutiBaseListAdapter;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/19.
 */

public class PHomeOrderEntity implements Serializable,BaseItemListener{
    public String compensation;
    public String count;
    public String paid_compensation;

    @Override
    public int getType() {
        return MutiBaseListAdapter.TYPE_HOME_ORDERIINFO;
    }
}
