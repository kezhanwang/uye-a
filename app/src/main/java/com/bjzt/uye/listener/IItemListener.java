package com.bjzt.uye.listener;

/**
 * Created by billy on 2017/10/13.
 */

public interface IItemListener {
    public static final int SRC_BTN_OK = 1;
    public static final int SRC_BTN_CANCLE = 2;

    public void onItemClick(Object obj,int tag);

}
