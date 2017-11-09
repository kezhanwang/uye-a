package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.activity.dialog.DialogKF;
import com.bjzt.uye.activity.dialog.DialogPicSelect;
import com.bjzt.uye.entity.POrgDetailEntity;
import com.bjzt.uye.entity.POrganizeEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspOrgDetailEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.OrgDetailBtnArea;
import com.bjzt.uye.views.component.OrgDetailCourseInfoView;
import com.bjzt.uye.views.component.OrgDetailCourseView;
import com.bjzt.uye.views.component.OrgDetailHeaderView;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.controller.LoginController;

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
    @BindView(R.id.course_itemview)
    OrgDetailCourseView mCourseView;
    @BindView(R.id.course_info_view)
    OrgDetailCourseInfoView mCourseInfo;
    @BindView(R.id.org_btn_area)
    OrgDetailBtnArea mBtnArea;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    private String orgId;
    private List<Integer> mReqList = new ArrayList<Integer>();

    private final int REQ_DATA_CHECK_APPLY = 10;
    private final int REQ_LOGIN = 11;

    private DialogKF mDialogKf;
    private POrgDetailEntity mEntity;

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
        String title = getString(R.string.org_detail_title);
        mHeader.setTitle(title);

        mBtnArea.setVisibility(View.GONE);
        mScrollView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();

        mHeaderView.setIItemListener(mHeaderViewListener);
        mBtnArea.setIItemListener(mListenerBtnArea);

        int seqNo = ProtocalManager.getInstance().reqOrgDetail(orgId,getCallBack());
        mReqList.add(seqNo);
    }

    private IItemListener mListenerBtnArea = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            if(tag == OrgDetailBtnArea.SRC_SIGN){
                btnSign();
            }else if(tag == OrgDetailBtnArea.SRC_PHONE){
                if(mEntity != null && mEntity.organize != null && !TextUtils.isEmpty(mEntity.organize.phone)){
                    showDialogKF(mEntity.organize.phone);
                }
            }else{
                String tips = getResources().getString(R.string.dev_ing);
                showToast(tips);
            }
        }
    };

    private void btnSign(){
        if(LoginController.getInstance().isLogin()){
            IntentUtils.startApplyFirstTransActivity(OrgDetailActivity.this,orgId, REQ_DATA_CHECK_APPLY);
        }else{
            IntentUtils.startLoginActivity(OrgDetailActivity.this,LoginActivity.TYPE_PHONE_VERIFY_CODE,REQ_LOGIN);
        }
    }

    private IItemListener mHeaderViewListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            POrganizeEntity pEntity = (POrganizeEntity) obj;
            if(tag == OrgDetailHeaderView.SRC_LOC){
                IntentUtils.startBaiduMapActivity(OrgDetailActivity.this,pEntity.map_lat,pEntity.map_lng,pEntity.org_name,pEntity.address);
            }else if(tag == OrgDetailHeaderView.SRC_BTN){
                btnSign();
            }
        }
    };

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            if(rsp instanceof RspOrgDetailEntity){
                RspOrgDetailEntity rspEntity = (RspOrgDetailEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        this.mEntity = rspEntity.mEntity;
                        mEmptyView.loadSucc();
                        mScrollView.setVisibility(View.VISIBLE);
                        mBtnArea.setVisibility(View.VISIBLE);
                        //init header info
                        mHeaderView.setInfo(rspEntity.mEntity.organize);
                        //set title
                        String orgName = rspEntity.mEntity.organize.org_name;
                        mHeader.setTitle(orgName);
                        //set course view
                        if(rspEntity.mEntity.courses != null && rspEntity.mEntity.courses.size() > 0){
                            mCourseView.setCourseList(rspEntity.mEntity.courses);
                        }
                        //set course info
                        String url = null;
                        if(rspEntity.mEntity.organize != null){
                            url = rspEntity.mEntity.organize.description;
                        }
                        if(StrUtil.isWebUrlLegal(url)){
                            mCourseInfo.setVisibility(View.VISIBLE);
                            mCourseInfo.setOrgInfo(url);
                        }else{
                            mCourseInfo.setVisibility(View.GONE);
                        }
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

    private void showDialogKF(final String tel){
        hideDialogKF();
        this.mDialogKf = new DialogKF(this,R.style.MyDialogBg);
        this.mDialogKf.updateType(DialogKF.TYPE_KF);
        this.mDialogKf.setTel(tel,null);
        this.mDialogKf.show();
        this.mDialogKf.setDialogClickListener(new DialogPicSelect.DialogClickListener() {
            @Override
            public void ItemMiddleClick() {
                IntentUtils.startSysCallActivity(OrgDetailActivity.this,tel);
            }
        });
    }

    private void hideDialogKF(){
        if(this.mDialogKf != null){
            this.mDialogKf.dismiss();
            this.mDialogKf = null;
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

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case REQ_DATA_CHECK_APPLY:
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideDialogKF();
    }
}
