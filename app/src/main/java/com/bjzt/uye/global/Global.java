package com.bjzt.uye.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;

import com.common.common.MyLog;
import com.common.util.DeviceUtil;

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
    private static int seqNo = 200;
    private static String KZUA;

    static{
        initHandlerThread();
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public static final void init(Context mContext){
        Global.mContext = mContext;
        init();
    }

    public static Looper getLooper(){
        return mLooper;
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
    /***
     * 获取课栈UA信息
     * @return
     */
    public static final String getYouyeUAInfo(){
        if(TextUtils.isEmpty(KZUA) || PKG_VER <= 0){
            StringBuilder builder = new StringBuilder();
            builder.append(MConfiger.CHANNEL_ID);					//channel id
            builder.append("||");
            builder.append(PKG_VER);								//安装包版本号
            builder.append("||");
            builder.append(DeviceUtil.getAndroidID());				//android id
            builder.append("||");
            builder.append(DeviceUtil.getAndroidSDKVersion());		//sdk version
            builder.append("||");
            builder.append("w=" + DeviceUtil.mWidth + ";h=" + DeviceUtil.mHeight);
            builder.append("||");
            builder.append(DeviceUtil.getIMEI());										//mie
            builder.append("||");
            builder.append(DeviceUtil.getMacAdd());
            builder.append("||");
            builder.append(DeviceUtil.getDeviceModel());
            builder.append("||");
            builder.append(DeviceUtil.getDeviceProduct());
            builder.append("||");
            builder.append(DeviceUtil.getDeviceRelease());
            builder.append("||");
            builder.append(MConfiger.JPUSH_ID);
            KZUA = builder.toString();
        }
        if(MyLog.isDebugable()){
            MyLog.debug(TAG,"[getYouyeUAInfo]" + " " + KZUA);
        }
        return KZUA;
    }


    public synchronized static final int getSeqNo(){
        return seqNo++;
    }
}
