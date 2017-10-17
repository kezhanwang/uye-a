package com.bjzt.uye.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.entity.PFaceVerifyEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspFaceVerifyCfgEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BindCardPhotoView;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.YHeaderView;

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

    private List<Integer> mReqList = new ArrayList<Integer>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_apply_id_layout;
    }

    @Override
    protected void initLayout() {
        mHeader.updateType(YHeaderView.TYPE_RIGHT_TXT);
        String title = "身份认证";
        mHeader.setTitle(title);
        String txtRight = "启动识别";
        mHeader.setRightTxt(txtRight);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

        //init name
        String hint = "请输入本人姓名";
        title = "姓名";
        itemName.setTitle(title);
        itemName.setHint(hint);
        itemName.hideArrow();
        title = "身份证号";
        hint = "请输入本人身份证号";
        itemID.setTitle(title);
        itemID.setHint(hint);
        itemID.hideArrow();
        title = "证件日期";
        hint = "请选择身份证起始日期";
        itemStart.setTitle(title);
        itemStart.setHint(hint);
        itemStart.showArrow();
        itemStart.setEditAble(false);
        itemStart.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        hint = "请选择身份证失效日期";
        itemEnd.setHint(hint);
        itemEnd.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        itemEnd.showArrow();
        itemEnd.setEditAble(false);
        itemEnd.hideTitle();
        //addr
        title = "证件住址";
        hint = "请输入您身份证上的地址";
        itemAddr.setTitle(title);
        itemAddr.setHint(hint);
        itemAddr.hideArrow();
        itemAddr.hideBottomLine();

        photoViewFront.updateType(BindCardPhotoView.TYPE_IMG_ID);
        photoViewFront.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });

        photoViewBack.updateType(BindCardPhotoView.TYPE_IMG_ID_BACK);
        photoViewBack.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });

        //银行卡号
        title = "银行卡号";
        hint = "请输入银行卡号";
        itemBankCard.setTitle(title);
        itemBankCard.setHint(hint);
        itemBankCard.hideArrow();
        itemBankCard.setInputTypeNumber(ItemView.INPUT_TYPE_BANK);

        //开户行
        title = "开户行";
        hint = "请选择开户行名称";
        itemBank.setTitle(title);
        itemBank.setHint(hint);
        itemBank.showArrow();
        itemBank.setEditAble(false);
        itemBank.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

        title = "手机号";
        hint = "请输入本人实名制手机号";
        itemPhone.setTitle(title);
        itemPhone.setHint(hint);
        itemPhone.setInputTypeNumber(ItemView.INPUT_TYPE_NUMBER);
        itemPhone.setType(ItemView.TYPE_VERIFY);
        itemPhone.setBtnListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });

        title = "验证码";
        hint = "请输入手机验证码";
        itemVerify.setTitle(title);
        itemVerify.setHint(hint);
        itemVerify.setEditTxtBtnListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        itemVerify.setInputTypeNumber(ItemView.INPUT_TYPE_NUMBER);
        itemVerify.setMaxCntInput(8);

        btnOk.setOnClickListener(this);

//        mEmptyView.showLoadingState();
        int seqNo = ProtocalManager.getInstance().reqFaceVerifyCfg(getCallBack());
        mReqList.add(seqNo);
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
                        openFaceAuth(rspEntity.mEntity);
                    }else{
                        String tips = getResources().getString(R.string.common_request_error);
                        showToast(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                }
            }
        }
    }

    private void openFaceAuth(PFaceVerifyEntity pEntity){

    }

    @Override
    protected void initExtras(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {

    }
}
