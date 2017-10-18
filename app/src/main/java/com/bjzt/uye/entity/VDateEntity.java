package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by diaosi on 2016/2/29.
 */
public class VDateEntity implements Serializable {
    public int year;
    public int month;
    public int date;
    public int hour;
    public int minute;

    public String getDate(){
        String str = "";
        str = year + "-" + month + "-" + date;
        return str;
    }

    public String getTime(){
        String str = "";
        String strMin = "";
        if(minute < 10){
            strMin = "0" + minute;
        }else{
            strMin = minute + "";
        }
        str = year + "." + month + "." + date + " " + hour + ":" + strMin;
        return str;
    }

    public String getReqTime(){
        String str = "";
        str = year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + "00";
        return str;
    }

    /***
     * 获取毫秒数
     * @return
     */
    public long getTimeInMillis(){
        long r = 0;
        Calendar c = Calendar.getInstance();
        c.set(year,month-1,date,0,0);
        r = c.getTimeInMillis();
        return r;
    }
}
