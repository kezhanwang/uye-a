package com.bjzt.uye.views.component;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.entity.PHomeOrderEntity;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.StrUtil;
import com.common.listener.NoConfusion;
import com.common.msglist.base.BaseItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/19.
 */

public class HomeOrderInfoView extends BaseItemView<PHomeOrderEntity> implements NoConfusion, View.OnClickListener{

    @BindView(R.id.txtview_max_payout)
    TextView mTxtMaxP;
    @BindView(R.id.txt_ordercnt)
    TextView mTxtOrderCnt;
    @BindView(R.id.txt_payed_tution)
    TextView mTxtPayOut;
    @BindView(R.id.home_order_rela_left)
    RelativeLayout relaLeft;
    @BindView(R.id.home_order_rela_right)
    RelativeLayout relaRight;

    private PHomeOrderEntity mEntity;
    private IItemListener mListener;

    public static final int SRC_RELA_LEFT = 1;
    public static final int SRC_RELA_RIGHT = 2;

    public HomeOrderInfoView(Context context) {
        super(context);
    }

    public HomeOrderInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void setMsg(PHomeOrderEntity pHomeOrderEntity) {
        this.mEntity = pHomeOrderEntity;
        setInfo(this.mEntity.vTop,this.mEntity.count,this.mEntity.paid_compensation);
    }

    @Override
    public PHomeOrderEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.home_orderinfo_view_layout,this,true);
        ButterKnife.bind(this);

        relaLeft.setOnClickListener(this);
        relaRight.setOnClickListener(this);
    }

    public void setInfo(long maxPayout,int orderCnt,int payOut){
        //max tution
        String str = StrUtil.getHomeTutionStr(maxPayout);
        mTxtMaxP.setText(str);
        mTxtOrderCnt.setText(orderCnt+"");
        Context mContext = getContext();
        String strPayout = mContext.getResources().getString(R.string.common_money_info,payOut);
        mTxtPayOut.setText(strPayout);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            if(v == relaLeft){
                mListener.onItemClick(this,SRC_RELA_LEFT);
            }
        }
    }
}
