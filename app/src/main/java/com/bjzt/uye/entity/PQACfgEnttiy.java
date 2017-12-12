package com.bjzt.uye.entity;

import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.MutiBaseListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billy on 2017/10/21.
 */

public class PQACfgEnttiy implements Serializable{
    public boolean need_question;
    public ArrayList<PQACfgItemEntity> questions;

    public Map<Integer,ArrayList<VQAItemEntity>> buildHashMap(){
        Map<Integer,ArrayList<VQAItemEntity>> mMap = new HashMap<>();
        if(questions != null && questions.size() > 0){
            for(int i = 0;i < questions.size();i++){
                PQACfgItemEntity entity = questions.get(i);
                mMap.put(Integer.valueOf(entity.id),new ArrayList<VQAItemEntity>());
            }
        }
        return mMap;
    }

    public List<BaseItemListener> buildList(){
        List<BaseItemListener> mList = new ArrayList<>();
        if(questions != null){
            BaseItemListener itemEntity = new BaseItemListener() {
                @Override
                public int getType() {
                    return MutiBaseListAdapter.TYPE_QA_HEADER;
                }
            };
            mList.add(itemEntity);

            for(int i = 0;i < questions.size();i++){
                PQACfgItemEntity entity = questions.get(i);
                mList.add(entity);
            }
        }
        return mList;
    }
}
