package com.bjzt.uye.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.views.component.ExtendEditText;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.controller.LoginController;

import butterknife.BindView;

/**
 * Created by billy on 2017/11/3.
 */
public class ModifyNickActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.edit_nick)
    ExtendEditText mEditNick;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_modify_nick_layout;
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
                String strNick = mEditNick.getText();
                if(TextUtils.isEmpty(strNick)){
                    String tips = getResources().getString(R.string.modify_nick_hint);
                    showToast(tips);
                    return;
                }
                String tips = getResources().getString(R.string.dev_ing);
                showToast(tips);
            }
        });
        String strTxtRight = getResources().getString(R.string.complete);
        mHeader.setRightTxt(strTxtRight);

        String title = getResources().getString(R.string.modify_nick_title);
        mHeader.setTitle(title);
        String hint = getResources().getString(R.string.modify_nick_hint);
        mEditNick.setHint(hint);
        String nickName = LoginController.getInstance().getNickName();
        if(!TextUtils.isEmpty(nickName)){
            mEditNick.setText(nickName);
        }
    }

    @Override
    protected void initExtras(Bundle bundle) {

    }
}
