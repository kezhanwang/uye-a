package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/26.
 */

public class ReqLocCityEntity extends ReqBaseEntity{
    public String province;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_LOC_CITYLIST;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("province",province);
        return mReqMap;
    }
}
