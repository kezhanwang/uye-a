package com.common.controller;

/**
 * Created by billy on 2017/9/18.
 */

public class LoginController {
    private final String TAG = getClass().getSimpleName();
    private static LoginController instance;

    private LoginController(){

    }

    public static final LoginController getInstance(){
        if(instance == null){
            instance = new LoginController();
        }
        return instance;
    }

    public String getCookie(){
        return "";
    }
}
