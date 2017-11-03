package com;

import android.app.Application;
import com.bjzt.uye.controller.LBSController;
import com.bjzt.uye.controller.OtherController;
import com.bjzt.uye.controller.WifiController;
import com.bjzt.uye.global.Global;
import com.common.controller.LoginController;
import com.common.http.HttpEngine;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by billy on 2017/9/18.
 */
public class MApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //multidex lib
        android.support.multidex.MultiDex.install(this);
        Global.init(this.getApplicationContext());

        delayTask();
    }

    private void delayTask(){
        Global.postDelay(new Runnable() {
            @Override
            public void run() {
                //加载LBS信息
                LBSController.getInstance().loadInfo();
                //加载wifi信息
                WifiController.getInstance().loadInfo();
                //加载登录信息
                LoginController.getInstance().loadInfo();

                //启动Http引擎
                HttpEngine.getInstance().startEngine();

                //x5内核SDK初始化
                QbSdk.preInit(MApp.this);
                //bugly初始化
                CrashReport.initCrashReport(getApplicationContext(), "0e4e5fe1e1", false);
            }
        });

        //6秒以后再启动其他
        Global.postDelay(new Runnable() {
            @Override
            public void run() {
                OtherController.getInstance().loadInfo();
            }
        },1000*6);
    }

}
