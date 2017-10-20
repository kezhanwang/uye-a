package com.bjzt.uye.http;

import android.text.TextUtils;

import com.bjzt.uye.entity.PBankEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.TaskCommonV2;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.http.req.ReqFaceVerifyCfgEntity;
import com.bjzt.uye.http.req.ReqIDentityCfgEntity;
import com.bjzt.uye.http.req.ReqIDentityInfoEntity;
import com.bjzt.uye.http.req.ReqIDentityPicEntity;
import com.bjzt.uye.http.req.ReqHomeEntity;
import com.bjzt.uye.http.req.ReqLoginPhoneEntity;
import com.bjzt.uye.http.req.ReqLoginPwdEntity;
import com.bjzt.uye.http.req.ReqLogoutEntity;
import com.bjzt.uye.http.req.ReqOrderInfoEntity;
import com.bjzt.uye.http.req.ReqOrderSubmitEntity;
import com.bjzt.uye.http.req.ReqPhoneVerifyEntity;
import com.bjzt.uye.http.req.ReqRegEntity;
import com.bjzt.uye.http.req.ReqSearchEntity;
import com.bjzt.uye.http.req.ReqSearchHotWEntity;
import com.bjzt.uye.http.req.ReqSubmitIDentityEntity;
import com.bjzt.uye.http.req.ReqUInfoEntity;
import com.bjzt.uye.http.req.ReqUploadPhoneListEntity;
import com.common.http.HttpEngine;

import java.util.List;

/**
 * Created by billy on 2017/10/12.
 */

public class ProtocalManager {
    private final String TAG = getClass().getSimpleName();

    private static ProtocalManager instance;

    private ProtocalManager(){

    }

    public synchronized static final ProtocalManager getInstance(){
        if(instance == null){
            instance = new ProtocalManager();
        }
        return instance;
    }

    /***
     * 对应的Task任务添加到Http请求引擎中
     * @param reqEntity
     * @param callBack
     * @return
     */
    private int addTask(ReqBaseEntity reqEntity, ICallBack<Object> callBack){
        int seqNo = Global.getSeqNo();
        reqEntity.seqNo = seqNo;
        TaskCommonV2 task = new TaskCommonV2(reqEntity,callBack);
        HttpEngine.getInstance().addTask(task);
        return seqNo;
    }

    /****
     * 获取手机验证码
     * @param phone
     * @param callBack
     * @return
     */
    public int reqPhoneVerify(String phone,ICallBack<Object> callBack){
        ReqPhoneVerifyEntity reqEntity = new ReqPhoneVerifyEntity();
        reqEntity.phone = phone;
        return addTask(reqEntity,callBack);
    }

    /**
     * 注册协议
     * @param phone
     * @param code
     * @param pwd
     * @param callBack
     * @return
     */
    public int reqReg(String phone,String code,String pwd,ICallBack<Object> callBack){
        ReqRegEntity reqEntity =new ReqRegEntity();
        reqEntity.code = code;
        reqEntity.password = pwd;
        reqEntity.phone = phone;
        return addTask(reqEntity,callBack);
    }

    /***
     * 使用密码登录
     * @param phone
     * @param pwd
     * @param callBack
     * @return
     */
    public int reqLoginPwd(String phone,String pwd,ICallBack<Object> callBack){
        ReqLoginPwdEntity reqEntity = new ReqLoginPwdEntity();
        reqEntity.password = pwd;
        reqEntity.phone = phone;
        return addTask(reqEntity,callBack);
    }

    /**
     * 手机验证码方式登录
     * @param phone
     * @param code
     * @param callBack
     * @return
     */
    public int reqLoginPhone(String phone,String code,ICallBack<Object> callBack){
        ReqLoginPhoneEntity reqEntity = new ReqLoginPhoneEntity();
        reqEntity.code = code;
        reqEntity.phone = phone;
        return addTask(reqEntity,callBack);
    }

    /**
     * 获取个人资料页卡相关
     * @param callBack
     * @return
     */
    public int reqUInfoDataCheck(ICallBack<Object> callBack){
        ReqUInfoEntity reqEntity = new ReqUInfoEntity();
        return addTask(reqEntity,callBack);
    }

    /**
     * 上报通讯录
     * @param mobile
     * @param callBack
     * @return
     */
    public int reqUploadPhoneList(String mobile,ICallBack<Object> callBack){
        ReqUploadPhoneListEntity reqEntity = new ReqUploadPhoneListEntity();
        reqEntity.mobile = mobile;
        return addTask(reqEntity,callBack);
    }

    /**
     * 获取云慧眼配置信息
     * @param callBack
     * @return
     */
    public int reqFaceVerifyCfg(ICallBack<Object> callBack){
        ReqFaceVerifyCfgEntity reqEntity = new ReqFaceVerifyCfgEntity();
        return addTask(reqEntity,callBack);
    }

    /***
     * 退出登录
     * @param callBack
     * @return
     */
    public int reqLogout(ICallBack<Object> callBack){
        ReqLogoutEntity reqEntity = new ReqLogoutEntity();
        return addTask(reqEntity,callBack);
    }

    /**
     * 搜索热词
     * @param callBack
     * @return
     */
    public int reqSearchHotW(ICallBack<Object> callBack){
        ReqSearchHotWEntity reqEntity = new ReqSearchHotWEntity();
        return addTask(reqEntity,callBack);
    }

    /**
     * 搜索列表
     * @param words
     * @param page
     * @param callBack
     * @return
     */
    public int reqSearchAgencyList(String words,int page,ICallBack<Object> callBack){
        ReqSearchEntity reqEntity = new ReqSearchEntity();
        reqEntity.word = words;
        reqEntity.page = page;
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取身份信息
     * @param callBack
     * @return
     */
    public int reqIDentifyInfo(ICallBack<Object> callBack){
        ReqIDentityInfoEntity reqEntity = new ReqIDentityInfoEntity();
        return addTask(reqEntity,callBack);
    }

    /***
     * 提交身份信息
     * @param name
     * @param idCard
     * @param idCardStart
     * @param idCardEnd
     * @param addr
     * @param picFront
     * @param picBack
     * @param authMobile
     * @param code
     * @param udOrder
     * @param callBack
     * @return
     */
    public int reqIDentitySubmit(String name,String idCard,String idCardStart,String idCardEnd,
                                 String addr,String picFront,String picBack,String authMobile,
                                 String bankNo,PBankEntity bankEntity,
                                 String code,String udOrder,ICallBack<Object> callBack
    ){
        ReqSubmitIDentityEntity reqEntity = new ReqSubmitIDentityEntity();
        reqEntity.full_name = name;
        reqEntity.id_card = idCard;
        reqEntity.id_card_start = idCardStart;
        reqEntity.id_card_end = idCardEnd;
        reqEntity.id_card_address = addr;
        reqEntity.id_card_info_pic = picFront;
        reqEntity.id_card_nation_pic = picBack;
        reqEntity.auth_mobile = authMobile;
        reqEntity.bank_card_number = bankNo;
        reqEntity.open_bank_code = bankEntity.open_bank_code;
        reqEntity.code = code;
        reqEntity.open_bank = bankEntity.open_bank;
        reqEntity.udcredit_order = udOrder;
        return addTask(reqEntity,callBack);
    }

    /***
     * 身份获取配置
     * @param callBack
     * @return
     */
    public int reqIDentityCfg(ICallBack<Object> callBack){
        ReqIDentityCfgEntity reqEntity = new ReqIDentityCfgEntity();
        reqEntity.isJsonArray = true;
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取云慧眼图片信息
     * @param order
     * @param callBack
     * @return
     */
    public int reqIDentityPic(String order,ICallBack<Object> callBack){
        ReqIDentityPicEntity reqEntity = new ReqIDentityPicEntity();
        reqEntity.udcredit_order = order;
        return addTask(reqEntity,callBack);
    }

    /**
     * 获取订单配置信息
     * @param orgId
     * @param callBack
     * @return
     */
    public int reqOrderInfo(String orgId,ICallBack<Object> callBack){
        ReqOrderInfoEntity reqEntity = new ReqOrderInfoEntity();
        reqEntity.org_id = orgId;
        return addTask(reqEntity,callBack);
    }

    /***
     * 提交保单
     * @param orgId
     * @param cid
     * @param tution
     * @param clazz
     * @param class_start
     * @param class_end
     * @param course_consultant
     * @param group_pic
     * @param training_pic
     * @param insured_type
     * @param callBack
     * @return
     */
    public int reqOrderSubmit(String orgId, String cid, String tution, String clazz,
                              String class_start, String class_end, String course_consultant,
                              String group_pic, List<String> training_pic, String insured_type,
                              ICallBack<Object> callBack){
        ReqOrderSubmitEntity reqEntity = new ReqOrderSubmitEntity();
        reqEntity.org_id = orgId;
        reqEntity.c_id = cid;
        long r = 0;
        if(TextUtils.isDigitsOnly(tution)){
            r = Long.valueOf(tution);
            r = r * 100;
        }
        reqEntity.tuition = r;
        reqEntity.clazz = clazz;
        reqEntity.class_start = class_start;
        reqEntity.class_end = class_end;
        reqEntity.course_consultant = course_consultant;
        reqEntity.group_pic = group_pic;
        reqEntity.training_pic = training_pic;
        reqEntity.insured_type = insured_type;
        return addTask(reqEntity,callBack);
    }

    /***
     * 请求首页配置信息
     * @param callBack
     * @return
     */
    public int reqHomeInfo(ICallBack<Object> callBack){
        ReqHomeEntity reqEntity = new ReqHomeEntity();
        return addTask(reqEntity,callBack);
    }
}
