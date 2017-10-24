package com.bjzt.uye.entity;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/24.
 */

public class PUpgradeEntity implements Serializable{
    public boolean isUpdate;
    public int version_code;
    public String version;
    public String desp;
    public String url;
    public String created_time;
    public String size;
    public boolean forceUpdate;
}
