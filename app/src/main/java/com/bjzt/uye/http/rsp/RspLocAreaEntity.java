package com.bjzt.uye.http.rsp;

import com.bjzt.uye.entity.PLocItemEntity;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/26.
 */

public class RspLocAreaEntity extends RspBaseEntity{
    public ArrayList<PLocItemEntity> list;

    @Override
    public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<PLocItemEntity>>(){}.getType();
        list = gson.fromJson(jsonArray.toString(),type);
        if(list != null){
            for(PLocItemEntity item : list){
                if(item != null){
                    item.mType = PLocItemEntity.TYPE_AREA;
                }
            }
        }
    }
}
