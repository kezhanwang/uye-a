package com.bjzt.uye.controller;

/**
 * Created by billy on 2017/10/20.
 */

public class CacheController {
    private static CacheController instance;

    private CacheController(){

    }

    public static CacheController getInstance(){
        if(instance == null){
            instance = new CacheController();
        }
        return instance;
    }

    public void clearCache(){
        try {
            Thread.sleep(1000*2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
