package com.bjzt.uye.http.rsp;

import com.bjzt.uye.entity.PBankEntity;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by billy on 2017/10/18.
 */
public class RspIDentityCfgEntity extends RspBaseEntity{
    public List<PBankEntity> mList;

    @Override
    public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<PBankEntity>>(){}.getType();
        mList = gson.fromJson(jsonArray.toString(),type);
    }
}
