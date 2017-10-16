package com.bjzt.uye.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.controller.LBSController;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.util.IntentUtils;
import com.common.common.MyLog;
import com.common.util.DeviceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/10.
 */
public class TestActivity extends BaseActivity{

    @BindView(R.id.btn_loc)
    Button btnLoc;

    @BindView(R.id.btn_loc_city)
    Button btnLocCity;

    @BindView(R.id.btn_main)
    Button btnMainPage;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.btn_register)
    Button btnReg;

    @BindView(R.id.btn_datacheck)
    Button btnDataCheck;

    @BindView(R.id.btn_webview)
    Button btnWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String strMac = DeviceUtil.getMacAdd();
        MyLog.d(TAG,"[onCreate]" + " strMac:" + strMac);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_layout;
    }

    protected void initLayout(){
        btnLoc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LBSController.getInstance().startLoc();
            }
        });

        btnLocCity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ProtocalManager.getInstance().reqLocCity(new ICallBack<Object>() {
                    @Override
                    public void getResponse(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {


                    }
                });
            }
        });

        btnMainPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentUtils.startMainActivity(TestActivity.this);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentUtils.startLoginActivity(TestActivity.this,LoginActivity.TYPE_PHONE_VERIFY_CODE,10);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                IntentUtils.startRegisterActivity(TestActivity.this,11);
            }
        });

        btnDataCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentUtils.startDataCheckActivity(TestActivity.this,0x10);
            }
        });

        btnWebView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentUtils.startWebViewActivity(TestActivity.this,"http://www.qq.com");
            }
        });
    }

    @Override
    protected void initExtras(Bundle bundle) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0,0);
    }
}
