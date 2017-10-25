package com.bjzt.uye.util;

import android.content.Intent;

import com.bjzt.uye.entity.VActivityResultEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/25.
 */

public class ActivityResultUtil {

    public static final VActivityResultEntity onActivityReult(List<String> sList, Intent data){
        VActivityResultEntity vEntity = new VActivityResultEntity();
        if(sList != null){
            if(data != null){
                ArrayList<String> tList = data.getStringArrayListExtra(IntentUtils.PARA_KEY_PUBLIC);
                if(tList != null){
                    if(sList.size() <= tList.size()){
                        vEntity.isDel = false;
                    }else{
                        vEntity.isDel = true;
                        vEntity.mList = tList;
                    }
                }
            }
        }
        return vEntity;
    }
}
