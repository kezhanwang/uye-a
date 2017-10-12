package com.bjzt.uye.util;

import android.text.TextUtils;

/**
 * Created by billy on 2017/10/12.
 */

public class StrUtil {


    /***
     * 删除所有空格相关
     * @param str
     * @return
     */
    public static final String trimAll(String str){
        String strResult = "";
        if(!TextUtils.isEmpty(str)){
            strResult = str.replaceAll("\\s*","");
        }
        return strResult;
    }

}
