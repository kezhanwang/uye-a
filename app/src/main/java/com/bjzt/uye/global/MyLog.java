package com.bjzt.uye.global;

import android.util.Log;

/**
 * Created by billy on 2017/9/18.
 */

public class MyLog {
    public static final String Billy = "Billywen";

    /****
     * 是否debug调试模式
     * @return
     */
    public static final boolean isDebugable(){
        return MConfiger.isDebug;
    }

    public static final void debug(String TAG,String msg){
        if(!MConfiger.isDebug){
            return;
        }
        Log.d(TAG,msg);
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
