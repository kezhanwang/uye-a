package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取机构详情
 * Created by billy on 2017/10/27.
 */
public class ReqOrgDetailEntity extends ReqBaseEntity{
    public String org_id;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_ORG_DETAIL;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("org_id",org_id);
        return mReqMap;
    }
}
