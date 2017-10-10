package com.bjzt.uye.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.controller.LBSController;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/10.
 */
public class TestActivity extends BaseActivity{

    @BindView(R.id.btn_loc)
    Button btnLoc;

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
    }
}
