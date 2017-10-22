package com.bjzt.uye.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import com.bjzt.uye.global.Global;
import com.common.common.MyLog;
import com.common.util.DeviceUtil;

/**
 * Created by billy on 2017/10/22.
 */

public class ViewUtils {
    private static final String TAG = "LoanViewUtil";
    private static float DEVICE_DENSITY = 0;

    public static float getSpValue(float value) {
        if (DEVICE_DENSITY == 0) {
            DEVICE_DENSITY = Global.getContext().getResources().getDisplayMetrics().densityDpi;
        }
        return value * DEVICE_DENSITY / 160;
    }

    public static int getSpValueInt(float value) {
        return (int) getSpValue(value);
    }

    public static boolean isChildOf(View c, View p) {
        if (c == p) {
            return true;
        } else if (p instanceof ViewGroup) {
            int count = ((ViewGroup) p).getChildCount();
            for (int i = 0; i < count; i++) {
                View ci = ((ViewGroup) p).getChildAt(i);
                if (isChildOf(c, ci)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Rect getRectBlock(View child) {
        int[] posContainer = new int[2];
        getChildPos(child, null, posContainer);
        return new Rect(posContainer[0], posContainer[1], posContainer[0] + child.getWidth(), posContainer[1] + child.getHeight());
    }

    /**
     * @param child
     * @param parent if null, return child position to view tree root.
     * @param posContainer
     */
    public static void getChildPos(View child, View parent, int[] posContainer) {
        if (posContainer == null || posContainer.length < 2) {
            return;
        }
        int x = 0;
        int y = 0;
        View vc = child;
        while (vc.getParent() != null) {
            x += vc.getLeft();
            y += vc.getTop();
            if (vc.getParent() == parent) {
                while(x > DeviceUtil.getDeviceWidth()){	//修正viewpager带来的位置问题
                    x -= DeviceUtil.getDeviceWidth();
                }
                posContainer[0] = x;
                posContainer[1] = y;
                if (posContainer.length >= 4) {
                    posContainer[2] = vc.getMeasuredWidth();
                    posContainer[3] = vc.getMeasuredHeight();
                }
                break;
            }
            try {
                vc = (View) vc.getParent();
                if (posContainer.length >= 4) {
                    posContainer[2] = vc.getMeasuredWidth();
                    posContainer[3] = vc.getMeasuredHeight();
                }
            } catch (ClassCastException e) {
                break;
            }
        }
        if (parent == null) {
            while(x > DeviceUtil.getDeviceWidth()){	//修正viewpager带来的位置问题
                x -= DeviceUtil.getDeviceWidth();
            }
            if(x < 0){
                x = DeviceUtil.getDeviceWidth() + x;
            }
            posContainer[0] = x;
            posContainer[1] = y;
        }
    }

    public static String getActivityName(Context ctx) {
        Context c = ctx;
        if (!(ctx instanceof Activity) && (ctx instanceof ContextWrapper)) {
            c = ((ContextWrapper) ctx).getBaseContext();
        }
        return c.getClass().getName();
    }

    public static int[] getPicBounds(View v) {
        int[] posContainer = new int[4];
        ViewUtils.getChildPos(v, null, posContainer);
        posContainer[0] += v.getPaddingLeft();
        posContainer[1] += v.getPaddingTop();
        posContainer[2] = v.getWidth() - v.getPaddingLeft() - v.getPaddingRight();
        posContainer[3] = v.getHeight() - v.getPaddingTop() - v.getPaddingBottom();
        return posContainer;
    }


    /***
     * 针对魅族手机Holde的属性选项
     * @param view
     * @param model
     */
    public static final void setOverScrollModel(View view,int model){
        if(view == null){
            return;
        }
        if(DeviceUtil.getAndroidSDKVersion() >= 9){
            try{
                view.setOverScrollMode(model);
            }catch(Exception ee){
                MyLog.error(TAG,"[setOverScrollModel]",ee);
            }
        }
    }
    
}
