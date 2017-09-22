package com.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	
	public static final long FIVE_MINIT = 1000 * 60 * 5;
	public static final long ONE_MINIT = 1000 * 60;
	public static final long ONE_DAY = ONE_MINIT * 60 * 12;
	
	private static final SimpleDateFormat timeLineSimpleDate = new SimpleDateFormat("M月d日");
	private static final SimpleDateFormat timeLineSimpleTime = new SimpleDateFormat("H点m分");
	
	public static final String getTimeStr(long time){
		String str = "";
		Date date = new Date(time);
		SimpleDateFormat formate = new SimpleDateFormat("MM-dd HH:mm:ss");
		str = formate.format(date);
		return str;
	}


	public static final String getDotTimeStr(long time){
		String str = "";
		Date date = new Date(time);
		SimpleDateFormat formate = new SimpleDateFormat("yyyy.MM.dd");
		str = formate.format(date);
		return str;
	}
	
	public static final String getDailyTipsTitle(long time){
		String str = "";
		Date date = new Date(time);
		SimpleDateFormat formate = new SimpleDateFormat("M月-d日");
		str = formate.format(date);
		return str;
	}
	
	public static final String getMyRecTimeStyle(long time){
		String str = "";
		Date date = new Date(time);
		SimpleDateFormat formate = new SimpleDateFormat("yyyy年M月d日");
		str = formate.format(date);
		return str;
	}
	
	public static final String getTimeLineTime(long time){
		String str = "";
		long curTime = System.currentTimeMillis();
		long detaData = Math.abs(curTime - time);
		if(detaData < ONE_MINIT){
			str = "刚刚";
		}else if(detaData < FIVE_MINIT){
			str = "5分钟前";
		}else if(detaData < ONE_DAY){
			Date date = new Date(time);
			str = timeLineSimpleTime.format(date);
		}else{
			Date date = new Date(time);
			str = timeLineSimpleDate.format(date);
		}
		return str;
	}
}
