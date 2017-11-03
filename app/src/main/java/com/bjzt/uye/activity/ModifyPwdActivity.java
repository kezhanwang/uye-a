package com.bjzt.uye.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.views.component.ExtendEditText;
import com.bjzt.uye.views.component.YHeaderView;
import butterknife.BindView;

/**
 * Created by billy on 2017/11/3.
 */
public class ModifyPwdActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.edit_pwd)
    ExtendEditText mEditPwd;
    @BindView(R.id.edit_pwd_again)
    ExtendEditText mEditPwdAgain;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_modify_pwd_layout;
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
                String pwd = mEditPwd.getText();
                String pwdAgain = mEditPwdAgain.getText();
                if(TextUtils.isEmpty(pwd)){
                    String tips = getResources().getString(R.string.modify_pwd_tips_input_pwd);
                    showToast(tips);
                    return;
                }
                if(TextUtils.isEmpty(pwdAgain)){
                    String tips = getResources().getString(R.string.modify_pwd_tips_input_pwd_again);
                    showToast(tips);
                    return;
                }
                if(!pwd.equals(pwdAgain)){
                    String tips = getResources().getString(R.string.modify_pwd_tips_input_pwd_no_same);
                    showToast(tips);
                    return;
                }
                String tips = "开发中...";
                showToast(tips);
            }
        });
        String title = getResources().getString(R.string.modify_pwd_title);
        mHeader.setTitle(title);
        String strTxtRight = getResources().getString(R.string.complete);
        mHeader.setRightTxt(strTxtRight);

        String hint = getResources().getString(R.string.modify_pwd_hint_input_pwd);
        mEditPwd.setHint(hint);
        mEditPwd.updateType(ExtendEditText.TYPE_PWD);

        hint = getResources().getString(R.string.modify_pwd_hint_confirm);
        mEditPwdAgain.setHint(hint);
        mEditPwdAgain.updateType(ExtendEditText.TYPE_PWD);
    }

    @Override
    protected void initExtras(Bundle bundle) {

    }
}
