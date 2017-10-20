package com.common.file;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.bjzt.uye.entity.PLoginEntity;
import com.bjzt.uye.global.Global;
import com.google.gson.Gson;

/**
 * Created by billy on 2017/10/16.
 */

public class SharePreLogin {
    private final String FILE_NAME = "RMS_LOGIN_INFO";
    private final String KEY = "key";

    /***
     * 保存正式测试环境的index下标
     * @params index
     */
    public void saveLoginInfo(PLoginEntity pLoginEntity){
        if(pLoginEntity != null){
            Gson gson = new Gson();
            String strInfo = gson.toJson(pLoginEntity);
            if(!TextUtils.isEmpty(strInfo)){
                SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(KEY,strInfo);
                editor.commit();
            }
        }
    }

    /****
     * 获取正式测试环境的index下标
     * @return
     */
    public PLoginEntity loadInfo(){
        PLoginEntity pEntity = null;
        SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String strInfo = sp.getString(KEY,"");
        if(!TextUtils.isEmpty(strInfo)){
            Gson gson = new Gson();
            pEntity = gson.fromJson(strInfo,PLoginEntity.class);
        }
        return pEntity;
    }

    /**
     * 清除文件信息
     */
    public void clearInfo(){
        SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
