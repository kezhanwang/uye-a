package com.bjzt.uye.fragments.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.listener.ICallBack;
import com.common.common.MyLog;
import com.common.util.DeviceUtil;

/**
 * Created by billy on 2017/9/21.
 */

public class BaseFragment extends Fragment {
    protected String TAG = this.getClass().getSimpleName();
    private Toast toast;

    /**
     * ViewPager页面选择的，会调用被选中的Fragment 页面；
     */
    public void fragmentSelected() {
        if(MyLog.isDebugable()){
            MyLog.d(TAG, "page select");
        }
    }

    protected void showToast(String str){
        Context mContext = Global.getContext();
        if(mContext != null){
            if(!TextUtils.isEmpty(str)){
                if(toast != null){
                    toast.cancel();
                    toast = null;
                }
                toast = new Toast(mContext);
                LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = li.inflate(R.layout.toast,null);
                TextView textView = view.findViewById(R.id.text);
                textView.setText(str);
                toast.setView(view);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, DeviceUtil.mHeight/4+20);
                toast.show();
            }
        }
    }

    protected void hideToast() {
        if(toast != null){
            toast.cancel();
            toast = null;
        }
    }

    private ICallBack<Object> callBack = new ICallBack<Object>() {
        @Override
        public void getResponse(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
            if(!isRemoving()){
                onRsp(rsp,isSucc,errorCode,seqNo,src);
            }
        }
    };

    protected ICallBack getCallBack(){
        return this.callBack;
    }

    protected void onRsp(Object rsp, boolean isSucc, int errorCode, int seqNo, int src){};

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHandler.removeCallbacksAndMessages(null);
    }

    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(!isRemoving()){
                handleMsg(msg);
            }
        }
    };

    protected void handleMsg(Message msg){

    }

    /**
     * @param msg
     */
    protected void sendMsg(Message msg){
        uiHandler.sendMessage(msg);
    }

    /***
     * 刷新UI
     */
    public void refreshPage(){}
}
