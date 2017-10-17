package com.bjzt.uye.controller;

import android.content.Context;
import android.widget.ImageView;

import com.bjzt.uye.global.Global;
import com.squareup.picasso.Picasso;

/**
 * Created by billy on 2017/10/17.
 */

public class PicController {
    private final String TAG = getClass().getSimpleName();

    private static PicController instance;

    private PicController(){

    }

    public static final PicController getInstance(){
        if(instance == null){
            instance = new PicController();
        }
        return instance;
    }

    /***
     * 展示图片
     * @param imgView
     * @param url
     */
    public void showPic(ImageView imgView,String url){
        Context mContext = Global.getContext();
        Picasso.with(mContext)
                .load(url)
                .placeholder(0)
                .error(0)
                .into(imgView);
    }
}
