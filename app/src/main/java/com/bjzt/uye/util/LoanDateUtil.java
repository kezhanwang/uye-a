package com.bjzt.uye.util;

import android.text.TextUtils;
import com.bjzt.uye.entity.VDateEntity;
import com.common.common.MyLog;
import com.common.controller.LoginController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoanDateUtil {
	private static final String TAG = "LoanDateUtil";
	public static final long SECOND = 1000;
	public static final long MINITE = SECOND * 60;
	public static final long HOUR = MINITE * 60;
	public static final long DAY = HOUR * 24;
	public static final long YEAR = DAY * 365;

	public static String DateToStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		return str;
	}

	public static String getCurrentTime(long date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String str = format.format(new Date(date));
		return str;
	}

	public static Date StrToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// 判断是否为闰年
	public static boolean isLeapYear(int year) {
		if (year % 100 == 0 && year % 400 == 0) {
			return true;
		} else if (year % 100 != 0 && year % 4 == 0) {
			return true;
		}
		return false;
	}
	//得到某月有多少天数
	public static int getDaysOfMonth(boolean isLeapyear, int month) {
		int daysOfMonth=0;
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				daysOfMonth = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				daysOfMonth = 30;
				break;
			case 2:
				if (isLeapyear) {
					daysOfMonth = 29;
				} else {
					daysOfMonth = 28;
				}

		}
		return daysOfMonth;
	}

	/***
	 * 获取身份证件开始时间
	 * @return
     */
	public static final String getCurrentDateByIDStartDate(){
		long cur = LoginController.getInstance().getMillseconds();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(new Date(cur));
		return str;
	}

	/***
	 * 获取身份证件结束时间
	 * @return
     */
	public static final String getCurrentDateByIDEndDate(){
		long cur = LoginController.getInstance().getMillseconds();
		//月-日
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		String str = format.format(new Date(cur));
		//cal year val
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(cur);
		int year = cal.get(Calendar.YEAR);
		year += 10;
		String strResult = year + "-" + str;
		return strResult;
	}

	public static final VDateEntity parseVDateEntitiy(String str){
		VDateEntity mEntity = null;
		if(!TextUtils.isEmpty(str)){
			String[] strArray = str.split("-");
			if(strArray != null){
				mEntity = new VDateEntity();
				for(int i = 0;i < strArray.length;i++){
					String strItem = strArray[i];
					try{
						int val = Integer.valueOf(strItem);
						switch(i){
							case 0:
								mEntity.year = val;
								break;
							case 1:
								mEntity.month = val;
								break;
							case 2:
								mEntity.date = val;
								break;
						}
					}catch(Exception ee){
						MyLog.error(TAG,"[parseVDateEntitiy]" + ee.getMessage());
					}
				}
			}
		}
		return mEntity;
	}
}