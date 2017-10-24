package com.bjzt.uye.file;

import android.content.Context;
import android.content.SharedPreferences;

import com.bjzt.uye.entity.PCourseEntity;
import com.bjzt.uye.entity.VOrderInfoEntity;
import com.bjzt.uye.global.Global;
import com.google.gson.Gson;

/**
 * Created by billy on 2017/10/24.
 */

public class SharePreOrderInfo {
    private final String FILE_NAME = "RMS_ORDER_INFO";
    private final String KEY = "key";
    private final String KEY_COURSE = "key_course";

    /***
     * 保存la,lo信息
     */
    public void saveOrderInfo(String orgId,VOrderInfoEntity vEntity){
        if(vEntity != null){
            Gson gson = new Gson();
            String str = gson.toJson(vEntity);
            SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(KEY+"_"+orgId,str);
            editor.commit();
        }
    }

    /****
     * 获取存储经纬度信息
     * @return
     */
    public VOrderInfoEntity loadOrderInfoEntity(String orgId){
        VOrderInfoEntity mEntity = null;
        SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        String strInfo = sp.getString(KEY+"_"+orgId,"");
        if(strInfo != null){
            Gson gson = new Gson();
            mEntity = gson.fromJson(strInfo,VOrderInfoEntity.class);
        }
        return mEntity;
    }

    public void saveCourseInfo(String orgId, PCourseEntity vEntity){
        if(vEntity != null){
            Gson gson = new Gson();
            String str = gson.toJson(vEntity);
            SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(KEY_COURSE+"_"+orgId,str);
            editor.commit();
        }
    }

    public PCourseEntity loadCourseInfo(String orgId){
        PCourseEntity pEntity = null;
        SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        String strInfo = sp.getString(KEY_COURSE+"_"+orgId,"");
        if(strInfo != null){
            Gson gson = new Gson();
            pEntity = gson.fromJson(strInfo,PCourseEntity.class);
        }
        return pEntity;
    }
}
