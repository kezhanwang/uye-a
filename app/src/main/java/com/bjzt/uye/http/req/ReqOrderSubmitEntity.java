package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保单提交
 * Created by billy on 2017/10/19.
 */
public class ReqOrderSubmitEntity extends ReqBaseEntity{
    public String org_id;
    public String c_id;
    public long tuition;
    public String clazz;
    public String class_start;
    public String class_end;
    public String course_consultant;
    public String group_pic;            //合影图片
    public List<String> training_pic;         //协议图片
    public String insured_type;         //投保类型，1：就业帮 2：高薪帮

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_ORDER_SUBMIT;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("org_id",org_id);
        mReqMap.put("c_id",c_id);
        mReqMap.put("tuition",tuition);
        mReqMap.put("class",clazz);
        mReqMap.put("class_start",class_start);
        mReqMap.put("class_end",class_end);
        mReqMap.put("course_consultant",course_consultant);
        mReqMap.put("group_pic",group_pic);
        if(training_pic != null && training_pic.size() > 0){
            Gson gson = new Gson();
            String strInfo = gson.toJson(training_pic);
            mReqMap.put("training_pic",strInfo);
        }
        mReqMap.put("insured_type",insured_type);
        return mReqMap;
    }
}
