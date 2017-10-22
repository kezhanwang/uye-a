package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.adapter.QAAdapter;
import com.bjzt.uye.entity.PQACfgItemEntity;
import com.bjzt.uye.entity.VQAItemEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspQACfgEntity;
import com.bjzt.uye.http.rsp.RspQASubmitEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.QAListItemView;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.TipsSubHeaderView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.msglist.base.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 就业调查
 * Created by billy on 2017/10/21
 */
public class QAActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.tips_subheader)
    TipsSubHeaderView tipsSub;
    @BindView(R.id.listview)
    ListView mListView;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private QAAdapter mAdapter;

    private String orgId;
    private List<Integer> mReqList = new ArrayList<>();
    private List<VQAItemEntity> mListSelect = new ArrayList<>();

    private final int REQ_DATA_CHECK = 0x10;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_qa_layout;
    }

    @Override
    protected void initLayout() {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
        String title = getResources().getString(R.string.qa_title);
        mHeader.setTitle(title);

        tipsSub.updateType(TipsSubHeaderView.TYPE_QA);

        btnOk.setOnClickListener(this);

        mScrollView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();
        int seqNo = ProtocalManager.getInstance().reqQACfg(this.orgId,getCallBack());
        mReqList.add(seqNo);
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspQACfgEntity){
                RspQACfgEntity rspEntity = (RspQACfgEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        if(rspEntity.mEntity.need_question){
                            if(rspEntity != null && rspEntity.mEntity != null && rspEntity.mEntity.questions != null && rspEntity.mEntity.questions.size() > 0){
                                mEmptyView.loadSucc();
                                mScrollView.setVisibility(View.VISIBLE);
                                initParams(rspEntity.mEntity.questions);
                            }else{
                                String tips = getResources().getString(R.string.common_cfg_empty);
                                initErrorStatus(tips);
                            }
                        }else{
                            IntentUtils.startDataCheckActivity(QAActivity.this,orgId,REQ_DATA_CHECK);
                        }
                    }else{
                        String tips = getResources().getString(R.string.common_cfg_empty);
                        initErrorStatus(tips);
                    }

                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    initErrorStatus(tips);
                }
            }else if(rsp instanceof RspQASubmitEntity){
                RspQASubmitEntity rspEntity = (RspQASubmitEntity) rsp;
                if(isSucc){
                    IntentUtils.startDataCheckActivity(QAActivity.this,orgId,REQ_DATA_CHECK);
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                }
            }
        }
    }

    private void initParams(List<PQACfgItemEntity> mList){
        if(mAdapter == null){
            mAdapter = new QAAdapter(mList);
            mAdapter.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
            mAdapter.setIItemListener(mItemListener);
            mListView.setAdapter(mAdapter);
        }else{
            mAdapter.reSetList(mList);
        }
    }

    private IItemListener mItemListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            QAListItemView itemView = (QAListItemView) obj;
            VQAItemEntity vEntity = itemView.getMsg();
            if(vEntity.isSelect){
                mListSelect.remove(vEntity);
            }else{
                mListSelect.add(vEntity);
            }
            vEntity.isSelect = !vEntity.isSelect;
            itemView.setMsg(vEntity);
        }
    };

    private void initErrorStatus(String tips){
        mEmptyView.showErrorState();
        mEmptyView.setErrorTips(tips);
        mEmptyView.setBlankListener(new BlankEmptyView.BlankBtnListener() {
            @Override
            public void btnRefresh() {
                mEmptyView.showLoadingState();
                int seqNo = ProtocalManager.getInstance().reqQACfg(orgId,getCallBack());
                mReqList.add(seqNo);
            }
        });
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.orgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }

    @Override
    public void onClick(View v) {
        if(v == this.btnOk){
            if(mListSelect.size() > 0){
                showLoading();
                int seqNo = ProtocalManager.getInstance().reqQASubmit(this.orgId,mListSelect,getCallBack());
                mReqList.add(seqNo);
            }else{
                String tips = getResources().getString(R.string.qa_select_tips);
                showToast(tips);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case REQ_DATA_CHECK:
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

}
