package com.bjzt.uye.http.req;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import java.util.Map;

/**
 * Created by billy on 2017/10/12
 */
public class ReqHomeEntity extends ReqBaseEntity{

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_HOME;
    }

    @Override
    public Map<String, Object> getReqData() {
        return null;
    }
}