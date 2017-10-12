package com.bjzt.uye.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.util.IntentUtils;

/**
 * Created by billy on 2017/10/12
 */
public class SplashActivity extends BaseActivity{
    private final int FLAG_MSG_DELAY = 0x100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);

        delayTask();
    }

    private void delayTask(){
        Message msg = Message.obtain();
        msg.what = FLAG_MSG_DELAY;
        sendMsgDelay(msg, MConfiger.SPLASH_INTERVAL);
    }

    @Override
    protected void handleMsg(Message msg) {
        int what = msg.what;
        if(what == FLAG_MSG_DELAY){
            IntentUtils.startMainActivity(this);
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
