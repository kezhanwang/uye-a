package com.bjzt.uye.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

/**
 * Created by billy on 2017/9/18.
 */

public class Global {
    private static final String TAG = "Global";
    public static Context mContext;
    private static HandlerThread mHandlerThread = null;
    private static Looper mLooper = null;
    private static Handler bizHandler = null;
    private static Handler uiHandler = null;
    public static int PKG_VER = 0;

    static{
        initHandlerThread();
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public static final void init(Context mContext){
        Global.mContext = mContext;
        init();
    }

    private static final void initHandlerThread(){
        mHandlerThread = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mLooper = mHandlerThread.getLooper();
        bizHandler = new Handler(mLooper);
    }

    /***
     * 初始化操作
     */
    public static void init(){
        String pkgName = Global.mContext.getPackageName();
        try {
            PackageInfo pkgInfo = Global.mContext.getPackageManager().getPackageInfo(pkgName,0);
            if(pkgInfo != null){
                int version = pkgInfo.versionCode;
                Global.PKG_VER = version;
            }
        } catch (PackageManager.NameNotFoundException e) {
            MyLog.error(TAG, e);
        }
    }


    public static final Context getContext(){
        return mContext;
    }

    public static final void postDelay(Runnable r){
        if(mHandlerThread != null && !mHandlerThread.isAlive()){
            initHandlerThread();
        }
        if(r != null){
            bizHandler.post(r);
        }
    }

    public static final void post2UI(Runnable r){
        uiHandler.post(r);
    }

    public static final void post2UIDelay(Runnable r,long delayMillis){
        uiHandler.postDelayed(r,delayMillis);
    }

    /****
     * 非UI操作，delay
     * @param r
     * @param delayMillis
     */
    public static final void postDelay(Runnable r,long delayMillis){
        bizHandler.postDelayed(r, delayMillis);
    }

    /***
     * 移除任务
     * @param r
     */
    public static final void removeDelay(Runnable r){
        bizHandler.removeCallbacks(r);
        uiHandler.removeCallbacks(r);
    }

    /****
     * 获取QUA信息
     * @return
     */
    public static final String getQUA(){
        String strInfo = "";

        return strInfo;
    }
}
