package com.bjzt.uye.views.component;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;

/**
 * Created by billy on 2017/10/15.
 */
public class TimerDownTextView extends TextView implements NoConfusion,View.OnClickListener {
    private IItemListener mListener;
    private int cnt = 60;
    private boolean lock = false;

    public TimerDownTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public TimerDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public TimerDownTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init(){
        setOnClickListener(this);
//		setText(cnt+"");
    }

    private Handler uiHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if(what == 0x100){
                int arg1 = msg.arg1;
                if(arg1 >= 0){
                    setEnabled(false);
                    setText("重发（"+arg1+"）s");
                    msg = Message.obtain();
                    msg.what = 0x100;
                    msg.arg1 = arg1-1;
                    uiHandler.sendMessageDelayed(msg, 1000);
                }else{
                    lock = false;
                    setEnabled(true);
                    setText("重新发送");
                    uiHandler.removeCallbacksAndMessages(null);
                }
            }
        };
    };

    public void setIBtnListener(IItemListener mListener){
        this.mListener = mListener;
    }

    public void startTimer(){
        uiHandler.removeCallbacksAndMessages(null);
        Message msg = Message.obtain();
        msg.what = 0x100;
        msg.arg1 = cnt;
        uiHandler.sendMessage(msg);
        lock = true;
    }

    public void stopTimer(){
        uiHandler.removeCallbacksAndMessages(null);
        setEnabled(true);
        setText("重新发送");
        lock = false;
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if(view == this){
            if(!lock){
                if(mListener != null){
                    mListener.onItemClick(null, -1);
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        uiHandler.removeCallbacksAndMessages(null);
    }

}
