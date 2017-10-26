package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/26.
 */

public class ReqLocAreaEntity extends ReqBaseEntity{
    public String city;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_LOC_AREA;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> reqMap = new HashMap<>();
        reqMap.put("city",city);
        return reqMap;
    }
}
