package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.Map;

/**
 * Created by billy on 2017/10/21.
 */

public class Req400ContactEntity extends ReqBaseEntity{

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_400_CONTACT;
    }

    @Override
    public Map<String, Object> getReqData() {
        return null;
    }
}
