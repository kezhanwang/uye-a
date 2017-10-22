package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.activity.dialog.DialogBankList;
import com.bjzt.uye.activity.dialog.DialogConfirmSingle;
import com.bjzt.uye.activity.dialog.DialogDateSelector;
import com.bjzt.uye.activity.dialog.DialogPicSelect;
import com.bjzt.uye.controller.AuthFaceController;
import com.bjzt.uye.controller.UploadPicController;
import com.bjzt.uye.entity.PAuthFaceResultEntity;
import com.bjzt.uye.entity.PBankEntity;
import com.bjzt.uye.entity.PFaceVerifyEntity;
import com.bjzt.uye.entity.PIDentityPicEntity;
import com.bjzt.uye.entity.PicEntity;
import com.bjzt.uye.entity.PicResultEntity;
import com.bjzt.uye.entity.VDateEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.listener.IUploadListener;
import com.bjzt.uye.http.rsp.RspFaceVerifyCfgEntity;
import com.bjzt.uye.http.rsp.RspIDentityCfgEntity;
import com.bjzt.uye.http.rsp.RspIDentityPicEntity;
import com.bjzt.uye.http.rsp.RspPhoneVerifyEntity;
import com.bjzt.uye.http.rsp.RspSubmitIDentityEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.FileUtil;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.LoanDateUtil;
import com.bjzt.uye.util.PicUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BindCardPhotoView;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.common.MyLog;
import com.common.common.NetCommon;
import com.common.http.HttpEngine;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 身份认证信息
 * Created by billy on 2017/10/16
 */
public class ApplyIDActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.apply_id_header)
    YHeaderView mHeader;
    @BindView(R.id.apply_id_emptyview)
    BlankEmptyView mEmptyView;
    @BindView(R.id.item_name)
    ItemView itemName;
    @BindView(R.id.item_id)
    ItemView itemID;
    @BindView(R.id.item_start)
    ItemView itemStart;
    @BindView(R.id.item_end)
    ItemView itemEnd;
    @BindView(R.id.item_addr)
    ItemView itemAddr;
    @BindView(R.id.photoview_front)
    BindCardPhotoView photoViewFront;
    @BindView(R.id.photoview_back)
    BindCardPhotoView photoViewBack;

    @BindView(R.id.item_bankcard)
    ItemView itemBankCard;
    @BindView(R.id.item_bank)
    ItemView itemBank;
    @BindView(R.id.item_phone)
    ItemView itemPhone;
    @BindView(R.id.item_verify)
    ItemView itemVerify;
    @BindView(R.id.btn_ok)
    Button btnOk;

    public static final int REQ_CODE_ID = DialogPicSelect.TYPE_ID_CARD;
    public static final int REQ_CODE_ID_BACK = DialogPicSelect.TYPE_ID_CARD_BACK;

    private List<Integer> mReqList = new ArrayList<Integer>();
    private DialogPicSelect mDialogPicSelect;
    private String mCammerImgPath;
    private DialogConfirmSingle mDialogCfg;
    private DialogBankList mDialogBankList;
    private DialogDateSelector mDialogDate;
    private int cnt = 1;
    private int MAX_CNT = 10;
    private int DELAY  = 1000 * 2;

    private final int FLAG_AUTH = 0x10;
    private final int FLAG_VERIFY_ERROR = 0x11;
    private final int FLAG_SET_LOADING = 0x12;
    private final int FLAG_HIDE_LOADING = 0x13;
    private final int FLAG_SHOW_TOAST = 0x14;
    private final int FLAG_FILL_BITMAP = 0x15;
    private final int FLAG_PIC_INFO = 0x16;

    private final int SRC_ID_START = 1;
    private final int SRC_ID_END = 2;
    private final int SRC_BIRTH = 3;

    private RspIDentityCfgEntity mRspEntity;
    private PBankEntity mBankEntitySelect;
    private PFaceVerifyEntity pFaceVerifyEntity;
    private PIDentityPicEntity pFacePicEntity;
    private String orgId;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_apply_id_layout;
    }

    @Override
    protected void initLayout() {
        mHeader.updateType(YHeaderView.TYPE_RIGHT_TXT);
        String title = getString(R.string.apply_id_title);
        mHeader.setTitle(title);
        String txtRight = getString(R.string.apply_id_start_rec);
        mHeader.setRightTxt(txtRight);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqFaceVerifyCfg(getCallBack());
                mReqList.add(seqNo);
            }
        });

        //init name
        String hint = getString(R.string.apply_id_input_name);
        title = getString(R.string.name);
        itemName.setTitle(title);
        itemName.setHint(hint);
        itemName.hideArrow();
        title = getString(R.string.apply_id_idno);
        hint = getString(R.string.apply_id_tips_input_idno);
        itemID.setTitle(title);
        itemID.setHint(hint);
        itemID.hideArrow();
        title = getString(R.string.apply_id_date);
        hint = getString(R.string.apply_id_tips_input_start);
        itemStart.setTitle(title);
        itemStart.setHint(hint);
        itemStart.showArrow();
        itemStart.setEditAble(false);
        itemStart.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String strInfo = itemStart.getInputTxt();
                showDialogDateSelector(SRC_ID_START,strInfo);
            }
        });
        hint = getString(R.string.apply_id_tips_input_end);
        itemEnd.setHint(hint);
        itemEnd.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String strInfo = itemEnd.getInputTxt();
                showDialogDateSelector(SRC_ID_END,strInfo);
            }
        });
        itemEnd.showArrow();
        itemEnd.setEditAble(false);
        itemEnd.hideTitle();
        //addr
        title = getString(R.string.apply_id_addr);
        hint = getString(R.string.apply_id_tips_input_addr);
        itemAddr.setTitle(title);
        itemAddr.setHint(hint);
        itemAddr.hideArrow();
        itemAddr.hideBottomLine();

        photoViewFront.updateType(BindCardPhotoView.TYPE_IMG_ID);
        photoViewFront.setIItemListener(mPhotoViewListener);

        photoViewBack.updateType(BindCardPhotoView.TYPE_IMG_ID_BACK);
        photoViewBack.setIItemListener(mPhotoViewListener);

        //银行卡号
        title = getString(R.string.apply_id_bankcardno);
        hint = getString(R.string.apply_id_tips_input_bankno);
        itemBankCard.setTitle(title);
        itemBankCard.setHint(hint);
        itemBankCard.hideArrow();
        itemBankCard.setInputTypeNumber(ItemView.INPUT_TYPE_BANK);

        //开户行
        title = getString(R.string.apply_id_bank);
        hint = getString(R.string.apply_id_input_bankname);
        itemBank.setTitle(title);
        itemBank.setHint(hint);
        itemBank.showArrow();
        itemBank.setEditAble(false);
        itemBank.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mRspEntity != null && mRspEntity.mList != null && mRspEntity.mList.size() > 0){
                    showDialogBankList(mRspEntity.mList);
                }else{
                    String tips = "银行列表为空~";
                    showToast(tips);
                }
            }
        });

        title = getString(R.string.apply_id_phone);
        hint = getString(R.string.apply_id_inputphone);
        itemPhone.setTitle(title);
        itemPhone.setHint(hint);
        itemPhone.setInputTypeNumber(ItemView.INPUT_TYPE_NUMBER);
        itemPhone.setType(ItemView.TYPE_VERIFY);
        itemPhone.setBtnListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                String strTel = itemPhone.getInputTxt();
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
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqPhoneVerify(strTel,getCallBack());
                mReqList.add(seqNo);
            }
        });

        title = getString(R.string.apply_id_verifycode);
        hint = getString(R.string.apply_id_input_verifycode);
        itemVerify.setTitle(title);
        itemVerify.setHint(hint);
        itemVerify.setInputTypeNumber(ItemView.INPUT_TYPE_NUMBER);
        itemVerify.setMaxCntInput(8);

        btnOk.setOnClickListener(this);

        //设置图片
        UploadPicController.getInstance().setUploadListener(uploadListener);
//        mEmptyView.showLoadingState();
//        int seqNo = ProtocalManager.getInstance().reqFaceVerifyCfg(getCallBack());
//        mReqList.add(seqNo);
        int seqNo = ProtocalManager.getInstance().reqIDentityCfg(getCallBack());
        mReqList.add(seqNo);
    }

    private IUploadListener uploadListener = new IUploadListener() {
        @Override
        public void upload(long curLen, long maxLen, String key, int mType) {
            String str = StrUtil.getProgressStr(curLen, maxLen);
            String tips = getResources().getString(R.string.img_uploading_progress, str);
            Message msg = Message.obtain();
            msg.what = FLAG_SET_LOADING;
            msg.obj = tips;
            sendMsg(msg);
        }

        @Override
        public void uploadError(int mType, String netUrl, String tips) {
            Message msg = Message.obtain();
            msg.what = FLAG_HIDE_LOADING;
            sendMsg(msg);

            msg = Message.obtain();
            msg.what = FLAG_SHOW_TOAST;
            msg.obj = tips;
            sendMsg(msg);
        }

        @Override
        public void uploadItemSucc(int mType, String netUrl) {
            Message msg = Message.obtain();
            msg.what = FLAG_HIDE_LOADING;
            sendMsg(msg);

            //展示不同样式
            PicEntity mEntity = UploadPicController.getInstance().getItemBySuccList(mType);
            if(mEntity != null && !TextUtils.isEmpty(mEntity.path)){
                File file = new File(mEntity.path);
                if(file != null && file.exists()){
                    Bitmap bitmap = FileUtil.getThumbBitmap(mEntity.path);
                    if(bitmap != null){
                        msg = Message.obtain();
                        msg.what = FLAG_FILL_BITMAP;
                        msg.obj = bitmap;
                        msg.arg1 = mType;
                        sendMsg(msg);
                    }
                }
            }
        }
    };

    private IItemListener mPhotoViewListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            if(tag == BindCardPhotoView.TAG_TYPE_IMG_ADD){
                if(obj == photoViewFront){
                    showDialogPicSelect(REQ_CODE_ID);
                }else if(obj == photoViewBack){
                    showDialogPicSelect(REQ_CODE_ID_BACK);
                }
            }else if(tag == BindCardPhotoView.TAG_TYPE_CLOSE){
                if(obj == photoViewFront){
                    photoViewFront.updateBitmap(null,true);
                }else if(obj == photoViewBack){
                    photoViewBack.updateBitmap(null,true);
                }
            }
        }
    };

    private void showDialogPicSelect(final int reqCode){
        hideDialogPicSelect();
        this.mDialogPicSelect = new DialogPicSelect(this,R.style.MyDialogBg);
        int type = -1;
        if(reqCode == REQ_CODE_ID){
            type = DialogPicSelect.TYPE_ID_CARD;
        }else if(reqCode == REQ_CODE_ID_BACK){
            type = DialogPicSelect.TYPE_ID_CARD_BACK;
        }
        this.mDialogPicSelect.updateType(type);
        this.mDialogPicSelect.show();
        this.mDialogPicSelect.setListener(new DialogPicSelect.DialogClickListener() {
            @Override
            public void ItemTopClick() {
                String filePath = FileUtil.getCammeraImgPath();
                mCammerImgPath = filePath;
                IntentUtils.startSysCammera(ApplyIDActivity.this, reqCode, filePath);
            }

            @Override
            public void ItemMiddleClick() {
                IntentUtils.startSysGallery(ApplyIDActivity.this,reqCode,1,null);
            }

            @Override
            public void ItemBottomClick() {
                hideDialogPicSelect();
            }
        });
    }

    private void hideDialogPicSelect(){
        if(this.mDialogPicSelect != null){
            this.mDialogPicSelect.dismiss();
            this.mDialogPicSelect = null;
        }
    }

    private void showDialogBankList(List<PBankEntity> mList){
        hideDialogBankList();
        this.mDialogBankList = new DialogBankList(this,R.style.MyDialogBg);
        this.mDialogBankList.show();
        this.mDialogBankList.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                if(mBankEntitySelect != null){
                    mBankEntitySelect.vIsSelected = false;
                }
                mBankEntitySelect = (PBankEntity) obj;
                mBankEntitySelect.vIsSelected = true;
                String name = mBankEntitySelect.open_bank;
                itemBank.setEditTxt(name);

                hideDialogBankList();
            }
        });
        this.mDialogBankList.setInfo(mList);
    }

    private void hideDialogBankList(){
        if(this.mDialogBankList != null){
            this.mDialogBankList.dismiss();
            this.mDialogBankList = null;
        }
    }

    private void showDialogDateSelector(final int src,String strDate){
        hideDialogDateSelector();
        this.mDialogDate = new DialogDateSelector(this,R.style.MyDialogBg);
        this.mDialogDate.show();
        if (src == SRC_ID_START || src == SRC_BIRTH) {
            mDialogDate.updateType(DialogDateSelector.TYPE_EDU_DATE);
        } else {
            mDialogDate.updateType(DialogDateSelector.TYPE_ADD_COURSE_DATE);
        }
        this.mDialogDate.setIDatePickerListener(new DialogDateSelector.IDatePickerListener() {
            @Override
            public void onPickerClick(VDateEntity vEntity) {
                String strInfo = vEntity.getDate();
                if(src == SRC_ID_START){
                    itemStart.setEditTxt(strInfo);
                }else{
                    itemEnd.setEditTxt(strInfo);
                }
            }
            @Override
            public void onPickCancle(){}
        });
        //reset date
        if(!TextUtils.isEmpty(strDate)){
            VDateEntity vEntity = LoanDateUtil.parseVDateEntitiy(strDate);
            if(vEntity != null){
                if(MyLog.isDebugable()){
                    MyLog.debug(TAG,"[setPos]" + " year:" + vEntity.year + " month:" + vEntity.month + " day:" + vEntity.date);
                }
                mDialogDate.setPos(vEntity.year,vEntity.month,vEntity.date);
            }
        }
    }

    private void hideDialogDateSelector(){
        if(this.mDialogDate != null){
            this.mDialogDate.dismiss();
            this.mDialogDate = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogPicSelect();
        hideDialogCfg();
        hideDialogDateSelector();
    }

    private void initParmas(PAuthFaceResultEntity entity,boolean isEditable){
        //init name
        String name = entity.id_name;
        itemName.setEditTxt(name);
        //init IDCard no
        String no = entity.id_no;
        itemID.setEditTxt(no);
        //init start date
        String strDateStart = entity.getIDCardStartTime();
        itemStart.setEditTxt(strDateStart);
        //init end date
        String strDateEnd = entity.getIDCardEndTime();
        itemEnd.setEditTxt(strDateEnd);
        //init addr
        String strAddr = entity.addr_card;
        itemAddr.setEditTxt(strAddr);
        //id card front 身份证件正面照
        String picFont = "";
        String picBack = "";
        if(pFacePicEntity != null){
            picFont = pFacePicEntity.id_card_info_pic;
            picBack = pFacePicEntity.id_card_nation_pic;
        }
        if(!TextUtils.isEmpty(picFont) && !TextUtils.isEmpty(picBack)){
            String tips = getResources().getString(R.string.common_pic_requesting);
            showLoading(tips);
            int seqNo = ProtocalManager.getInstance().reqIDentityPic(null,getCallBack());
            mReqList.add(seqNo);
            Message msg = Message.obtain();
            msg.what = FLAG_PIC_INFO;
            sendMsgDelay(msg,DELAY);
        }
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        int what = msg.what;
        switch(what){
            case FLAG_AUTH:
                PAuthFaceResultEntity entity = (PAuthFaceResultEntity) msg.obj;
                if(entity != null){
                    initParmas(entity,false);
                }
                break;
            case FLAG_VERIFY_ERROR:
                String tips = (String) msg.obj;
                if(!TextUtils.isEmpty(tips)){
                    showToast(tips);
                }
                break;
            case FLAG_SET_LOADING:
                tips = (String) msg.obj;
                if(!TextUtils.isEmpty(tips) && isDialogShowing()){
                    setLoadingTips(tips);
                }else{
                    showLoading(tips);
                }
                break;
            case FLAG_HIDE_LOADING:
                hideLoadingDialog();
                break;
            case FLAG_SHOW_TOAST:
                tips = (String) msg.obj;
                showToast(tips);
                break;
            case FLAG_FILL_BITMAP:
                Bitmap bitmap = (Bitmap) msg.obj;
                int arg1 = msg.arg1;
                PicEntity mEntity = UploadPicController.getInstance().getItemBySuccList(arg1);
                if(arg1 ==  PicEntity.TYPE_ID1){
                    photoViewFront.updateBitmap(bitmap,mEntity.mNetPath,true);
                }else if(arg1 ==  PicEntity.TYPE_ID2){
                    photoViewBack.updateBitmap(bitmap,mEntity.mNetPath,true);
                }
                break;
            case FLAG_PIC_INFO:
                int seqNo = ProtocalManager.getInstance().reqIDentityPic(null,getCallBack());
                mReqList.add(seqNo);
                break;
        }
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspFaceVerifyCfgEntity){
                RspFaceVerifyCfgEntity rspEntity = (RspFaceVerifyCfgEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        this.pFaceVerifyEntity = rspEntity.mEntity;
                        openFaceAuth(rspEntity.mEntity);
                    }else{
                        String tips = getResources().getString(R.string.common_request_error);
                        showToast(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                }
            }else if(rsp instanceof RspIDentityCfgEntity){
                RspIDentityCfgEntity rspEntity = (RspIDentityCfgEntity) rsp;
                if(isSucc){
                    if(rspEntity != null && rspEntity.mList != null && rspEntity.mList.size() > 0){
                        this.mRspEntity = rspEntity;
                    }else{
                        String tips = getResources().getString(R.string.common_cfg_error);
                        showDialogCfg(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showDialogCfg(tips);
                }
            }else if(rsp instanceof RspPhoneVerifyEntity){
                RspPhoneVerifyEntity rspEntity = (RspPhoneVerifyEntity) rsp;
                if(isSucc){

                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                }
            }else if(rsp instanceof RspIDentityPicEntity){
                RspIDentityPicEntity rspEntity = (RspIDentityPicEntity) rsp;
                cnt++;
                boolean succ = false;
                if(isSucc){
                    if(rspEntity.mEntity != null && rspEntity.mEntity.isLegal()){
                        this.pFacePicEntity = rspEntity.mEntity;
                        succ = true;
                    }
                }else{

                }
                if(cnt <= MAX_CNT && !succ){
                    String tips = getResources().getString(R.string.common_pic_requesting,cnt);
                    showLoading(tips,false);
                    Message msg = Message.obtain();
                    msg.what = FLAG_PIC_INFO;
                    sendMsgDelay(msg,DELAY);
                }
            }else if(rsp instanceof RspSubmitIDentityEntity){
                RspSubmitIDentityEntity rspEntity = (RspSubmitIDentityEntity) rsp;
                if(isSucc){
                    setResult(Activity.RESULT_OK);
                    finish();
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                }
            }
        }
    }

    private void showDialogCfg(String tips){
        hideDialogCfg();
        this.mDialogCfg = new DialogConfirmSingle(this,R.style.MyDialog);
        this.mDialogCfg.show();
        this.mDialogCfg.updateType(DialogConfirmSingle.TYPE_CONTACTLIST_CFG);
        this.mDialogCfg.setIBtnListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                finish();
            }
        });
        String btnStr = getResources().getString(R.string.dialog_btn_str_okay);
        this.mDialogCfg.setContents(tips,btnStr);
    }

    private void hideDialogCfg(){
        if(this.mDialogCfg != null){
            this.mDialogCfg.dismiss();
            this.mDialogCfg = null;
        }
    }

    private void openFaceAuth(PFaceVerifyEntity pEntity){
        String orderId = pEntity.order;
        String authKey = pEntity.key;
        String notifyUrl = pEntity.notify_url;
        if(TextUtils.isEmpty(notifyUrl)){
            notifyUrl = HttpEngine.getInstance().getRefer(NetCommon.NET_INTERFACE_TYPE_UYE);
        }
        MyLog.d(TAG,"[openFaceAuth]" + " orderId:" + orderId + " authKey:" + authKey + " notifyUrl:" + notifyUrl + " userId:" + pEntity.user_id);
        AuthBuilder builder = new AuthBuilder(orderId, authKey, notifyUrl,mUDListener);
        builder.faceAuth(ApplyIDActivity.this);
    }

    private OnResultListener mUDListener = new OnResultListener() {
        @Override
        public void onResult(String s) {
            if(MyLog.isDebugable()){
                MyLog.debug(TAG,"[onResult]" + " s:" + s);
            }
            if(!isFinishing()){
                handleUserAuthRsp(s,FLAG_AUTH, FLAG_VERIFY_ERROR);
            }
        }
    };


    protected void handleUserAuthRsp(final String str, final int flagAuth, final int flagToast){
        if(!TextUtils.isEmpty(str)){
            Global.postDelay(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    PAuthFaceResultEntity mEntity = gson.fromJson(str,PAuthFaceResultEntity.class);
                    if(mEntity != null && mEntity.isSucc()){
                        //识别结果
                        if(!TextUtils.isEmpty(mEntity.result_auth)){
                            if(mEntity.result_auth.equals("T")){
//                                int age = mEntity.getAge();
//                                MyLog.debug(TAG,"[handleUserAuthRsp]" + " age:" + age);
                                AuthFaceController.getInstance().setLoanAuthFaceEntity(mEntity);
                                Message msg = Message.obtain();
                                msg.what = flagAuth;
                                msg.obj = mEntity;
                                sendMsg(msg);
                            }else if(mEntity.result_auth.equals("C") || mEntity.result_auth.equals("F")){
                                String tips = "";
                                if(mEntity.result_auth.equals("C")){
                                    tips = getResources().getString(R.string.face_auth_sdk_k12_result_checking);
                                }else{
                                    tips = getResources().getString(R.string.face_auth_sdk_k12_result_failure);
                                }
                                Message msg = Message.obtain();
                                msg.what = flagToast;
                                msg.obj = tips;
                                sendMsg(msg);
                            }
                        }else{
                            String tips = getResources().getString(R.string.face_auth_sdk_error);
                            Message msg = Message.obtain();
                            msg.what = flagToast;
                            msg.obj = tips;
                            sendMsg(msg);
                        }
                    }else{
                        String tips = getResources().getString(R.string.face_auth_sdk_error);
                        if(mEntity != null && !TextUtils.isEmpty(mEntity.ret_msg)){
                            tips = mEntity.ret_msg;
                        }
                        Message msg = Message.obtain();
                        msg.what = flagToast;
                        msg.obj = tips;
                        sendMsg(msg);
                    }
                }
            });
        }
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.orgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }

    @Override
    public void onClick(View view) {
        if(view == this.btnOk){
            String strName = itemName.getInputTxt();
            if(TextUtils.isEmpty(strName)){
                String tips = getString(R.string.apply_id_input_name);
                showToast(tips);
                return;
            }
            String strIDNo = itemID.getInputTxt();
            if(TextUtils.isEmpty(strIDNo)){
                String tips = getString(R.string.apply_id_tips_input_idno);
                showToast(tips);
                return;
            }
            String strDateStart = itemStart.getInputTxt();
            if(TextUtils.isEmpty(strDateStart)){
                String tips = getString(R.string.apply_id_tips_input_start);
                showToast(tips);
                return;
            }
            String strDateEnd = itemEnd.getInputTxt();
            if(TextUtils.isEmpty(strDateEnd)){
                String tips = getString(R.string.apply_id_tips_input_end);
                showToast(tips);
                return;
            }
            String strAddr = itemAddr.getInputTxt();
            if(TextUtils.isEmpty(strAddr)){
                String tips = getString(R.string.apply_id_tips_input_addr);
                showToast(tips);
                return;
            }
            String picUrlFront = photoViewFront.getUrl();
            if(TextUtils.isEmpty(picUrlFront)){
                String tips = getResources().getString(R.string.apply_id_tips_front);
                showToast(tips);
                return;
            }
            String picUrlBack = photoViewBack.getUrl();
            if(TextUtils.isEmpty(picUrlBack)){
                String tips = getString(R.string.apply_id_tips_back);
                showToast(tips);
                return;
            }
            String strBankNo = itemBankCard.getInputTxt();
            if(TextUtils.isEmpty(strBankNo)){
               String tips = getString(R.string.apply_id_tips_input_bankno);
                showToast(tips);
                return;
            }
            if(mBankEntitySelect == null){
                String tips = getString(R.string.apply_id_input_bankname);
                showToast(tips);
                return;
            }
            String strPhone = itemPhone.getInputTxt();
            if(TextUtils.isEmpty(strPhone)){
                String tips = getString(R.string.apply_id_inputphone);
                showToast(tips);
                return;
            }
            String strVerifyCode = itemVerify.getInputTxt();
            if(TextUtils.isEmpty(strVerifyCode)){
                String tips = getString(R.string.apply_id_input_verifycode);
                showToast(tips);
                return;
            }
            String udOrder = "";
//            if(pFaceVerifyEntity != null){
//                udOrder = pFaceVerifyEntity.user_id;
//            }
            showLoading();
            int seqNo = ProtocalManager.getInstance().reqIDentitySubmit(strName,strIDNo,strDateStart,strDateEnd,strAddr,
                    picUrlFront,picUrlBack,strPhone,strBankNo,mBankEntitySelect,strVerifyCode,udOrder,getCallBack());
            mReqList.add(seqNo);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQ_CODE_ID || requestCode == REQ_CODE_ID_BACK){
                PicResultEntity rPicEntity = PicUtils.onPicCammerResult(this,data,mCammerImgPath);
                String path = rPicEntity.path;
                PicEntity picEntity = PicEntity.buildPicEntityByApplyType(path,requestCode);
                picEntity.src = requestCode;
                if(picEntity != null && !TextUtils.isEmpty(path)){
                    String str_loading = getString(R.string.common_pic_uploading);
                    showLoading(str_loading, false);
                    UploadPicController.getInstance().addUploadPicQueue(picEntity);
                }else{
                    String tips = getString(R.string.common_pic_path_failure);
                    showToast(tips);
                }
            }
        }
    }
}
