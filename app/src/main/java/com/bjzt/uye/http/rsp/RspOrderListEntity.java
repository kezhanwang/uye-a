package com.bjzt.uye.http.rsp;

import com.bjzt.uye.entity.PInsureOrderEntity;
import com.bjzt.uye.entity.PInsureOrderItemEntity;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/21.
 */

public class RspOrderListEntity extends RspBaseEntity{
    public PInsureOrderEntity mEntity;

    @Override
    public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity) {
        Gson gson = new Gson();
        mEntity = gson.fromJson(jsonObj.toString(),PInsureOrderEntity.class);
    }

    public List<PInsureOrderEntity> buildList(){
        List<PInsureOrderEntity> mList = new ArrayList<PInsureOrderEntity>();
        mList.add(mEntity);
        mList.add(mEntity);
        mList.add(mEntity);
        return mList;
    }
}
