package com.bjzt.uye.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
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

    @BindView(R.id.btn_faceverify)
    Button btnVerify;

    @BindView(R.id.btn_ud_sdk)
    Button btnUDSDK;

    @BindView(R.id.btn_logout)
    Button btnLogout;

    @BindView(R.id.btn_search)
    Button btnSearch;

    @BindView(R.id.btn_orderinfo)
    Button btnOrderInfo;

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
//                ProtocalManager.getInstance().reqLocCity(new ICallBack<Object>() {
//                    @Override
//                    public void getResponse(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
//
//
//                    }
//                });
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

        btnVerify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentUtils.startApplyIDActivity(TestActivity.this,0x10);
            }
        });

        btnUDSDK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                商户号：201710139094511687
//                密码：5G3qaDDa
//                合同编号：KZ1WY1610021
//                security_key（secretkey）：c46d326d-dd79-44f1-82b4-41d71299df48
//                pub_key（pubkey、Apikey、authKey）：50e00acb-4146-4084-94ac-bc48472d5b1f
                String sKey = "50e00acb-4146-4084-94ac-bc48472d5b1f";
                String orderId = "201710139094511687";

                AuthBuilder builder = new AuthBuilder(orderId,sKey, "http://www.baidu.com", new OnResultListener() {
                    @Override
                    public void onResult(String s) {

                    }
                });
                builder.faceAuth(TestActivity.this);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ProtocalManager.getInstance().reqLogout(getCallBack());
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentUtils.startSearchActivity(TestActivity.this);
            }
        });

        btnOrderInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentUtils.startOrderInfoActivity(TestActivity.this,"",10);
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
