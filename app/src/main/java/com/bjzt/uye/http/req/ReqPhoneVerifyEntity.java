package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/15.
 */

public class ReqPhoneVerifyEntity extends ReqBaseEntity{
    public String phone;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_PHONE_VERIFY_CODE;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mMap = new HashMap<>();
        mMap.put("phone",phone);
        return mMap;
    }
}
