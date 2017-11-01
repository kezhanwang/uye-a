package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.activity.dialog.DialogLocation;
import com.bjzt.uye.activity.dialog.DialogStrNormalList;
import com.bjzt.uye.controller.ExperiController;
import com.bjzt.uye.entity.BDialogStrEntity;
import com.bjzt.uye.entity.PExperiBaseCfgEntity;
import com.bjzt.uye.entity.PExperiBaseInfoEntity;
import com.bjzt.uye.entity.PLocItemEntity;
import com.bjzt.uye.entity.VExperienceBaseEntity;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.req.ReqExperiListEntity;
import com.bjzt.uye.http.rsp.RspExperiBaseCfgEntity;
import com.bjzt.uye.http.rsp.RspExperiBaseCommitEntity;
import com.bjzt.uye.http.rsp.RspExperiBaseInfoEntity;
import com.bjzt.uye.http.rsp.RspExperiListEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.EmployArea;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.YHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by billy on 2017/10/30.
 * 个人经历-基本信息
 */
public class ApplyMyExperienceBaseActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;

    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.item_high_edu)
    ItemView mItemViewHighEdu;
    @BindView(R.id.item_occ)
    ItemView mItemViewOcc;
    @BindView(R.id.item_income)
    ItemView mItemViewIncome;
    @BindView(R.id.item_house)
    ItemView mItemViewHouse;
    @BindView(R.id.item_employ_area)
    EmployArea mEmployArea;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private DialogLocation mDialogLoc;
    private DialogStrNormalList mDialogStrNor;

    private final int SRC_DEGREE = 1;
    private final int SRC_OCC = 2;
    private final int SRC_INCOME = 3;
    private final int SRC_HOUSE = 4;

    private final int REQ_EXPERI_OCC = 100;
    private final int REQ_EXPERI_DEGREE = 101;

    private List<Integer> mReqList = new ArrayList<>();
    private PExperiBaseCfgEntity pEntity;
    private String orgId;
    private String strDegree;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_myexperience_layout;
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
        String title = getResources().getString(R.string.myexperience_base_title);
        mHeader.setTitle(title);

        title = getResources().getString(R.string.myexperience_base_edu_title);
        String hint = getResources().getString(R.string.myexperience_base_edu_hint);
        mItemViewHighEdu.setTitle(title);
        mItemViewHighEdu.setHint(hint);
        mItemViewHighEdu.setEditAble(false);
        mItemViewHighEdu.showArrow();
        mItemViewHighEdu.setEditTxtBtnListener(mItemClickListener);

        title = getResources().getString(R.string.myexperience_base_occ_title);
        hint = getResources().getString(R.string.myexperience_base_occ_hint);
        mItemViewOcc.setTitle(title);
        mItemViewOcc.setHint(hint);
        mItemViewOcc.setEditAble(false);
        mItemViewOcc.showArrow();
        mItemViewOcc.setEditTxtBtnListener(mItemClickListener);

        title = getResources().getString(R.string.myexperience_base_income_title);
        hint = getResources().getString(R.string.myexperience_base_income_hint);
        mItemViewIncome.setTitle(title);
        mItemViewIncome.setHint(hint);
        mItemViewIncome.setEditAble(false);
        mItemViewIncome.showArrow();
        mItemViewIncome.setEditTxtBtnListener(mItemClickListener);

        title = getResources().getString(R.string.myexperience_base_house_title);
        hint = getResources().getString(R.string.myexperience_base_house_hint);
        mItemViewHouse.setTitle(title);
        mItemViewHouse.setHint(hint);
        mItemViewHouse.setEditAble(false);
        mItemViewHouse.showArrow();
        mItemViewHouse.setEditTxtBtnListener(mItemClickListener);

        btnOk.setOnClickListener(this);

        mEmployArea.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                if(tag == EmployArea.SRC_ADD){
                    showDialogLoc();
                }
            }
        });

        mScrollView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();
        int seqNo = ProtocalManager.getInstance().reqMyExperiBaseCfg(getCallBack());
        mReqList.add(seqNo);
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspExperiBaseCfgEntity){
                RspExperiBaseCfgEntity rspEntity = (RspExperiBaseCfgEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        this.pEntity = rspEntity.mEntity;
                        ExperiController.getInstance().setCfgEntity(this.pEntity);
                        mScrollView.setVisibility(View.VISIBLE);
                        mEmptyView.loadSucc();
                        int seq = ProtocalManager.getInstance().reqMyExperiBaseInfo(getCallBack());
                        mReqList.add(seq);
                    }else{
                        String tips = getResources().getString(R.string.common_cfg_empty);
                        initErrorStatus(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    initErrorStatus(tips);
                }
            }else if(rsp instanceof RspExperiBaseInfoEntity){
                RspExperiBaseInfoEntity rspEntity = (RspExperiBaseInfoEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null && rspEntity.mEntity.isOk()){
                        initParams(rspEntity.mEntity);
                    }
                }
            }else if(rsp instanceof RspExperiBaseCommitEntity){
                RspExperiBaseCommitEntity rspEntity = (RspExperiBaseCommitEntity) rsp;
                if(isSucc){
                    String tips = getResources().getString(R.string.common_request_succ);
                    showToast(tips);
                    if(isNotEmployed()){
                        showLoading();
                        int seq = ProtocalManager.getInstance().reqMyExperiListDegree(getCallBack());
                        mReqList.add(seq);
                    }else{
                        showLoading();
                        int seq = ProtocalManager.getInstance().reqMyExperiListOcc(getCallBack());
                        mReqList.add(seq);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                }
            }else if(rsp instanceof RspExperiListEntity){
                RspExperiListEntity rspEntity = (RspExperiListEntity) rsp;
                int mType = rspEntity.mType;
                if(mType == ReqExperiListEntity.TYPE_DEGREE){
                    IntentUtils.startMyExperienceOccActivity(ApplyMyExperienceBaseActivity.this,orgId,REQ_EXPERI_DEGREE,false,rspEntity);
                }else if(mType == ReqExperiListEntity.TYPE_OCC){
                    IntentUtils.startMyExperienceOccActivity(ApplyMyExperienceBaseActivity.this,orgId,REQ_EXPERI_OCC,true,rspEntity);
                }
            }
        }
    }

    private void initParams(PExperiBaseInfoEntity pEntity){
        //init degree
        String strDegree = pEntity.highest_education;
        setItemViewTxt(mItemViewHighEdu,strDegree);
        //init occ
        String strOcc = pEntity.profession;
        setItemViewTxt(mItemViewOcc,strOcc);
        this.strDegree = strOcc;
        //init income
        String strIncome = pEntity.monthly_income;
        setItemViewTxt(mItemViewIncome,strIncome);
        //init house
        String strHouse = pEntity.housing_situation;
        setItemViewTxt(mItemViewHouse,strHouse);
        //init city area
        List<String> mLocList = pEntity.will_work_city;
        if(mLocList != null && mLocList.size() > 0){
            mEmployArea.setList(mLocList);
        }
    }

    private void setItemViewTxt(ItemView mItemView,String strInfo){
        if(!TextUtils.isEmpty(strInfo)){
            mItemView.setEditTxt(strInfo);
        }else{
            mItemView.setEditTxt("");
        }
    }

    private void initErrorStatus(String tips){
        mEmptyView.showErrorState();
        mEmptyView.setErrorTips(tips);
        mEmptyView.setBlankListener(new BlankEmptyView.BlankBtnListener() {
            @Override
            public void btnRefresh() {
                mEmptyView.showLoadingState();
                int seqNo = ProtocalManager.getInstance().reqMyExperiBaseCfg(getCallBack());
                mReqList.add(seqNo);
            }
        });
    }

    View.OnClickListener mItemClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int src = -1;
            String strSelect = null;
            if(pEntity != null){
                List<String> mList = null;
                if(v == mItemViewHighEdu.getEditTxt()){
                    src = SRC_DEGREE;
                    mList = pEntity.highest_education;
                    strSelect = mItemViewHighEdu.getInputTxt();
                }else if(v == mItemViewHouse.getEditTxt()){
                    src = SRC_HOUSE;
                    mList = pEntity.housing_situation;
                    strSelect = mItemViewHouse.getInputTxt();
                }else if(v == mItemViewIncome.getEditTxt()){
                    src = SRC_INCOME;
                    mList = pEntity.monthly_income;
                    strSelect = mItemViewIncome.getInputTxt();
                }else{
                    src = SRC_OCC;
                    mList = pEntity.profession;
                    strSelect = mItemViewOcc.getInputTxt();
                }
                showDialogNormal(mList,strSelect,src);
            }else{
                String tips = getResources().getString(R.string.common_cfg_empty);
                showToast(tips);
            }
        }
    };

    /***
     * 未就业
     * @return
     */
    private boolean isNotEmployed(){
        return !TextUtils.isEmpty(strDegree) && MConfiger.STR_NO_EMPLOYMENT.equals(strDegree);
    }

    private void showDialogNormal(final List<String> mList,String strSelect,final int src){
        hideDialogNormal();
        this.mDialogStrNor = new DialogStrNormalList(this,R.style.MyDialogBg);
        this.mDialogStrNor.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                hideDialogNormal();
                BDialogStrEntity bEntity = (BDialogStrEntity) obj;
                String strInfo = bEntity.str;
                if(!TextUtils.isEmpty(strInfo)){
                    switch(src){
                        case SRC_DEGREE:
                            ApplyMyExperienceBaseActivity.this.strDegree = strInfo;
                            mItemViewHighEdu.setEditTxt(strInfo);
                            break;
                        case SRC_HOUSE:
                            mItemViewHouse.setEditTxt(strInfo);
                            break;
                        case SRC_INCOME:
                            mItemViewIncome.setEditTxt(strInfo);
                            break;
                        case SRC_OCC:
                            mItemViewOcc.setEditTxt(strInfo);
                            break;
                    }
                }
            }
        });
        this.mDialogStrNor.show();
        String title = "";
        switch(src){
            case SRC_DEGREE:
                title = "最高学历";
                break;
            case SRC_HOUSE:
                title = "住房状态";
                break;
            case SRC_INCOME:
                title = "月收入";
                break;
            case SRC_OCC:
                title = "选择职业";
                break;
        }
        BDialogStrEntity bEntity = null;
        if(!TextUtils.isEmpty(strSelect)){
            bEntity = new BDialogStrEntity();
            bEntity.str = strSelect;
            bEntity.isSelect = true;
        }
        this.mDialogStrNor.setStrInfo(DialogStrNormalList.buildNormalList(mList),bEntity,title);
    }

    private void hideDialogNormal(){
        if(this.mDialogStrNor != null){
            this.mDialogStrNor.dismiss();
            this.mDialogStrNor = null;
        }
    }

    private void showDialogLoc(){
        hideDialogLoc();
        this.mDialogLoc = new DialogLocation(this,R.style.MyDialogBg);
        this.mDialogLoc.show();
        this.mDialogLoc.setIListener(new DialogLocation.LoanIDialogLocListener() {
            @Override
            public void onLocSelect(PLocItemEntity mEntityPro, PLocItemEntity mEntityCity, PLocItemEntity mEntityArea) {
                boolean isSucc = mEmployArea.appendLocEntity(mEntityPro,mEntityCity,mEntityArea);
                if(!isSucc){
                    String tips = getResources().getString(R.string.myexperience_base_samecity_tips);
                    showToast(tips);
                }
            }
        });
        this.mDialogLoc.setType(DialogLocation.TYPE_CITY);
        String strTitle = getResources().getString(R.string.myexperience_base_locdialog_title);
        this.mDialogLoc.setTitleInfo(strTitle);
    }

    private void hideDialogLoc(){
        if(this.mDialogLoc != null){
            this.mDialogLoc.dismiss();
            this.mDialogLoc = null;
        }
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        orgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case REQ_EXPERI_OCC:
                case REQ_EXPERI_DEGREE:
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogLoc();
        hideDialogNormal();
    }

    @Override
    public void onClick(View v) {
        if(v == this.btnOk){
            VExperienceBaseEntity vEntity = buildResult(true);
            if(vEntity.isSucc){
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqMyExperBaseCommit(vEntity,getCallBack());
                mReqList.add(seqNo);
            }else{
                String tips = vEntity.msg;
                showToast(tips);
            }
        }
    }


    private VExperienceBaseEntity buildResult(boolean isInterrupt){
        VExperienceBaseEntity vEntity = new VExperienceBaseEntity();
        //degree
        String strDegree = mItemViewHighEdu.getInputTxt();
        vEntity.strDegree = strDegree;
        if(isInterrupt && TextUtils.isEmpty(strDegree)){
            String tips = getResources().getString(R.string.myexperience_base_edu_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //occ
        String strOcc = mItemViewOcc.getInputTxt();
        vEntity.strOcc = strOcc;
        if(isInterrupt && TextUtils.isEmpty(strOcc)){
            String tips = getResources().getString(R.string.myexperience_base_occ_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //income
        String strIncome = mItemViewIncome.getInputTxt();
        vEntity.strIncome = strIncome;
        if(isInterrupt && TextUtils.isEmpty(strIncome)){
            String tips = getResources().getString(R.string.myexperience_base_income_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //house
        String strHouse = mItemViewHouse.getInputTxt();
        vEntity.strHouse = strHouse;
        if(isInterrupt && TextUtils.isEmpty(strHouse)){
            String tips = getResources().getString(R.string.myexperience_base_house_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //will work area
        List<EmployArea.BLocEntity> mList = mEmployArea.getLocList();
        vEntity.mLocList = mList;
        if(isInterrupt && (mList == null || mList.size() <= 0)){
            String tips = getResources().getString(R.string.myexperience_base_locarea_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        vEntity.isSucc = true;
        return vEntity;
    }
}
