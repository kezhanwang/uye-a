package com.bjzt.uye.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.LoginActivity;
import com.bjzt.uye.activity.MainActivity;
import com.bjzt.uye.adapter.UYeAdapter;
import com.bjzt.uye.controller.OtherController;
import com.bjzt.uye.entity.PInsureOrderEntity;
import com.bjzt.uye.fragments.base.BaseFragment;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspOrderListEntity;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.InsureOrderItemView;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.controller.LoginController;
import com.common.listener.ILoginListener;
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
    private boolean isLoaded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uye_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        mHeader.updateType(YHeaderView.TYPE_RIGHT_TXT_ONLY);
        mHeader.setRightClickAble(false);
        String title = getResources().getString(R.string.tab_uye);
        mHeader.setTitle(title);
        String txtRight = getResources().getString(R.string.order_info_empty_zero);
        mHeader.setRightTxt(txtRight);
        mHeader.setRightTxtAlignRight();

        mEmptyView.updateType(BlankEmptyView.TYPE_EMPTY_ORDER);
        mEmptyView.showLoadingState();
        mViewPager.setVisibility(View.GONE);
        mViewPager.setOnPageChangeListener(mPageChangeListener);

        OtherController.getInstance().registerRefreshListener(mDataRefreshListener);
        LoginController.getInstance().registerListener(mLoginListener);
    }

    private ILoginListener mLoginListener = new ILoginListener() {
        @Override
        public void loginSucc() {
            int seqNo = ProtocalManager.getInstance().reqOrderList(PageType.FIRST_PAGE,getCallBack());
            mReqList.add(seqNo);
        }

        @Override
        public void logout() {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.reSetState();
            mEmptyView.showErrorState();
            mViewPager.setVisibility(View.GONE);
            mAdapter = null;
            reSetRightTxtTips(0);
        }
    };

    private OtherController.DataRefreshListener mDataRefreshListener = new OtherController.DataRefreshListener() {
        @Override
        public void onRefresh() {
            int seqNo = ProtocalManager.getInstance().reqOrderList(PageType.FIRST_PAGE,getCallBack());
            mReqList.add(seqNo);
        }
    };

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
                    PInsureOrderEntity mEntity = itemView.getMsgEntity();
                    if(mEntity.p - 1 == position){
                        itemView.reqInfo(pEntity);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    private void reSetRightTxtTips(int curCnt){
        String tips = getResources().getString(R.string.order_info_empty_zero);
        if(mAdapter != null){
            int totalCnt = mAdapter.getCount();
            tips = curCnt + "/" + totalCnt;
        }
        mHeader.setRightTxt(tips);
    }

    private IItemListener mItemListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            if(obj != null && obj instanceof  PInsureOrderEntity){
                PInsureOrderEntity mEntity = (PInsureOrderEntity) obj;
                switch(tag){
                    case InsureOrderItemView.SRC_EMPLOYED:
                        showToast(getResources().getString(R.string.dev_ing));
                        break;
                    case InsureOrderItemView.SRC_EMPOY_PROGRESS:
                        if(mEntity.insured_order != null){
                            IntentUtils.startEmployProActivity(getActivity(),mEntity.insured_order.insured_id,MainActivity.REQ_EMPLOY_PRO);
                        }
                        break;
                }
            }
        }
    };

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
                                this.mAdapter.setListener(mItemListener);
                                reSetRightTxtTips(PageType.FIRST_PAGE);
                                this.mViewPager.setAdapter(this.mAdapter);
                                this.mViewPager.setCurrentItem(0);
                            }else{
                                mAdapter.reSetList(mList);
                            }
                        }else{
                            String tips = getResources().getString(R.string.uye_order_empty_tips);
                            initErrorStatus(tips);
                        }
                    }else{
                        String tips = getResources().getString(R.string.common_cfg_empty_orderlist);
                        initErrorStatus(tips);
                    }
                }else{
                    String tips;
                    if(rspEntity.code == 1013){
                        tips = getResources().getString(R.string.uye_order_empty_tips);
                    }else{
                        tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    }
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
                if(LoginController.getInstance().isLogin()){
                    mEmptyView.showLoadingState();
                    int seqNo = ProtocalManager.getInstance().reqOrderList(PageType.FIRST_PAGE,getCallBack());
                    mReqList.add(seqNo);
                }else{
                    IntentUtils.startLoginActivity(getActivity(), LoginActivity.TYPE_PHONE_VERIFY_CODE,MainActivity.REQ_CODE_LOGIN);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OtherController.getInstance().unRegisterRefreshListener(mDataRefreshListener);
        LoginController.getInstance().unRegisterListener(mLoginListener);
    }

    @Override
    public void refreshPage() {
        super.refreshPage();
        int seqNo = ProtocalManager.getInstance().reqOrderList(PageType.FIRST_PAGE,getCallBack());
        mReqList.add(seqNo);
    }

    @Override
    public void fragmentSelected() {
        super.fragmentSelected();
        if(!this.isLoaded){
            refreshPage();
            this.isLoaded = true;
        }
    }
}
