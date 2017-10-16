package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/16.
 */

public class ReqLoginPhoneEntity extends ReqBaseEntity{
    public String phone;
    public String code;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_LOGIN_PHONE;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("phone",phone);
        mReqMap.put("code",code);
        return mReqMap;
    }
}
