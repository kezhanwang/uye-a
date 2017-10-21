package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/21
 */
public class ReqOrderListEntity extends ReqBaseEntity{
    public int page;
    public int pageSize = 10;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_ORDER_LIST;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("page",page);
        mReqMap.put("pageSize",pageSize);
        return mReqMap;
    }
}
