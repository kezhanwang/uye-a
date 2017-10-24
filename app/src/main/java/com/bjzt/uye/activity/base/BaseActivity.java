package com.bjzt.uye.activity.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.dialog.MLoadingDialog;
import com.bjzt.uye.http.listener.ICallBack;
import com.common.util.DeviceUtil;

import butterknife.ButterKnife;

/**
 * Created by billy on 2017/9/21.
 */
public abstract class BaseActivity extends FragmentActivity {
    protected final String TAG = getClass().getSimpleName();
    private Toast toast;
    private MLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        initExtras(savedInstanceState);
        bindView();
        initLayout(savedInstanceState);
    }

    abstract protected int getLayoutID();

    abstract  protected void initLayout(Bundle bundle);

    abstract  protected  void initExtras(Bundle bundle);

    protected void bindView(){
        ButterKnife.bind(this);
    }

    public void showToast(String str){
        showToast(str,Toast.LENGTH_SHORT);
    }

    public void showToast(String str,int duration){
        if(!TextUtils.isEmpty(str)){
            if(toast != null){
                toast.cancel();
                toast = null;
            }
            toast = new Toast(this);
            View view=getLayoutInflater().inflate(R.layout.toast,null);
            TextView textView=(TextView) view.findViewById(R.id.text);
            textView.setText(str);
            toast.setView(view);
            toast.setDuration(duration);
            toast.setGravity(Gravity.TOP, 0, DeviceUtil.mHeight/4+20);
            toast.show();
        }
    }

    protected void hideToast(){
        if(toast != null){
            toast.cancel();
            toast = null;
        }
    }

    public void hideLoadingDialog(){
        if(mLoadingDialog != null){
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void showLoading(String msg,boolean isCancle){
        hideLoadingDialog();
        if(mLoadingDialog == null){
            mLoadingDialog = new MLoadingDialog(this,R.style.MyDialogBg);
            mLoadingDialog.setCanceledOnTouchOutside(isCancle);
            mLoadingDialog.setCancelable(isCancle);
            mLoadingDialog.show();
            if(TextUtils.isEmpty(msg)){
                msg = getResources().getString(R.string.msgitem_bottom_loading);
            }
            if(mLoadingDialog != null){
                mLoadingDialog.setMyTips(msg);
            }
        }
    }


    public void showLoading(){
        hideLoadingDialog();
        String tips = getResources().getString(R.string.common_request);
        showLoading(tips,true);
    }

    public void showLoading(String tips){
        showLoading(tips,true);
    }

    public boolean isDialogShowing(){
        if(this.mLoadingDialog != null && this.mLoadingDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public void setLoadingTips(String tips){
        if(this.mLoadingDialog != null && this.mLoadingDialog.isShowing()){
            if(!TextUtils.isEmpty(tips)){
                mLoadingDialog.setMyTips(tips);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHandler.removeCallbacksAndMessages(null);
    }

    protected Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(!isFinishing()){
                handleMsg(msg);
            }
        }
    };

    protected void handleMsg(Message msg){}

    protected void sendMsg(Message msg){
        uiHandler.sendMessage(msg);
    }

    protected void sendMsgDelay(Message msg,long milli){
        uiHandler.sendMessageDelayed(msg,milli);
    }

    protected ICallBack<Object> callBack = new ICallBack<Object>() {
        @Override
        public void getResponse(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
            if(!isFinishing()){
                onRsp(rsp,isSucc,errorCode,seqNo,src);
            }
        }
    };

    protected ICallBack<Object> getCallBack(){
        return callBack;
    }

    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src){}

}
