package com.common.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bjzt.uye.global.Global;

public class SharePreRefreshTime {
	private final String FILE_NAME = "RMD_REFRSHTIME";
	private final String KEY_TIME = "key_time";
	
	public SharePreRefreshTime() {
		// TODO Auto-generated constructor stub
	}
	
	public final void saveTime(long time){
		Context mContext = Global.getContext();
		SharedPreferences sharePre = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharePre.edit();
		editor.putLong(KEY_TIME,time);
		editor.commit();
	}
	
	public final long loadTime(){
		Context mContext = Global.getContext();
		SharedPreferences sharePre = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		long time = sharePre.getLong(KEY_TIME,0);
		return time;
	}
}
