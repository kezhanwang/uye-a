package com.bjzt.uye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.adapter.AdapterEmployPro;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspEmployProList;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.msglist.MsgPage;
import com.common.msglist.MsgPageBottomView;
import com.common.msglist.NLPullRefreshView;
import com.common.msglist.PageAction;
import com.common.msglist.PageType;
import com.common.msglist.entity.PPageEntity;
import com.common.msglist.listener.IRefreshListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 就业进展
 * Created by billy on 2017/10/25.
 */
public class ApplyEmployProActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.msgpage)
    MsgPage mMsgPage;
    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private String insureId;
    private AdapterEmployPro mAdapter;

    private final int REQ_EMPLOY_ADD = 0x10;
    private Map<Integer,PageAction> mReqList = new HashMap<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_employ_progress_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader.updateType(YHeaderView.TYPE_RIGHT_TXT);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                IntentUtils.startEmployProAddActivity(ApplyEmployProActivity.this,insureId,REQ_EMPLOY_ADD);
            }
        });
        String title = getResources().getString(R.string.employ_progress_title);
        mHeader.setTitle(title);
        String txtRight = getResources().getString(R.string.employ_progress_add);
        mHeader.setRightTxt(txtRight);

        //show loading
        mEmptyView.setVisibility(View.VISIBLE);
        mMsgPage.setVisibility(View.GONE);
        mEmptyView.showLoadingState();

        mMsgPage.setRefreshListener(mRefreshListener);
        int seqNo = ProtocalManager.getInstance().reqEmployProList(insureId,PageType.FIRST_PAGE,getCallBack());
        mReqList.put(seqNo,PageAction.TYPE_REFRESH);
    }

    private IRefreshListener mRefreshListener = new IRefreshListener() {
        @Override
        public void onRefresh(NLPullRefreshView view) {
            int seqNo = ProtocalManager.getInstance().reqEmployProList(insureId,PageType.FIRST_PAGE,getCallBack());
            mReqList.put(seqNo,PageAction.TYPE_REFRESH);
        }

        @Override
        public void bottomClick(int state) {
            if(mAdapter != null){
                PPageEntity pEntity = mAdapter.getPageFlag();
                if(pEntity != null){
                    mAdapter.updateState(MsgPageBottomView.STATE_LISTVIEW_LOADING);
                    int seqNo = ProtocalManager.getInstance().reqEmployProList(insureId,PageType.getNextPage(pEntity),getCallBack());
                    mReqList.put(seqNo,PageAction.TYPE_LOAD_MORE);
                }
            }
        }
    };


    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.containsKey(Integer.valueOf(seqNo))){
            PageAction pageAction = mReqList.get(Integer.valueOf(seqNo));
            if(rsp instanceof RspEmployProList){
                RspEmployProList rspEntity = (RspEmployProList) rsp;
                if(isSucc){
                    if(rspEntity.mEntity == null || rspEntity.mEntity.lists == null || rspEntity.mEntity.lists.size() <= 0){
                        String tips = getResources().getString(R.string.common_cfg_empty);
                        initErrorStatus(tips);
                    }else{
                        mEmptyView.loadSucc();
                        mMsgPage.setVisibility(View.VISIBLE);
                        if(mAdapter == null){
                            mAdapter = new AdapterEmployPro(rspEntity.mEntity.lists);
                            mMsgPage.setListAdapter(mAdapter);
                        }else{
                            if(pageAction == PageAction.TYPE_REFRESH){
                                mAdapter.reSetList(rspEntity.mEntity.lists);
                            }else{
                                mAdapter.appendList(rspEntity.mEntity.lists);
                            }
                        }
                        mAdapter.updatePageFlag(rspEntity.mEntity.pages);
                        mMsgPage.completeRefresh(true);
                    }
                }else{
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
                int seqNo = ProtocalManager.getInstance().reqEmployProList(insureId,PageType.FIRST_PAGE,getCallBack());
                mReqList.put(seqNo,PageAction.TYPE_REFRESH);
            }
        });
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.insureId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }
}
