package com.common.entity;

/**
 * Created by billy on 2017/9/18.
 */

public class PHttpHeader {
    public String key = "Set-Cookie";
    public String key_kz_vp = "kz_vp";

    //验证码 cookie信息
    public String kz_vp;

    public String parseVal(){
        String val = "";
        String[] array = kz_vp.split("=");
        if(array != null && array.length >= 1){
            val = array[1];
        }
        return val;
    }
}
