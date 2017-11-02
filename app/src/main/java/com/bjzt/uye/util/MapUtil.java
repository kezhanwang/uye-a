package com.bjzt.uye.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.bjzt.uye.global.Global;
import com.common.common.MyLog;

import java.net.URISyntaxException;

public class MapUtil {
	private static final String TAG = "MapUtil";

	public static final String PACKAGE_BAIDU_MAP = "com.baidu.BaiduMap";
	public static final String PACKAGE_GAODE_MAP = "com.autonavi.minimap";
	public static final String PACKAGE_TENCENT_MAP = "com.tencent.map";

	public static final boolean isIntallBaiduMap() {
		return isIntall(PACKAGE_BAIDU_MAP);
	}

	public static final boolean isIntallGaodeMap() {
		return isIntall(PACKAGE_GAODE_MAP);
	}

	public static final boolean isIntallTencentMap() {
		return isIntall(PACKAGE_TENCENT_MAP);
	}

	/**
	 * 打开百度地图导航
	 * 
	 * @param mContext
	 * @param startLat
	 *            起点纬度
	 * @param startLng
	 *            起点经度
	 * @param endLat
	 *            终点纬度
	 * @param endLng
	 *            终点经度
	 * @param sName
	 *            终点名称
	 */
	public static void openBaiduMap(Context mContext, double startLat,
									double startLng, double endLat, double endLng, String sName) {
		Intent intent;
		try {
			String str = "intent://map/direction?origin=latlng:"
					+ startLat
					+ ","
					+ startLng
					+ "|name:我的位置"
					+ "&destination=latlng:"
					+ endLat
					+ ","
					+ endLng
					+ "|name:"
					+ sName
					+ "&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;"
					+ "package=com.baidu.BaiduMap;end";
			intent = Intent.getIntent(str);
			mContext.startActivity(intent);// 启动调用
		} catch (URISyntaxException ee) {
			// TODO Auto-generated catch block
			MyLog.error(TAG, ee);
		}
	}

	/**
	 * 打开高德地图导航
	 * 
	 * @param mContext
	 * @param endLat
	 *            终点纬度
	 * @param endLng
	 *            终点经度
	 * @param sName
	 *            终点名称
	 */
	public static void openGaodeMap(Context mContext, double endLat,
									double endLng, String sName) {
		Intent intent;
		try {
			String str = "androidamap://viewMap?sourceApplication=课栈网&poiname="
					+ sName + "&lat=" + endLat + "&lon=" + endLng + "&dev=0";
			intent = Intent.getIntent(str);
			mContext.startActivity(intent);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开腾讯地图导航
	 * 
	 * @param mContext
	 * @param startLat
	 * @param startLng
	 * @param endLat
	 * @param endLng
	 * @param sName
	 */
	public static void openTencentMap(Context mContext, double startLat,
									  double startLng, double endLat, double endLng, String sName) {
		Intent intent;
		try {
			String str = "qqmap://map/routeplan?type=drive&fromcoord="
					+ startLat + "," + startLng + "&from=我的位置" + "&tocoord="
					+ endLat + "," + endLng + "&to=" + sName
					+ "&policy=1&referer=tengxun";
			intent = Intent.getIntent(str);
			mContext.startActivity(intent);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断对应app是否已安装
	 * 
	 * @param pkgName
	 * @return
	 */
	private static final boolean isIntall(String pkgName) {
		boolean isInstall = false;
		Context context = Global.mContext;
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
		}
		if (packageInfo == null || packageInfo.applicationInfo == null || packageInfo.applicationInfo.uid <= 0){
			isInstall = false;
		} else {
			isInstall = true;
		}
		return isInstall;
	}
}
