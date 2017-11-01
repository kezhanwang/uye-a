package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.activity.dialog.DialogDateSelector;
import com.bjzt.uye.activity.dialog.DialogStrNormalList;
import com.bjzt.uye.controller.ExperiController;
import com.bjzt.uye.entity.BDialogStrEntity;
import com.bjzt.uye.entity.PExperiEntity;
import com.bjzt.uye.entity.VDateEntity;
import com.bjzt.uye.entity.VExperiOccEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspExperiAddEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.LoanDateUtil;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.common.MyLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by billy on 2017/10/30.
 * 个人经历-职业添加
 */
public class ApplyMyExperienceOccAddActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;

    @BindView(R.id.item_cpname)
    ItemView mItemViewCpName;
    @BindView(R.id.item_entrytime)
    ItemView mItemViewEntryTime;
    @BindView(R.id.item_turnovertime)
    ItemView mItemViewTurnOverTime;
    @BindView(R.id.item_pos)
    ItemView mItemViewPos;
    @BindView(R.id.item_income)
    ItemView mItemViewIncome;

    @BindView(R.id.btn_ok)
    Button btnOk;

    private List<Integer> mReqList = new ArrayList<>();
    private DialogDateSelector mDialogDate;
    private DialogStrNormalList mDialogNor;

    private final int SRC_DATE_START = 1;
    private final int SRC_DATE_END = 2;

    private final int SRC_NOR_POS = 1;
    private final int SRC_NOR_INCOME = 2;

    public static final int TYPE_ADD = 0;
    public static final int TYPE_EDIT = 1;
    private int mType = TYPE_ADD;
    private PExperiEntity pEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_myexperience_occ_add_layout;
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
        String title = "添加职业";
        if(mType == TYPE_EDIT){
            title = "编辑职业";
        }
        mHeader.setTitle(title);

        title = "单位名称";
        String hint = getResources().getString(R.string.myexperience_degree_cpname_hint);
        mItemViewCpName.setTitle(title);
        mItemViewCpName.setHint(hint);

        title = "入职时间";
        hint = getString(R.string.myexperience_degree_datestart_hint);
        mItemViewEntryTime.setTitle(title);
        mItemViewEntryTime.setHint(hint);
        mItemViewEntryTime.setEditAble(false);
        mItemViewEntryTime.showArrow();
        mItemViewEntryTime.setEditTxtBtnListener(mItemListener);

        title = "离职时间";
        hint = getString(R.string.myexperience_degree_dateend_hint);
        mItemViewTurnOverTime.setTitle(title);
        mItemViewTurnOverTime.setHint(hint);
        mItemViewTurnOverTime.setEditAble(false);
        mItemViewTurnOverTime.showArrow();
        mItemViewTurnOverTime.setEditTxtBtnListener(mItemListener);

        title = "职位";
        hint = getString(R.string.myexperience_degree_pos_hint);
        mItemViewPos.setTitle(title);
        mItemViewPos.setHint(hint);
        mItemViewPos.setEditAble(false);
        mItemViewPos.showArrow();
        mItemViewPos.setEditTxtBtnListener(mItemListener);

        title = "薪资";
        hint = getString(R.string.myexperience_degree_income_hint);
        mItemViewIncome.setTitle(title);
        mItemViewIncome.setHint(hint);
        mItemViewIncome.setEditAble(false);
        mItemViewIncome.showArrow();
        mItemViewIncome.setEditTxtBtnListener(mItemListener);

        btnOk.setOnClickListener(this);
        if(pEntity != null){
            fillInVal(pEntity);
        }
    }

    private void fillInVal(PExperiEntity pEntity){
        String strCpNamme = pEntity.work_name;
        setItemTxt(mItemViewCpName,strCpNamme);
        String strDateStart = pEntity.date_start;
        setItemTxt(mItemViewEntryTime,strDateStart);
        String strDateEnd = pEntity.date_end;
        setItemTxt(mItemViewTurnOverTime,strDateEnd);
        String strPos = pEntity.work_position;
        setItemTxt(mItemViewPos,strPos);
        String strIncome = pEntity.work_salary;
        setItemTxt(mItemViewIncome,strIncome);
    }

    private void setItemTxt(ItemView mItemView,String strTxt){
        if(!TextUtils.isEmpty(strTxt)){
            mItemView.setEditTxt(strTxt);
        }else{
            mItemView.setEditTxt("");
        }
    }

    private void showDialogDate(final int src,String strTimeSelect){
        hideDialogDate();
        this.mDialogDate = new DialogDateSelector(this,R.style.MyDialogBg);
        this.mDialogDate.setIDatePickerListener(new DialogDateSelector.IDatePickerListener() {
            @Override
            public void onPickerClick(VDateEntity vEntity) {
                String strInfo = vEntity.getDate();
                if(src == SRC_DATE_START){
                    String strEnd = mItemViewTurnOverTime.getInputTxt();
                    if(!TextUtils.isEmpty(strEnd)){
                        long start = LoanDateUtil.strToLong(strInfo);
                        long end = LoanDateUtil.strToLong(strEnd);
                        if(end >= start){
                            mItemViewEntryTime.setEditTxt(strInfo);
                        }else{
                            String tips = getResources().getString(R.string.myexperience_degree_tips_date_select_return);
                            showToast(tips);
                        }
                    }else{
                        mItemViewEntryTime.setEditTxt(strInfo);
                    }
                }else if(src == SRC_DATE_END){
                    String strStart = mItemViewEntryTime.getInputTxt();
                    if(!TextUtils.isEmpty(strStart)){
                        long start = LoanDateUtil.strToLong(strStart);
                        long end = LoanDateUtil.strToLong(strInfo);
                        if(end >= start){
                            mItemViewTurnOverTime.setEditTxt(strInfo);
                        }else{
                            String tips = getResources().getString(R.string.myexperience_degree_tips_date_select);
                            showToast(tips);
                        }
                    }else{
                        mItemViewTurnOverTime.setEditTxt(strInfo);
                    }
                }
            }
        });
        this.mDialogDate.show();
        this.mDialogDate.updateType(DialogDateSelector.TYPE_EDU_DATE);
        this.mDialogDate.setCurDaySelected();
        //reset date
        if(!TextUtils.isEmpty(strTimeSelect)){
            VDateEntity vEntity = LoanDateUtil.parseVDateEntitiy(strTimeSelect);
            if(vEntity != null){
                if(MyLog.isDebugable()){
                    MyLog.debug(TAG,"[setPos]" + " year:" + vEntity.year + " month:" + vEntity.month + " day:" + vEntity.date);
                }
                mDialogDate.setPos(vEntity.year,vEntity.month,vEntity.date);
            }
        }
    }

    private void hideDialogDate(){
        if(this.mDialogDate != null){
            this.mDialogDate.dismiss();
            this.mDialogDate = null;
        }
    }

    private void hideDialogNor(){
        if(this.mDialogNor != null){
            this.mDialogNor.dismiss();
            this.mDialogNor = null;
        }
    }

    private void showDialogNor(List<String> mList,final int src,String strSelect){
        hideDialogNor();
        this.mDialogNor = new DialogStrNormalList(this,R.style.MyDialogBg);
        this.mDialogNor.show();
        this.mDialogNor.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                BDialogStrEntity bEntity = (BDialogStrEntity) obj;
                if(src == SRC_NOR_POS){
                    mItemViewPos.setEditTxt(bEntity.str);
                }else if(src == SRC_NOR_INCOME){
                    mItemViewIncome.setEditTxt(bEntity.str);
                }
                hideDialogNor();
            }
        });
        String title = "";
        switch(src){
            case SRC_NOR_POS:
                title = "请选择职位";
                break;
            case SRC_NOR_INCOME:
                title = "请选择月薪范围";
                break;
        }
        BDialogStrEntity bEntity = null;
        if(!TextUtils.isEmpty(strSelect)){
            bEntity = new BDialogStrEntity();
            bEntity.str = strSelect;
            bEntity.isSelect = true;
        }
        this.mDialogNor.setStrInfo(DialogStrNormalList.buildNormalList(mList),bEntity,title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogDate();
        hideDialogNor();
    }

    private View.OnClickListener mItemListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(v == mItemViewEntryTime.getEditTxt()){
                String strDate = mItemViewEntryTime.getInputTxt();
                showDialogDate(SRC_DATE_START,strDate);
            }else if(v == mItemViewTurnOverTime.getEditTxt()){
                String strDate = mItemViewTurnOverTime.getInputTxt();
                showDialogDate(SRC_DATE_END,strDate);
            }else if(v == mItemViewPos.getEditTxt()){
                List<String> mList = ExperiController.getInstance().getPostList();
                if(mList != null && mList.size() > 0){
                    String strSelect = mItemViewPos.getInputTxt();
                    showDialogNor(mList,SRC_NOR_POS,strSelect);
                }else{
                    String tips = getString(R.string.common_cfg_empty);
                    showToast(tips);
                }
            }else if(v == mItemViewIncome.getEditTxt()){
                List<String> mList = ExperiController.getInstance().getIncomeList();
                if(mList != null && mList.size() > 0){
                    String strSelect = mItemViewIncome.getInputTxt();
                    showDialogNor(mList,SRC_NOR_INCOME,strSelect);
                }else{
                    String tips = getString(R.string.common_cfg_empty);
                    showToast(tips);
                }
            }
        }
    };

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.mType = intent.getIntExtra(IntentUtils.PARA_KEY_TYPE,TYPE_ADD);
        this.pEntity = (PExperiEntity) intent.getSerializableExtra(IntentUtils.PARA_KEY_DATA);
    }

    private VExperiOccEntity buildResultEntity(boolean isInterrupt){
        VExperiOccEntity vEntity = new VExperiOccEntity();
        String strCpName = mItemViewCpName.getInputTxt();
        vEntity.strCpName = strCpName;
        if(isInterrupt && TextUtils.isEmpty(strCpName)){
            String tips = getString(R.string.myexperience_degree_cpname_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        String strDateStart = mItemViewEntryTime.getInputTxt();
        vEntity.strDateStart = strDateStart;
        if(isInterrupt && TextUtils.isEmpty(strDateStart)){
            String tips = getString(R.string.myexperience_degree_datestart_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        String strDateEnd = mItemViewTurnOverTime.getInputTxt();
        vEntity.strDateEnd = strDateEnd;
        if(isInterrupt && TextUtils.isEmpty(strDateEnd)){
            String tips = getString(R.string.myexperience_degree_dateend_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        String strPos = mItemViewPos.getInputTxt();
        vEntity.strPos = strPos;
        if(isInterrupt && TextUtils.isEmpty(strPos)){
            String tips = getString(R.string.myexperience_degree_pos_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        String strIncome = mItemViewIncome.getInputTxt();
        vEntity.strIncome = strIncome;
        if(isInterrupt && TextUtils.isEmpty(strIncome)){
            String tips = getString(R.string.myexperience_degree_income_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        vEntity.isSucc = true;
        return vEntity;
    }

    @Override
    public void onClick(View v) {
        if(v == this.btnOk){
            VExperiOccEntity vEntity = buildResultEntity(true);
            if(vEntity.isSucc){
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqMyExperiOccAdd(vEntity,getCallBack(),pEntity);
                mReqList.add(seqNo);
            }else{
                String tips = vEntity.msg;
                showToast(tips);
            }
        }
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspExperiAddEntity){
                RspExperiAddEntity rspEntity = (RspExperiAddEntity) rsp;
                if(isSucc){
                    String tips = getResources().getString(R.string.common_request_succ);
                    showToast(tips);
                    setResult(Activity.RESULT_OK);
                    finish();
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                }
            }
        }
    }
}
