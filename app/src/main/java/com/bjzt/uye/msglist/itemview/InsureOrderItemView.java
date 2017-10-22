package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.entity.PInsureOrderEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.http.rsp.RspOrderListEntity;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/21.
 */

public class InsureOrderItemView extends LinearLayout{

    @BindView(R.id.scollview)
    ScrollView mScrollView;
    @BindView(R.id.order_title)
    TextView mTxtOrderNo;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private PInsureOrderEntity mEntity;
    private List<PInsureOrderEntity> mList;
    private List<Integer> mReqList = new ArrayList<>();

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
    }

    public void setInfo(List<PInsureOrderEntity> mList,PInsureOrderEntity mEntity){
        this.mEntity = mEntity;
        this.mList = mList;
        boolean needReq;
        if(this.mEntity.isFake){
            needReq = true;
        }else{
            needReq = false;
        }
        if(needReq){
            mScrollView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.showLoadingState();
            int seqNo = ProtocalManager.getInstance().reqOrderList(this.mEntity.p,callBack);
            mReqList.add(seqNo);
        }else{
            mScrollView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            //init order number
            String strOrderInfo = this.mEntity.insured_order.insured_order;
            mTxtOrderNo.setText("订单编号" + strOrderInfo);
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
}
