package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/17.
 */

public class ReqSearchEntity extends ReqBaseEntity{
    public String word;
    public int page = 1;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_SEARCH;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("word",word);
        mReqMap.put("page",page);
        return mReqMap;
    }
}
