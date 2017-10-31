package com.bjzt.uye.http.req;

import android.text.TextUtils;
import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/31.
 */

public class ReqExperiAddEntity extends ReqBaseEntity{
    public int type;
    public String date_start;
    public String date_end;
    public String work_name;
    public String work_position;
    public String work_salary;
    public String school_name;
    public String school_profession;
    public String school_address;
    public String education;
    public int id = -1;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_EXPERIENCE_ADD;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("type",type);
        mReqMap.put("date_start",date_start);
        mReqMap.put("date_end",date_end);
        if(!TextUtils.isEmpty(work_name)){
            mReqMap.put("work_name",work_name);
        }
        if(!TextUtils.isEmpty(work_position))
            mReqMap.put("work_position",work_position);
        if(!TextUtils.isEmpty(work_salary))
            mReqMap.put("work_salary",work_salary);
        if(!TextUtils.isEmpty(school_name))
            mReqMap.put("school_name",school_name);
        if(!TextUtils.isEmpty(school_profession))
            mReqMap.put("school_profession",school_profession);
        if(!TextUtils.isEmpty(school_address))
            mReqMap.put("school_address",school_address);
        if(!TextUtils.isEmpty(education)){
            mReqMap.put("education",education);
        }
        if(id >= 0){
            mReqMap.put("id",id);
        }
        return mReqMap;
    }
}
