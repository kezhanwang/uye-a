package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/18.
 */

public class ReqIDentityPicEntity extends ReqBaseEntity{
    public String udcredit_order;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_IDENTITY_PIC;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("udcredit_order",udcredit_order);
        return mReqMap;
    }
}
