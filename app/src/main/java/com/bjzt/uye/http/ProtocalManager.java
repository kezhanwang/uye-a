package com.bjzt.uye.http;

import android.text.TextUtils;

import com.bjzt.uye.entity.PBankEntity;
import com.bjzt.uye.entity.PExperiEntity;
import com.bjzt.uye.entity.PIDentityInfoEntity;
import com.bjzt.uye.entity.VApplyEmplyAddEntity;
import com.bjzt.uye.entity.VContactInfoEntity;
import com.bjzt.uye.entity.VExperiDegreeAddEntity;
import com.bjzt.uye.entity.VExperiOccEntity;
import com.bjzt.uye.entity.VExperienceBaseEntity;
import com.bjzt.uye.entity.VOrderInfoEntity;
import com.bjzt.uye.entity.VQAItemEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.bjzt.uye.http.base.TaskCommonV2;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.http.req.Req400ContactEntity;
import com.bjzt.uye.http.req.ReqContactCfgEntity;
import com.bjzt.uye.http.req.ReqContactInfoEntity;
import com.bjzt.uye.http.req.ReqContactSubmitEntity;
import com.bjzt.uye.http.req.ReqEmployProCfgEntity;
import com.bjzt.uye.http.req.ReqEmployProList;
import com.bjzt.uye.http.req.ReqEmployProSubmit;
import com.bjzt.uye.http.req.ReqExperiAddEntity;
import com.bjzt.uye.http.req.ReqExperiBaseCfgEntity;
import com.bjzt.uye.http.req.ReqExperiBaseCommitEntity;
import com.bjzt.uye.http.req.ReqExperiBaseInfoEntity;
import com.bjzt.uye.http.req.ReqExperiDelEntity;
import com.bjzt.uye.http.req.ReqExperiListEntity;
import com.bjzt.uye.http.req.ReqFaceVerifyCfgEntity;
import com.bjzt.uye.http.req.ReqIDentityCfgEntity;
import com.bjzt.uye.http.req.ReqIDentityInfoEntity;
import com.bjzt.uye.http.req.ReqIDentityPicEntity;
import com.bjzt.uye.http.req.ReqHomeEntity;
import com.bjzt.uye.http.req.ReqLocAreaEntity;
import com.bjzt.uye.http.req.ReqLocCityEntity;
import com.bjzt.uye.http.req.ReqLocProEntity;
import com.bjzt.uye.http.req.ReqLoginPhoneEntity;
import com.bjzt.uye.http.req.ReqLoginPwdEntity;
import com.bjzt.uye.http.req.ReqLogoutEntity;
import com.bjzt.uye.http.req.ReqOrderInfoEntity;
import com.bjzt.uye.http.req.ReqOrderListEntity;
import com.bjzt.uye.http.req.ReqOrderSubmitEntity;
import com.bjzt.uye.http.req.ReqOrgDetailEntity;
import com.bjzt.uye.http.req.ReqPhoneVerifyEntity;
import com.bjzt.uye.http.req.ReqQACfgEnttiy;
import com.bjzt.uye.http.req.ReqQASubmitEntity;
import com.bjzt.uye.http.req.ReqRegEntity;
import com.bjzt.uye.http.req.ReqSearchEntity;
import com.bjzt.uye.http.req.ReqSearchHotWEntity;
import com.bjzt.uye.http.req.ReqSubmitIDentityEntity;
import com.bjzt.uye.http.req.ReqUInfoEntity;
import com.bjzt.uye.http.req.ReqUpgradeEntity;
import com.bjzt.uye.http.req.ReqUploadPhoneListEntity;
import com.bjzt.uye.views.component.EmployArea;
import com.common.http.HttpEngine;

import java.util.ArrayList;
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
     * @param mEntity
     * @param udOrder
     * @param callBack
     * @return
     */
    public int reqIDentitySubmit(PIDentityInfoEntity mEntity,PBankEntity bankEntity, String udOrder,ICallBack<Object> callBack){
        ReqSubmitIDentityEntity reqEntity = new ReqSubmitIDentityEntity();
        reqEntity.full_name = mEntity.full_name;
        reqEntity.id_card = mEntity.id_card;
        reqEntity.id_card_start = mEntity.id_card_start;
        reqEntity.id_card_end = mEntity.id_card_end;
        reqEntity.id_card_address = mEntity.id_card_address;
        reqEntity.id_card_info_pic = mEntity.id_card_info_pic;
        reqEntity.id_card_nation_pic = mEntity.id_card_nation_pic;
        reqEntity.auth_mobile = mEntity.auth_mobile;
        reqEntity.bank_card_number = mEntity.bank_card_number;
        reqEntity.open_bank_code = bankEntity.open_bank_code;
        reqEntity.code = mEntity.vCode;
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
     * @param vEntity
     * @param insured_type
     * @param callBack
     * @return
     */
    public int reqOrderSubmit(String orgId, String cid, VOrderInfoEntity vEntity,int insured_type,
                              ICallBack<Object> callBack){
        ReqOrderSubmitEntity reqEntity = new ReqOrderSubmitEntity();
        reqEntity.org_id = orgId;
        reqEntity.c_id = cid;
        long r = 0;
        String tution = vEntity.tuition;
        if(TextUtils.isDigitsOnly(tution)){
            r = Long.valueOf(tution);
            r = r * 100;
        }
        reqEntity.tuition = r;
        reqEntity.clazz = vEntity.clazz;
        reqEntity.class_start = vEntity.class_start;
        reqEntity.class_end = vEntity.class_end;
        reqEntity.course_consultant = vEntity.course_consultant;
        reqEntity.group_pic = vEntity.group_pic;
        reqEntity.training_pic = vEntity.training_pic;
        reqEntity.insured_type = insured_type+"";
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

    /***
     * 请求400电话配置
     * @param callBack
     * @return
     */
    public int req400Contact(ICallBack<Object> callBack){
        Req400ContactEntity reqEntity = new Req400ContactEntity();
        return addTask(reqEntity,callBack);
    }

    /**
     * 获取问答配置
     * @param callBack
     * @return
     */
    public int reqQACfg(String orgId,ICallBack<Object> callBack){
        ReqQACfgEnttiy reqEntity = new ReqQACfgEnttiy();
        reqEntity.org_id = orgId;
        return addTask(reqEntity,callBack);
    }

    /**
     * 提交问答结果
     * @param mList
     * @param callBack
     * @return
     */
    public int reqQASubmit(String orgId,List<VQAItemEntity> mList,ICallBack<Object> callBack){
        ReqQASubmitEntity reqEntity = new ReqQASubmitEntity();
        reqEntity.parseInfo(mList);
        reqEntity.org_id = orgId;
        return addTask(reqEntity,callBack);
    }

    /***
     * 请求订单列表
     * @param callBack
     * @return
     */
    public int reqOrderList(int page,ICallBack<Object> callBack){
        ReqOrderListEntity reqEntity = new ReqOrderListEntity();
        reqEntity.page = page;
        reqEntity.pageSize = 10;
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取升级配置
     * @param callBack
     * @return
     */
    public int reqUpgradeInfo(ICallBack<Object> callBack){
        ReqUpgradeEntity reqEntity = new ReqUpgradeEntity();
        reqEntity.version_code = MConfiger.VERSION;
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取省份列表
     * @param callBack
     * @return
     */
    public int reqLocProList(ICallBack<Object> callBack){
        ReqLocProEntity reqEntity = new ReqLocProEntity();
        reqEntity.isJsonArray = true;
        return addTask(reqEntity,callBack);
    }

    /**
     * 获取城市列表
     * @param proId
     * @param callBack
     * @return
     */
    public int reqLocCityList(String proId,ICallBack<Object> callBack){
        ReqLocCityEntity reqEntity = new ReqLocCityEntity();
        reqEntity.isJsonArray = true;
        reqEntity.province = proId;
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取地区列表
     * @param cityId
     * @param callBack
     * @return
     */
    public int reqLocAreaList(String cityId,ICallBack<Object> callBack) {
        ReqLocAreaEntity reqEntity = new ReqLocAreaEntity();
        reqEntity.isJsonArray = true;
        reqEntity.city = cityId;
        return addTask(reqEntity,callBack);
    }

    /****
     * 获取机构详情
     * @param orgId
     * @param callBack
     * @return
     */
    public int reqOrgDetail(String orgId,ICallBack<Object> callBack){
        ReqOrgDetailEntity reqEntity = new ReqOrgDetailEntity();
        reqEntity.org_id = orgId;
        return addTask(reqEntity,callBack);
    }

    /**
     * 获取联系配置信息
     * @param callBack
     * @return
     */
    public int reqContactCfg(ICallBack<Object> callBack){
        ReqContactCfgEntity reqEntity = new ReqContactCfgEntity();
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取联系信息
     * @param callBack
     * @return
     */
    public int reqContactInfo(ICallBack<Object> callBack){
        ReqContactInfoEntity reqEntity = new ReqContactInfoEntity();
        return addTask(reqEntity,callBack);
    }

    /***
     * 个人信息-提交
     * @param vEntity
     * @param callBack
     * @return
     */
    public int reqContactSubmit(VContactInfoEntity vEntity, ICallBack<Object> callBack){
        ReqContactSubmitEntity reqEntity = new ReqContactSubmitEntity();
        reqEntity.home_province = vEntity.mLocPro.id;
        reqEntity.home_city = vEntity.mLocCity.id;
        reqEntity.home_area = vEntity.mLocArea.id;
        reqEntity.home_address = vEntity.strAddrDetail;
        reqEntity.marriage = vEntity.strMarriage;
        reqEntity.email = vEntity.strMail;
        reqEntity.qq = vEntity.strQQ;
        reqEntity.wechat = vEntity.strWechat;
        reqEntity.contact1_relation = vEntity.strSecRela;
        reqEntity.contact1_name = vEntity.strSecName;
        reqEntity.contact1_phone = vEntity.strSecPhone;
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取个人经历配置信息
     * @param callBack
     * @return
     */
    public int reqMyExperiBaseCfg(ICallBack<Object> callBack){
        ReqExperiBaseCfgEntity reqEntity = new ReqExperiBaseCfgEntity();
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取个人经历信息
     * @param callBack
     * @return
     */
    public int reqMyExperiBaseInfo(ICallBack<Object> callBack){
        ReqExperiBaseInfoEntity reqEntity = new ReqExperiBaseInfoEntity();
        return addTask(reqEntity,callBack);
    }

    /***
     * 个人基本信息提交
     * @param vEntity
     * @param callBack
     * @return
     */
    public int reqMyExperBaseCommit(VExperienceBaseEntity vEntity,ICallBack<Object> callBack){
        ReqExperiBaseCommitEntity reqEntity = new ReqExperiBaseCommitEntity();
        reqEntity.highest_education = vEntity.strDegree;
        reqEntity.profession = vEntity.strOcc;
        reqEntity.housing_situation = vEntity.strHouse;
        reqEntity.monthly_income = vEntity.strIncome;
        List<EmployArea.BLocEntity> mList = vEntity.mLocList;
        List<String> rList = new ArrayList<>();
        if(mList != null && mList.size() > 0){
            for(int i = 0;i < mList.size();i++){
                EmployArea.BLocEntity bEntity = mList.get(i);
                if(!bEntity.isFake){
                    rList.add(bEntity.mLocArea.name);
                }
            }
        }
        reqEntity.will_work_city = rList;
        return addTask(reqEntity,callBack);
    }

    /***
     * 个人经历-列表相关
     * @param callBack
     * @return
     */
    public int reqMyExperiListOcc(ICallBack<Object> callBack){
        ReqExperiListEntity reqEntity = new ReqExperiListEntity();
        reqEntity.type = ReqExperiListEntity.TYPE_OCC;
//        reqEntity.isJsonArray = true;
        return addTask(reqEntity,callBack);
    }

    /**
     * 个人经历-学历列表
     * @param callBack
     * @return
     */
    public int reqMyExperiListDegree(ICallBack<Object> callBack){
        ReqExperiListEntity reqEntity = new ReqExperiListEntity();
        reqEntity.type = ReqExperiListEntity.TYPE_DEGREE;
//        reqEntity.isJsonArray = true;
        return addTask(reqEntity,callBack);
    }

    /***
     * 添加我的职业经历
     * @param vEntity
     * @param callBack
     * @return
     */
    public int reqMyExperiOccAdd(VExperiOccEntity vEntity, ICallBack<Object> callBack, PExperiEntity pEntity){
        ReqExperiAddEntity reqEntity = new ReqExperiAddEntity();
        reqEntity.type = ReqExperiListEntity.TYPE_OCC;
        reqEntity.date_start = vEntity.strDateStart;
        reqEntity.date_end = vEntity.strDateEnd;
        reqEntity.work_name = vEntity.strCpName;
        reqEntity.work_position = vEntity.strPos;
        reqEntity.work_salary = vEntity.strIncome;
        if(pEntity != null){
            reqEntity.id = pEntity.id;
        }
        return addTask(reqEntity,callBack);
    }

    /**
     * 添加我的-学历
     * @param vEntity
     * @param callBack
     * @return
     */
    public int reqMyExperiDegreeAdd(VExperiDegreeAddEntity vEntity,ICallBack<Object> callBack,PExperiEntity pEntity){
        ReqExperiAddEntity reqEntity = new ReqExperiAddEntity();
        reqEntity.type = ReqExperiListEntity.TYPE_DEGREE;
        reqEntity.date_start = vEntity.strDateStart;
        reqEntity.date_end = vEntity.strDateEnd;
        reqEntity.school_name = vEntity.strSchName;
        reqEntity.school_address = vEntity.strSchAddr;
        reqEntity.school_profession = vEntity.strMajor;
        reqEntity.education = vEntity.strDegree;
        if(pEntity != null){
            reqEntity.id = pEntity.id;
        }
        return addTask(reqEntity,callBack);
    }

    /***
     * 删除个人经历
     * @param id
     * @param callBack
     * @return
     */
    public int reqMyExperiDel(int id,ICallBack<Object> callBack){
        ReqExperiDelEntity reqEntity = new ReqExperiDelEntity();
        reqEntity.id = id;
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取就业进展配置信息
     * @param insureId
     * @param callBack
     * @return
     */
    public int reqEmployProCfg(String insureId,ICallBack<Object> callBack){
        ReqEmployProCfgEntity reqEntity = new ReqEmployProCfgEntity();
        reqEntity.insured_id = insureId;
        return addTask(reqEntity,callBack);
    }

    /***
     * 获取就业进展列表
     * @param insureId
     * @param callBack
     * @return
     */
    public int reqEmployProList(String insureId,int page,ICallBack<Object> callBack){
        ReqEmployProList reqEntity = new ReqEmployProList();
        reqEntity.insured_id = insureId;
        reqEntity.page = page;
        return addTask(reqEntity,callBack);
    }

    /***
     * 提交就业进展信息
     * @param callBack
     * @return
     */
    public int reqEmployProSubmit(String inSureId,VApplyEmplyAddEntity vEntity, ICallBack<Object> callBack){
        ReqEmployProSubmit reqEntity = new ReqEmployProSubmit();
        reqEntity.insured_id = inSureId;
        reqEntity.date = vEntity.strTime;
        reqEntity.work_province = vEntity.mLocEntityPro.id;
        reqEntity.work_city = vEntity.mLocEntityCity.id;
        reqEntity.work_area = vEntity.mLocEntityArea.id;
        reqEntity.work_address = vEntity.strAddr;
        reqEntity.work_name = vEntity.cpName;
        reqEntity.position = vEntity.cpPos;
        reqEntity.monthly_income = vEntity.salary;
        reqEntity.is_hiring = MConfiger.isHireSucc(vEntity.employStatus);
        reqEntity.pic_json = (ArrayList<String>) vEntity.mPicList;
        return addTask(reqEntity,callBack);
    }
}
