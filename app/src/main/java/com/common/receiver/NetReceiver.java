package com.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.bjzt.uye.controller.WifiController;

/**
 * Created by billy on 2017/10/25.
 */

public class NetReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager mConnectivityManager;
        NetworkInfo netInfo;
        if(intent != null && context != null){
            String action = intent.getAction();
            if(!TextUtils.isEmpty(action)){
                mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo != null && netInfo.isAvailable()){
                    if(netInfo.getType() == ConnectivityManager.TYPE_WIFI){
                        WifiController.getInstance().notifyNetInfo(true);
                    }
                }
            }
        }
    }
}
