package com.bjzt.uye.util;

import android.widget.EditText;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.common.common.MyLog;
import com.common.util.DeviceUtil;
import java.lang.reflect.Field;

/**
 * Created by billy on 2017/10/15.
 */
public class ReflectUtils {
    private static final String TAG = "ReflectUtils";

    /***
     * 设置输入框颜色
     */
    public static final void setEditCursorColor(EditText editTxt, int c){
        int sdkVer = DeviceUtil.getAndroidSDKVersion();
        if(sdkVer >= 12){
            try {
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(editTxt, R.drawable.rectangle_green_line_color);
            } catch (Exception ee) {
                MyLog.error(TAG,ee);
            }
        }
    }

}
