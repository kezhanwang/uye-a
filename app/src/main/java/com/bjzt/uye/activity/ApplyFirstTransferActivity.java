package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.rsp.RspQACfgEntity;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.StrUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 开始进入到申请流程
 * Created by billy on 2017/10/23.
 */
public class ApplyFirstTransferActivity extends BaseActivity{
    private String mOrgId;
    private List<Integer> mReqList = new ArrayList<>();

    private final int REQ_CODE_QA = 0x10;
    private final int REQ_DATA_CHECK = 0x11;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_transparent_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        showLoading();
        int seqNo = ProtocalManager.getInstance().reqQACfg(this.mOrgId,getCallBack());
        mReqList.add(seqNo);
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        mOrgId = intent.getStringExtra(IntentUtils.PARA_KEY_PUBLIC);
    }

    @Override
    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
        super.onRsp(rsp, isSucc, errorCode, seqNo, src);
        if(mReqList.contains(Integer.valueOf(seqNo))){
            if(rsp instanceof RspQACfgEntity){
                RspQACfgEntity rspEntity = (RspQACfgEntity) rsp;
                if(isSucc && rspEntity.mEntity != null){
                    if(rspEntity.mEntity.need_question){    //跳转到调查问卷
                        if(rspEntity.mEntity.questions != null && rspEntity.mEntity.questions.size() > 0){
                            IntentUtils.startQAActivity(this,this.mOrgId,rspEntity,REQ_CODE_QA);
                        }else{
                            String tips = getResources().getString(R.string.qa_request_cfg_error);
                            showToast(tips);
                            finish();
                        }
                    }else{
                        IntentUtils.startDataCheckActivity(this,this.mOrgId,REQ_DATA_CHECK);
                    }
                }else{
                    String tips = StrUtil.getErrorTipsByCode(errorCode,rspEntity);
                    showToast(tips);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case REQ_CODE_QA:
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
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
}
