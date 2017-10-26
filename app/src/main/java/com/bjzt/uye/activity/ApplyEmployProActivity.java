package com.bjzt.uye.activity;

import android.content.Intent;
import android.os.Bundle;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.adapter.AdapterEmployPro;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.msglist.MsgPage;
import butterknife.BindView;

/**
 * 就业进展
 * Created by billy on 2017/10/25.
 */
public class ApplyEmployProActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.msgpage)
    MsgPage mMsgPage;
    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private String orgId;
    private AdapterEmployPro mAdapter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_employ_progress_layout;
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
                super.onRightClick();
            }
        });
        String title = getResources().getString(R.string.employ_progress_title);
        mHeader.setTitle(title);
        String txtRight = getResources().getString(R.string.employ_progress_add);
        mHeader.setRightTxt(txtRight);

    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.orgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }
}
