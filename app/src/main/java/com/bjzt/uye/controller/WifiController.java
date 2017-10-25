package com.bjzt.uye.controller;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.bjzt.uye.global.Global;
import com.common.entity.BWifiEntity;
import com.common.file.SharePreWifi;
import com.common.receiver.NetReceiver;
import com.common.util.APNUtils;
import com.common.util.DeviceUtil;

/**
 * Created by billy on 2017/10/25.
 */

public class WifiController {
    private final String TAG = getClass().getSimpleName();
    private static WifiController instance;
    private BWifiEntity bEntity;

    private WifiController(){
        Context mContext = Global.getContext();
        bEntity = new BWifiEntity();
        //获取广播对象
        NetReceiver receiver = new NetReceiver();
        //创建意图过滤器
        IntentFilter filter = new IntentFilter();
        //添加动作，监听网络
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        try {
            mContext.unregisterReceiver(receiver);
            mContext.registerReceiver(receiver, filter);
        }catch(Exception ee){
            ee.printStackTrace();
        }
    }

    public synchronized static final WifiController getInstance(){
        if(instance == null){
            instance = new WifiController();
        }
        return instance;
    }

    public void notifyNetInfo(boolean isWifi){
        if(isWifi){
            Global.postDelay(new Runnable() {
                @Override
                public void run() {
                    //refresh
                    String ssid = getWifiSSID();
                    String mac = DeviceUtil.getMacAdd();
                    bEntity.ssid = ssid;
                    bEntity.mac = mac;
                    SharePreWifi mSharePre = new SharePreWifi();
                    mSharePre.saveWifiEntity(bEntity);
                }
            });
        }
    }

    private String getWifiSSID(){
        Context mContext = Global.getContext();
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if(wifiInfo != null){
                return wifiInfo.getSSID();
            }
        }
        return null;
    }

    public BWifiEntity getWifiEntity(){
        return this.bEntity;
    }

    public void loadInfo(){
        SharePreWifi mSharePre = new SharePreWifi();
        BWifiEntity entity = mSharePre.loadWifiEntity();
        if(entity != null){
            this.bEntity = entity;
        }
        if(APNUtils.isWifi()){
            String ssid = getWifiSSID();
            String mac = DeviceUtil.getMacAdd();
            if(!TextUtils.isEmpty(ssid)){
                bEntity.ssid = ssid;
                bEntity.mac = mac;
                mSharePre = new SharePreWifi();
                mSharePre.saveWifiEntity(bEntity);
            }
        }
    }
}
