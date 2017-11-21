package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/11/20.
 */

public class ReqEmployProSubmit extends ReqBaseEntity{
    public String insured_id;
    public String date;
    public String work_province;
    public String work_city;
    public String work_area;
    public String work_address;
    public String work_name;
    public String position;
    public String monthly_income;
    public int is_hiring;
    public ArrayList<String> pic_json;     //json

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_EMPLOY_PRO_SUBMIT;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("insured_id",insured_id);
        mReqMap.put("date",date);
        mReqMap.put("work_province",work_province);
        mReqMap.put("work_city",work_city);
        mReqMap.put("work_area",work_area);
        mReqMap.put("work_address",work_address);
        mReqMap.put("work_name",work_name);
        mReqMap.put("position",position);
        mReqMap.put("monthly_income",monthly_income);
        mReqMap.put("is_hiring",is_hiring);
        if(pic_json != null && pic_json.size() > 0){
            Gson gson = new Gson();
            String str = gson.toJson(pic_json);
            mReqMap.put("pic_json",str);
        }
        return mReqMap;
    }
}
