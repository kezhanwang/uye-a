package com.bjzt.uye.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.controller.LBSController;
import com.bjzt.uye.entity.VPicFileEntity;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.views.component.PicSelectView;
import com.common.common.MyLog;
import com.common.util.DeviceUtil;

import java.util.ArrayList;

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

    @BindView(R.id.btn_qa)
    Button btnQA;

    @BindView(R.id.btn_orderlist)
    Button btnOrderList;

    @BindView(R.id.btn_employ_pross)
    Button btnEmployPro;

    @BindView(R.id.btn_employ_pross_add)
    Button btnEmployProAdd;

    @BindView(R.id.btn_contactinfo)
    Button btnContactInfo;

    @BindView(R.id.btn_org_detail)
    Button btnOrgDetail;

    @BindView(R.id.btn_experi_base)
    Button btnExperiBase;

    @BindView(R.id.btn_experi_occ)
    Button btnExperiOcc;

    @BindView(R.id.btn_experi_add)
    Button btnExperiOccAdd;

    @BindView(R.id.btn_degreelist)
    Button btnDegreeList;

    @BindView(R.id.btn_degree_add)
    Button btnDegreeAdd;

    @BindView(R.id.btn_touch)
    Button btnTouch;

    @BindView(R.id.btn_profile)
    Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String strMac = DeviceUtil.getMacAdd();
        MyLog.d(TAG,"[onCreate]" + " strMac:" + strMac);
        //稍后献上，敬请期待  开发中~
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_layout;
    }

    protected void initLayout(Bundle bundle){
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
                IntentUtils.startDataCheckActivity(TestActivity.this,"",0x10);
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
                IntentUtils.startSearchActivity(TestActivity.this,22);
            }
        });

        btnOrderInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                IntentUtils.startOrderInfoActivity(TestActivity.this,MConfiger.TEST_ORG_ID,10);
            }
        });

        btnQA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startQAActivity(TestActivity.this,"10049",12);
            }
        });

        btnOrderList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int seqNo = ProtocalManager.getInstance().reqOrderList(1,getCallBack());
            }
        });


        btnEmployPro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startEmployProActivity(TestActivity.this,"",12);
            }
        });

        btnEmployProAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startEmployProAddActivity(TestActivity.this,"",false,15);
            }
        });

        btnContactInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startContactInActivity(TestActivity.this,"",16);
            }
        });

        btnOrgDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startOrgDetailActivity(TestActivity.this,"10049",12);
            }
        });

        btnExperiBase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startMyExperienceBaseActivity(TestActivity.this,"",10);
            }
        });

        btnExperiOcc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startMyExperienceOccActivity(TestActivity.this,"",10,true,null);
            }
        });

        btnExperiOccAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startMyExperienceOccAddActivity(TestActivity.this,"",11);
            }
        });

        btnDegreeList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startMyExperienceDegreeActivity(TestActivity.this,"",11);
            }
        });

        btnDegreeAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startMyExperienceDegreeAddActivity(TestActivity.this,"",11);
            }
        });


        btnTouch.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MyLog.d(MyLog.BILLY,"[onTouch]" + " button touch listener");
                return false;
            }
        });

        btnTouch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MyLog.d(MyLog.BILLY,"[onClick]" + " button click...");
                showLoading();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                IntentUtils.startProfileActivity(TestActivity.this,16);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MyLog.d(MyLog.BILLY,"[dispatchTouchEvent]" + " TestActivity");
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MyLog.d(MyLog.BILLY,"[onTouchEvent]" + " TestActivity");
        return super.onTouchEvent(event);
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
