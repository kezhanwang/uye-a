package com.bjzt.uye.controller;

import com.bjzt.uye.entity.P400ContactEntity;
import com.bjzt.uye.file.SharePre400;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.http.rsp.Rsp400ContactEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by billy on 2017/10/21.
 * 1.400电话相关业务
 * 2.
 */
public class OtherController {

    private static OtherController instance;
    private List<Integer> mReqList = new ArrayList<Integer>();
    private P400ContactEntity m400ContactEntity;
    private Set<DataRefreshListener> mSet = new HashSet<>();

    private OtherController(){

    }

    public static final OtherController getInstance(){
        if(instance == null){
            instance = new OtherController();
        }
        return instance;
    }

    /***
     * 请求客服联系配置
     */
    public void requestKFContactInfo(){
        int seqNo = ProtocalManager.getInstance().req400Contact(callBack);
        mReqList.add(seqNo);
    }


    private ICallBack<Object> callBack = new ICallBack<Object>() {
        @Override
        public void getResponse(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
            if(mReqList.contains(Integer.valueOf(seqNo))){
                if(rsp instanceof Rsp400ContactEntity){
                    Rsp400ContactEntity rspEntity = (Rsp400ContactEntity) rsp;
                    if(isSucc && rspEntity.mEntity != null){
                        m400ContactEntity = rspEntity.mEntity;
                        //save to sharePre
                        save400Info2File(m400ContactEntity);
                    }
                }
            }
        }
    };

    private void save400Info2File(final P400ContactEntity mEntity){
        Global.postDelay(new Runnable() {
            @Override
            public void run() {
                SharePre400 mSharePre = new SharePre400();
                mSharePre.save400ContactEntity(mEntity);
            }
        });
    }

    public void loadInfo(){
        SharePre400 mSharePre = new SharePre400();
        this.m400ContactEntity = mSharePre.load400ContactEntity();
    }

    /**
     * 获取客服电话
     * @return
     */
    public String getKF(){
        if(this.m400ContactEntity != null){
            return this.m400ContactEntity.company_phone;
        }
        return null;
    }

    public static interface DataRefreshListener {
        public void onRefresh();
    }

    public void registerRefreshListener(DataRefreshListener mListener){
        mSet.add(mListener);
    }

    public void unRegisterRefreshListener(DataRefreshListener mListener){
        mSet.remove(mListener);
    }

    public void notifyRefresh(){
        for(DataRefreshListener mListener : mSet){
            mListener.onRefresh();
        }
    }
}
