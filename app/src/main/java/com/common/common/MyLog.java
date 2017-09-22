package com.common.common;

import android.util.Log;

import com.bjzt.uye.global.MConfiger;

/**
 * Created by billy on 2017/9/21.
 */

public class MyLog {

    public static boolean isDebugable(){
        return MConfiger.isDebug;
    }

    public static final void d(String TAG,String msg){
        if(!MConfiger.isDebug){
            return;
        }
        Log.d(TAG,msg);
    }


    public static final void debug(String TAG,String msg){
        d(TAG,msg);
    }

    public static final void error(String TAG,String msg,Throwable throwable){
        if(!MConfiger.isDebug){
            return;
        }
        Log.e(TAG, msg, throwable);
    }

    public static final void error(String TAG,String msg){
        if(!MConfiger.isDebug){
            return;
        }
        Log.e(TAG, msg);
    }

    public static final void error(String TAG,Throwable throwable){
        if(!MConfiger.isDebug){
            return;
        }
        Log.e(TAG, "", throwable);
    }


}
