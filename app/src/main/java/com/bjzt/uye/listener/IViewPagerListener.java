package com.bjzt.uye.listener;

import com.bjzt.uye.entity.PAdEntity;

/**
 * Created by billy on 2017/10/20.
 */

public abstract class IViewPagerListener {
    public void onPageSelect(int index){};

    public void onItemClick(PAdEntity entity){};
}
