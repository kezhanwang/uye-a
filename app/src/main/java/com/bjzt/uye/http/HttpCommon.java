package com.bjzt.uye.http;

import com.bjzt.uye.http.rsp.RspLocCityEntity;
import com.bjzt.uye.http.rsp.RspLoginPhoneEntity;
import com.bjzt.uye.http.rsp.RspLoginPwdEntity;
import com.bjzt.uye.http.rsp.RspPhoneVerifyEntity;
import com.bjzt.uye.http.rsp.RspRegEntity;
import com.bjzt.uye.http.rsp.RspUInfoEntity;
import com.bjzt.uye.http.rsp.RspUploadPhoneListEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/12.
 */

public class HttpCommon {

    public static Map<String,Class> mMap = new HashMap<>();

    public static final String URL_LOC_CITY = "/app/index/index";
    public static final String URL_PHONE_VERIFY_CODE = "/safe/Getmsgcode";      //手机验证码
    public static final String URL_REG = "/login/register";                     //注册
    public static final String URL_LOGIN_PWD = "/login/login";                  //登录
    public static final String URL_LOGIN_PHONE = "/login/Loginphone";           //手机验证码登录
    public static final String URL_GET_UINFO = "/app/user/index";               //获取个人资料
    public static final String URL_UPLOAD_CONTACTLIST = "/app/mobile/submit";   //通讯录上传

    static{
        //请求定位城市
        mMap.put(URL_LOC_CITY,RspLocCityEntity.class);
        //手机验证码
        mMap.put(URL_PHONE_VERIFY_CODE, RspPhoneVerifyEntity.class);
        //注册
        mMap.put(URL_REG,RspRegEntity.class);
        //登录通过密码
        mMap.put(URL_LOGIN_PWD, RspLoginPwdEntity.class);
        //手机验证码登录
        mMap.put(URL_LOGIN_PHONE,RspLoginPhoneEntity.class);
        //个人资料
        mMap.put(URL_GET_UINFO,RspUInfoEntity.class);
        //通讯录上传
        mMap.put(URL_UPLOAD_CONTACTLIST,RspUploadPhoneListEntity.class);

    }
}
