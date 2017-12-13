package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.adapter.QAAdapter;
import com.bjzt.uye.entity.PQACfgItemEntity;
import com.bjzt.uye.entity.VQAItemEntity;
import com.bjzt.uye.entity.VQAResultEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspQACfgEntity;
import com.bjzt.uye.http.rsp.RspQASubmitEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.msglist.itemview.QAItemFooterView;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.common.MyLog;
import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.BaseListAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;

/**
 * 就业调查
 * Created by billy on 2017/10/21
 */
public class QAActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.listview)
    ListView mListView;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private QAAdapter mAdapter;

    private String orgId;
    private RspQACfgEntity mRspEntity;
    private List<Integer> mReqList = new ArrayList<>();
    private Map<Integer,ArrayList<VQAItemEntity>> mMap = new HashMap<>();

    private final int REQ_DATA_CHECK = 0x10;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_qa_layout;
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
        String title = getResources().getString(R.string.qa_title);
        mHeader.setTitle(title);

//        tipsSub.updateType(TipsSubHeaderView.TYPE_QA);
//
//        btnOk.setOnClickListener(this);

        if(this.mRspEntity != null && this.mRspEntity.mEntity != null){
            mListView.setVisibility(View.VISIBLE);
            mEmptyView.loadSucc();
            mMap = mRspEntity.mEntity.buildHashMap();
            initParams(this.mRspEntity.mEntity.buildList());
        }else{
            mListView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.showLoadingState();
            int seqNo = ProtocalManager.getInstance().reqQACfg(this.orgId,getCallBack());
            mReqList.add(seqNo);
        }
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
                        this.mRspEntity = rspEntity;
                        if(rspEntity.mEntity.need_question){
                            if(rspEntity != null && rspEntity.mEntity != null && rspEntity.mEntity.questions != null && rspEntity.mEntity.questions.size() > 0){
                                mEmptyView.loadSucc();
                                mListView.setVisibility(View.VISIBLE);
                                mMap = rspEntity.mEntity.buildHashMap();
                                initParams(rspEntity.mEntity.buildList());
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

    private void initParams(List<BaseItemListener> mList){
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
            if(obj instanceof  VQAItemEntity){
                VQAItemEntity vEntity = (VQAItemEntity) obj;
                int qaId = vEntity.vQAId;
                int type = vEntity.vType;
                //获取当前的答案
                ArrayList<VQAItemEntity> mAsList = mMap.get(Integer.valueOf(qaId));
                MyLog.d(TAG,"[onItemClick]" + "" + mAsList);
                if(type == PQACfgItemEntity.TYPE_SINGLE){
                    if(mAsList.size() <= 0){
                        vEntity.isSelect = true;
                        mAsList.add(vEntity);
                        mAdapter.notifyDataSetChanged();
                    }else{
                        //clear all
                        for(int i = 0;i < mAsList.size();i++){
                            VQAItemEntity tempEntity = mAsList.get(i);
                            tempEntity.isSelect = false;
                        }
                        mAsList.clear();
                        //选择当前选项
                        vEntity.isSelect = true;
                        mAsList.add(vEntity);
                        mAdapter.notifyDataSetChanged();
                    }
                }else{
                    if(vEntity.isSelect){
                        mAsList.remove(vEntity);
                    }else{
                        mAsList.add(vEntity);
                    }
                    vEntity.isSelect = !vEntity.isSelect;
                    mAdapter.notifyDataSetChanged();
                }
            }else if(obj instanceof QAItemFooterView){
                if(mRspEntity != null && mRspEntity.mEntity != null && mRspEntity.mEntity.questions != null && mRspEntity.mEntity.questions.size() > 0){
                    VQAResultEntity rEntity = buildResultEntity(mRspEntity.mEntity.questions);
                    if(rEntity.isSucc){
                        showLoading();
                        int seqNo = ProtocalManager.getInstance().reqQASubmit(orgId,rEntity.mList,getCallBack());
                        mReqList.add(seqNo);
                    }else{
                        String msg = rEntity.msg;
                        String tips = getResources().getString(R.string.qa_select_tips,msg);
                        showToast(tips);
                    }
                }else{
                    String tips = getResources().getString(R.string.common_cfg_empty);
                    showToast(tips);
                }
            }
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
        this.mRspEntity = (RspQACfgEntity) intent.getSerializableExtra(IntentUtils.PARA_KEY_DATA);
    }

    @Override
    public void onClick(View v) {}

    private VQAResultEntity buildResultEntity(List<PQACfgItemEntity> mList){
        VQAResultEntity vEntity = new VQAResultEntity();
        List<VQAItemEntity> rList = new ArrayList<>();
        vEntity.mList = rList;
        vEntity.isSucc = true;
        if(mList != null){
            for(Map.Entry<Integer,ArrayList<VQAItemEntity>> entry : mMap.entrySet()){
                ArrayList<VQAItemEntity> val = entry.getValue();
                if(val == null || val.size() <= 0){
                    vEntity.isSucc = false;
                    for(int j = 0;j < mList.size();j++){
                        PQACfgItemEntity pEntity = mList.get(j);
                        if(pEntity.id == entry.getKey()){
                            vEntity.msg = pEntity.question;
                        }
                    }
                }
                rList.addAll(val);
            }
        }
        return vEntity;
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
        }else{
            setResult(resultCode);
            finish();
        }
    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        //关闭窗体动画显示
//        this.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
//    }

}
