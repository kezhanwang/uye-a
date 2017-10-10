package com.bjzt.uye.file;

import android.content.Context;
import android.content.SharedPreferences;
import com.bjzt.uye.entity.VLoc;
import com.bjzt.uye.global.Global;

/**
 * Created by billy on 2017/10/10.
 */

public class SharePreLBS {
    private final String FILE_NAME = "RMS_LBS";
    private final String KEY_SREACH_LA = "la";
    private final String KEY_SREACH_LO = "lo";

    /***
     * 保存la,lo信息
     * @param la
     */
    public void saveLBSInfo(String la,String lo){
        SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_SREACH_LA,la);
        editor.putString(KEY_SREACH_LO,lo);
        editor.commit();
    }

    /****
     * 获取存储经纬度信息
     * @return
     */
    public VLoc getLocInfo(){
        SharedPreferences sp = Global.getContext().getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        String la = sp.getString(KEY_SREACH_LA, "");
        String lo = sp.getString(KEY_SREACH_LO,"");
        VLoc entity = new VLoc();
        entity.la = la;
        entity.lo = lo;
        return entity;
    }

}
