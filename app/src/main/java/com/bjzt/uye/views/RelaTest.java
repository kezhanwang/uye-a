package com.bjzt.uye.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.common.common.MyLog;
import com.common.listener.NoConfusion;

/**
 * Created by billy on 2017/11/2.
 */

public class RelaTest extends RelativeLayout implements NoConfusion{
    private final String TAG = getClass().getSimpleName();

    public RelaTest(Context context) {
        super(context);
    }

    public RelaTest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MyLog.d(MyLog.BILLY,"[onTouchEvent]" + " RelaTest");
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        MyLog.d(MyLog.BILLY,"[onInterceptTouchEvent]" +  " RelaTest");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        MyLog.d(MyLog.BILLY,"[dispatchTouchEvent]" + " RelaTest");
        return super.dispatchTouchEvent(ev);
    }
}
