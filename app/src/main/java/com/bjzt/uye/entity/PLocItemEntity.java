package com.bjzt.uye.entity;

import java.io.Serializable;

/**
 * Created by ningdan on 2016/9/28.
 */
public class PLocItemEntity implements Serializable {
    public static final int TYPE_PROVINCE=1;//省
    public static final int TYPE_CITY=2;//市
    public static final int TYPE_AREA=3;//地区

    public String id;
    public String name;
    public String parentid;
    public String joinname;
    public boolean isArrow;
    public boolean vIsSelect;   //是否选中
    public int mType;
}
