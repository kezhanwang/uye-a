package com.bjzt.uye.file;

import android.content.Context;
import android.content.SharedPreferences;
import com.bjzt.uye.entity.P400ContactEntity;
import com.bjzt.uye.global.Global;
import com.google.gson.Gson;

/**
 * Created by billy on 2017/10/21.
 */

public class SharePre400 {
    private final String FILE_NAME = "RMS_400";
    private final String KEY = "key";
    /***
     * 保存la,lo信息
     */
    public void save400ContactEntity(P400ContactEntity mEntity){
        if(mEntity != null){
            Gson gson = new Gson();
            String str = gson.toJson(mEntity);
            SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(KEY,str);
            editor.commit();
        }
    }

    /****
     * 获取存储经纬度信息
     * @return
     */
    public P400ContactEntity load400ContactEntity(){
        P400ContactEntity mEntity = null;
        SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        String strInfo = sp.getString(KEY,"");
        if(strInfo != null){
            Gson gson = new Gson();
            mEntity = gson.fromJson(strInfo,P400ContactEntity.class);
        }
        return mEntity;
    }
}
