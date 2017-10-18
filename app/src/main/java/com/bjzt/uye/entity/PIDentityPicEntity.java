package com.bjzt.uye.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/18.
 */

public class PIDentityPicEntity implements Serializable{
    public String id_card_info_pic;
    public String id_card_nation_pic;

    public boolean isLegal(){
        return !TextUtils.isEmpty(id_card_info_pic) && !TextUtils.isEmpty(id_card_nation_pic);
    }
}
