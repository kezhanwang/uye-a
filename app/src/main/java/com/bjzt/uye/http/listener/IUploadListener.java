package com.bjzt.uye.http.listener;

/**
 * Created by billy on 2017/10/18.
 */

public abstract class IUploadListener {
    public abstract void upload(long curLen,long maxLen,String key,int mType);

    public void uploadItemSucc(int mType,String netUrl){};

    public  void uploadError(int mType,String netUrl,String tips){};
}
