package com.bjzt.uye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.YHeaderView;
import butterknife.BindView;

/**
 * Created by billy on 2017/10/30.
 * 个人经历-职业列表
 */
public class ApplyMyExperienceOccDegreeActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private int mType;
    private String orgId;
    public static final int TYPE_OCC = 1;       //职业
    public static final int TYPE_DEGREE = 2;    //学历

    private final int REQ_ADD_OCC = 1;
    private final int REQ_ADD_DEGREE = 2;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_myexperience_occ_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader.updateType(YHeaderView.TYPE_RIGHT_TXT);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                switch(mType) {
                    case TYPE_DEGREE:
//                        IntentUtils.startMyExperienceOccAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_ADD_DEGREE);
                        break;
                    case TYPE_OCC:
                        IntentUtils.startMyExperienceOccAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_ADD_OCC);
                        break;
                }
            }
        });
        String strTxtRight = "";
        String title = "";
        switch(this.mType){
            case TYPE_OCC:
                title = "个人经历-职业";
                strTxtRight = "添加职业";
                break;
            case TYPE_DEGREE:
                title = "个人经历-学历";
                strTxtRight = "添加学历";
                break;
        }
        mHeader.setTitle(title);
        mHeader.setRightTxt(strTxtRight);
    }


    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.orgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
        this.mType = intent.getIntExtra(IntentUtils.PARA_KEY_TYPE,TYPE_OCC);
    }
}
