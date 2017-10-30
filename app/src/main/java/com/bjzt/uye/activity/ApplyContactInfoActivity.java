package com.bjzt.uye.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.activity.dialog.DialogConfirmSingle;
import com.bjzt.uye.activity.dialog.DialogLocation;
import com.bjzt.uye.activity.dialog.DialogStrNormalList;
import com.bjzt.uye.entity.BDialogStrEntity;
import com.bjzt.uye.entity.PContactCfgEntity;
import com.bjzt.uye.entity.PContactInfoEntity;
import com.bjzt.uye.entity.PLocItemEntity;
import com.bjzt.uye.entity.VContactInfoEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspContactCfgEntity;
import com.bjzt.uye.http.rsp.RspContactInfoEntity;
import com.bjzt.uye.http.rsp.RspContactSubmitEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.ItemViewTitle;
import com.bjzt.uye.views.component.YHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 联系信息
 * Created by billy on 2017/10/26.
 */
public class ApplyContactInfoActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.itemview_title_info)
    ItemViewTitle itemViewTitleInfo;
    @BindView(R.id.item_addr)
    ItemView mItemAddr;
    @BindView(R.id.item_addr_detail)
    ItemView mItemAddrDetail;
    @BindView(R.id.item_marriage)
    ItemView mItemMarriage;
    @BindView(R.id.item_email)
    ItemView mItemMail;
    @BindView(R.id.item_wechat)
    ItemView mItemWeChat;
    @BindView(R.id.item_qq)
    ItemView mItemQQ;
    @BindView(R.id.itemview_title_sec)
    ItemViewTitle itemViewTitleSec;
    @BindView(R.id.item_sec_rela)
    ItemView mItemSecRela;              //第二联系人关系
    @BindView(R.id.item_sec_name)
    ItemView mItemSecName;
    @BindView(R.id.item_sec_phone)
    ItemView mItemSecPhone;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private DialogLocation mDialogLoc;
    private DialogConfirmSingle mDialogCfg;
    private DialogStrNormalList mDialogNor;

    private PLocItemEntity mLocPro;
    private PLocItemEntity mLocCity;
    private PLocItemEntity mLocArea;
    private PContactCfgEntity pCfgEntity;

    private List<Integer> mReqList = new ArrayList<>();

    private final int SRC_MARRIAGE = 1;
    private final int SRC_RELA = 2;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_contactinfo_layout;
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
        String title = getResources().getString(R.string.contact_info_title);
        mHeader.setTitle(title);

        title = getResources().getString(R.string.contact_info_info);
        itemViewTitleInfo.setTitle(title);
        title = getResources().getString(R.string.contact_info_addr);
        String hint = getResources().getString(R.string.contact_info_addr_hint);
        mItemAddr.setTitle(title);
        mItemAddr.setHint(hint);
        mItemAddr.setEditAble(false);
        mItemAddr.showArrow();
        mItemAddr.setBottomLineMargin();
        mItemAddr.setEditTxtBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLoc(mLocPro,mLocCity,mLocArea);
            }
        });

        hint = getResources().getString(R.string.contact_info_addr_detail_hint);
        mItemAddrDetail.hideTitle();
        mItemAddrDetail.setHint(hint);

        title = getResources().getString(R.string.contact_info_marriage);
        hint = getResources().getString(R.string.contact_info_marriage_hint);
        mItemMarriage.setTitle(title);
        mItemMarriage.setHint(hint);
        mItemMarriage.setEditAble(false);
        mItemMarriage.showArrow();
        mItemMarriage.setEditTxtBtnListener(mItemClickListener);

        title = getResources().getString(R.string.contact_info_email);
        hint = getResources().getString(R.string.contact_info_email_hint);
        mItemMail.setTitle(title);
        mItemMail.setHint(hint);

        title = getResources().getString(R.string.contact_info_wechat);
        hint = getResources().getString(R.string.contact_info_wechat_hint);
        mItemWeChat.setTitle(title);
        mItemWeChat.setHint(hint);

        title = getResources().getString(R.string.contact_info_qq);
        hint = getResources().getString(R.string.contact_info_qq_hint);
        mItemQQ.setTitle(title);
        mItemQQ.setHint(hint);
        mItemQQ.setInputTypeNumber(ItemView.INPUT_TYPE_NUMBER);

        title = getResources().getString(R.string.contact_info_sec);
        itemViewTitleSec.setTitle(title);
        title = getResources().getString(R.string.contact_info_sec_rela);
        hint = getResources().getString(R.string.contact_info_sec_hint);
        mItemSecRela.setTitle(title);
        mItemSecRela.setHint(hint);
        mItemSecRela.setEditAble(false);
        mItemSecRela.showArrow();
        mItemSecRela.setEditTxtBtnListener(mItemClickListener);

        title = getResources().getString(R.string.name);
        hint = getResources().getString(R.string.contact_info_sec_name_hint);
        mItemSecName.setTitle(title);
        mItemSecName.setHint(hint);

        title = getResources().getString(R.string.contact_info_sec_phone);
        hint = getResources().getString(R.string.contact_info_sec_phone_hint);
        mItemSecPhone.setTitle(title);
        mItemSecPhone.setHint(hint);
        mItemSecPhone.setInputTypeNumber(ItemView.INPUT_TYPE_NUMBER);

        btnOk.setOnClickListener(this);

        mEmptyView.setVisibility(View.VISIBLE);
        mScrollView.setVisibility(View.GONE);
        mEmptyView.showLoadingState();
        //
        int seqNo = ProtocalManager.getInstance().reqContactCfg(getCallBack());
        mReqList.add(seqNo);
    }

    private View.OnClickListener mItemClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(v == mItemMarriage.getEditTxt()){
                if(pCfgEntity != null && pCfgEntity.marriage != null && pCfgEntity.marriage.size() > 0){
                    List<String> mList = pCfgEntity.marriage;
                    showDialogNor(mList,SRC_MARRIAGE);
                }else{
                    String tips = getResources().getString(R.string.common_cfg_empty);
                    showToast(tips);
                }
            }else if(v == mItemSecRela.getEditTxt()){
                if(pCfgEntity != null && pCfgEntity.relation != null && pCfgEntity.relation.size() > 0){
                    List<String> mList = pCfgEntity.relation;
                    showDialogNor(mList,SRC_RELA);
                }else{
                    String tips = getResources().getString(R.string.common_cfg_empty);
                    showToast(tips);
                }
            }
        }
    };

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspContactCfgEntity){
                RspContactCfgEntity rspEntity = (RspContactCfgEntity) rsp;
                if(isSucc && rspEntity.mEntity != null){
                    this.pCfgEntity = rspEntity.mEntity;
                    mEmptyView.loadSucc();
                    mScrollView.setVisibility(View.VISIBLE);
                    //请求回填信息
                    int seqData = ProtocalManager.getInstance().reqContactInfo(getCallBack());
                    mReqList.add(seqData);
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    mEmptyView.showErrorState();
                    mEmptyView.setErrorTips(tips);
                    showCfgDialog(tips);
                }
            }else if(rsp instanceof RspContactInfoEntity){
                RspContactInfoEntity rspEntity = (RspContactInfoEntity) rsp;
                if(isSucc && rspEntity.mEntity != null && rspEntity.mEntity.isOk()){
                    initParams(rspEntity.mEntity);
                }
            }else if(rsp instanceof RspContactSubmitEntity){
                RspContactSubmitEntity rspEntity = (RspContactSubmitEntity) rsp;
                if(isSucc){
                    String tips = getResources().getString(R.string.common_request_succ);
                    showToast(tips);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        }
    }

    private void initParams(PContactInfoEntity pEntity){
        //init marriage
        String strMarriage = pEntity.marriage;
        setItemTxt(mItemMarriage,strMarriage);
        //email
        String strMail = pEntity.email;
        setItemTxt(mItemMail,strMail);
        //wechat
        String strWechat = pEntity.wechat;
        setItemTxt(mItemWeChat,strWechat);
        //QQ
        String strQQ = pEntity.qq;
        setItemTxt(mItemQQ,strQQ);
        //联系人
        String strSecRela = pEntity.contact1_relation;
        setItemTxt(mItemSecRela,strSecRela);
        //sec name
        String strSecName = pEntity.contact1_name;
        setItemTxt(mItemSecName,strSecName);
        //phone
        String strPhone = pEntity.contact1_phone;
        setItemTxt(mItemSecPhone,strPhone);
    }

    private void setItemTxt(ItemView itemView,String strInfo){
        if(!TextUtils.isEmpty(strInfo)){
            itemView.setEditTxt(strInfo);
        }else{
            itemView.setEditTxt("");
        }
    }

    private void showDialogNor(List<String> mList,final int src){
        hideDialogNor();
        this.mDialogNor = new DialogStrNormalList(this,R.style.MyDialogBg);
        this.mDialogNor.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                BDialogStrEntity pEntity = (BDialogStrEntity) obj;
                if(src == SRC_MARRIAGE){
                    String strInfo = pEntity.str;
                    mItemMarriage.setEditTxt(strInfo);
                }else if(src == SRC_RELA){
                    String strInfo = pEntity.str;
                    mItemSecRela.setEditTxt(strInfo);
                }
                hideDialogNor();
            }
        });
        this.mDialogNor.show();
        String title = null;
        switch(src){
            case SRC_MARRIAGE:
                title = "请选择婚姻状况";
                break;
            case SRC_RELA:
                title = "选择与本人关系";
                break;
        }
        this.mDialogNor.setStrInfo(DialogStrNormalList.buildNormalList(mList),null,title);

    }

    private void hideDialogNor(){
        if(this.mDialogNor != null){
            this.mDialogNor.dismiss();
            this.mDialogNor = null;
        }
    }

    private void showCfgDialog(String tips){
        hideCfgDialog();
        this.mDialogCfg = new DialogConfirmSingle(this,R.style.MyDialogBg);
        this.mDialogCfg.show();
        this.mDialogCfg.setMCancleable(false);
        this.mDialogCfg.setContents(tips,null);
        this.mDialogCfg.setIBtnListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {
                finish();
            }
        });
    }

    private void hideCfgDialog(){
        if(this.mDialogCfg != null){
            this.mDialogCfg.dismiss();
            this.mDialogCfg = null;
        }
    }

    private void showDialogLoc(PLocItemEntity mLocPro, PLocItemEntity mLocCity, PLocItemEntity mLocArea){
        this.mDialogLoc = new DialogLocation(this,R.style.MyDialogBg);
        this.mDialogLoc.show();
        this.mDialogLoc.setIListener(new DialogLocation.LoanIDialogLocListener() {
            @Override
            public void onLocSelect(PLocItemEntity mEntityPro, PLocItemEntity mEntityCity, PLocItemEntity mEntityArea) {
                ApplyContactInfoActivity.this.mLocPro = mEntityPro;
                ApplyContactInfoActivity.this.mLocCity = mEntityCity;
                ApplyContactInfoActivity.this.mLocArea = mEntityArea;
                String strInfo = StrUtil.getLocAddrStr(mEntityPro,mEntityCity,mEntityArea);
                mItemAddr.setEditTxt(strInfo);
            }
        });
        if(mLocPro != null && mLocCity != null && mLocArea != null){
            mDialogLoc.setSelectInfo(mLocPro,mLocCity,mLocArea);
        }
    }

    private void hideDialogLoc(){
        if(this.mDialogLoc != null){
            this.mDialogLoc.dismiss();
            this.mDialogLoc = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogLoc();
        hideCfgDialog();
        hideDialogNor();
    }

    @Override
    protected void initExtras(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        if(v == this.btnOk){
            VContactInfoEntity vEntity =  buildVResultEntity(true);
            if(vEntity.isSucc){
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqContactSubmit(vEntity,getCallBack());
                mReqList.add(seqNo);
            }else{
                String tips = vEntity.msg;
                showToast(tips);
            }
        }
    }

    private VContactInfoEntity buildVResultEntity(boolean isInterrupt){
        VContactInfoEntity vEntity = new VContactInfoEntity();
        //marriage
        String strMarriage = mItemMarriage.getInputTxt();
        vEntity.strMarriage = strMarriage;
        if(isInterrupt && TextUtils.isEmpty(strMarriage)){
            String tips = getResources().getString(R.string.contact_info_marriage_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //email
        String strMail = mItemMail.getInputTxt();
        vEntity.strMail = strMail;
        if(isInterrupt && TextUtils.isEmpty(strMail)){
            String tips = getResources().getString(R.string.contact_info_email_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //wechat
        String strWechat = mItemWeChat.getInputTxt();
        vEntity.strWechat = strWechat;
        if(isInterrupt && TextUtils.isEmpty(strWechat)){
            String tips = getResources().getString(R.string.contact_info_wechat_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //loc pro,city,area
        vEntity.mLocPro = this.mLocPro;
        vEntity.mLocCity = this.mLocCity;
        vEntity.mLocArea = this.mLocArea;
        if(isInterrupt && (mLocPro == null || mLocCity == null || mLocArea == null)){
            String tips = getResources().getString(R.string.contact_info_addr_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //address detail
        String strAddrDetail = mItemAddrDetail.getInputTxt();
        vEntity.strAddrDetail = strAddrDetail;
        if(isInterrupt && TextUtils.isEmpty(strAddrDetail)){
            String tips = getResources().getString(R.string.contact_info_addr_detail_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //QQ
        String strQQ = mItemQQ.getInputTxt();
        vEntity.strQQ = strQQ;
        if(isInterrupt && TextUtils.isEmpty(strQQ)){
            String tips = getResources().getString(R.string.contact_info_qq_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //sec rela
        String strRela = mItemSecRela.getInputTxt();
        vEntity.strSecRela = strRela;
        if(isInterrupt && TextUtils.isEmpty(strRela)){
            String tips = getResources().getString(R.string.contact_info_sec_hint) + getResources().getString(R.string.contact_info_sec);
            vEntity.msg = tips;
            return vEntity;
        }
        //sec name
        String strSecName = mItemSecName.getInputTxt();
        vEntity.strSecName = strSecName;
        if(isInterrupt && TextUtils.isEmpty(strSecName)){
            String tips = getResources().getString(R.string.contact_info_sec_name_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        //sec phone
        String strSecPhone = mItemSecPhone.getInputTxt();
        vEntity.strSecPhone = strSecPhone;
        if(isInterrupt && TextUtils.isEmpty(strSecPhone)){
            String tips = getResources().getString(R.string.contact_info_sec_phone_hint);
            vEntity.msg = tips;
            return vEntity;
        }
        vEntity.isSucc = true;
        return vEntity;
    }
}
