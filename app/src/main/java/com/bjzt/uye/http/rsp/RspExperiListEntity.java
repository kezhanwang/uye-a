package com.bjzt.uye.http.rsp;

import com.bjzt.uye.entity.PExperiEntity;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.bjzt.uye.http.req.ReqExperiListEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by billy on 2017/10/31
 */
public class RspExperiListEntity extends RspBaseEntity implements Serializable{
    public List<PExperiEntity> mList;
    public int mType;

    @Override
    public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<PExperiEntity>>(){}.getType();
        mList = gson.fromJson(jsonArray.toString(),type);

        ReqExperiListEntity reqExEntity = (ReqExperiListEntity) reqEntity;
        int typeInt = reqExEntity.type;
        this.mType = typeInt;
        if(mList != null){
            for(int i = 0;i < mList.size();i++){
                PExperiEntity pEntity = mList.get(i);
                pEntity.type = typeInt;
            }
        }
    }
}
