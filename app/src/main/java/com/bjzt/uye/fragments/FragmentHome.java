package com.bjzt.uye.fragments;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.bjzt.uye.R;
import com.bjzt.uye.adapter.AdapterHome;
import com.bjzt.uye.adapter.HomeAdAdapter;
import com.bjzt.uye.controller.LBSController;
import com.bjzt.uye.entity.PAdEntity;
import com.bjzt.uye.entity.PHomeOrderEntity;
import com.bjzt.uye.fragments.base.BaseFragment;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.http.rsp.RspHomeEntity;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.listener.IViewPagerListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.HomeHeader;
import com.bjzt.uye.views.component.HomeHeaderView;
import com.bjzt.uye.views.component.HomeLocView;
import com.bjzt.uye.views.component.HomeOrderInfoView;
import com.common.controller.LoginController;
import com.common.listener.ILoginListener;
import com.common.msglist.MsgPage;
import com.common.msglist.NLPullRefreshView;
import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.BaseListAdapter;
import com.common.msglist.base.MutiBaseListAdapter;
import com.common.msglist.listener.IRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 * Created by billy on 2017/10/19
 */
public class FragmentHome extends BaseFragment{

    @BindView(R.id.header)
    HomeHeader mHeader;
    @BindView(R.id.home_msgpage)
    MsgPage mMsgPage;
    @BindView(R.id.home_emptyview)
    BlankEmptyView mEmptyView;

    private AdapterHome mAdapter;
    private List<Integer> mReqList = new ArrayList<Integer>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        mHeader.setIItemListener(mItemListener);


        mMsgPage.setRefreshListener(refreshListener);
        mMsgPage.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();
        LBSController.getInstance().registerListener(mLBSListener);
        LoginController.getInstance().registerListener(mLoginListener);

        int seqNo = ProtocalManager.getInstance().reqHomeInfo(getCallBack());
        mReqList.add(seqNo);
    }

    private ILoginListener mLoginListener = new ILoginListener() {
        @Override
        public void loginSucc() {
            refreshListener.onRefresh(null);
        }

        @Override
        public void logout() {
            refreshListener.onRefresh(null);
        }
    };

    private LBSController.ILBSListener mLBSListener = new LBSController.ILBSListener() {
        @Override
        public void onLBSNotify() {
            refreshListener.onRefresh(null);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LBSController.getInstance().unRegisterListener(mLBSListener);
        LoginController.getInstance().unRegisterListener(mLoginListener);
    }

    private IRefreshListener refreshListener = new IRefreshListener() {
        @Override
        public void onRefresh(NLPullRefreshView view) {
            int seqNo = ProtocalManager.getInstance().reqHomeInfo(getCallBack());
            mReqList.add(seqNo);
        }
    };

    private IItemListener mItemListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            if(obj == mHeader){
                switch(tag){
                    case HomeHeader.TAG_CONTENTS:
                        IntentUtils.startSearchActivity(getActivity());
                        break;
                    case HomeHeader.TAG_LOC:
                        showToast("开发中~");
                        break;
                    case HomeHeader.TAG_SYSMSG:
                        showToast("开发中~");
                        break;
                }
            }
        }
    };

    private IViewPagerListener mPagerListener = new IViewPagerListener() {
        @Override
        public void onPageSelect(int index) {

        }
    };

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            if(rsp instanceof RspHomeEntity){
                RspHomeEntity rspEntity = (RspHomeEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        //set loc
                        LBSController.getInstance().setLocStr(rspEntity.mEntity.loaction);
                        mEmptyView.loadSucc();
                        mMsgPage.setVisibility(View.VISIBLE);
                        List<BaseItemListener> mList = AdapterHome.buildList(rspEntity.mEntity);
                        if(mAdapter == null){
                            mAdapter = new AdapterHome(mList,MutiBaseListAdapter.ADAPTER_TYPE_HOME);
                            mAdapter.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
                            mAdapter.setIItemListener(mItemListener);
                            mAdapter.setIPagerListener(mPagerListener);
                            mMsgPage.setListAdapter(mAdapter);
                        }else{
                            mAdapter.reSetList(mList);
                        }
                    }else{
                        String tips = getResources().getString(R.string.common_cfg_error);
                        initErrorStatus(tips);
                    }
                    mMsgPage.completeRefresh(true);
                }else{
                    mMsgPage.completeRefresh(false);
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    initErrorStatus(tips);
                }
            }
        }
    }

    private void initErrorStatus(String tips){
        mEmptyView.showErrorState();
        mEmptyView.setErrorTips(tips);
        mEmptyView.setBlankListener(new BlankEmptyView.BlankBtnListener() {
            @Override
            public void btnRefresh() {
                mEmptyView.showLoadingState();
                int seqNo = ProtocalManager.getInstance().reqHomeInfo(getCallBack());
                mReqList.add(seqNo);
            }
        });
    }

}