package com.common.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

import com.bjzt.uye.global.Global;
import com.common.common.MyLog;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class DeviceUtil {
	private static final String TAG = "DeviceUtil";
	
	public static int mWidth;
	public static int mHeight;
	
	static{
		WindowManager wm = (WindowManager) Global.mContext.getSystemService(Context.WINDOW_SERVICE);
		mWidth = wm.getDefaultDisplay().getWidth();
		mHeight = wm.getDefaultDisplay().getHeight();
	}
	
	/***
	 * 获得IMEI
	 * @return
	 */
	public static final String getIMEI(){
		String str = "";
		TelephonyManager telManager = (TelephonyManager) Global.mContext.getSystemService(Context.TELEPHONY_SERVICE);
		if(telManager != null){
			str = telManager.getDeviceId();
		}
		return str;
	}
	
	/****
	 * android os序列号
	 * @return
	 */
	public static final String getAndroidSer(){
		String ser = "";
		ser = android.os.Build.SERIAL;
		return ser;
	}
	
	public static final String getAndroidVer(){
		String ver = "";
		ver = android.os.Build.VERSION.SDK;
		return ver;
	}
	
	public static final String getAndroidTime(){
		String time="";
		time = android.os.Build.TIME+"";
		return time;
	}
	
	/***
	 * 获取mac地址
	 * @return
	 */
	public static final String getMacAdd(){
		 // 获取mac地址：
		 String command = "cat /sys/class/net/wlan0/address ";
		 return getStringInfo(command);
	}
	
	/***
	 * 获取CPU信息
	 * @return
	 */
	public static final String getCpuInfo(){
		String command = "cat /proc/cpuinfo";
		return getStringInfo(command);
	}
	
	/***
	 * 获取android ID
	 * @return
	 */
	public static final String getAndroidID(){
		String androidID = "";
		androidID = android.os.Build.ID;
		return androidID;
	}
	
	/***
	 * 获取屏幕宽度
	 * @return
	 */
	public static final int getDeviceWidth(){
		return mWidth;
	}
	
	/***
	 * 获取屏幕高度
	 * @return
	 */
	public static final int getDeviceHeight(){
		return mHeight;
	}
	

	private static final String getStringInfo(String command){
        String str = "";
        try {
             Process pp = Runtime.getRuntime().exec(command);
             InputStreamReader ir = new InputStreamReader(pp.getInputStream());
             LineNumberReader input = new LineNumberReader(ir);
             for (;null != str;) {
                    str = input.readLine();
                    if (str != null) {
                            str = str.trim();// 去空格
                            break;
                    }
              }
        } catch (IOException ex) {
                // 赋予默认值
                ex.printStackTrace();
                str = "00000000";
        }
        return str;
	}
	
	public static int getAndroidSDKVersion() {
		int ver = 0;
		String version = android.os.Build.VERSION.SDK;
		if(!TextUtils.isEmpty(version)){
			try{
				ver = Integer.valueOf(version);
			}catch(Exception ee){
				MyLog.error(TAG,ee);
			}
		}
		return ver;
	}
	
	/***
	 * 获取手机型号
	 * @return
	 */
	public static final String getDeviceModel(){
		String model = android.os.Build.MODEL;
		return model;
	}

	/***
	 * 获取手机制作厂商
	 * @return
	 */
	public static final String getDeviceProduct(){
		return android.os.Build.MANUFACTURER;
	}
	
	/***
	 * 获取realse信息
	 * @return
	 */
	public static final String getDeviceRelease(){
		return android.os.Build.VERSION.RELEASE;
	}
}
