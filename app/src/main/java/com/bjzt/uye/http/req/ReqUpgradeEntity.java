package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/24.
 */

public class ReqUpgradeEntity extends ReqBaseEntity{
    public int version_code;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_UPGRADE;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("version_code",version_code);
        return mReqMap;
    }
}
