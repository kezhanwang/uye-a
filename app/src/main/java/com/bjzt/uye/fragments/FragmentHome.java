package com.bjzt.uye.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.LoginActivity;
import com.bjzt.uye.activity.MainActivity;
import com.bjzt.uye.activity.SearchActivity;
import com.bjzt.uye.adapter.AdapterHome;
import com.bjzt.uye.controller.LBSController;
import com.bjzt.uye.controller.OtherController;
import com.bjzt.uye.entity.PHomeEntity;
import com.bjzt.uye.entity.POrganizeEntity;
import com.bjzt.uye.fragments.base.BaseFragment;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspHomeEntity;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.listener.IViewPagerListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.HomeHeader;
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
    private List<Integer> mReqList = new ArrayList<>();
    private PHomeEntity pEntity;

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
        OtherController.getInstance().registerRefreshListener(mDataRefreshListener);
        int seqNo = ProtocalManager.getInstance().reqHomeInfo(getCallBack());
        mReqList.add(seqNo);
    }

    private OtherController.DataRefreshListener mDataRefreshListener = new OtherController.DataRefreshListener(){
        @Override
        public void onRefresh() {
            refreshListener.onRefresh(null);
        }
    };

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
        OtherController.getInstance().unRegisterRefreshListener(mDataRefreshListener);
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
                        IntentUtils.startSearchActivity(getActivity(),MainActivity.REQ_SEARCH);
                        break;
                    case HomeHeader.TAG_LOC:
                        break;
                    case HomeHeader.TAG_SYSMSG:
                        showToast(getResources().getString(R.string.dev_ing));
                        break;
                }
            }else if(obj instanceof HomeLocView){
                Activity ac = getActivity();
                switch(tag){
                    case HomeLocView.TAG_BTN_SIGN:
                        if(pEntity != null){
                            if(ac != null){
                                if(pEntity == null || pEntity.organize == null || pEntity.organize.size() <= 0){
                                    IntentUtils.startSearchActivity(getActivity(),MainActivity.REQ_SEARCH);
                                }else{
                                    POrganizeEntity orgEntity = pEntity.organize.get(0);
                                    if(orgEntity != null && !TextUtils.isEmpty(orgEntity.org_id)){
                                        if(LoginController.getInstance().isLogin()){
                                            IntentUtils.startApplyFirstTransActivity(ac,orgEntity.org_id,MainActivity.REQ_START_APPLY);
                                        }else{
                                            IntentUtils.startLoginActivity(ac, LoginActivity.TYPE_PHONE_VERIFY_CODE,MainActivity.REQ_CODE_LOGIN);
                                        }
                                    }
                                }
                            }
                        }else{
                            String tips = getContext().getResources().getString(R.string.common_cfg_error);
                            showToast(tips);
                        }
                        break;
                    case HomeLocView.TAG_TXT_BTN_LOC:
                        if(ac != null){
                            IntentUtils.startSearchActivity(getActivity(),MainActivity.REQ_SEARCH);
                        }
                        break;
                }
            }else if(obj instanceof HomeOrderInfoView){
                switch(tag){
                    case HomeOrderInfoView.SRC_RELA_LEFT:
                        Activity ac = getActivity();
                        if(ac != null){
                            if(LoginController.getInstance().isLogin()){
                                if(ac instanceof  MainActivity){
                                    MainActivity mAc = (MainActivity) ac;
                                    mAc.setIndex(1);
                                }
                            }else{
                                IntentUtils.startLoginActivity(ac,MainActivity.REQ_CODE_LOGIN);
                            }
                        }
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
                        this.pEntity = rspEntity.mEntity;
                        String locTxt = rspEntity.mEntity.loaction;
                        if(!TextUtils.isEmpty(locTxt)){
                            //set loc
                            LBSController.getInstance().setLocStr(rspEntity.mEntity.loaction);
                            //headerview set loc
                            mHeader.setLocTxt(rspEntity.mEntity.loaction);
                        }
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

    @Override
    public void refreshPage() {
        super.refreshPage();
        int seqNo = ProtocalManager.getInstance().reqHomeInfo(getCallBack());
        mReqList.add(seqNo);
    }
}
