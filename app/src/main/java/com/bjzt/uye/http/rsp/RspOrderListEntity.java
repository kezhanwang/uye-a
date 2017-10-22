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
        List<PInsureOrderEntity> mList = new ArrayList<>();
        if(mEntity != null && mEntity.page != null){
            for(int i = 0;i < mEntity.page.totalCount;i++){
                PInsureOrderEntity pEntity = new PInsureOrderEntity();
                if(i == 0){
                    pEntity.isFake = false;
                    pEntity.insured_order = mEntity.insured_order;
                    pEntity.page = mEntity.page;
                }
                pEntity.p = i + 1;
                mList.add(pEntity);
            }
        }
        return mList;
    }
}
