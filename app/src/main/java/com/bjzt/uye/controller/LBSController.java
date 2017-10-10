package com.bjzt.uye.controller;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bjzt.uye.entity.VLoc;
import com.bjzt.uye.file.SharePreLBS;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.ILocListener;
import com.common.common.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/10.
 */

public class LBSController {
    private final String TAG = "LBSController";

    private static LBSController instance;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    private String la;
    private String lo;
    private List<Integer> mReqList = null;
    private ILocListener mLocListener;
    private final int TIME_DELAY = 10 * 1000;

    private LBSController() {
        // TODO Auto-generated constructor stub
        mLocationClient = new LocationClient(Global.mContext);     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mReqList = new ArrayList<Integer>();
    }

    public synchronized  static final LBSController getInstance(){
        if(instance == null){
            instance = new LBSController();
        }
        return instance;
    }

    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLoc) {
            if(MyLog.isDebugable()){
                MyLog.debug(TAG,"[onReceiveLocation]" + " 定位成功...la:" + bdLoc.getLatitude() + " lo:" + bdLoc.getLongitude());
            }
            if(bdLoc != null){
                lo = bdLoc.getLongitude()+"";
                la = bdLoc.getLatitude()+"";

                if(mLocationClient != null){
                    mLocationClient.stop();
                }
                Global.removeDelay(rStop);
                Global.removeDelay(rTimeOut);

                Global.postDelay(new Runnable() {
                    @Override
                    public void run() {
                        SharePreLBS mShare = new SharePreLBS();
                        mShare.saveLBSInfo(la, lo);
                    }
                });
            }
        }
    }

    public void loadInfo(){
        SharePreLBS mShare = new SharePreLBS();
        VLoc entity = mShare.getLocInfo();
        this.la = entity.la;
        this.lo = entity.lo;
    }

    public void setILocListener(ILocListener mLocListener){
        this.mLocListener = mLocListener;
    }

    public void startLoc(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setIsNeedAddress(true);
        int span=0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

        mLocationClient.setLocOption(option);
        try{
            mLocationClient.start();
            Global.post2UIDelay(rTimeOut,TIME_DELAY);
        }catch(Exception ee){
            MyLog.error(TAG,"[startLoc]",ee);
        }
        MyLog.debug(TAG,"[startLoc]");
        Global.removeDelay(rStop);
        Global.postDelay(rStop,1000*15);
    }


    private Runnable rTimeOut = new Runnable() {
        @Override
        public void run() {
            if(mLocListener != null){
                mLocListener.onLocError(null);
            }
        }
    };

    private Runnable rStop = new Runnable() {
        @Override
        public void run() {
            if(mLocationClient != null){
                mLocationClient.stop();
            }
        }
    };


}
