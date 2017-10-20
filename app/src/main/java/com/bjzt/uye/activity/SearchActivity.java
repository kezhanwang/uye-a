package com.bjzt.uye.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspSearchWEntity;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.QAPublishCatView;
import com.bjzt.uye.views.component.SearchHeader;
import com.common.msglist.MsgPage;

import java.util.ArrayList;
import java.util.List;

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

    private List<Integer> mReqList = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_search_layout;
    }

    @Override
    protected void initLayout() {
        mHeader.setISearchListener(new SearchHeader.ISearchListener() {
            @Override
            public void onSearch(String msg) {
                onActionSearch(msg);
            }

            @Override
            public void onTxtCancle() {
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

        int seqNo = ProtocalManager.getInstance().reqSearchHotW(getCallBack());
        mReqList.add(seqNo);
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
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
                int seqNo = ProtocalManager.getInstance().reqSearchHotW(getCallBack());
                mReqList.add(seqNo);
            }
        });
    }

    private IItemListener mHotListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            onActionSearch((String)obj);
        }
    };

    private IItemListener mHistoryListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            onActionSearch((String)obj);
        }
    };

    private void onActionSearch(String strInfo){
        if(!TextUtils.isEmpty(strInfo)){
            int seqNo = ProtocalManager.getInstance().reqSearchAgencyList(strInfo,1,getCallBack());
            mReqList.add(seqNo);
        }else{

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
}
