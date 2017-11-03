package com.bjzt.uye.entity;

import android.content.Context;
import android.text.TextUtils;

import com.bjzt.uye.activity.ApplyIDActivity;
import com.bjzt.uye.activity.dialog.DialogPicSelect;
import com.bjzt.uye.global.Global;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diaosi on 2016/9/26.
 */
public class PicEntity implements Serializable {
    public static final int TYPE_ID1 = 1;
    public static final int TYPE_ID2 = 2;
    public static final int TYPE_HOLDID = 3;    //手持身份证
    public static final int TYPE_BANKCARD = 4;  //银行卡照
    public static final int TYPE_CJ = 5;        //场景照
    public static final int TYPE_DEGREE = 6;    //最高学历照
    public static final int TYPE_STATEMENT = 7; //声明
    public static final int TYPE_PRO_FIRST = 8; //协议照
    public static final int TYPE_PRO_SECOND = 9; //协议签字照
    public static final int TYPE_STUIDCARD = 10;  //学生证，学籍照
    public static final int TYPE_EMPLOY_TRACKS = 11;//就业足迹
    public static final int TYPE_USER_ICON = 12;    //用户头像

    public static final String KEY_IDCARD = "idcard_pic";
    public static final String KEY_IDCARD_BACK = "idcard_pic_back";
    public static final String KEY_BANKCARD = "bank_account_pic";   //银行卡
    public static final String KEY_PERSONHOLD = "idcard_person_pic";    //手持身份证照
    public static final String KEY_CJ = "school_pic";               //场景照
    public static final String KEY_DEGREE = "degree_pic";           //学历照
    public static final String KEY_STATEMENT = "statement_pic";     //声明
    public static final String KEY_PRO_FIRST = "training_contract_first";    //协议照正面
    public static final String KEY_PRO_SECOND = "training_contract_end";     //协议签字照
    public static final String KEY_STUIDCARD = "stu_id_card";
    public static final String KEY_EMPLOY_TRACKS = "employee_tracks";
    public static final String KEY_USER_ICON = "head_portrait";

    public int mType;
    public String path;
    public String mNetPath;
    public String key = "mkey";
    public String tips;
    public int src;
    public boolean isAuthFace = false;

    public static final String getFileName(String path){
        String fileName = "";
        if(!TextUtils.isEmpty(path)){
            int index = path.lastIndexOf("/");
            if(index+1 > 0){
                fileName = path.substring(index+1);
            }
        }
        return fileName;
    }

    public static final List<Integer> buildKZCardTypeAllList(){
        List<Integer> mList = new ArrayList<Integer>();
        mList.add(TYPE_ID1);
        mList.add(TYPE_ID2);
        mList.add(TYPE_HOLDID);
        mList.add(TYPE_BANKCARD);
//        mList.add(TYPE_CJ);
//        mList.add(TYPE_DEGREE);
        return mList;
    }

    
    public static final List<Integer> buildNormalTypeAllList(boolean needTrainPro){
        List<Integer> mList = new ArrayList<Integer>();
        mList.add(TYPE_ID1);
        mList.add(TYPE_ID2);
        mList.add(TYPE_HOLDID);
        mList.add(TYPE_BANKCARD);
        mList.add(TYPE_CJ);
        if(needTrainPro){
            mList.add(TYPE_PRO_FIRST);
            mList.add(TYPE_PRO_SECOND);
        }
        return mList;
    }

    public static final PicEntity buildPicEntityByApplyType(String path, int requestCode){
        PicEntity entity = null;
        switch(requestCode){
            case ApplyIDActivity.REQ_CODE_ID:
                entity = new PicEntity();
                entity.mType = PicEntity.TYPE_ID1;
                entity.key = PicEntity.KEY_IDCARD;
                break;
            case ApplyIDActivity.REQ_CODE_ID_BACK:
                entity = new PicEntity();
                entity.mType = PicEntity.TYPE_ID2;
                entity.key = PicEntity.KEY_IDCARD_BACK;
                break;
            case DialogPicSelect.TYPE_HOLD:
                entity = new PicEntity();
                entity.mType = PicEntity.TYPE_HOLDID;
                entity.key = PicEntity.KEY_PERSONHOLD;
                break;
            case DialogPicSelect.TYPE_PROTOCAL:
                entity = new PicEntity();
                entity.mType = PicEntity.TYPE_PRO_FIRST;
                entity.key = PicEntity.KEY_PRO_FIRST;
                break;
            case DialogPicSelect.TYPE_EMPLOY_TRACKS:
                entity = new PicEntity();
                entity.mType = PicEntity.TYPE_EMPLOY_TRACKS;
                entity.key = PicEntity.KEY_EMPLOY_TRACKS;
                break;
            case DialogPicSelect.TYPE_USER_ICON:
                entity = new PicEntity();
                entity.mType = PicEntity.TYPE_USER_ICON;
                entity.key = PicEntity.KEY_USER_ICON;
                break;
        }
        if(entity != null){
            entity.path = path;
        }
        return entity;
    }

    /***
     * use by LoanForthActivity
     * @param path
     * @param requestCode
     * @return
     */
    public static final PicEntity buildPicEntityByReqCode(String path, int requestCode){
        PicEntity mEntity = null;
        switch(requestCode){
            case ApplyIDActivity.REQ_CODE_ID:
                mEntity = new PicEntity();
                mEntity.mType = PicEntity.TYPE_ID1;
                break;
            case ApplyIDActivity.REQ_CODE_ID_BACK:
                mEntity = new PicEntity();
                mEntity.mType = PicEntity.TYPE_ID2;
                break;
        }
        if(mEntity != null){
            mEntity.path = path;
        }
        return mEntity;
    }

}
