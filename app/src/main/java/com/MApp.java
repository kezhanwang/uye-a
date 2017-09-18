package com;

import android.app.Application;

import com.bjzt.uye.global.Global;

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

    }

}
