package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.adapter.SearchAdapter;
import com.bjzt.uye.entity.PAgencyEntity;
import com.bjzt.uye.entity.POrganizeEntity;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspSearchEntity;
import com.bjzt.uye.http.rsp.RspSearchWEntity;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.SearchItemView;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.QAPublishCatView;
import com.bjzt.uye.views.component.SearchHeader;
import com.common.controller.LoginController;
import com.common.msglist.MsgPage;
import com.common.msglist.MsgPageBottomView;
import com.common.msglist.NLPullRefreshView;
import com.common.msglist.PageAction;
import com.common.msglist.PageType;
import com.common.msglist.entity.PPageEntity;
import com.common.msglist.listener.IRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by billy on 2017/10/17.
 */

public class SearchActivity extends BaseActivity{

    @BindView(R.id.header)
    SearchHeader mHeader;

    @BindView(R.id.scrollview)
    ScrollView mScrollView;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    @BindView(R.id.catview_hot)
    QAPublishCatView catHot;

    @BindView(R.id.catview_history)
    QAPublishCatView catHistory;

    @BindView(R.id.msgpage)
    MsgPage mMsgPage;

    private Map<Integer,PageAction> mReqList = new HashMap<>();
    private SearchAdapter mAdapter;

    private final int REQ_DATA_CHECK = 10;
    private final int REQ_LOGIN = 11;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_search_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader.setISearchListener(new SearchHeader.ISearchListener() {
            @Override
            public void onSearch(String msg) {
                onActionSearch(msg);
            }

            @Override
            public void onTxtCancle() {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }

            @Override
            public void onTxtChanged(String info) {
                onActionSearch(info);
            }
        });
        //
        String str = "请输入您要搜索的机构名称";
        mHeader.setHint(str);

        this.mScrollView.setVisibility(BlankEmptyView.GONE);
        this.mEmptyView.setVisibility(BlankEmptyView.VISIBLE);
        this.mEmptyView.showLoadingState();
        this.mMsgPage.setVisibility(View.GONE);
        this.mMsgPage.setEnablePullDown(false);

        this.mMsgPage.setRefreshListener(refreshListener);
        int seqNo = ProtocalManager.getInstance().reqSearchHotW(getCallBack());
        mReqList.put(seqNo,PageAction.TYPE_REFRESH);
    }

    private IRefreshListener refreshListener = new IRefreshListener() {
        @Override
        public void onRefresh(NLPullRefreshView view) {
            super.onRefresh(view);
        }

        @Override
        public void bottomClick(int state) {
            if(mAdapter != null){
                String words = mHeader.getContentTxt();
                PPageEntity pageEntity = mAdapter.getPageFlag();
                mAdapter.updateState(MsgPageBottomView.STATE_LISTVIEW_LOADING);
                int seqNo = ProtocalManager.getInstance().reqSearchAgencyList(words, PageType.getNextPage(pageEntity),getCallBack());
                mReqList.put(seqNo,PageAction.TYPE_LOAD_MORE);
            }
        }
    };

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.containsKey(Integer.valueOf(seqNo))){
            if(rsp instanceof RspSearchWEntity){
                RspSearchWEntity rspEntity = (RspSearchWEntity) rsp;
                if(isSucc){
                    mEmptyView.loadSucc();
                    mScrollView.setVisibility(View.VISIBLE);
                    if(rspEntity.mEntity.hot_search != null && rspEntity.mEntity.hot_search.size() > 0){
                        catHot.setInfo(rspEntity.mEntity.hot_search,mHotListener);
                    }
                    if(rspEntity.mEntity.history != null && rspEntity.mEntity.history.size() > 0){
                        catHistory.setInfo(rspEntity.mEntity.history,mHistoryListener);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    initErrorStatus(tips);
                }
            }else if(rsp instanceof RspSearchEntity){
                RspSearchEntity rspEntitiy = (RspSearchEntity) rsp;
                if(isSucc && rspEntitiy.mEntity != null){
                    if(rspEntitiy.mEntity.organizes != null && rspEntitiy.mEntity.organizes.size() > 0){
                        if(this.mAdapter == null){
                            this.mAdapter = new SearchAdapter(rspEntitiy.mEntity.organizes);
                            this.mAdapter.setIItemListener(this.mItemListener);
                            this.mMsgPage.setListAdapter(this.mAdapter);
                        }else{
                            PageAction pAction = mReqList.remove(Integer.valueOf(seqNo));
                            if(pAction == PageAction.TYPE_REFRESH){
                                this.mAdapter.reSetList(rspEntitiy.mEntity.organizes);
                            }else{
                                this.mAdapter.appendList(rspEntitiy.mEntity.organizes);
                            }
                        }
                        this.mAdapter.updatePageFlag(rspEntitiy.mEntity.page);
                        udpatePageStatus(false);
                    }else{
                        String tips = getResources().getString(R.string.search_result_empty);
                        showToast(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntitiy);
                    showToast(tips);
                }
            }
        }
    }

    private IItemListener mItemListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            PAgencyEntity pEntity = (PAgencyEntity) obj;
            switch(tag){
                case SearchItemView.SRC_ITEM:
                    String tips = "开发中...";
                    showToast(tips);
                    break;
                case SearchItemView.SRC_BTN_OK:
                    //        this.orgId = "10049"; test code
                    String orgId = MConfiger.TEST_ORG_ID;
//                    orgId = pEntity.org_id;
                    if(LoginController.getInstance().isLogin()){
//                        IntentUtils.startQAActivity(SearchActivity.this,orgId,REQ_DATA_CHECK);
                        IntentUtils.startApplyFirstTransActivity(SearchActivity.this,orgId,REQ_DATA_CHECK);
                    }else{
                        IntentUtils.startLoginActivity(SearchActivity.this,LoginActivity.TYPE_PHONE_VERIFY_CODE,REQ_LOGIN);
                    }
                    break;
            }
        }
    };

    private void udpatePageStatus(boolean isHot){
        if(isHot){
            mScrollView.setVisibility(View.VISIBLE);
            mMsgPage.setVisibility(View.GONE);
        }else{
            mScrollView.setVisibility(View.GONE);
            mMsgPage.setVisibility(View.VISIBLE);
        }
    }

    private void initErrorStatus(String tips){
        mEmptyView.showErrorState();
        mEmptyView.setErrorTips(tips);
        mEmptyView.setBlankListener(new BlankEmptyView.BlankBtnListener() {
            @Override
            public void btnRefresh() {
                mEmptyView.showLoadingState();
                int seqNo = ProtocalManager.getInstance().reqSearchHotW(getCallBack());
                mReqList.put(seqNo,PageAction.TYPE_REFRESH);
            }
        });
    }

    private IItemListener mHotListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            String strMsg = (String) obj;
            mHeader.setContentTxt(strMsg);
        }
    };

    private IItemListener mHistoryListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            String strMsg = (String) obj;
            mHeader.setContentTxt(strMsg);
        }
    };

    private void onActionSearch(String strInfo){
        if(!TextUtils.isEmpty(strInfo)){
            if(mAdapter != null){
                mAdapter.clear();
            }
            udpatePageStatus(false);
            int seqNo = ProtocalManager.getInstance().reqSearchAgencyList(strInfo,1,getCallBack());
            mReqList.put(seqNo,PageAction.TYPE_REFRESH);
        }else{
            udpatePageStatus(true);
        }
    }

    @Override
    protected void initExtras(Bundle bundle) {

    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(0, R.anim.activity_bottom_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case REQ_DATA_CHECK:
                    String tips = "申请成功~";
                    showToast(tips);
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
            }
        }
    }
}
