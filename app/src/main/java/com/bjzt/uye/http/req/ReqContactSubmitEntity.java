package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/30.
 */

public class ReqContactSubmitEntity extends ReqBaseEntity{
    public String home_province;
    public String home_city;
    public String home_area;
    public String home_address;
    public String email;
    public String wechat;
    public String qq;
    public String marriage;
    public String contact1_name;
    public String contact1_phone;
    public String contact1_relation;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_CONTACT_SUBMIT;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("home_province",home_province);
        mReqMap.put("home_city",home_city);
        mReqMap.put("home_area",home_area);
        mReqMap.put("home_address",home_address);
        mReqMap.put("email",email);
        mReqMap.put("wechat",wechat);
        mReqMap.put("qq",qq);
        mReqMap.put("marriage",marriage);
        mReqMap.put("contact1_name",contact1_name);
        mReqMap.put("contact1_phone",contact1_phone);
        mReqMap.put("contact1_relation",contact1_relation);
        return mReqMap;
    }
}
