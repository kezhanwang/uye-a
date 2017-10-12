package com.bjzt.uye.http;

import com.bjzt.uye.http.rsp.RspLocCityEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/12.
 */

public class HttpCommon {

    public static Map<String,Class> mMap = new HashMap<>();

    public static final String URL_LOC_CITY = "/app/index/index";

    static{
        //请求定位城市
        mMap.put(URL_LOC_CITY,RspLocCityEntity.class);

    }
}
