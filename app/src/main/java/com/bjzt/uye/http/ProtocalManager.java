package com.bjzt.uye.http;

import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.TaskCommonV2;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.http.req.ReqLocCityEntity;
import com.common.http.HttpEngine;

/**
 * Created by billy on 2017/10/12.
 */

public class ProtocalManager {
    private final String TAG = getClass().getSimpleName();

    private static ProtocalManager instance;

    private ProtocalManager(){

    }

    public synchronized static final ProtocalManager getInstance(){
        if(instance == null){
            instance = new ProtocalManager();
        }
        return instance;
    }

    /***
     * 对应的Task任务添加到Http请求引擎中
     * @param reqEntity
     * @param callBack
     * @return
     */
    private int addTask(ReqBaseEntity reqEntity, ICallBack<Object> callBack){
        int seqNo = Global.getSeqNo();
        reqEntity.seqNo = seqNo;
        TaskCommonV2 task = new TaskCommonV2(reqEntity,callBack);
        HttpEngine.getInstance().addTask(task);
        return seqNo;
    }

    /***
     * 获取定位城市
     * @param callBack
     * @return
     */
    public int reqLocCity(ICallBack<Object> callBack){
        ReqLocCityEntity reqEntity = new ReqLocCityEntity();
        return addTask(reqEntity,callBack);
    }
}
