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
import com.bjzt.uye.activity.dialog.DialogConfirmSingle;
import com.bjzt.uye.activity.dialog.DialogDateSelector;
import com.bjzt.uye.activity.dialog.DialogLocation;
import com.bjzt.uye.activity.dialog.DialogPicSelect;
import com.bjzt.uye.activity.dialog.DialogStrSelect;
import com.bjzt.uye.controller.UploadPicController;
import com.bjzt.uye.entity.PLocItemEntity;
import com.bjzt.uye.entity.PicEntity;
import com.bjzt.uye.entity.PicResultEntity;
import com.bjzt.uye.entity.VActivityResultEntity;
import com.bjzt.uye.entity.VApplyEmplyAddEntity;
import com.bjzt.uye.entity.VDateEntity;
import com.bjzt.uye.entity.VPicFileEntity;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.listener.IUploadListener;
import com.bjzt.uye.http.rsp.RspEmployProCfgEntity;
import com.bjzt.uye.http.rsp.RspEmployProSubmitEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.photo.activity.LoanPicScanActivity;
import com.bjzt.uye.util.ActivityResultUtil;
import com.bjzt.uye.util.FileUtil;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.PicUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.PicSelectView;
import com.bjzt.uye.views.component.YHeaderView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 新增就业进展
 * Created by billy on 2017/10/25.
 */
public class ApplyEmployProAddActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    @BindView(R.id.item_time)
    ItemView itemViewTime;
    @BindView(R.id.item_addr)
    ItemView itemViewAddr;
    @BindView(R.id.item_addr_detail)
    ItemView itemViewAddDetail;

    @BindView(R.id.item_cp)
    ItemView itemViewCp;
    @BindView(R.id.item_position)
    ItemView itemViewPos;
    @BindView(R.id.item_salary)
    ItemView itemViewSalary;
    @BindView(R.id.item_work_status)
    ItemView itemViewWorkStatus;
    @BindView(R.id.pic_select_view)
    PicSelectView picSelectView;
    @BindView(R.id.btn_ok)
    Button btnOk;

    private final int REQ_CODE_PIC = DialogPicSelect.TYPE_EMPLOY_TRACKS;
    private final int REQ_CODE_PIC_SCANE = REQ_CODE_PIC + 1;

    private final int FLAG_HIDE_LOADING = 100;
    private final int FLAG_SHOW_TOAST = 101;
    private final int FLAG_SET_LOADING = 102;
    private final int FLAG_FILL_BITMAP = 103;

    private DialogDateSelector mDialogDate;
    private DialogPicSelect mDialogPic;
    private DialogConfirmSingle mDialogCfg;

    private DialogLocation mDialogLoc;
    private PLocItemEntity mLocEntityPro;
    private PLocItemEntity mLocEntityCity;
    private PLocItemEntity mLocEntityArea;
    private String mCammerImgPath;

    private List<Integer> mReqList = new ArrayList<>();
    private String mInsureId;
    private RspEmployProCfgEntity mRspEntityCfg;
    private DialogStrSelect mDialogStr;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_employ_pro_add_layout;
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
        String title = getResources().getString(R.string.employ_progress_add_title);
        mHeader.setTitle(title);

        title = getResources().getString(R.string.time);
        itemViewTime.setTitle(title);
        String hint = getResources().getString(R.string.employ_progress_add_select_time);
        itemViewTime.setHint(hint);
        itemViewTime.setEditAble(false);
        itemViewTime.showArrow();
        itemViewTime.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDialogDateSelector();
            }
        });

        title = getResources().getString(R.string.employ_progress_add_work_addr);
        itemViewAddr.setTitle(title);
        hint = getResources().getString(R.string.employ_progress_add_select_addr);
        itemViewAddr.setHint(hint);
        itemViewAddr.setEditAble(false);
        itemViewAddr.showArrow();
        itemViewAddr.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String strTitleInfo = getResources().getString(R.string.employ_progress_add_work_addr);
                showDiaogLoc(ApplyEmployProAddActivity.this.mLocEntityPro,ApplyEmployProAddActivity.this.mLocEntityCity,ApplyEmployProAddActivity.this.mLocEntityArea,strTitleInfo);
            }
        });

        title = getResources().getString(R.string.employ_progress_add_work_addr_detail);
        itemViewAddDetail.setTitle(title);
        hint = getResources().getString(R.string.employ_progress_add_work_addr_detail_hint);
        itemViewAddDetail.setHint(hint);

        title = getResources().getString(R.string.employ_progress_add_cp);
        hint = getResources().getString(R.string.employ_progress_add_cp_hint);
        itemViewCp.setTitle(title);
        itemViewCp.setHint(hint);

        title = getResources().getString(R.string.employ_progress_add_pos);
        hint = getResources().getString(R.string.employ_progress_add_pos_hint);
        itemViewPos.setTitle(title);
        itemViewPos.setHint(hint);

        title = getResources().getString(R.string.employ_progress_add_salary);
        hint = getResources().getString(R.string.employ_progress_add_salary_hint);
        itemViewSalary.setTitle(title);
        itemViewSalary.setHint(hint);
        itemViewSalary.setEditAble(false);
        itemViewSalary.showArrow();
        itemViewSalary.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mRspEntityCfg != null && mRspEntityCfg.mEntity != null && mRspEntityCfg.mEntity.monthly_income != null){
                    String strInfo = itemViewSalary.getInputTxt();
                    showDialogStrList(mRspEntityCfg.mEntity.monthly_income,strInfo,DialogStrSelect.TYPE_SALARY);
                }
            }
        });

        title = getResources().getString(R.string.employ_progress_add_workstatus);
        hint = getResources().getString(R.string.employ_progress_add_workstatus_hint);
        itemViewWorkStatus.setTitle(title);
        itemViewWorkStatus.setHint(hint);
        itemViewWorkStatus.setEditAble(false);
        itemViewWorkStatus.showArrow();
        itemViewWorkStatus.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ArrayList<String> mList = MConfiger.buildEmployStatusList();
                String strInfo = itemViewWorkStatus.getInputTxt();
                showDialogStrList(mList,strInfo,DialogStrSelect.TYPE_EMPLOY_PRO_ADD);
            }
        });

        picSelectView.initData(PicSelectView.TYPE_EMPLOY_TRACK);
        picSelectView.setOnItemClickListener(mSelectListener);

        btnOk.setOnClickListener(this);
        UploadPicController.getInstance().setUploadListener(mUploadListener);

        mScrollView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();
        int seqNo = ProtocalManager.getInstance().reqEmployProCfg(this.mInsureId,getCallBack());
        mReqList.add(seqNo);
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        int what = msg.what;
        switch(what){
            case FLAG_HIDE_LOADING:
                hideLoadingDialog();
                break;
            case FLAG_SHOW_TOAST:
                String str = (String) msg.obj;
                if(!TextUtils.isEmpty(str)){
                    showToast(str);
                }
                break;
            case FLAG_SET_LOADING:
                String tips = (String) msg.obj;
                if(!TextUtils.isEmpty(tips) && isDialogShowing()){
                    setLoadingTips(tips);
                }else{
                    showLoading(tips);
                }
                break;
            case FLAG_FILL_BITMAP:
                Bitmap bitmap = (Bitmap) msg.obj;
                int arg1 = msg.arg1;
                PicEntity mEntity = UploadPicController.getInstance().getItemBySuccList(arg1);
                if(mEntity != null){
                    VPicFileEntity vPicEntity = new VPicFileEntity();
                    vPicEntity.filePath = mEntity.path;
                    vPicEntity.url = mEntity.mNetPath;
                    vPicEntity.mBitmap = bitmap;
                    picSelectView.insertLastBefore(vPicEntity);
                }
                break;
        }
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

    private PicSelectView.ISelectPicItemClickListener mSelectListener = new PicSelectView.ISelectPicItemClickListener() {
        @Override
        public void selectPic(int type) {
            showDialogPicSelect(REQ_CODE_PIC);
        }

        @Override
        public void showBigPic(int type, ArrayList<VPicFileEntity> mList, int pos) {
            ArrayList<String> rList = new ArrayList<>();
            if(mList != null){
                for(int i = 0;i < mList.size();i++){
                    rList.add(mList.get(i).url);
                }
            }
            IntentUtils.startPicScaneActivity(ApplyEmployProAddActivity.this,REQ_CODE_PIC_SCANE,rList,pos, LoanPicScanActivity.TYPE_NET);
        }
    };

    private void showDialogDateSelector(){
        hideDialogDateSelector();
        this.mDialogDate = new DialogDateSelector(this,R.style.MyDialogBg);
        this.mDialogDate.show();
        this.mDialogDate.updateType(DialogDateSelector.TYPE_ADD_COURSE_DATE);
        this.mDialogDate.setIDatePickerListener(new DialogDateSelector.IDatePickerListener() {
            @Override
            public void onPickerClick(VDateEntity vEntity) {
                String str = vEntity.getDate();
                itemViewTime.setEditTxt(str);
            }
        });
    }

    private void hideDialogDateSelector(){
        if(this.mDialogDate != null){
            this.mDialogDate.dismiss();
            this.mDialogDate = null;
        }
    }

    private void showDiaogLoc(PLocItemEntity mLocProEntity, PLocItemEntity mLocCityEntity, PLocItemEntity mLocAreaEntity,String title){
        hideDialogLoc();
        this.mDialogLoc = new DialogLocation(this,R.style.MyDialogBg);
        this.mDialogLoc.show();
        this.mDialogLoc.setIListener(new DialogLocation.LoanIDialogLocListener() {
            @Override
            public void onLocSelect(PLocItemEntity mEntityPro, PLocItemEntity mEntityCity, PLocItemEntity mEntityArea) {
                ApplyEmployProAddActivity.this.mLocEntityPro = mEntityPro;
                ApplyEmployProAddActivity.this.mLocEntityCity = mEntityCity;
                ApplyEmployProAddActivity.this.mLocEntityArea = mEntityArea;
                String str = StrUtil.getLocAddrStr(mLocEntityPro,mLocEntityCity,mLocEntityArea);
                itemViewAddr.setEditTxt(str);
            }
        });
        if(mLocProEntity != null && mLocCityEntity != null && mLocAreaEntity != null){
            this.mDialogLoc.setSelectInfo(mLocProEntity,mLocCityEntity,mLocAreaEntity);
        }
        this.mDialogLoc.setTitleInfo(title);
    }

    private void hideDialogLoc(){
        if(this.mDialogLoc != null){
            this.mDialogLoc.dismiss();
            this.mDialogLoc = null;
        }
    }

    private void showDialogPicSelect(final int reqCode){
        hideDialogPicSelect();
        this.mDialogPic = new DialogPicSelect(this,R.style.MyDialogBg);
        this.mDialogPic.show();
        this.mDialogPic.updateType(DialogPicSelect.TYPE_EMPLOY_TRACKS);
        this.mDialogPic.setListener(new DialogPicSelect.DialogClickListener() {
            @Override
            public void ItemTopClick() {
                String filePath = FileUtil.getCammeraImgPath();
                mCammerImgPath = filePath;
                IntentUtils.startSysCammera(ApplyEmployProAddActivity.this, reqCode, filePath);
            }

            @Override
            public void ItemMiddleClick() {
                IntentUtils.startSysGallery(ApplyEmployProAddActivity.this,reqCode,1,null);
            }
        });
    }

    private void hideDialogPicSelect(){
        if(this.mDialogPic != null){
            this.mDialogPic.dismiss();
            this.mDialogPic = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogDateSelector();
        hideDialogLoc();
        hideDialogPicSelect();
        hideDialogCfg();
        UploadPicController.getInstance().setUploadListener(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQ_CODE_PIC){
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
            }else if(requestCode == REQ_CODE_PIC_SCANE){
                List<String> mList = picSelectView.getNetUrlList();
                VActivityResultEntity vEntity = ActivityResultUtil.onActivityReult(mList,data);
                if(vEntity != null && vEntity.isDel){
                    picSelectView.clearData();
                    picSelectView.insertList(vEntity.mList);
                }
            }
        }
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspEmployProCfgEntity){
                RspEmployProCfgEntity mRspEntity = (RspEmployProCfgEntity) rsp;
                if(isSucc && mRspEntity != null && mRspEntity.mEntity != null && mRspEntity.mEntity.monthly_income != null){
                    this.mRspEntityCfg = mRspEntity;
                    mEmptyView.loadSucc();
                    mScrollView.setVisibility(View.VISIBLE);
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,mRspEntity);
                    showDialogCfg(tips);
                }
            }else if(rsp instanceof RspEmployProSubmitEntity){
                RspEmployProSubmitEntity rspEntity = (RspEmployProSubmitEntity) rsp;
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
        this.mDialogCfg = new DialogConfirmSingle(this,R.style.MyDialogBg);
        this.mDialogCfg.setMCancleable(false);
        this.mDialogCfg.setIBtnListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                hideDialogCfg();
                finish();
            }
        });
        this.mDialogCfg.setContents(tips,null);
        this.mDialogCfg.show();
    }

    private void hideDialogCfg(){
        if(this.mDialogCfg != null){
            this.mDialogCfg.dismiss();
            this.mDialogCfg = null;
        }
    }

    private void showDialogStrList(ArrayList<String> mList,String strSelect,final int src){
        hideDialogStrList();
        this.mDialogStr = new DialogStrSelect(this,R.style.MyDialogBg);
        this.mDialogStr.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                hideDialogStrList();
                if(tag == IItemListener.SRC_BTN_OK){
                    if(src == DialogStrSelect.TYPE_EMPLOY_PRO_ADD){
                        itemViewWorkStatus.setEditTxt(obj+"");
                    }else if(src == DialogStrSelect.TYPE_SALARY){
                        itemViewSalary.setEditTxt(obj+"");
                    }
                }
            }
        });
        this.mDialogStr.show();
        this.mDialogStr.updateType(src);
        this.mDialogStr.setListInfo(mList);
        this.mDialogStr.setSelectInfo(strSelect);
    }

    private void hideDialogStrList(){
        if(this.mDialogStr != null){
            this.mDialogStr.dismiss();
            this.mDialogStr = null;
        }
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.mInsureId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }

    @Override
    public void onClick(View v) {
        if(v == this.btnOk){
            VApplyEmplyAddEntity vEntity = buildApplyEmployResult();
            if(vEntity.isSucc){
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqEmployProSubmit(mInsureId,vEntity,getCallBack());
                mReqList.add(seqNo);
            }else{
                String tips = vEntity.tips;
                showToast(tips);
            }
        }
    }

    private VApplyEmplyAddEntity buildApplyEmployResult(){
        VApplyEmplyAddEntity vEntity = new VApplyEmplyAddEntity();
        String strTime = itemViewTime.getInputTxt();
        vEntity.strTime = strTime;
        if(TextUtils.isEmpty(strTime)){
            vEntity.tips  = getResources().getString(R.string.employ_progress_add_select_time);
            return vEntity;
        }
        vEntity.mLocEntityPro = this.mLocEntityPro;
        vEntity.mLocEntityCity = this.mLocEntityCity;
        vEntity.mLocEntityArea = this.mLocEntityArea;
        if(mLocEntityPro == null || mLocEntityCity == null || mLocEntityArea == null){
            String tips = getResources().getString(R.string.employ_progress_add_select_addr);
            vEntity.tips = tips;
            return vEntity;
        }
        //addr detai
        String strAddrDetail = itemViewAddDetail.getInputTxt();
        vEntity.strAddr = strAddrDetail;
        if(TextUtils.isEmpty(strAddrDetail)){
            String tips = getResources().getString(R.string.employ_progress_add_work_addr_detail_hint);
            vEntity.tips = tips;
            return vEntity;
        }
        String strCpName = itemViewCp.getInputTxt();
        vEntity.cpName = strCpName;
        if(TextUtils.isEmpty(strCpName)){
            String tips = getResources().getString(R.string.employ_progress_add_cp_hint);
            vEntity.tips = tips;
            return vEntity;
        }
        //
        String strPos = itemViewPos.getInputTxt();
        vEntity.cpPos = strPos;
        if(TextUtils.isEmpty(strPos)){
            String tips = getResources().getString(R.string.employ_progress_add_pos_hint);
            vEntity.tips = tips;
            return vEntity;
        }
        //salary
        String strSalary = itemViewSalary.getInputTxt();
        vEntity.salary = strSalary;
        if(TextUtils.isEmpty(strSalary)){
            String tips = getResources().getString(R.string.employ_progress_add_salary_hint);
            vEntity.tips = tips;
            return vEntity;
        }
        //是否录用
        String strEmployStatus = itemViewWorkStatus.getInputTxt();
        vEntity.employStatus = strEmployStatus;
        if(TextUtils.isEmpty(strEmployStatus)){
            String tips = getResources().getString(R.string.employ_progress_add_workstatus_hint);
            vEntity.tips = tips;
            return vEntity;
        }
        //就业足迹
        List<String> mListPic = picSelectView.getNetUrlList();
        vEntity.mPicList = mListPic;
        if(mListPic == null || mListPic.size() <= 0){
            String tips = getResources().getString(R.string.pic_select_tips_employ_track);
            vEntity.tips = tips;
            return vEntity;
        }
        vEntity.isSucc = true;
        return vEntity;
    }
}
