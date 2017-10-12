package com.bjzt.uye.util;

import android.content.Context;
import android.content.Intent;

import com.bjzt.uye.activity.MainActivity;

/**
 * Created by billy on 2017/10/12.
 */

public class IntentUtils {


    /**
     * 打开首页
     * @param mContext
     * @param clearTop
     */
    public static final void startMainActivity(Context mContext,boolean clearTop){
        Intent intent = new Intent(mContext,MainActivity.class);
        if(clearTop){
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }


    /***
     * 打开首页
     * @param mContext
     */
    public static final void startMainActivity(Context mContext){
        startMainActivity(mContext,true);
    }
}
