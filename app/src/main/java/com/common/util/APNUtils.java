package com.common.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.bjzt.uye.global.Global;


/*****
 * 获取接入点
 */
public class APNUtils {

	public static final int NET_TYPE_UNKUNOW = -1;
	public static final int NET_TYPE_WIFI = 1;	//wifi网络类型
	public static final int NET_TYPE_GPRS_2 = 2;	//2g网络类型
	public static final int NET_TYPE_GPRS_3 = 3;	//3g网络
	public static final int NET_TYPE_GPRS_4 = 4;	//4g网络

	/***
	 * 获取当前网络类型
	 * @return
	 */
	public static final int getNetType(){
		int type = APNUtils.NET_TYPE_UNKUNOW;
		Context context = Global.mContext;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm != null){
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info == null) {
				type = APNUtils.NET_TYPE_UNKUNOW;
			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				type = APNUtils.NET_TYPE_WIFI;
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				int subType = info.getSubtype();
				if (subType == TelephonyManager.NETWORK_TYPE_CDMA
						|| subType == TelephonyManager.NETWORK_TYPE_GPRS
						|| subType == TelephonyManager.NETWORK_TYPE_EDGE) {
					type = APNUtils.NET_TYPE_GPRS_2;
				} else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
						|| subType == TelephonyManager.NETWORK_TYPE_HSDPA
						|| subType == TelephonyManager.NETWORK_TYPE_EVDO_A
						|| subType == TelephonyManager.NETWORK_TYPE_EVDO_0
						|| subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
					type = APNUtils.NET_TYPE_GPRS_3;
				} else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
					type = APNUtils.NET_TYPE_GPRS_4;
				}
			}
		}
		return type;
	}


	/****
	 * 判断是否为弱网络,目前只是判断是2G网络为弱网络类型
	 * @return
	 */
	public static final boolean isWeakNet(){
		int type = getNetType();
		if(type == APNUtils.NET_TYPE_GPRS_2) {
			return true;
		}
		return false;
	}

	/****
	 * 是否是wifi网络
	 * @return
	 */
	public static final boolean isWifi(){
		int type = getNetType();
		if(type == APNUtils.NET_TYPE_WIFI){
			return true;
		}
		return false;
	}


	/****
	 * 根据type显示网络类型的名称
	 * @param mType
	 * @return
	 */
	public static final String getNameByApnType(int mType){
		String str = "";
		switch(mType){
			case APNUtils.NET_TYPE_UNKUNOW:
				str = "未知网络";
				break;
			case APNUtils.NET_TYPE_WIFI:
				str = "Wifi网络";
				break;
			case APNUtils.NET_TYPE_GPRS_2:
				str = "2G网络";
				break;
			case APNUtils.NET_TYPE_GPRS_3:
				str = "3G网络";
				break;
			case APNUtils.NET_TYPE_GPRS_4:
				str = "4G网路";
				break;
		}
		return str;
	}
}
