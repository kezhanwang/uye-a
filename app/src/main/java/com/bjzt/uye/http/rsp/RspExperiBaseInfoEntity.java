package com.bjzt.uye.http.rsp;

import com.bjzt.uye.entity.PExperiBaseInfoEntity;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by billy on 2017/10/31.
 */

public class RspExperiBaseInfoEntity extends RspBaseEntity{
    public PExperiBaseInfoEntity mEntity;

    @Override
    public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity) {
        Gson gson = new Gson();
        mEntity = gson.fromJson(jsonObj.toString(),PExperiBaseInfoEntity.class);
    }
}
