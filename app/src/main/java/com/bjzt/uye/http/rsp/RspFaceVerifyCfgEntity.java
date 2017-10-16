package com.bjzt.uye.http.rsp;

import com.bjzt.uye.entity.PFaceVerifyEntity;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by billy on 2017/10/16.
 */

public class RspFaceVerifyCfgEntity extends RspBaseEntity{
    public PFaceVerifyEntity mEntity;

    @Override
    public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity) {
        Gson gson = new Gson();
        mEntity = gson.fromJson(jsonObj.toString(),PFaceVerifyEntity.class);
    }
}
