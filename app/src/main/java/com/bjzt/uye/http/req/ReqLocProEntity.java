package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.Map;

/**
 * 获取省份列表
 * Created by billy on 2017/10/26.
 */
public class ReqLocProEntity extends ReqBaseEntity{

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_LOC_PROVINCE;
    }

    @Override
    public Map<String, Object> getReqData() {
        return null;
    }
}
