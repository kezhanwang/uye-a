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

import java.util.ArrayList;
import java.util.List;

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
    private List<Integer> mReqList = new ArrayList();

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

        int seqNo = ProtocalManager.getInstance().reqEmployProList(insureId,getCallBack());
        mReqList.add(seqNo);
    }


    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            if(rsp instanceof RspEmployProList){
                RspEmployProList rspEntity = (RspEmployProList) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null && rspEntity.mEntity.work != null && rspEntity.mEntity.work.size() > 0){
                        String tips = getResources().getString(R.string.common_cfg_empty);
                        initErrorStatus(tips);
                    }else{
                        if(mAdapter == null){
                            mAdapter = new AdapterEmployPro(rspEntity.mEntity.work);
                            mMsgPage.setListAdapter(mAdapter);
                        }else{
                            mAdapter.reSetList(rspEntity.mEntity.work);
                        }
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
                int seqNo = ProtocalManager.getInstance().reqEmployProList(insureId,getCallBack());
                mReqList.add(seqNo);
            }
        });
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.insureId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }
}
