package com;

import android.app.Application;
import com.bjzt.uye.controller.LBSController;
import com.bjzt.uye.global.Global;
import com.common.controller.LoginController;
import com.common.http.HttpEngine;

/**
 * Created by billy on 2017/9/18.
 */
public class MApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Global.init(this.getApplicationContext());

        delayTask();
    }

    private void delayTask(){
        Global.postDelay(new Runnable() {
            @Override
            public void run() {
                //加载LBS信息
                LBSController.getInstance().loadInfo();
                //加载登录信息
                LoginController.getInstance().loadInfo();

                //启动Http引擎
                HttpEngine.getInstance().startEngine();
            }
        });
    }

}
