package com.common.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.bjzt.uye.global.Global;

public class SharePreDev {
	private final String FILE_NAME = "RMS_DEV";
	private final String KEY_SREACH_ID = "index";
	
	/***
	 * 保存正式测试环境的index下标
	 * @params index
	 */
	public void saveDevIndex(int index){
		SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putInt(KEY_SREACH_ID,index);
		editor.commit();
	}
	
	/****
	 * 获取正式测试环境的index下标
	 * @return
	 */
	public int getDevIndex(){
		int searchId = 0;
		SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		searchId = sp.getInt(KEY_SREACH_ID,0);
		return searchId;
	}
	
	
}
