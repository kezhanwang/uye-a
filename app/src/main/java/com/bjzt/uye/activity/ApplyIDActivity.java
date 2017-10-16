package com.bjzt.uye.activity;

import android.os.Bundle;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.entity.PFaceVerifyEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspFaceVerifyCfgEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.util.StrUtil;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.YHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 身份认证信息
 * Created by billy on 2017/10/16
 */
public class ApplyIDActivity extends BaseActivity{

    @BindView(R.id.apply_id_header)
    YHeaderView mHeader;
    @BindView(R.id.apply_id_emptyview)
    BlankEmptyView mEmptyView;

    private List<Integer> mReqList = new ArrayList<Integer>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_apply_id_layout;
    }

    @Override
    protected void initLayout() {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        String title = "身份认证";
        mHeader.setTitle(title);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });

        mEmptyView.showLoadingState();
        int seqNo = ProtocalManager.getInstance().reqFaceVerifyCfg(getCallBack());
        mReqList.add(seqNo);
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            hideLoadingDialog();
            if(rsp instanceof RspFaceVerifyCfgEntity){
                RspFaceVerifyCfgEntity rspEntity = (RspFaceVerifyCfgEntity) rsp;
                if(isSucc){
                    if(rspEntity.mEntity != null){
                        openFaceAuth(rspEntity.mEntity);
                    }else{
                        String tips = getResources().getString(R.string.common_request_error);
                        showToast(tips);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                }
            }
        }
    }

    private void openFaceAuth(PFaceVerifyEntity pEntity){

    }

    @Override
    protected void initExtras(Bundle bundle) {

    }
}
