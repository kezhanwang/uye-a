package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspPhoneVerifyEntity;
import com.bjzt.uye.http.rsp.RspRegEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.listener.ITextListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.ExtendEditText;
import com.bjzt.uye.views.component.TimerDownTextView;
import com.bjzt.uye.views.component.YHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 注册相关
 * Created by billy on 2017/10/15.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.reg_header)
    YHeaderView mHeader;
    @BindView(R.id.reg_tel)
    ExtendEditText editPhone;
    @BindView(R.id.reg_txt_timerdown)
    TimerDownTextView mTimerDown;
    @BindView(R.id.reg_verify)
    ExtendEditText editVerify;
    @BindView(R.id.reg_pwd)
    ExtendEditText editPwd;
    @BindView(R.id.reg_register)
    Button btnReg;
    @BindView(R.id.txt_bottom_tips)
    TextView mTxtProtocal;

    private List<Integer> mReqList = new ArrayList<Integer>();
    private String phone;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        String title = getResources().getString(R.string.reg_header_title);
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        mHeader.setTitle(title);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
        //init phone
        String hint = getResources().getString(R.string.reg_hint_tel);
        editPhone.setHint(hint);
        editPhone.setTxtChangeListener(mTxtChangeListener);

        //init timer down
        mTimerDown.setIBtnListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                String strPhone = editPhone.getText();
                if(TextUtils.isEmpty(strPhone)){
                    String tips = getResources().getString(R.string.login_tips_input_tel);
                    showToast(tips);
                    return;
                }
                if(!StrUtil.isPhotoLegal(strPhone)){
                    String tips = getResources().getString(R.string.login_tips_input_tel_legal);
                    showToast(tips);
                    return;
                }
                mTimerDown.startTimer();
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqPhoneVerify(strPhone,getCallBack());
                mReqList.add(seqNo);
            }
        });
        //init verify
        hint = getResources().getString(R.string.login_hint_sms_verify_code);
        editVerify.setHint(hint);
        editVerify.setTxtChangeListener(mTxtChangeListener);

        //init
        hint = getResources().getString(R.string.reg_hint_pwd);
        editPwd.setHint(hint);
        editPwd.setTxtChangeListener(mTxtChangeListener);
        editPwd.updateType(ExtendEditText.TYPE_PWD);

        btnReg.setOnClickListener(this);
        btnReg.setEnabled(false);

        mTxtProtocal.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTxtProtocal.getPaint().setAntiAlias(true);//抗锯齿
        mTxtProtocal.setOnClickListener(this);
    }

    private ITextListener mTxtChangeListener = new ITextListener() {
        @Override
        public void onTxtState(boolean isEmpty) {
            reSetBtnStatus();
        }
    };

    private void reSetBtnStatus(){
        String strTel = editPhone.getText();
        String strVerify = editVerify.getText();
        String strPwd = editPwd.getText();
        if(!TextUtils.isEmpty(strTel) && !TextUtils.isEmpty(strVerify) && !TextUtils.isEmpty(strPwd)){
            btnReg.setEnabled(true);
        }else{
            btnReg.setEnabled(false);
        }
    }

    @Override
    protected void initExtras(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {
        if(view == this.mTxtProtocal){
            IntentUtils.startWebViewActivity(this, HttpCommon.getRegProtocal());
        }else if(view == btnReg){
            String phone = editPhone.getText();
            String code = editVerify.getText();
            String pwd = editPwd.getText();
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
            if(TextUtils.isEmpty(code)){
                String tips = getResources().getString(R.string.reg_tips_please_input_verify_code);
                showToast(tips);
                return;
            }
            if(TextUtils.isEmpty(pwd)){
                String tips = getResources().getString(R.string.reg_tips_input_pwd);
                showToast(tips);
                return;
            }
            this.phone = phone;
            showLoading();
            int seqNo = ProtocalManager.getInstance().reqReg(phone,code,pwd,getCallBack());
            mReqList.add(seqNo);
        }
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof  RspPhoneVerifyEntity){
                RspPhoneVerifyEntity rspEntity = (RspPhoneVerifyEntity) rsp;
                if(isSucc && rspEntity != null){

                }else{
                    mTimerDown.stopTimer();
                    String tips = StrUtil.getErrorTipsByCode(errorCode);
                    if(!TextUtils.isEmpty(tips)){
                        tips = rspEntity.msg;
                    }
                    showToast(tips);
                }
            }else if(rsp instanceof RspRegEntity){
                RspRegEntity rspEntity = (RspRegEntity) rsp;
                if(isSucc && rspEntity != null){
                    Intent intent = new Intent();
                    intent.putExtra(IntentUtils.KEY_PHONE,this.phone);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode);
                    if(!TextUtils.isEmpty(tips)){
                        tips = rspEntity.msg;
                    }
                    showToast(tips);
                }
            }
        }
    }
}
