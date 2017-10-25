package com.bjzt.uye.views.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.adapter.HomeAdAdapter;
import com.bjzt.uye.entity.BStrFontEntity;
import com.bjzt.uye.entity.PAdEntity;
import com.bjzt.uye.entity.VHomeHeaderViewEntity;
import com.bjzt.uye.listener.IViewPagerListener;
import com.bjzt.uye.util.StrUtil;
import com.common.listener.NoConfusion;
import com.common.msglist.base.BaseItemView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/19.
 */

public class HomeHeaderView extends BaseItemView<VHomeHeaderViewEntity> implements NoConfusion{

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.txt_contents)
    TextView mTxtCounter;

    private ArrayList<PAdEntity> mList;
    private HomeAdAdapter mAdapter;
    private IViewPagerListener mPagerListener;
    private VHomeHeaderViewEntity mEntity;

    public HomeHeaderView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(VHomeHeaderViewEntity vHomeHeaderViewEntity) {
        this.mEntity = vHomeHeaderViewEntity;
        setInfo(this.mEntity.mList,this.mEntity.strOrderCnt);
    }

    @Override
    public VHomeHeaderViewEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        init();
    }

    public HomeHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.home_headerview_layout,this,true);
        ButterKnife.bind(this);
    }

    public void setIPageListener(IViewPagerListener mListener){
        this.mPagerListener = mListener;
    }

    public void setInfo(ArrayList<PAdEntity> mList,String strCountOrderInfo){
        this.mList = mList;
        if(this.mList != null && this.mList.size() > 0){
            if(mAdapter != null){
                mAdapter.resetList(mList);
            }else{
                mAdapter = new HomeAdAdapter(mList);
                mAdapter.setIPagerListener(mPagerListener);
                mViewPager.setAdapter(mAdapter);
            }
        }
        if(!TextUtils.isEmpty(strCountOrderInfo)){
            mTxtCounter.setText(strCountOrderInfo);
            //
            List<BStrFontEntity> fList = StrUtil.buildStrNumList(strCountOrderInfo);
            if(fList != null && fList.size() > 0){
                SpannableStringBuilder builder = StrUtil.getSpansStrBuilder(strCountOrderInfo,fList);
                mTxtCounter.setText(builder);
            }
        }
    }
}
