package com.bjzt.uye.entity;

import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.MutiBaseListAdapter;

import java.util.List;

/**
 * Created by billy on 2017/10/20.
 */

public class VHomeLocEntity implements BaseItemListener{
    public String strLoc;
    public List<POrganizeEntity> organize;
    
    @Override
    public int getType() {
        return MutiBaseListAdapter.TYPE_HOME_LOC;
    }
}
