package com.bjzt.uye.global;

/**
 * Created by billy on 2017/9/18.
 */

public class MConfiger {
    public static final int VERSION = 101;       //业务版本号码

    public static final String TEST_ORG_ID = "10049";
    public static final String JPUSH_ID = "0000";
    public static String CHANNEL_ID = "6000";

    public static final boolean isDebug = true; //是否开启debug开关
    public static final boolean isJsonDebug = true; //json解析debug开关是否打开

    public static final int AUTO_LOAD_ITEM_CNT = 16;
    public static final int MAX_SIZE_PIC_ABLUEM_SIZE = 5;
    public static final int TIME_OUT = 1000 * 20;	//20秒超时
    public static final int SPLASH_INTERVAL = 1000 * 3; //闪屏3秒钟
    public static final String STR_NO_EMPLOYMENT = "未就业";
}
