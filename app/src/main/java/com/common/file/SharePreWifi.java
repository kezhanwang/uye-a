package com.common.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.bjzt.uye.global.Global;
import com.common.entity.BWifiEntity;
import com.google.gson.Gson;

public class SharePreWifi {
	private final String FILE_NAME = "RMS_WIFI";
	private final String KEY = "key";
	
	/***
	 * 保存正式测试环境的index下标
	 * @params index
	 */
	public void saveWifiEntity(BWifiEntity mEntity){
		if(mEntity != null){
			Gson gson = new Gson();
			SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			String strInfo = gson.toJson(mEntity);
			editor.putString(KEY,strInfo);
			editor.commit();
		}
	}
	
	/****
	 * 获取正式测试环境的index下标
	 * @return
	 */
	public BWifiEntity loadWifiEntity(){
		BWifiEntity bEntity = null;
		SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		String str = sp.getString(KEY,"");
		if(!TextUtils.isEmpty(str)){
			Gson gson = new Gson();
			bEntity = gson.fromJson(str,BWifiEntity.class);
		}
		return bEntity;
	}
	
	
}
