package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.activity.dialog.DialogPicSelect;
import com.bjzt.uye.controller.UploadPicController;
import com.bjzt.uye.entity.PicEntity;
import com.bjzt.uye.entity.PicResultEntity;
import com.bjzt.uye.http.listener.IUploadListener;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.FileUtil;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.PicUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.ProfileIconItemView;
import com.bjzt.uye.views.component.ProfileItemView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.controller.LoginController;

import java.io.File;

import butterknife.BindView;

/**
 * Created by billy on 2017/11/2.
 */

public class ProfileActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.item_view_icon)
    ProfileIconItemView mItemViewIcon;
    @BindView(R.id.item_view_nick)
    ProfileItemView mItemViewNick;
    @BindView(R.id.item_view_phone)
    ProfileItemView mItemViewPhone;
    @BindView(R.id.item_view_resetpwd)
    ProfileItemView mItemViewResetPwd;

    private final int REQ_CODE_RESETPWD = 0x10;
    private final int REQ_CODE_RESETNICK = 0x11;
    private final int REQ_CODE_PICSELECT = 0x12;

    private DialogPicSelect mDialogPicSelect;
    private String mCammerImgPath;

    private final int FLAG_SET_LOADING = 0x100;
    private final int FLAG_HIDE_LOADING = 0x101;
    private final int FLAG_SHOW_TOAST = 0x102;
    private final int FLAG_FILL_BITMAP = 0x103;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
        String title = getResources().getString(R.string.profile_title);
        mHeader.setTitle(title);

        title = getResources().getString(R.string.profile_avator_modify);
        mItemViewIcon.setTitle(title);
        mItemViewIcon.setIItemListener(mItemListener);

        title = getResources().getString(R.string.profile_nickname_modify);
        String strTail = LoginController.getInstance().getNickName();
        mItemViewNick.setTitle(title);
        mItemViewNick.setTail(strTail);
        mItemViewNick.setIItemListener(mItemListener);

        title = getResources().getString(R.string.profile_phone_modify);
        strTail = LoginController.getInstance().getPhoneNum();
        mItemViewPhone.setTitle(title);
        mItemViewPhone.setTail(strTail);
        mItemViewPhone.setIItemListener(mItemListener);

        title = getResources().getString(R.string.profile_pwd_modify);
        mItemViewResetPwd.setTitle(title);
        mItemViewResetPwd.setTail("");
        mItemViewResetPwd.setIItemListener(mItemListener);

        UploadPicController.getInstance().setUploadListener(mPicUploadListener);
    }

    private IUploadListener mPicUploadListener = new IUploadListener() {
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

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        int what = msg.what;
        switch(what){
            case FLAG_SET_LOADING:
                String tips = (String) msg.obj;
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
                if(arg1 ==  PicEntity.TYPE_USER_ICON){
                    String url = mEntity.mNetPath;
                    mItemViewIcon.setImageIcon(url);
                    //协议上报到服务器 开发中...
                }
                break;
        }
    }

    private IItemListener mItemListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            if(obj == mItemViewResetPwd){
                IntentUtils.startModifyPwdActivity(ProfileActivity.this,REQ_CODE_RESETPWD);
            }else if(obj == mItemViewNick){
                IntentUtils.startMofifyNickActivity(ProfileActivity.this,REQ_CODE_RESETNICK);
            }else if(obj == mItemViewPhone){
                String tips = getResources().getString(R.string.profile_modify_pwd_not_support);
                showToast(tips);
            }else{
                showDialogPicSelect(REQ_CODE_PICSELECT);
            }
        }
    };

    @Override
    protected void initExtras(Bundle bundle) {

    }

    private void showDialogPicSelect(final int reqCode){
        hideDialogPicSelect();
        this.mDialogPicSelect = new DialogPicSelect(this,R.style.MyDialogBg);
        int type = -1;
        if(reqCode == REQ_CODE_PICSELECT){
            type = DialogPicSelect.TYPE_USER_ICON;
        }
        this.mDialogPicSelect.updateType(type);
        this.mDialogPicSelect.show();
        this.mDialogPicSelect.setListener(new DialogPicSelect.DialogClickListener() {
            @Override
            public void ItemTopClick() {
                String filePath = FileUtil.getCammeraImgPath();
                mCammerImgPath = filePath;
                IntentUtils.startSysCammera(ProfileActivity.this, reqCode, filePath);
            }

            @Override
            public void ItemMiddleClick() {
                IntentUtils.startSysGallery(ProfileActivity.this,reqCode,1,null);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQ_CODE_PICSELECT){
                PicResultEntity rPicEntity = PicUtils.onPicCammerResult(this,data,mCammerImgPath);
                String path = rPicEntity.path;
                PicEntity picEntity = PicEntity.buildPicEntityByApplyType(path,DialogPicSelect.TYPE_USER_ICON);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogPicSelect();
    }
}
