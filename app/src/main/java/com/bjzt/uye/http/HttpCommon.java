package com.bjzt.uye.http;

import com.bjzt.uye.http.rsp.Rsp400ContactEntity;
import com.bjzt.uye.http.rsp.RspFaceVerifyCfgEntity;
import com.bjzt.uye.http.rsp.RspIDentityCfgEntity;
import com.bjzt.uye.http.rsp.RspIDentityInfoEntity;
import com.bjzt.uye.http.rsp.RspIDentityPicEntity;
import com.bjzt.uye.http.rsp.RspHomeEntity;
import com.bjzt.uye.http.rsp.RspLoginPhoneEntity;
import com.bjzt.uye.http.rsp.RspLoginPwdEntity;
import com.bjzt.uye.http.rsp.RspLogoutEntity;
import com.bjzt.uye.http.rsp.RspOrderInfoEntity;
import com.bjzt.uye.http.rsp.RspOrderListEntity;
import com.bjzt.uye.http.rsp.RspOrderSubmitEntity;
import com.bjzt.uye.http.rsp.RspPhoneVerifyEntity;
import com.bjzt.uye.http.rsp.RspQACfgEntity;
import com.bjzt.uye.http.rsp.RspQASubmitEntity;
import com.bjzt.uye.http.rsp.RspRegEntity;
import com.bjzt.uye.http.rsp.RspSearchEntity;
import com.bjzt.uye.http.rsp.RspSearchWEntity;
import com.bjzt.uye.http.rsp.RspSubmitIDentityEntity;
import com.bjzt.uye.http.rsp.RspUInfoEntity;
import com.bjzt.uye.http.rsp.RspUpgradeEntity;
import com.bjzt.uye.http.rsp.RspUploadPhoneListEntity;
import com.common.common.NetCommon;
import com.common.http.HttpEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/12.
 */

public class HttpCommon {

    public static Map<String,Class> mMap = new HashMap<>();

    public static final String URL_HOME = "/app/index/index";               //首页
    public static final String URL_PHONE_VERIFY_CODE = "/safe/Getmsgcode";      //手机验证码
    public static final String URL_REG = "/login/register";                     //注册
    public static final String URL_LOGIN_PWD = "/login/login";                  //登录
    public static final String URL_LOGIN_PHONE = "/login/Loginphone";           //手机验证码登录
    public static final String URL_GET_UINFO = "/app/user/index";               //获取个人资料
    public static final String URL_UPLOAD_CONTACTLIST = "/app/mobile/submit";   //通讯录上传
    public static final String URL_FACE_VERIFY_CFG = "/udcredit/index";         //获取云慧眼配置信息
    public static final String URL_LOGOUT = "/login/logout";                    //退出登录
    public static final String URL_SEARCH_HOTWORDS = "/app/index/search";       //搜索热词
    public static final String URL_SEARCH = "/app/index/inquire";               //机构搜索
    public static final String URL_IDENTIFY_INFO = "/app/identity/info";        //身份信息
    public static final String URL_IDENTITY_SUBMIT = "/app/identity/submit";    //提交身份信息
    public static final String URL_IDENTITY_CFG = "/app/identity/config";       //获取配置信息,银行列表
    public static final String URL_IDENTITY_PIC = "/app/identity/pic";          //获取云慧眼图片
    public static final String URL_ORDERINFO = "/app/insured/config";           //获取订单配置
    public static final String URL_ORDER_SUBMIT = "/app/insured/submit";        //保单提交
    public static final String URL_400_CONTACT = "/common/get400";              //400电话
    public static final String URL_QA_CFG = "/app/question/config";             //获取问答配置
    public static final String URL_QA_SUBMIT = "/app/question/submit";          //调查问卷提交
    public static final String URL_ORDER_LIST = "/app/order/list";              //订单列表
    public static final String URL_UPGRADE = "/app/config/version";             //升级逻辑

    static{
        //请求定位城市
        mMap.put(URL_HOME,RspHomeEntity.class);
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
        //云慧眼配置
        mMap.put(URL_FACE_VERIFY_CFG, RspFaceVerifyCfgEntity.class);
        //退出登录
        mMap.put(URL_LOGOUT, RspLogoutEntity.class);
        //搜索热词
        mMap.put(URL_SEARCH_HOTWORDS, RspSearchWEntity.class);
        //搜索机构列表
        mMap.put(URL_SEARCH,RspSearchEntity.class);
        //身份信息页
        mMap.put(URL_IDENTIFY_INFO, RspIDentityInfoEntity.class);
        //身份信息页提交
        mMap.put(URL_IDENTITY_SUBMIT,RspSubmitIDentityEntity.class);
        //配置信息
        mMap.put(URL_IDENTITY_CFG, RspIDentityCfgEntity.class);
        //获取云慧眼图片信息
        mMap.put(URL_IDENTITY_PIC, RspIDentityPicEntity.class);
        //获取订单信息配置
        mMap.put(URL_ORDERINFO, RspOrderInfoEntity.class);
        //保单提交
        mMap.put(URL_ORDER_SUBMIT,RspOrderSubmitEntity.class);
        //400电话
        mMap.put(URL_400_CONTACT, Rsp400ContactEntity.class);
        //问答配置
        mMap.put(URL_QA_CFG, RspQACfgEntity.class);
        //问答提交结果
        mMap.put(URL_QA_SUBMIT, RspQASubmitEntity.class);
        //订单列表
        mMap.put(URL_ORDER_LIST,RspOrderListEntity.class);
        //升级逻辑
        mMap.put(URL_UPGRADE, RspUpgradeEntity.class);
    }

    /***
     * 获取图片上传地址
     * @return
     */
    public static final String getUploadPicUrl(){
        String refer = HttpEngine.getInstance().getRefer(NetCommon.NET_INTERFACE_TYPE_UYE);
        return refer + "/common/upload";
    }
}
