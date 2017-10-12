package com.bjzt.uye.http.rsp;

import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.common.common.MyLog;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by billy on 2017/10/12.
 */

public class RspLocCityEntity extends RspBaseEntity{

    @Override
    public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity){
        MyLog.d(TAG,"[parseData]" + " jsonObj -> " + jsonObj);
    }
}
