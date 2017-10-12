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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);
        ButterKnife.bind(this);

        initLayout();
    }

    private void initLayout(){
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
