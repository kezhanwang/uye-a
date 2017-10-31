package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billy on 2017/10/31.
 */

public class ReqExperiBaseCommitEntity extends ReqBaseEntity{
    public String highest_education;
    public String profession;
    public String housing_situation;
    public List<String> will_work_city;
    public String monthly_income;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_EXPERIENCE_BASE_COMMIT;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("highest_education",highest_education);
        mReqMap.put("profession",profession);
        mReqMap.put("housing_situation",housing_situation);
        mReqMap.put("monthly_income",monthly_income);
        if(will_work_city != null && will_work_city.size() > 0){
            Gson gson = new Gson();
            String str = gson.toJson(will_work_city);
            mReqMap.put("will_work_city",str);
        }
        return mReqMap;
    }
}
