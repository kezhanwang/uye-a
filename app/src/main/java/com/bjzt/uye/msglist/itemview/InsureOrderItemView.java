package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.entity.PInsureOrderEntity;
import com.bjzt.uye.entity.PTrainEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.http.rsp.RspOrderListEntity;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/21.
 */

public class InsureOrderItemView extends LinearLayout implements  View.OnClickListener{

    @BindView(R.id.scollview)
    ScrollView mScrollView;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.order_title)
    TextView mTxtOrderNo;
    @BindView(R.id.txt_type)
    TextView mTxtType;
    @BindView(R.id.txt_tution)
    TextView mTxtTution;
    @BindView(R.id.order_txt_status)
    TextView mTxtStatus;
    @BindView(R.id.btn_employ_pross)
    Button btnEmployPross;
    @BindView(R.id.btn_employed)
    Button btnEmployed;

    @BindView(R.id.txt_payout)
    TextView mTxtPayoutLimited;
    @BindView(R.id.txt_employ_date)
    TextView mTxtEmployDate;
    @BindView(R.id.txt_payout_date)
    TextView mTxtPayoutDate;
    @BindView(R.id.txt_train_date)
    TextView mTxtTrainDate;
    @BindView(R.id.txt_retrain_date)
    TextView mTxtReTrainDate;
    @BindView(R.id.txt_gradu_date)
    TextView mTxtEndTrainDate;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private PInsureOrderEntity mEntity;
    private List<PInsureOrderEntity> mList;
    private List<Integer> mReqList = new ArrayList<>();
    private IItemListener mListener;

    public static final int SRC_EMPLOYED = 1;
    public static final int SRC_EMPOY_PROGRESS = 2;

    public InsureOrderItemView(Context context) {
        super(context);
        init();
    }

    public InsureOrderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.insure_order_itemview_layout,this,true);
        ButterKnife.bind(this);

        btnEmployPross.setOnClickListener(this);
        btnEmployed.setOnClickListener(this);
    }

    public void reqInfo(PInsureOrderEntity mEntity){
        this.mEntity = mEntity;
        if(this.mEntity.insured_order == null){
            mScrollView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.showLoadingState();
            int seqNo = ProtocalManager.getInstance().reqOrderList(this.mEntity.p,callBack);
            mReqList.add(seqNo);
        }else{
            setInfo(mList,mEntity);
        }
    }

    public PInsureOrderEntity getMsgEntity(){
        return this.mEntity;
    }

    public void setInfo(List<PInsureOrderEntity> mList,PInsureOrderEntity mEntity){
        this.mEntity = mEntity;
        this.mList = mList;
        mScrollView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();
        if(mEntity.insured_order != null){
            mScrollView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            //init order number
            String strOrderInfo = this.mEntity.insured_order.insured_order;
            String strOrderSimple = StrUtil.getInsureOrderSimple(strOrderInfo);
            mTxtOrderNo.setText("订单编号:  " + strOrderSimple);
            //init type
            String strType = this.mEntity.insured_order.insured_type;
            mTxtType.setText("类型: " + strType);
            //init tution
            String strTution = StrUtil.getMoneyInfoByFen(this.mEntity.insured_order.tuition);
            mTxtTution.setText(strTution);
            //init status
            String strStatus = this.mEntity.insured_order.insured_status_desp;
            mTxtStatus.setText(strStatus);
            //pic logo
            String picUrl = this.mEntity.insured_order.org_logo;
            if(!TextUtils.isEmpty(picUrl)){
                PicController.getInstance().showPic(imgLogo,picUrl);
            }
            //赔付上限
            String strInfo = StrUtil.getMoneyInfoByFen(this.mEntity.insured_order.premium_amount_top);
            mTxtPayoutLimited.setText(strInfo);
            //择业日期
            String strEmployDate = this.mEntity.insured_order.career_time;
            mTxtEmployDate.setText(strEmployDate);
            //理赔日期
            mTxtPayoutDate.setText(this.mEntity.insured_order.repay_time);

            //培训时间
            PTrainEntity pTrain = this.mEntity.insured_order.train;
            if(pTrain != null){
                mTxtTrainDate.setText(pTrain.first_train);
                mTxtReTrainDate.setText(pTrain.second_train);
                mTxtEndTrainDate.setText(pTrain.end_train);
            }else{
                mTxtTrainDate.setText("");
                mTxtReTrainDate.setText("");
                mTxtEndTrainDate.setText("");
            }
        }
    }

    public void recyle(){

    }

    private ICallBack<Object> callBack = new ICallBack<Object>() {
        @Override
        public void getResponse(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
            if(mReqList.contains(Integer.valueOf(seqNo))){
                RspOrderListEntity rspEntity = (RspOrderListEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        PInsureOrderEntity pEntity = rspEntity.mEntity;
                        pEntity.isFake = false;
                        pEntity.p = mEntity.p;
                        pEntity.insured_order = rspEntity.mEntity.insured_order;
                        pEntity.page = rspEntity.mEntity.page;
                        //
                        mEntity.insured_order = pEntity.insured_order;
                        mEntity.page = pEntity.page;
                        mEntity.isFake = pEntity.isFake;
                        setInfo(mList,mEntity);
                    }else{
                        String tips = getResources().getString(R.string.common_cfg_error);
                        initErrorStatus(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    initErrorStatus(tips);
                }
            }
        }
    };

    private void initErrorStatus(String tips){
        mEmptyView.showErrorState();
        mEmptyView.setErrorTips(tips);
        mEmptyView.setBlankListener(new BlankEmptyView.BlankBtnListener() {
            @Override
            public void btnRefresh() {
                mEmptyView.showLoadingState();
                int seqNo = ProtocalManager.getInstance().reqOrderList(mEntity.p,callBack);
                mReqList.add(seqNo);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.btnEmployed){
                this.mListener.onItemClick(null,SRC_EMPLOYED);
            }else{
                this.mListener.onItemClick(null,SRC_EMPOY_PROGRESS);
            }
        }
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
