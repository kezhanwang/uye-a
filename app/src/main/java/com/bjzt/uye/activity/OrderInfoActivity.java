package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.activity.dialog.DialogCourseList;
import com.bjzt.uye.activity.dialog.DialogDateSelector;
import com.bjzt.uye.activity.dialog.DialogPicSelect;
import com.bjzt.uye.controller.UploadPicController;
import com.bjzt.uye.entity.PCourseEntity;
import com.bjzt.uye.entity.POrderInfoEntity;
import com.bjzt.uye.entity.POrganizeEntity;
import com.bjzt.uye.entity.PicEntity;
import com.bjzt.uye.entity.PicResultEntity;
import com.bjzt.uye.entity.VDateEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.listener.IUploadListener;
import com.bjzt.uye.http.rsp.RspOrderInfoEntity;
import com.bjzt.uye.http.rsp.RspOrderSubmitEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.FileUtil;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.PicUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BindCardPhotoView;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.OrderHeaderView;
import com.bjzt.uye.views.component.YHeaderView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 订单信息页
 * Created by billy on 2017/10/18
 */
public class OrderInfoActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.headerview)
    OrderHeaderView headerView;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.item_coursename)
    ItemView itemCName;
    @BindView(R.id.item_tution)
    ItemView itemTution;
    @BindView(R.id.item_clazz)
    ItemView itemClazz;
    @BindView(R.id.item_datestart)
    ItemView itemDateStart;
    @BindView(R.id.item_dateend)
    ItemView itemDateEnd;
    @BindView(R.id.item_adviser)
    ItemView itemAdvise;
    @BindView(R.id.photoview_hold)
    BindCardPhotoView photoViewHold;
    @BindView(R.id.photoview_protocal)
    BindCardPhotoView photoViewProtocal;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private final int REQ_CODE_HOLD = DialogPicSelect.TYPE_HOLD;
    private final int REQ_CODE_PROTOCAL = DialogPicSelect.TYPE_PROTOCAL;
    private final int REQ_DATA_CHECK = 100;

    private final int FLAG_SET_LOADING = 10;
    private final int FLAG_HIDE_LOADING = 11;
    private final int FLAG_SHOW_TOAST = 12;
    private final int FLAG_FILL_BITMAP = 13;

    private final int SRC_COURSE_START = 1;
    private final int SRC_COURSE_END = 2;

    private DialogPicSelect mDialogSelect;
    private DialogCourseList mDialogCourse;
    private DialogDateSelector mDialogDate;

    private List<Integer> mReqList = new ArrayList<Integer>();
    private String orgId;
    private String mCammerImgPath;
    private RspOrderInfoEntity mRspEntity;
    private PCourseEntity mCourseEntitySelect;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_orderinfo_layout;
    }

    @Override
    protected void initLayout() {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        String title = getString(R.string.order_info_title);
        mHeader.setTitle(title);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });


        //init course name
        title = getString(R.string.order_info_cname);
        String hint = getString(R.string.order_info_cname_tips);
        itemCName.setTitle(title);
        itemCName.setHint(hint);
        itemCName.setEditAble(false);
        itemCName.showArrow();
        itemCName.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mRspEntity != null && mRspEntity.mEntity != null && mRspEntity.mEntity.courses != null && mRspEntity.mEntity.courses.size() > 0){
                    showDialogCourseList(mRspEntity.mEntity.courses);
                }else{
                    String tips = getResources().getString(R.string.common_course_empty);
                    showToast(tips);
                }
            }
        });
        itemCName.showStar();

        title = getString(R.string.order_info_tution);
        hint = getString(R.string.order_info_tution_tips);
        itemTution.setTitle(title);
        itemTution.setHint(hint);
        itemTution.setInputTypeNumber(ItemView.INPUT_TYPE_NUMBER);
        itemTution.setMaxCntInput(6);
        itemTution.showStar();

        title = getString(R.string.order_info_clazz_name);
        hint = getString(R.string.order_info_clazz_name_tips);
        itemClazz.setTitle(title);
        itemClazz.setHint(hint);
        itemClazz.showStar();

        title = getString(R.string.order_info_train_date);
        hint = getString(R.string.order_info_train_date_tips);
        itemDateStart.setTitle(title);
        itemDateStart.setHint(hint);
        itemDateStart.showStar();
        itemDateStart.setEditAble(false);
        itemDateStart.showArrow();
        itemDateStart.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialogDateSelector(SRC_COURSE_START);
            }
        });

        hint = getString(R.string.order_info_train_date_end_tips);
        itemDateEnd.hideTitle();
        itemDateEnd.setHint(hint);
        itemDateEnd.setEditAble(false);
        itemDateEnd.showArrow();
        itemDateEnd.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showDialogDateSelector(SRC_COURSE_END);
            }
        });

        title = getString(R.string.order_info_adviser);
        hint = getString(R.string.order_info_advise_tips);
        itemAdvise.setTitle(title);
        itemAdvise.setHint(hint);
        itemAdvise.showStar();

        photoViewHold.updateType(BindCardPhotoView.TYPE_IMG_PERSONAL_HOLD);
        photoViewHold.setIItemListener(mPhotoViewListener);
        photoViewHold.showStar(true);

        photoViewProtocal.updateType(BindCardPhotoView.TYPE_IMG_PROTOCAL);
        photoViewProtocal.setIItemListener(mPhotoViewListener);
        photoViewProtocal.showStar(true);

        btnOk.setOnClickListener(this);

//        mEmptyView.setVisibility(View.VISIBLE);
//        mScrollView.setVisibility(View.GONE);
//        mEmptyView.showLoadingState();
        int seqNo = ProtocalManager.getInstance().reqOrderInfo(this.orgId,getCallBack());
        mReqList.add(seqNo);

        UploadPicController.getInstance().setUploadListener(mUploadListener);
    }

    private IUploadListener mUploadListener = new IUploadListener() {
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
                if(obj == photoViewHold){
                    showDialogPicSelect(REQ_CODE_HOLD);
                }else if(obj == photoViewProtocal){
                    showDialogPicSelect(REQ_CODE_PROTOCAL);
                }
            }else{

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
                if(arg1 ==  PicEntity.TYPE_HOLDID){
                    photoViewHold.updateBitmapAdd(bitmap,mEntity.mNetPath);
                }else if(arg1 ==  PicEntity.TYPE_PRO_FIRST){
                    photoViewProtocal.updateBitmapAdd(bitmap,mEntity.mNetPath);
                }
                break;
        }
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspOrderInfoEntity){
                RspOrderInfoEntity rspEntity = (RspOrderInfoEntity) rsp;
                if(isSucc){
                    if(rspEntity != null && rspEntity.mEntity != null){
                        mEmptyView.loadSucc();
                        mScrollView.setVisibility(View.VISIBLE);
                        this.mRspEntity = rspEntity;
                        initParams(rspEntity.mEntity);
                    }else{
                        String tips = getResources().getString(R.string.common_cfg_error);
                        initErrorStatus(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    initErrorStatus(tips);
                }
            }else if(rsp instanceof RspOrderSubmitEntity) {
                RspOrderSubmitEntity rspEntity = (RspOrderSubmitEntity) rsp;
                if (isSucc) {
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    String tips = StrUtil.getErrorTipsByCode(errorCode, rspEntity);
                    showToast(tips);
                }
            }
        }
    }

    private void initParams(POrderInfoEntity mEntity){
        if(mEntity != null){
            POrganizeEntity orgEntity = mEntity.organize;
            if(orgEntity != null){
                headerView.setInfo(orgEntity);
            }
        }
    }

    private void initErrorStatus(String tips){
        mEmptyView.showErrorState();
        mEmptyView.setErrorTips(tips);
        mEmptyView.setBlankListener(new BlankEmptyView.BlankBtnListener() {
            @Override
            public void btnRefresh() {
                mEmptyView.showLoadingState();
                int seqNo = ProtocalManager.getInstance().reqOrderInfo(orgId,getCallBack());
                mReqList.add(seqNo);
            }
        });
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.orgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }

    @Override
    public void onClick(View view) {
        if(view == this.btnOk){
            String strCourseName = itemCName.getInputTxt();
            if(TextUtils.isEmpty(strCourseName)){
                String tips = getString(R.string.order_info_cname_tips);
                showToast(tips);
                return;
            }
            String strTution = itemTution.getInputTxt();
            if(TextUtils.isEmpty(strTution)){
                String tips = getResources().getString(R.string.order_info_tution_tips);
                showToast(tips);
                return;
            }
            String strClazzName = itemClazz.getInputTxt();
            if(TextUtils.isEmpty(strClazzName)){
                String tips = getResources().getString(R.string.order_info_clazz_name_tips);
                showToast(tips);
                return;
            }
            String strDateStart = itemDateStart.getInputTxt();
            if(TextUtils.isEmpty(strDateStart)){
                String tips = getResources().getString(R.string.order_info_train_date_tips);
                showToast(tips);
                return;
            }
            String strDateEnd = itemDateEnd.getInputTxt();
            if(TextUtils.isEmpty(strDateEnd)){
                String tips = getResources().getString(R.string.order_info_train_date_end_tips);
                showToast(tips);
                return;
            }
            String strAdviser = itemAdvise.getInputTxt();
            if(TextUtils.isEmpty(strAdviser)){
                String tips = getResources().getString(R.string.order_info_advise_tips);
                showToast(tips);
                return;
            }
            String strPicHold = photoViewHold.getUrl();
            if(TextUtils.isEmpty(strPicHold)){
                String tips = getResources().getString(R.string.order_info_upload_pic_hold_tips);
                showToast(tips);
                return;
            }
            String strPicPicProtocal = photoViewProtocal.getUrl();
            if(TextUtils.isEmpty(strPicPicProtocal)){
                String tips = getResources().getString(R.string.order_info_upload_pic_protocal_tips);
                showToast(tips);
                return;
            }
            showLoading();
            String orgId = "";
            if(mRspEntity != null && mRspEntity.mEntity != null && mRspEntity.mEntity.organize != null){
                orgId = mRspEntity.mEntity.organize.org_id;
            }
            List<String> mList = new ArrayList<>();
            mList.add(strPicPicProtocal);
            int seqNo = ProtocalManager.getInstance().reqOrderSubmit(orgId,mCourseEntitySelect.c_id,strTution,strClazzName,
                    strDateStart,strDateEnd,strAdviser,strPicHold,mList,"1",getCallBack());
            mReqList.add(seqNo);
        }
    }

    private void showDialogDateSelector(final int src){
        hideDialogDateSelector();
        this.mDialogDate = new DialogDateSelector(this,R.style.MyDialogBg);
        this.mDialogDate.show();
        if(src == SRC_COURSE_START){
            this.mDialogDate.updateType(DialogDateSelector.TYPE_ADD_COURSE_DATE);
        }else{
            this.mDialogDate.updateType(DialogDateSelector.TYPE_ADD_COURSE_DATE);
        }
        this.mDialogDate.setIDatePickerListener(new DialogDateSelector.IDatePickerListener() {
            @Override
            public void onPickerClick(VDateEntity vEntity) {
                String strInfo = vEntity.getDate();
                if(src == SRC_COURSE_START){
                    itemDateStart.setEditTxt(strInfo);
                }else{
                    itemDateEnd.setEditTxt(strInfo);
                }
            }
        });
    }

    private void hideDialogDateSelector(){
        if(this.mDialogDate != null){
            this.mDialogDate.dismiss();
            this.mDialogDate = null;
        }
    }

    private void showDialogPicSelect(final int reqCode){
        hideDialogPicSelect();
        this.mDialogSelect = new DialogPicSelect(this,R.style.MyDialogBg);
        this.mDialogSelect.show();
        this.mDialogSelect.updateType(reqCode);
        this.mDialogSelect.setListener(new DialogPicSelect.DialogClickListener() {
            @Override
            public void ItemTopClick() {
                String filePath = FileUtil.getCammeraImgPath();
                mCammerImgPath = filePath;
                IntentUtils.startSysCammera(OrderInfoActivity.this, reqCode, filePath);
            }

            @Override
            public void ItemMiddleClick() {
                IntentUtils.startSysGallery(OrderInfoActivity.this,reqCode,1,null);
            }

            @Override
            public void ItemBottomClick() {
                hideDialogPicSelect();

            }
        });
    }

    private void hideDialogPicSelect(){
        if(this.mDialogSelect != null){
            this.mDialogSelect.dismiss();
            this.mDialogSelect = null;
        }
    }

    private void showDialogCourseList(List<PCourseEntity> mList){
        hideDialogCourseList();
        this.mDialogCourse = new DialogCourseList(this,R.style.MyDialogBg);
        this.mDialogCourse.show();
        this.mDialogCourse.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                PCourseEntity cEntity = (PCourseEntity) obj;
                mCourseEntitySelect = cEntity;
                String name = cEntity.c_name;
                itemCName.setEditTxt(name);
                hideDialogCourseList();
            }
        });
        this.mDialogCourse.setCourseList(mList);
    }

    private void hideDialogCourseList(){
        if(this.mDialogCourse != null){
            this.mDialogCourse.dismiss();
            this.mDialogCourse = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogPicSelect();
        hideDialogCourseList();
        hideDialogDateSelector();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQ_CODE_HOLD || requestCode == REQ_CODE_PROTOCAL){
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
