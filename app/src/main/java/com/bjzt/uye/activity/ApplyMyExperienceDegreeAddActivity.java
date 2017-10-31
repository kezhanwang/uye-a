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
import com.bjzt.uye.activity.dialog.DialogDateSelector;
import com.bjzt.uye.activity.dialog.DialogStrNormalList;
import com.bjzt.uye.controller.ExperiController;
import com.bjzt.uye.entity.BDialogStrEntity;
import com.bjzt.uye.entity.PExperiEntity;
import com.bjzt.uye.entity.VDateEntity;
import com.bjzt.uye.entity.VExperiDegreeAddEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspExperiAddEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.LoanDateUtil;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.common.MyLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by billy on 2017/10/30.
 */

public class ApplyMyExperienceDegreeAddActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.item_degree)
    ItemView mItemViewDegree;
    @BindView(R.id.item_schname)
    ItemView mItemViewSchName;
    @BindView(R.id.item_schaddr)
    ItemView mItemViewSchAddr;
    @BindView(R.id.item_major)
    ItemView mItemViewMajor;
    @BindView(R.id.item_entrytime)
    ItemView mItemViewEntryTime;
    @BindView(R.id.item_graduation)
    ItemView mItemViewGraduTime;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private DialogStrNormalList mDialogNor;
    private DialogDateSelector mDialogDate;

    private final int SRC_DEGREE = 1;

    private final int SRC_DATE_START = 1;
    private final int SRC_DATE_END = 2;
    private List<Integer> mReqList = new ArrayList<>();


    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;
    private int mType = TYPE_ADD;
    private PExperiEntity pEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_myexperience_degree_add_layout;
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
        String title = "添加学历";
        if(mType == TYPE_EDIT){
            title = "编辑学历";
        }
        mHeader.setTitle(title);

        //degree
        title = "学历";
        String hint = getResources().getString(R.string.myexperience_degree_hint);
        mItemViewDegree.setTitle(title);
        mItemViewDegree.setHint(hint);
        mItemViewDegree.setEditAble(false);
        mItemViewDegree.showArrow();
        mItemViewDegree.setEditTxtBtnListener(mItemListener);

        //sch name
        title = "学校名称";
        hint = getResources().getString(R.string.myexperience_degree_schname_hint);
        mItemViewSchName.setTitle(title);
        mItemViewSchName.setHint(hint);

        //sch addr
        title = "学校地址";
        hint = getResources().getString(R.string.myexperience_degree_schaddr_hint);
        mItemViewSchAddr.setTitle(title);
        mItemViewSchAddr.setHint(hint);

        //major
        title = "专业";
        hint = getString(R.string.myexperience_degree_major_hint);
        mItemViewMajor.setTitle(title);
        mItemViewMajor.setHint(hint);

        //entry time
        title = "入学时间";
        hint = getResources().getString(R.string.myexperience_degree_startdate_hint);
        mItemViewEntryTime.setTitle(title);
        mItemViewEntryTime.setHint(hint);
        mItemViewEntryTime.setEditAble(false);
        mItemViewEntryTime.showArrow();
        mItemViewEntryTime.setEditTxtBtnListener(mItemListener);

        //graduate time
        title = "毕业时间";
        hint = getString(R.string.myexperience_degree_endate_hint);
        mItemViewGraduTime.setTitle(title);
        mItemViewGraduTime.setHint(hint);
        mItemViewGraduTime.setEditAble(false);
        mItemViewGraduTime.showArrow();
        mItemViewGraduTime.setEditTxtBtnListener(mItemListener);
        btnOk.setOnClickListener(this);

        if(pEntity != null){
            fillInVal(pEntity);
        }
    }

    private void fillInVal(PExperiEntity pEntity){
        //degree
        String strDegree = pEntity.education;
        setItemTxt(mItemViewDegree,strDegree);
        //sch name
        String strSchName = pEntity.school_name;
        setItemTxt(mItemViewSchName,strSchName);
        //sch addr
        String strSchAddr = pEntity.school_address;
        setItemTxt(mItemViewSchAddr,strSchAddr);
        //major
        String strMajor = pEntity.school_profession;
        setItemTxt(mItemViewMajor,strMajor);
        //start date
        String strDateStart = pEntity.date_start;
        setItemTxt(mItemViewEntryTime,strDateStart);
        //end date
        String strDateEnd = pEntity.date_end;
        setItemTxt(mItemViewGraduTime,strDateEnd);
    }

    private void setItemTxt(ItemView mItemView,String strInfo){
        if(!TextUtils.isEmpty(strInfo)){
            mItemView.setEditTxt(strInfo);
        }else{
            mItemView.setEditTxt("");
        }
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.mType = intent.getIntExtra(IntentUtils.PARA_KEY_TYPE,TYPE_ADD);
        this.pEntity = (PExperiEntity) intent.getSerializableExtra(IntentUtils.PARA_KEY_DATA);
    }

    private View.OnClickListener mItemListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(v == mItemViewEntryTime.getEditTxt()){
                showDialogDate(SRC_DATE_START,mItemViewEntryTime.getInputTxt());
            }else if(v == mItemViewGraduTime.getEditTxt()){
                showDialogDate(SRC_DATE_END,mItemViewGraduTime.getInputTxt());
            }else if(v == mItemViewDegree.getEditTxt()){
                List<String> mList = ExperiController.getInstance().getDegreeList();
                if(mList != null && mList.size() > 0){
                    showDialogStrNor(mList,SRC_DEGREE,mItemViewDegree.getInputTxt());
                }else{
                    String tips = getResources().getString(R.string.common_cfg_empty);
                    showToast(tips);
                }
            }
        }
    };

    private void showDialogDate(final int src,String strDate){
        hideDialogDate();
        this.mDialogDate = new DialogDateSelector(this,R.style.MyDialogBg);
        this.mDialogDate.setIDatePickerListener(new DialogDateSelector.IDatePickerListener() {
            @Override
            public void onPickerClick(VDateEntity vEntity) {
                String strDate = vEntity.getDate();
                if(src == SRC_DATE_START){
                    mItemViewEntryTime.setEditTxt(strDate);
                }else{
                    mItemViewGraduTime.setEditTxt(strDate);
                }
            }
        });
        this.mDialogDate.show();
        this.mDialogDate.updateType(DialogDateSelector.TYPE_EDU_DATE);
        this.mDialogDate.setCurDaySelected();

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

    private void hideDialogDate(){
        if(this.mDialogDate != null){
            this.mDialogDate.dismiss();
            this.mDialogDate = null;
        }
    }

    private void showDialogStrNor(List<String> mList, int src,String strSelect){
        hideDialogStrNor();
        this.mDialogNor = new DialogStrNormalList(this,R.style.MyDialogBg);
        this.mDialogNor.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                hideDialogStrNor();
                BDialogStrEntity bEntity = (BDialogStrEntity) obj;
                mItemViewDegree.setEditTxt(bEntity.str);
            }
        });
        this.mDialogNor.show();
        String title = "选择最高学历";
        BDialogStrEntity bEntity = null;
        if(!TextUtils.isEmpty(strSelect)){
            bEntity = new BDialogStrEntity();
            bEntity.isSelect = true;
            bEntity.str = strSelect;
        }
        this.mDialogNor.setStrInfo(DialogStrNormalList.buildNormalList(mList),bEntity,title);
    }

    private void hideDialogStrNor() {
        if(this.mDialogNor != null){
            this.mDialogNor.dismiss();
            this.mDialogNor = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogStrNor();
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspExperiAddEntity){
                RspExperiAddEntity rspEntity = (RspExperiAddEntity) rsp;
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

    @Override
    public void onClick(View v) {
        if(v == this.btnOk){
            VExperiDegreeAddEntity vEntity = buildResultEntity(true);
            if(vEntity.isSucc){
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqMyExperiDegreeAdd(vEntity,getCallBack(),pEntity);
                mReqList.add(seqNo);
            }else{
                String tips = vEntity.msg;
                showToast(tips);
            }
        }
    }

    private VExperiDegreeAddEntity buildResultEntity(boolean isInterrupt){
        VExperiDegreeAddEntity rEntity = new VExperiDegreeAddEntity();
        String strCpName = mItemViewDegree.getInputTxt();
        rEntity.strDegree = strCpName;
        if(isInterrupt && TextUtils.isEmpty(strCpName)){
            String tips = getString(R.string.myexperience_degree_hint);
            rEntity.msg = tips;
            return rEntity;
        }
        String strSchName = mItemViewSchName.getInputTxt();
        rEntity.strSchName = strSchName;
        if(isInterrupt && TextUtils.isEmpty(strSchName)){
            String tips = getString(R.string.myexperience_degree_schname_hint);
            rEntity.msg = tips;
            return rEntity;
        }
        String strSchAddr = mItemViewSchAddr.getInputTxt();
        rEntity.strSchAddr = strSchAddr;
        if(isInterrupt && TextUtils.isEmpty(strSchAddr)){
            String tips = getString(R.string.myexperience_degree_schaddr_hint);
            rEntity.msg = tips;
            return rEntity;
        }
        String strMajor = mItemViewMajor.getInputTxt();
        rEntity.strMajor = strMajor;
        if(isInterrupt && TextUtils.isEmpty(strMajor)){
            String tips = getString(R.string.myexperience_degree_major_hint);
            rEntity.msg = tips;
            return rEntity;
        }
        String strDateStart = mItemViewEntryTime.getInputTxt();
        rEntity.strDateStart = strDateStart;
        if(isInterrupt && TextUtils.isEmpty(strDateStart)){
            String tips = getString(R.string.myexperience_degree_startdate_hint);
            rEntity.msg = tips;
            return rEntity;
        }
        String strDateEnd = mItemViewGraduTime.getInputTxt();
        rEntity.strDateEnd = strDateEnd;
        if(isInterrupt && TextUtils.isEmpty(strDateEnd)){
            String tips = getString(R.string.myexperience_degree_endate_hint);
            rEntity.msg = tips;
            return rEntity;
        }
        rEntity.isSucc = true;
        return rEntity;
    }
}
