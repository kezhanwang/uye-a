package com.bjzt.uye.http.req;

import com.bjzt.uye.entity.VQAItemEntity;
import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by billy on 2017/10/21.
 */

public class ReqQASubmitEntity extends ReqBaseEntity implements  Serializable{
    public String question;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_QA_SUBMIT;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("question",question);
        return mReqMap;
    }

    public void parseInfo(List<VQAItemEntity> mList){
        List<QASubmitEntity> rList = new ArrayList<>();
        for(int i = 0;i < mList.size();i++){
            VQAItemEntity vEntity = mList.get(i);
            int id = vEntity.id;
            QASubmitEntity entity = findIdSucc(rList,id);
            if(entity != null){
                entity.answer.add(vEntity.data);
            }else{
                entity = new QASubmitEntity();
                entity.id = id;
                entity.answer.add(vEntity.data);
                rList.add(entity);
            }
        }

        Gson gson = new Gson();
        this.question = gson.toJson(rList);
    }

    private QASubmitEntity findIdSucc(List<QASubmitEntity> mList, int id){
        QASubmitEntity rEntity = null;
        if(mList != null && mList.size() > 0){
            for(int i = 0;i < mList.size();i++){
                QASubmitEntity entity = mList.get(i);
                if(entity != null && entity.id == id){
                    rEntity = entity;
                    break;
                }
            }
        }
        return rEntity;
    }

    public class QASubmitEntity implements Serializable{
        public int id;
        public List<String> answer = new ArrayList<String>();
    }
}
