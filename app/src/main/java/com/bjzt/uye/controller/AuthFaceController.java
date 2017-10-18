package com.bjzt.uye.controller;

import com.bjzt.uye.entity.PAuthFaceResultEntity;

/**
 * Created by billy on 2017/10/18.
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
