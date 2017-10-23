package com.bjzt.uye.file;

import android.content.Context;
import android.content.SharedPreferences;
import com.bjzt.uye.global.Global;

/**
 * Created by billy on 2017/10/23
 */
public class SharePreID {
    private final String FILE_NAME = "RMS_CFG_ID";
    private final String KEY = "key";

    /***
     * 保存la,lo信息
     */
    public void saveFirstFlag(boolean isFirst){
        SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY,isFirst);
        editor.commit();
    }

    /****
     * 获取存储经纬度信息
     * @return
     */
    public boolean loadFirstFlag(){
        SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(KEY,true);
    }
}
