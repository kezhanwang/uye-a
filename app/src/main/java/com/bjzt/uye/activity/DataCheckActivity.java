package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.activity.dialog.DialogConfirmSingle;
import com.bjzt.uye.entity.PUInfoEntity;
import com.bjzt.uye.entity.PhoneUserEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.req.ReqUploadPhoneListEntity;
import com.bjzt.uye.http.rsp.RspUInfoEntity;
import com.bjzt.uye.http.rsp.RspUploadPhoneListEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.PhoneUtil;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.DataCheckItemView;
import com.bjzt.uye.views.component.YHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 个人资料页卡
 * Created by billy on 2017/10/16.
 */
public class DataCheckActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;

    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.datacheck_item_identity)
    DataCheckItemView itemIDentity;
    @BindView(R.id.datacheck_item_phonecontact)
    DataCheckItemView itemPhoneContact;
    @BindView(R.id.cash_datacheck_item_contact)
    DataCheckItemView itemContact;
    @BindView(R.id.cash_datacheck_item_experience)
    DataCheckItemView itemExperience;
    @BindView(R.id.cash_datacheck_item_sesame)
    DataCheckItemView itemSesame;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.emptyveiw)
    BlankEmptyView mEmptyView;

    private List<Integer> mReqList = new ArrayList<Integer>();
    private final int FLAG_SHOW_LOADING = 0x10;
    private final int FALG_HIDE_LOADING = 0x11;
    private final int FLAG_CONTACTLIST_FAILURE = 0x12;

    private DialogConfirmSingle mDialogCfgSingle;

    private final int REQ_IDENTITY = 10;
    private final int REQ_ORDERINFO = 11;
    private final int REQ_CONTACT_INFO = 12;
    private final int REQ_MYEXPERIENCE = 13;

    private String orgId;
    private PUInfoEntity mEntity;
    private final String KEY_UPLOAD_CONTACT = "key_contact";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_datacheck_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        String title = getResources().getString(R.string.cash_data_title);
        mHeader.setTitle(title);

        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        itemIDentity.updateType(DataCheckItemView.TYPE_IDDENTITY);
        itemIDentity.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                IntentUtils.startApplyIDActivity(DataCheckActivity.this,REQ_IDENTITY);
            }
        });
        //通讯录
        itemPhoneContact.updateType(DataCheckItemView.TYPE_CONTACT_LIST);
        itemPhoneContact.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                Global.postDelay(rUploadPhone);
            }
        });
        //联系方式
        itemContact.updateType(DataCheckItemView.TYPE_CONTACT);
        itemContact.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                IntentUtils.startContactInActivity(DataCheckActivity.this,orgId,REQ_CONTACT_INFO);
            }
        });

        //个人经历
        itemExperience.updateType(DataCheckItemView.TYPE_EXPERIENCE);
        itemExperience.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                IntentUtils.startMyExperienceBaseActivity(DataCheckActivity.this,orgId,REQ_MYEXPERIENCE);
            }
        });

        //芝麻信用
        itemSesame.updateType(DataCheckItemView.TYPE_SESAME);
        itemSesame.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });
        itemSesame.updateTailContent(true);
        itemSesame.setVisibility(View.GONE);

        btnOk.setOnClickListener(this);

        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();
        mScrollView.setVisibility(View.GONE);
        int seqNo = ProtocalManager.getInstance().reqUInfoDataCheck(getCallBack());
        mReqList.add(seqNo);

        if(bundle != null){
            if(bundle.containsKey(KEY_UPLOAD_CONTACT)){
                boolean isPhoneContactIDentity = bundle.getBoolean(KEY_UPLOAD_CONTACT);
                itemPhoneContact.updateTailContent(isPhoneContactIDentity);
            }
        }
    }

    private Runnable rUploadPhone = new Runnable() {
        @Override
        public void run() {
            Message msg = Message.obtain();
            msg.what = FLAG_SHOW_LOADING;
            String tips = getResources().getString(R.string.contactlist_requesting);
            msg.obj = tips;
            sendMsg(msg);

            List<PhoneUserEntity> mList = PhoneUtil.listAllPhoneUserV2(DataCheckActivity.this);
            if(mList == null || mList.size() <= 0){
                //无法获取通讯录提示
                msg = Message.obtain();
                msg.what = FLAG_CONTACTLIST_FAILURE;
                sendMsg(msg);

                //hide loading dialog
                msg = Message.obtain();
                msg.what = FALG_HIDE_LOADING;
                sendMsg(msg);
            }else{
                msg = Message.obtain();
                msg.what = FLAG_SHOW_LOADING;
                tips = getResources().getString(R.string.common_request);
                msg.obj = tips;
                String str = ReqUploadPhoneListEntity.parseContactData(mList);
                int seqNo = ProtocalManager.getInstance().reqUploadPhoneList(str,getCallBack());
                mReqList.add(seqNo);
            }
        }
    };

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        int what = msg.what;
        switch(what){
            case FLAG_SHOW_LOADING:
                String tips = (String) msg.obj;
                if(!TextUtils.isEmpty(tips)){
                    showLoading(tips);
                }
                break;
            case FALG_HIDE_LOADING:
                hideLoadingDialog();
                break;
            case FLAG_CONTACTLIST_FAILURE:
                showDialogSingle();
                break;
        }
    }

    private void showDialogSingle(){
        hideDialogSingle();
        this.mDialogCfgSingle = new DialogConfirmSingle(this,R.style.MyDialogBg);
        this.mDialogCfgSingle.show();
        this.mDialogCfgSingle.updateType(DialogConfirmSingle.TYPE_CONTACTLIST_CFG);
        this.mDialogCfgSingle.setIBtnListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                hideDialogSingle();
            }
        });
    }

    private void hideDialogSingle(){
        if(this.mDialogCfgSingle != null){
            this.mDialogCfgSingle.dismiss();
            this.mDialogCfgSingle = null;
        }
    }

    private void initErrorStatus(String tips){
        mEmptyView.showErrorState();
        mEmptyView.setErrorTips(tips);
        mEmptyView.setBlankListener(new BlankEmptyView.BlankBtnListener() {
            @Override
            public void btnRefresh() {
                mEmptyView.showLoadingState();
                refresh();
            }
        });
    }

    private void refreshUI(){
        if(this.mEntity != null){
            //身份认证
            itemIDentity.updateTailContent(this.mEntity.identity);
            //联系信息
            itemContact.updateTailContent(this.mEntity.contact);
            //个人经历
            itemExperience.updateTailContent(this.mEntity.experience);
        }else{
            itemIDentity.updateTailContent(false);
            itemContact.updateTailContent(false);
            itemExperience.updateTailContent(false);
        }
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof  RspUInfoEntity){
                RspUInfoEntity rspEntity = (RspUInfoEntity) rsp;
                if(rspEntity != null){
                    if(rspEntity.mEntity != null){
                        mEmptyView.loadSucc();
                        mScrollView.setVisibility(View.VISIBLE);
                        mEntity = rspEntity.mEntity;
                        refreshUI();
                    }else{
                        String tips = getResources().getString(R.string.common_request_error);
                        initErrorStatus(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    initErrorStatus(tips);
                }
            }else if(rsp instanceof RspUploadPhoneListEntity){
                RspUploadPhoneListEntity rspEntity = (RspUploadPhoneListEntity) rsp;
                if(isSucc){
                    itemPhoneContact.updateTailContent(true);
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                }
            }
        }
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.orgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogSingle();
        this.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    @Override
    public void onClick(View view) {
        if(view == this.btnOk){
            if(mEntity != null){
                boolean isIDIDentity = mEntity.identity;
                boolean isContactListIDentity = itemPhoneContact.isIDentity();
                boolean isContact = mEntity.contact;
                boolean isExperience = mEntity.experience;
                if(!isIDIDentity){
                    String tips = getResources().getString(R.string.data_check_tips_identity_id);
                    showToast(tips);
                    return;
                }
                if(!isContactListIDentity){
                    String tips = getResources().getString(R.string.data_check_tips_identity_contacts_upload);
                    showToast(tips);
                    return;
                }
                if(!isContact){
                    String tips = getResources().getString(R.string.data_check_tips_identity_contacts_info);
                    showToast(tips);
                    return;
                }
                if(!isExperience){
                    String tips = getResources().getString(R.string.data_check_tips_identity_experience_info);
                    showToast(tips);
                    return;
                }
                IntentUtils.startOrderInfoActivity(DataCheckActivity.this,orgId,REQ_ORDERINFO);
            }else{
                String tips = getResources().getString(R.string.common_cfg_empty);
                showToast(tips);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case REQ_IDENTITY:
                case REQ_CONTACT_INFO:
                case REQ_MYEXPERIENCE:
                    refresh();
                    break;
                case REQ_ORDERINFO:
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    private void refresh(){
        showLoading();
        int seqNo = ProtocalManager.getInstance().reqUInfoDataCheck(getCallBack());
        mReqList.add(seqNo);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_UPLOAD_CONTACT,itemPhoneContact.isIDentity());
    }
}
