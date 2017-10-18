package com.bjzt.uye.entity;

import android.text.TextUtils;
import java.io.Serializable;

/**
 * Created by diaosi on 2017/1/20.
 */
public class PAuthFaceResultEntity implements Serializable {
    private final String TAG = getClass().getSimpleName();

    public String flag_sex;         //性别 男 女
    public String transcode;        //
    public String ret_msg;          //返回消息体
    public String id_name;          //姓名
    public String be_idcard;        //识别程度
    public String id_no;            //身份证件号码
    public String date_birthday;    //生日
    public String addr_card;        //地址
    public String branch_issued;    //签发机构
    public String state_id;         //国家
    public String info_order;       //
    public String ret_code;         //000000 标识成功
    public String result_auth;      //T、F、 C  成功失败，失败，正在人工审核
    public String start_card;       //身份证件开始时间-身份证件结束时间
    public String str_start;
    public String str_period;
    public String idcard_pic;
    public String idcard_pic_back;
    public boolean isSucc;          //是否成功

    @Override
    public String toString() {
        String str = "flagsex:" + flag_sex + " result_auth:" + result_auth;
        return str;
    }

    /***
     * 人脸识别度
     * @return
     */
    public boolean isAuthResultSucc(){
        boolean isSucc = false;
        if(!TextUtils.isEmpty(result_auth)){
            if(result_auth.equals("T")){
                isSucc = true;
            }else{
                isSucc = false;
            }
        }
        return isSucc;
    }

    public boolean isSucc(){
        boolean isSucc = false;
        if(!TextUtils.isEmpty(ret_code) && ret_code.equals("000000")){
            isSucc = true;
        }
        return isSucc;
    }

    public String getIDCardStartTime(){
        String str = "";
        if(!TextUtils.isEmpty(start_card)){
            String[] strArray = start_card.split("-");
            if(strArray != null && strArray.length > 0){
                str = strArray[0];
                str = str.replaceAll("\\.","-");
            }
        }
        return str;
    }

    public String getIDCardEndTime(){
        String str = "";
        if(!TextUtils.isEmpty(start_card)){
            String[] strArray = start_card.split("-");
            if(strArray != null && strArray.length > 1){
                str = strArray[1];
                str = str.replaceAll("\\.","-");
            }
        }
        return str;
    }

    public String getDateBirthday(){
        String str = "";
        if(!TextUtils.isEmpty(date_birthday)){
            str = date_birthday.replaceAll("\\.","-");
        }
        return str;
    }

//    public int getAge(){
//        int age = -1;
//        long curMilli = LoginController.getInstance().getMillseconds();
//        String strBirthday = this.date_birthday;
//        if(!TextUtils.isEmpty(strBirthday)){
//            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
//            try {
//                Date date = format.parse(strBirthday);
//                long birthMilli = date.getTime();
//                long result = Math.abs(birthMilli - curMilli);
//                age = (int) (result / DateUtil.YEAR);
//            } catch (ParseException ee) {
//                LoanLog.error(TAG,ee);
//            }
//        }
//        return age;
//    }
}
