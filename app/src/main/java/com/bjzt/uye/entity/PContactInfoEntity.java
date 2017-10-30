package com.bjzt.uye.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/30.
 */

public class PContactInfoEntity implements Serializable{
    public String uid;
    public String home_province;
    public String home_city;
    public String home_area;
    public String homt_address;
    public String email;
    public String qq;
    public String wechat;
    public String marriage;
    public String contact1_name;
    public String contact1_phone;
    public String contact1_relation;


    public boolean isOk(){
        return !TextUtils.isEmpty(marriage) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(qq) && !TextUtils.isEmpty(wechat);
    }
}
