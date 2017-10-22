package com.bjzt.uye.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bjzt.uye.R;
import com.bjzt.uye.adapter.UYeAdapter;
import com.bjzt.uye.entity.PInsureOrderEntity;
import com.bjzt.uye.fragments.base.BaseFragment;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspOrderListEntity;
import com.bjzt.uye.msglist.itemview.InsureOrderItemView;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.msglist.PageType;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/20
 */
public class FragmentUYe extends BaseFragment{

    @BindView(R.id.u_header)
    YHeaderView mHeader;
    @BindView(R.id.u_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.u_emptyview)
    BlankEmptyView mEmptyView;

    private List<Integer> mReqList = new ArrayList<>();
    private UYeAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uye_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        mHeader.updateType(YHeaderView.TYPE_RIGHT_TXT_ONLY);
        String title = getResources().getString(R.string.tab_uye);
        mHeader.setTitle(title);
        String txtRight = "0/0";
        mHeader.setRightTxt(txtRight);

        mEmptyView.updateType(BlankEmptyView.TYPE_EMPTY_ORDER);
        mEmptyView.showLoadingState();
        mViewPager.setVisibility(View.GONE);
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        int seqNo = ProtocalManager.getInstance().reqOrderList(PageType.FIRST_PAGE,getCallBack());
        mReqList.add(seqNo);
    }

    ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            reSetRightTxtTips(position+1);
            PInsureOrderEntity pEntity = mAdapter.getItem(position);
            for(int i = 0;i < mViewPager.getChildCount();i++){
                View mView = mViewPager.getChildAt(i);
                if(mView instanceof InsureOrderItemView){
                    InsureOrderItemView itemView = (InsureOrderItemView) mView;
                    itemView.setInfo(mAdapter.getList(),pEntity);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    private void reSetRightTxtTips(int curCnt){
        String tips = "0/0";
        if(mAdapter != null){
            int totalCnt = mAdapter.getCount();
            tips = curCnt + "/" + totalCnt;
        }
        mHeader.setRightTxt(tips);
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            if(rsp instanceof RspOrderListEntity){
                RspOrderListEntity rspEntity = (RspOrderListEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        List<PInsureOrderEntity> mList = rspEntity.buildList();
                        if(mList.size() > 0){
                            mEmptyView.loadSucc();
                            mViewPager.setVisibility(View.VISIBLE);
                            if(this.mAdapter == null){
                                this.mAdapter = new UYeAdapter(Global.getContext(),mList);
                                reSetRightTxtTips(PageType.FIRST_PAGE);
                                this.mViewPager.setAdapter(this.mAdapter);
                            }else{
                                mAdapter.reSetList(mList);
                            }
                        }else{
                            String tips = getResources().getString(R.string.uye_order_empty_tips);
                            initErrorStatus(tips);
                        }
                    }else{
                        String tips = getResources().getString(R.string.common_cfg_empty);
                        initErrorStatus(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    initErrorStatus(tips);
                }
            }
        }
    }

    private void initErrorStatus(String tips){
        mEmptyView.setErrorTips(tips);
        mEmptyView.showErrorState();
        mEmptyView.setBlankListener(new BlankEmptyView.BlankBtnListener() {
            @Override
            public void btnRefresh() {
                mEmptyView.showLoadingState();
                int seqNo = ProtocalManager.getInstance().reqOrderList(1,getCallBack());
                mReqList.add(seqNo);
            }
        });
    }

}
