package com.bjzt.uye.http.rsp;

import com.bjzt.uye.entity.PExperiEntity;
import com.bjzt.uye.entity.PExperiInfoEntity;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.bjzt.uye.http.req.ReqExperiListEntity;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;

/**
 * Created by billy on 2017/10/31
 */
public class RspExperiListEntity extends RspBaseEntity implements Serializable{
    public PExperiInfoEntity mEntity;
    public int mType;

    @Override
    public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity) {
        Gson gson = new Gson();
//        Type type = new TypeToken<List<PExperiEntity>>(){}.getType();
//        mList = gson.fromJson(jsonArray.toString(),type);
        mEntity = gson.fromJson(jsonObj.toString(),PExperiInfoEntity.class);
        ReqExperiListEntity reqExEntity = (ReqExperiListEntity) reqEntity;
        int typeInt = reqExEntity.type;
        this.mType = typeInt;
        if(mEntity.list != null){
            for(int i = 0;i < mEntity.list.size();i++){
                PExperiEntity pEntity = mEntity.list.get(i);
                pEntity.type = typeInt;
            }
        }
    }
}
