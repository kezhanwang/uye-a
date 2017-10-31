package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/31
 */
public class ReqExperiListEntity extends ReqBaseEntity{
    public static final int TYPE_DEGREE = 1;    //学历
    public static final int TYPE_OCC = 2;       //职业
    public int type = TYPE_OCC;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_EXPERIENCE_LIST;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("type",type);
        return mReqMap;
    }
}
