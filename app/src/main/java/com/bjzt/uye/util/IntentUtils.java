package com.bjzt.uye.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.bjzt.uye.activity.LoginActivity;
import com.bjzt.uye.activity.MainActivity;
import com.bjzt.uye.activity.RegisterActivity;

/**
 * Created by billy on 2017/10/12.
 */

public class IntentUtils {

    public static final String PARA_KEY_PUBLIC = "key_public";


    /**
     * 打开首页
     * @param mContext
     * @param clearTop
     */
    public static final void startMainActivity(Context mContext,boolean clearTop){
        Intent intent = new Intent(mContext,MainActivity.class);
        if(clearTop){
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }


    /***
     * 打开首页
     * @param mContext
     */
    public static final void startMainActivity(Context mContext){
        startMainActivity(mContext,true);
    }

    /***
     * 打开登录页卡
     * @param mContext
     * @param requestCode
     */
    public static final void startLoginActivity(Activity mContext,int type,int requestCode){
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,type);
        mContext.startActivityForResult(intent,requestCode);
    }

    /***
     * 打开注册页卡
     * @param mContext
     * @param requestCode
     */
    public static final void startRegisterActivity(Activity mContext,int requestCode){
        Intent intent = new Intent(mContext, RegisterActivity.class);
        mContext.startActivityForResult(intent,requestCode);
    }
}
