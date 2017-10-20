package com.common.file;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bjzt.uye.global.Global;

/***
 * 快捷方式flag标志位
 * @date 2015/08/03
 */
public class SharePreShortCut {
	private final static String FILE_NAME = "RMS_SHORT_CUT";
	private final static String KEY_ID = "firstflag";
	
	public static final boolean isFirstOpen(Activity mContext){
		SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(KEY_ID,true);
	}
	
	public static final void saveFristOpenFlag(Context mContext, boolean flag){
		SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(KEY_ID, flag);
		editor.commit();
	}
}
