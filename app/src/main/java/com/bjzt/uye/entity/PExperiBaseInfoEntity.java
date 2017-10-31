package com.bjzt.uye.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/31.
 */

public class PExperiBaseInfoEntity implements Serializable{
    public String highest_education;
    public String profession;
    public String monthly_income;
    public String housing_situation;

    public boolean isOk(){
        return !TextUtils.isEmpty(highest_education) && !TextUtils.isEmpty(profession);
    }
}
