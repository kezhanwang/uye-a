package com.common.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;

/***
 * 工具类
 * @date 2015
 */
public class Utils {
	private static final String TAG = "Utils";
	private static Toast mToast;
	
	/***
	 * toast弹出层
	 * @param str
	 * @param isLongShow
	 */
	public static final void toast(String str, boolean isLongShow){
		toast(str,isLongShow,false);
	}
	
	public static final void toast(String str, boolean isLongShow, boolean isBottom){
		if(mToast != null){
			mToast.cancel();
			mToast = null;
		}
		Context mContext = Global.getContext();
		mToast = new Toast(mContext);
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view= li.inflate(R.layout.toast,null);
		TextView textView=(TextView) view.findViewById(R.id.text);
		textView.setText(str);
		mToast.setView(view);
		int height = DeviceUtil.getDeviceHeight();
		if(isBottom){
			mToast.setGravity(Gravity.TOP, 0, (height/4)*3+20);
		}else{
			mToast.setGravity(Gravity.TOP, 0, height/4+20);
		}
		if(isLongShow){
			mToast.setDuration(2000);
		}else{
			mToast.setDuration(1000);
		}
		mToast.show();
	}
	
	public static final int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,Global.mContext.getResources().getDisplayMetrics());
	}
	
	
	 /**
     * 复制内容到剪切系统剪切板
     *
     * @param content 传入内容
     */
    public static boolean copyContentToClipboard(String content) {
        boolean succ = false;
        if (content.length() > 0) {
            try {
                android.text.ClipboardManager clip4previous = (android.text.ClipboardManager) Global.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clip4previous.setText(content);
                succ = true;
            } catch (NoClassDefFoundError e) {           // 兼容下 2.3X 的系统...
                android.text.ClipboardManager clip4previous = (android.text.ClipboardManager) Global.getContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                clip4previous.setText(content);
                succ = true;
            }
        }
        return succ;
    }

}
