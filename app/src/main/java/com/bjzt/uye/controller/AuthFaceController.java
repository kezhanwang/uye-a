package com.bjzt.uye.controller;

import com.bjzt.uye.entity.PAuthFaceResultEntity;

/**
 * 云慧眼认证相关数据保存
 * Created by billy on 2017/10/18
 */
public class AuthFaceController {

    private static AuthFaceController instance;

    private PAuthFaceResultEntity mEntity;

    private AuthFaceController(){

    }

    public static final AuthFaceController getInstance(){
        if(instance == null){
            instance = new AuthFaceController();
        }
        return instance;
    }

    public void setLoanAuthFaceEntity(PAuthFaceResultEntity mEntity){
        this.mEntity = mEntity;
    }
}
