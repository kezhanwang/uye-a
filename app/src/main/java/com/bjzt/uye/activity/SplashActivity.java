package com.bjzt.uye.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.util.IntentUtils;
import com.common.util.ShortCutUtils;

/**
 * Created by billy on 2017/10/12
 */
public class SplashActivity extends BaseActivity{
    private final int FLAG_MSG_DELAY = 0x100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delayTask();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash_layout;
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initExtras(Bundle bundle) {

    }

    private void delayTask(){
        Global.postDelay(new Runnable() {
            @Override
            public void run() {
                //快捷方式创建
                //
                ShortCutUtils.createShortCut(SplashActivity.this);
                Message msg = Message.obtain();
                msg.what = FLAG_MSG_DELAY;
                sendMsgDelay(msg, MConfiger.SPLASH_INTERVAL);
            }
        });
    }

    @Override
    protected void handleMsg(Message msg) {
        int what = msg.what;
        if(what == FLAG_MSG_DELAY){
            IntentUtils.startMainActivity(this,false);
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0, 0);
    }

}
