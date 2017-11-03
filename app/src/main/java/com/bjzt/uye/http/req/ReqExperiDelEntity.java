package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/11/3.
 */

public class ReqExperiDelEntity extends ReqBaseEntity{
    public int id;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_EXPERIENCE_DEL;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("id",id);
        return mReqMap;
    }
}
