package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diaosi on 2016/6/3.
 */
public class VTimeEntity implements Serializable {

    public String strInfo;

    public String buildTime(){
        return strInfo;
    }

    public static List<VTimeEntity> buildCfgList(List<String> mReqList){
        List<VTimeEntity> mList = new ArrayList<VTimeEntity>();
        if(mReqList != null){
            for(String str : mReqList){
                VTimeEntity vEntity = new VTimeEntity();
                vEntity.strInfo = str;
                mList.add(vEntity);
            }
        }
        return mList;
    }
}
