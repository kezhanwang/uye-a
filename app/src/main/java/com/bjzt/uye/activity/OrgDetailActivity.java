package com.bjzt.uye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspOrgDetailEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.OrgDetailHeaderView;
import com.bjzt.uye.views.component.ScoreView;
import com.bjzt.uye.views.component.YHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 机构详情
 * Created by billy on 2017/10/27.
 */
public class OrgDetailActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.headerview)
    OrgDetailHeaderView mHeaderView;
    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;


    private String orgId;
    private List<Integer> mReqList = new ArrayList<Integer>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_org_detail_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
        String title = "机构详情";
        mHeader.setTitle(title);

        mScrollView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();
        int seqNo = ProtocalManager.getInstance().reqOrgDetail(orgId,getCallBack());
        mReqList.add(seqNo);
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            if(rsp instanceof RspOrgDetailEntity){
                RspOrgDetailEntity rspEntity = (RspOrgDetailEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        mEmptyView.loadSucc();
                        mScrollView.setVisibility(View.VISIBLE);
                        //init header info
                        mHeaderView.setInfo(rspEntity.mEntity.organize);

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
        mEmptyView.showErrorState();
        mEmptyView.setErrorTips(tips);
        mEmptyView.setBlankListener(new BlankEmptyView.BlankBtnListener() {
            @Override
            public void btnRefresh() {
                mEmptyView.showLoadingState();
                int seqNo = ProtocalManager.getInstance().reqOrgDetail(orgId,getCallBack());
                mReqList.add(seqNo);
            }
        });
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        orgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }
}
