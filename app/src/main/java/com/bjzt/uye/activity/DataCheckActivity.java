package com.bjzt.uye.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.activity.dialog.DialogConfirmSingle;
import com.bjzt.uye.entity.PhoneUserEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.req.ReqUploadPhoneListEntity;
import com.bjzt.uye.http.rsp.RspUInfoEntity;
import com.bjzt.uye.http.rsp.RspUploadPhoneListEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.PhoneUtil;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.DataCheckItemView;
import com.bjzt.uye.views.component.YHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 个人资料页卡
 * Created by billy on 2017/10/16.
 */
public class DataCheckActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;

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

    private List<Integer> mReqList = new ArrayList<Integer>();
    private final int FLAG_SHOW_LOADING = 0x10;
    private final int FALG_HIDE_LOADING = 0x11;
    private final int FLAG_CONTACTLIST_FAILURE = 0x12;

    private DialogConfirmSingle mDialogCfgSingle;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_datacheck_layout;
    }

    @Override
    protected void initLayout() {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        String title = getResources().getString(R.string.cash_data_title);
        mHeader.setTitle(title);

        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
        itemIDentity.updateType(DataCheckItemView.TYPE_IDDENTITY);
        itemIDentity.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

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

            }
        });
        itemContact.updateTailContent(true);

        //个人经历
        itemExperience.updateType(DataCheckItemView.TYPE_EXPERIENCE);
        itemExperience.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });
        itemExperience.updateTailContent(true);

        //芝麻信用
        itemSesame.updateType(DataCheckItemView.TYPE_SESAME);
        itemSesame.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });
        itemSesame.updateTailContent(true);

        int seqNo = ProtocalManager.getInstance().reqUInfoDataCheck(getCallBack());
        mReqList.add(seqNo);
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

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof  RspUInfoEntity){
                RspUInfoEntity rspEntity = (RspUInfoEntity) rsp;
                if(isSucc){

                }else{

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogSingle();
    }
}
