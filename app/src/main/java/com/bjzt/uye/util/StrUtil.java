package com.bjzt.uye.util;

import android.content.Context;
import android.text.TextUtils;

import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.common.common.NetCommon;

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

    /***
     * 判断手机号码是否合法
     * @param phone
     * @return
     */
    public static final boolean isLegal(String phone){
        if(!TextUtils.isEmpty(phone) && phone.length() == 11){
            return true;
        }
        return false;
    }

    public static final String getErrorTipsByCode(int errorCode){
        String str = "";
        Context mContext = Global.getContext();
        if(errorCode == NetCommon.ERROR_CODE_TIME_OUT){
            str = mContext.getResources().getString(R.string.common_timeout);
        }else{
            str = mContext.getResources().getString(R.string.common_request_error_code,errorCode);
        }
        return str;
    }


    public static final String getErrorTipsByCode(int errorCode, RspBaseEntity rspEntity){
        String str = getErrorTipsByCode(errorCode);
        if(rspEntity != null && !TextUtils.isEmpty(rspEntity.msg)){
            str = rspEntity.msg;
        }
        return str;
    }

}
