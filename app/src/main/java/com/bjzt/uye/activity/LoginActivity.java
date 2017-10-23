package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspLoginPhoneEntity;
import com.bjzt.uye.http.rsp.RspLoginPwdEntity;
import com.bjzt.uye.http.rsp.RspPhoneVerifyEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.listener.ITextListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.ExtendEditText;
import com.bjzt.uye.views.component.TimerDownTextView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.controller.LoginController;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 登录页卡
 * Created by billy on 2017/10/15.
 */
public class LoginActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.login_header)
    YHeaderView mHeader;
    @BindView(R.id.login_tel)
    ExtendEditText editTel;
    @BindView(R.id.txt_timerdown)
    TimerDownTextView mTxtTimerDown;
    @BindView(R.id.login_pwd)
    ExtendEditText editPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.txt_bottom_tips)
    TextView mTxtBottom;

    public static final int TYPE_PHONE_VERIFY_CODE = 1;//手机验证码登录
    public static final int TYPE_PWD = 2;              //密码登录
    private int mType;
    private List<Integer> mReqList = new ArrayList<Integer>();

    private final int REQ_CODE_PWD = 0x100;
    private final int REQ_CODE_REG = 0x101;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login_layout;
    }

    protected void initExtras(Bundle bundle){
        Intent intent = getIntent();
        mType = intent.getIntExtra(IntentUtils.PARA_KEY_PUBLIC,TYPE_PHONE_VERIFY_CODE);
    }

    @Override
    protected void initLayout(Bundle bundle){
        mHeader.updateType(YHeaderView.TYPE_RIGHT_TXT);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                IntentUtils.startRegisterActivity(LoginActivity.this,REQ_CODE_REG);
            }
        });
        String title = getResources().getString(R.string.login_header_title);
        mHeader.setTitle(title);
        String txtRight = getResources().getString(R.string.reg_header_title);
        mHeader.setRightTxt(txtRight);

        String hint = getResources().getString(R.string.login_hint_tel);
        editTel.setHint(hint);
        editTel.setTxtChangeListener(mTxtChangeListener);
        editTel.setInputType(InputType.TYPE_CLASS_PHONE);
        editTel.setMaxLength(11);

        editPwd.setTxtChangeListener(mTxtChangeListener);
        btnLogin.setOnClickListener(this);

        mTxtBottom.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mType == TYPE_PWD){
                    finish();
                }else{
                    IntentUtils.startLoginActivity(LoginActivity.this,LoginActivity.TYPE_PWD,REQ_CODE_PWD);
                }
            }
        });

        refresh();
        reSetBtnStatus();
    }

    private ITextListener mTxtChangeListener = new ITextListener() {
        @Override
        public void onTxtState(boolean isEmpty) {
            reSetBtnStatus();
        }
    };

    private void reSetBtnStatus(){
        String phone = editTel.getText();
        String pwd = editPwd.getText();
        if(!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd)){
            btnLogin.setEnabled(true);
        }else{
            btnLogin.setEnabled(false);
        }
    }

    private void refresh(){
        String hint = "";
        String tipsBottom = "";
        if(mType == TYPE_PWD){
            editPwd.updateType(ExtendEditText.TYPE_PWD);
            hint = getResources().getString(R.string.login_hint_pwd);
            mTxtTimerDown.setVisibility(View.GONE);
            editPwd.setMaxLength(20);
            tipsBottom = getResources().getString(R.string.login_tips_bottom_phone_verify);
        }else if(mType == TYPE_PHONE_VERIFY_CODE){
            editPwd.setInputType(InputType.TYPE_CLASS_NUMBER);
            editPwd.setMaxLength(8);
            hint = getResources().getString(R.string.login_hint_sms_verify_code);
            mTxtTimerDown.setVisibility(View.VISIBLE);
            mTxtTimerDown.setIBtnListener(new IItemListener() {
                @Override
                public void onItemClick(Object obj, int tag) {
                    String strTel = editTel.getText();
                    if(TextUtils.isEmpty(strTel)){
                        String tips = getResources().getString(R.string.login_tips_input_tel);
                        showToast(tips);
                        return;
                    }
                    if(!StrUtil.isPhotoLegal(strTel)){
                        String tips = getResources().getString(R.string.login_tips_input_tel_legal);
                        showToast(tips);
                        return;
                    }
                    mTxtTimerDown.startTimer();
                    showLoading();
                    int seqNo = ProtocalManager.getInstance().reqPhoneVerify(strTel,getCallBack());
                    mReqList.add(seqNo);
                }
            });
            tipsBottom = getResources().getString(R.string.login_tips_bottom_phone_pwd);
        }
        if(!TextUtils.isEmpty(hint)){
            editPwd.setHint(hint);
        }
        mTxtBottom.setText(tipsBottom);
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspPhoneVerifyEntity){
                RspPhoneVerifyEntity rspEntity = (RspPhoneVerifyEntity) rsp;
                if(isSucc){

                }else{
                    mTxtTimerDown.stopTimer();
                    String tips = StrUtil.getErrorTipsByCode(errorCode);
                    if(!TextUtils.isEmpty(tips)){
                        tips = rspEntity.msg;
                    }
                    showToast(tips);
                }
            }else if(rsp instanceof RspLoginPhoneEntity){
                RspLoginPhoneEntity rspEntity = (RspLoginPhoneEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        LoginController.getInstance().loginSucc(rspEntity.mEntity);
                        String tips = getResources().getString(R.string.login_tips_succ);
                        showToast(tips);
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode);
                    if(!TextUtils.isEmpty(rspEntity.msg)){
                        tips = rspEntity.msg;
                    }
                    showToast(tips);
                }
            }else if(rsp instanceof RspLoginPwdEntity){
                RspLoginPwdEntity rspEntity = (RspLoginPwdEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        LoginController.getInstance().loginSucc(rspEntity.mEntity);
                        String tips = getResources().getString(R.string.login_tips_succ);
                        showToast(tips);
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode);
                    if(!TextUtils.isEmpty(rspEntity.msg)){
                        tips = rspEntity.msg;
                    }
                    showToast(tips);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case REQ_CODE_PWD:
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
                case REQ_CODE_REG:
                    String phone = data.getStringExtra(IntentUtils.KEY_PHONE);
                    if(!TextUtils.isEmpty(phone)){
                        editTel.setText(phone);
                    }
                    mType = TYPE_PWD;
                    refresh();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view == this.btnLogin){
            String phone = editTel.getText();
            if(TextUtils.isEmpty(phone)){
                String tips = getResources().getString(R.string.login_tips_input_tel);
                showToast(tips);
                return;
            }
            if(!StrUtil.isPhotoLegal(phone)){
                String tips = getResources().getString(R.string.login_tips_input_tel_legal);
                showToast(tips);
                return;
            }
            if(mType == TYPE_PHONE_VERIFY_CODE){
                String code = editPwd.getText();
                if(TextUtils.isEmpty(code)){
                    String tips = getResources().getString(R.string.login_tips_input_verify_code);
                    showToast(tips);
                    return;
                }
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqLoginPhone(phone,code,getCallBack());
                mReqList.add(seqNo);
            }else if(mType == TYPE_PWD){
                String pwd = editPwd.getText();
                if(TextUtils.isEmpty(pwd)){
                    String tips = getResources().getString(R.string.login_tips_input_pwd);
                    showToast(tips);
                    return;
                }
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqLoginPwd(phone,pwd,getCallBack());
                mReqList.add(seqNo);
            }
        }
    }
}
