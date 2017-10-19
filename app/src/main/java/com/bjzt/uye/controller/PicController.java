package com.bjzt.uye.controller;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.common.common.NetCommon;
import com.common.http.HttpEngine;
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
        if(!TextUtils.isEmpty(url)){
            if(!url.startsWith("http") && !url.startsWith("https")){
                url = HttpEngine.getInstance().getRefer(NetCommon.NET_INTERFACE_TYPE_UYE) + url;
            }
            Picasso.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.round_rect_grey_shape)
//                    .error(R.drawable.round_rect_grey_shape)
                    .into(imgView);
        }

    }
}
