package com.bjzt.uye.http.listener;

/**
 * Created by billy on 2017/10/12.
 */
public interface ICallBack<Object>{
    public void getResponse(Object rsp, boolean isSucc, int errorCode, int seqNo, int src);
}
