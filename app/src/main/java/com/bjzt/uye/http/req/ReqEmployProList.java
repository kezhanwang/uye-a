package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/11/20
 */
public class ReqEmployProList extends ReqBaseEntity{
    public String insured_id;
    public int page;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_EMPLOY_PRO_LIST;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("insured_id",insured_id);
        mReqMap.put("page",page);
        return mReqMap;
    }
}
