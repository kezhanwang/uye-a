package com.bjzt.uye.http.base;

import org.json.JSONObject;

/**
 * Created by billy on 2017/10/12.
 */

public interface ITaskListener {
    public static final int TYPE_SRC_NET = 100;
    public static final int TYPE_SRC_CACHE = 101;
    public static final int TYPE_SRC_MEM = 102;

    public void recyle();
    public void getResponse(JSONObject jsonObj, boolean isSucc, int errorCode, int seqNo, int src);
    public Object getReq();

}
