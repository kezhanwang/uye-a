package com.bjzt.uye.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspPhoneVerifyEntity;
import com.bjzt.uye.http.rsp.RspRegEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.listener.ITextListener;
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

    private List<Integer> mReqList = new ArrayList<Integer>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_register_layout;
    }

    @Override
    protected void initLayout() {
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
                if(!StrUtil.isLegal(strPhone)){
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

        btnReg.setOnClickListener(this);
    }

    private ITextListener mTxtChangeListener = new ITextListener() {
        @Override
        public void onTxtState(boolean isEmpty) {

        }
    };

    @Override
    protected void initExtras(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {
        if(view == btnReg){
            String phone = editPhone.getText();
            String code = editVerify.getText();
            String pwd = editPwd.getText();
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
