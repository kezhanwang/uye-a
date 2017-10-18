package com.common.controller;

import android.text.TextUtils;

import com.bjzt.uye.entity.PLoginEntity;
import com.bjzt.uye.global.Global;
import com.common.file.SharePreLogin;
import com.common.listener.ILoginListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by billy on 2017/9/18.
 */

public class LoginController {
    private final String TAG = getClass().getSimpleName();
    private static LoginController instance;
    public PLoginEntity pEntity;
    private Set<ILoginListener> mSet;

    private LoginController(){
        mSet = new HashSet<>();
    }

    public static final LoginController getInstance(){
        if(instance == null){
            instance = new LoginController();
        }
        return instance;
    }

    public void registerListener(ILoginListener mListener){
        mSet.add(mListener);
    }

    public void unRegisterListener(ILoginListener mListener){
        mSet.remove(mListener);
    }

    /***
     * 获取cookie data
     * @return
     */
    private String getCookieData(){
        if(pEntity != null && pEntity.cookie != null && !TextUtils.isEmpty(pEntity.cookie.b42e7_uye_user)){
            return this.pEntity.cookie.b42e7_uye_user;
        }
        return null;
    }

    /***
     * 获取分期Cookie数据Data
     * @return
     */
    public String getLoanCookie(){
        String cookie = getCookieData();
        if(!TextUtils.isEmpty(cookie)){
            return "b42e7_uye_user="+cookie+";";
        }
        return cookie;
    }

    public String getCookie(){
        String cookie = getLoanCookie();
        return cookie;
    }

    /***
     * 是否登录
     * @return
     */
    public boolean isLogin(){
        String cookie = getCookie();
        if(!TextUtils.isEmpty(cookie)){
            return true;
        }
        return false;
    }

    public String getUid(){
        if(pEntity != null){
            return pEntity.uid;
        }
        return null;
    }

    public void loginSucc(final PLoginEntity pLoginEntity){
        if(pLoginEntity != null){
            this.pEntity = pLoginEntity;
            //notify register listener
            notifySucc();
            //save to file
            Global.postDelay(new Runnable() {
                @Override
                public void run() {
                    SharePreLogin mSharePre = new SharePreLogin();
                    mSharePre.saveLoginInfo(pLoginEntity);
                }
            });
        }
    }

    public void loadInfo(){
        SharePreLogin mSharePre = new SharePreLogin();
        PLoginEntity pLoginEntity = mSharePre.loadInfo();
        if(pLoginEntity != null){
            this.pEntity = pLoginEntity;
        }
    }

    private void notifySucc(){
        for(ILoginListener mListener : mSet){
            if(mListener != null){
                mListener.loginSucc();
            }
        }
    }

    public long getMillseconds(){
        return System.currentTimeMillis();
    }
}
