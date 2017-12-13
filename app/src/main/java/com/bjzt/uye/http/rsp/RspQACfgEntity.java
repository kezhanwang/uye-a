package com.bjzt.uye.http.rsp;

import com.bjzt.uye.entity.PQACfgEnttiy;
import com.bjzt.uye.entity.PQACfgItemEntity;
import com.bjzt.uye.entity.VQAItemEntity;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/21.
 */

public class RspQACfgEntity extends RspBaseEntity implements Serializable{
    public PQACfgEnttiy mEntity;

    @Override
    public void parseData(JSONObject jsonObj, JSONArray jsonArray, boolean isArray, ReqBaseEntity reqEntity) {
        Gson gson = new Gson();
        this.mEntity = gson.fromJson(jsonObj.toString(),PQACfgEnttiy.class);
        if(this.mEntity.questions != null){
            for(int i = 0;i < this.mEntity.questions.size();i++){
                PQACfgItemEntity item = this.mEntity.questions.get(i);
//                //test code
//                if(i == 1){
//                    item.type = PQACfgItemEntity.TYPE_MUTI;
//                }
                if(item != null && item.answer != null){
                    ArrayList<VQAItemEntity> mList = new ArrayList<VQAItemEntity>();
                    for(int j = 0;j < item.answer.size();j++){
                        String str = item.answer.get(j);
                        VQAItemEntity vEntity = new VQAItemEntity();
                        vEntity.id = item.id;
                        vEntity.data = str;
                        vEntity.c = (char) ('A' + j);
                        mList.add(vEntity);
                        if(j >= item.answer.size() - 1){
                            vEntity.isBottom = true;
                        }
                        vEntity.vQAId = item.id;
                        vEntity.vType = item.type;
                    }
                    item.vAnswer = mList;
                }
            }
        }
    }
}
