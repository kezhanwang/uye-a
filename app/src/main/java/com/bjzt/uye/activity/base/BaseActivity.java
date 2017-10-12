package com.bjzt.uye.activity.base;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.dialog.MLoadingDialog;
import com.common.util.DeviceUtil;

/**
 * Created by billy on 2017/9/21.
 */
public class BaseActivity extends FragmentActivity {
    protected final String TAG = getClass().getSimpleName();
    private Toast toast;
    private MLoadingDialog mLoadingDialog;

    protected void showToast(){

    }

    protected void showToast(String str,int duration){
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


    protected void hideLoadingDialog(){
        if(mLoadingDialog != null){
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    protected void showLoading(String msg,boolean isCancle){
        hideLoadingDialog();
        if(!isFinishing() && mLoadingDialog == null){
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideToast();
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
}
