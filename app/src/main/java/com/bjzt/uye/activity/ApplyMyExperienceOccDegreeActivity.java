package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.adapter.ExperiListAdapter;
import com.bjzt.uye.entity.PExperiEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.req.ReqExperiListEntity;
import com.bjzt.uye.http.rsp.RspExperiDelEntity;
import com.bjzt.uye.http.rsp.RspExperiListEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.msglist.MsgPage;
import com.common.msglist.NLPullRefreshView;
import com.common.msglist.base.BaseListAdapter;
import com.common.msglist.listener.IRefreshListener;
import com.common.msglist.swipe.SwipeMenu;
import com.common.msglist.swipe.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by billy on 2017/10/30.
 * 个人经历-职业列表
 */
public class ApplyMyExperienceOccDegreeActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.msgpage)
    MsgPage mMsgPage;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private int mType;
    private String orgId;
    public static final int TYPE_OCC = 1;       //职业
    public static final int TYPE_DEGREE = 2;    //学历

    private final int REQ_ADD_OCC = 1;
    private final int REQ_ADD_DEGREE = 2;
    private final int REQ_EXPERI_DEGREE_LIST = 3;
    private final int REQ_EDIT_OCC = 4;
    private final int REQ_EDIT_DEGREE = 5;
    private final int REQ_ADD_OCC_CANCLE = 6;
    private final int REQ_ADD_DEGREE_CANCLE = 7;

    private List<Integer> mReqList = new ArrayList<>();
    private ExperiListAdapter mAdapter;
    private RspExperiListEntity mRspParamsEntity;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_myexperience_occ_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader.updateType(YHeaderView.TYPE_RIGHT_TXT);
        mHeader.setIListener(mHeaderListener);
        String strTxtRight = "";
        String title = "";
        String btnTxt = "";
        switch(this.mType){
            case TYPE_OCC:
                title = "个人经历-职业";
                strTxtRight = "添加职业";
                btnTxt = getResources().getString(R.string.next);
                break;
            case TYPE_DEGREE:
                title = "个人经历-学历";
                strTxtRight = "添加学历";
                btnTxt = getResources().getString(R.string.complete);
                break;
        }
        mHeader.setTitle(title);
        mHeader.setRightTxt(strTxtRight);

        mMsgPage.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();

        btnOk.setOnClickListener(this);
        btnOk.setText(btnTxt);

        mMsgPage.setRefreshListener(mRefreshListener);
        mMsgPage.setEnablePullDown(true);
        mMsgPage.setListViewScrollBar(true);
        mMsgPage.initCreator();
        mMsgPage.setOnMenuItemClickListener(menuItemListener);
        mMsgPage.getListView().setOnItemClickListener(mItemClickListener);
        boolean needRefresh = false;
        if(mRspParamsEntity != null){
            if(mRspParamsEntity.mList == null || mRspParamsEntity.mList.size() <= 0){
                switch(mType) {
                    case TYPE_DEGREE:
                        IntentUtils.startMyExperienceDegreeAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_ADD_DEGREE_CANCLE);
                        break;
                    case TYPE_OCC:
                        IntentUtils.startMyExperienceOccAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_ADD_OCC_CANCLE);
                        break;
                }
            }else{
                needRefresh = true;
            }
        }else{
            needRefresh = true;
        }

        if(needRefresh){
            refresh();
        }
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(mAdapter != null){
                PExperiEntity pEntity = (PExperiEntity) mAdapter.getItem(position);
                if(pEntity != null){
                    switch(mType){
                        case TYPE_OCC:  //打开添加职位界面
                            IntentUtils.startMyExperienceOccAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_EDIT_OCC,pEntity);
                            break;
                        case TYPE_DEGREE:
                            IntentUtils.startMyExperienceDegreeAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_EDIT_DEGREE,pEntity);
                            break;
                    }
                }
            }
        }
    };

    private IRefreshListener mRefreshListener = new IRefreshListener() {
        @Override
        public void onRefresh(NLPullRefreshView view) {
            refresh();
        }
    };

    private SwipeMenuListView.OnMenuItemClickListener menuItemListener = new SwipeMenuListView.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            // TODO Auto-generated method stub
            if(mAdapter != null){
                if(mAdapter.getCount() >= 2){
                    PExperiEntity entity =  (PExperiEntity) mAdapter.getItem(position);
                    if(entity != null){
                        mAdapter.removeItem(entity);
                        int seqNo = ProtocalManager.getInstance().reqMyExperiDel(entity.id,getCallBack());
                        mReqList.add(seqNo);
                    }
                }else{
                    String tips = "删除失败~";
                    showToast(tips);
                }
            }
            return false;
        }
    };

    private IHeaderListener mHeaderListener = new IHeaderListener() {
        @Override
        public void onLeftClick() {
            finish();
        }

        @Override
        public void onRightClick() {
            switch(mType) {
                case TYPE_DEGREE:
                    IntentUtils.startMyExperienceDegreeAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_ADD_DEGREE);
                    break;
                case TYPE_OCC:
                    IntentUtils.startMyExperienceOccAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_ADD_OCC);
                    break;
            }
        }
    };

    private void refresh(){
        int seqNo = -1;
        switch(mType){
            case TYPE_OCC:
                seqNo = ProtocalManager.getInstance().reqMyExperiListOcc(getCallBack());
                break;
            case TYPE_DEGREE:
                seqNo = ProtocalManager.getInstance().reqMyExperiListDegree(getCallBack());
                break;
        }
        mReqList.add(seqNo);
    }

    private IItemListener mItemListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            PExperiEntity pEntity = (PExperiEntity) obj;
            switch(mType){
                case TYPE_OCC:  //打开添加职位界面
                    IntentUtils.startMyExperienceOccAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_EDIT_OCC,pEntity);
                    break;
                case TYPE_DEGREE:
                    IntentUtils.startMyExperienceDegreeAddActivity(ApplyMyExperienceOccDegreeActivity.this,orgId,REQ_EDIT_DEGREE,pEntity);
                    break;
            }
        }
    };

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspExperiListEntity){
                RspExperiListEntity rspEntity = (RspExperiListEntity) rsp;
                if(isSucc){
                    if(rspEntity != null && rspEntity.mList != null && rspEntity.mList.size() > 0){
                        mEmptyView.loadSucc();
                        mMsgPage.setVisibility(View.VISIBLE);
                        if(this.mAdapter == null){
                            mAdapter = new ExperiListAdapter(rspEntity.mList);
                            mAdapter.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
                            mMsgPage.setListAdapter(mAdapter);
                        }else{
                            mAdapter.reSetList(rspEntity.mList);
                        }
                    }else{
                        String tips = "暂无信息";
                        initErrorStatus(tips);
                    }
                    mMsgPage.completeRefresh(true);
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    initErrorStatus(tips);
                    mMsgPage.completeRefresh(false);
                }
            }else if(rsp instanceof RspExperiDelEntity){
                RspExperiDelEntity rspEntity = (RspExperiDelEntity) rsp;
                if(isSucc){
                    String tips = "删除成功~";
                    showToast(tips);
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
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
                refresh();
            }
        });
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        this.orgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
        this.mType = intent.getIntExtra(IntentUtils.PARA_KEY_TYPE,TYPE_OCC);
        this.mRspParamsEntity = (RspExperiListEntity) intent.getSerializableExtra(IntentUtils.PARA_KEY_RSP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_ADD_DEGREE_CANCLE || requestCode == REQ_ADD_OCC_CANCLE){
            if(resultCode == Activity.RESULT_OK){
                refresh();
            }else{
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }else{
            if(resultCode == Activity.RESULT_OK){
                switch(requestCode){
                    case REQ_ADD_OCC:
                    case REQ_EDIT_OCC:
                        refresh();
                        break;
                    case REQ_ADD_DEGREE:
                    case REQ_EDIT_DEGREE:
                        refresh();
                        break;
                    case REQ_EXPERI_DEGREE_LIST:
                        setResult(Activity.RESULT_OK);
                        finish();
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnOk){
            if(mType == TYPE_OCC){
                IntentUtils.startMyExperienceDegreeActivity(this,orgId,REQ_EXPERI_DEGREE_LIST);
            }else{
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    }

}
