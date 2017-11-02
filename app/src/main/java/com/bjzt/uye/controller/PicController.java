package com.bjzt.uye.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.common.common.NetCommon;
import com.common.http.HttpEngine;

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
    public void showPic(ImageView imgView,String url,boolean isRound){
        Context mContext = Global.getContext();
        if(!TextUtils.isEmpty(url)){
            if(!url.startsWith("http") && !url.startsWith("https")){
                url = HttpEngine.getInstance().getRefer(NetCommon.NET_INTERFACE_TYPE_UYE) + url;
            }
//            Picasso.with(mContext)
//                    .load(url)
//                    .placeholder(R.drawable.round_rect_grey_shape)
////                    .error(R.drawable.round_rect_grey_shape)
//                    .into(imgView);
            Drawable d = null;
            if(isRound){
                d = mContext.getResources().getDrawable(R.drawable.round_rect_grey_shape);
            }else{
                d = mContext.getResources().getDrawable(R.drawable.rect_grey_shape);
            }
            DrawableRequestBuilder builder = Glide.with(mContext).load(url);
            builder.centerCrop();
            if(d != null){
                builder.placeholder(d).crossFade();
            }
            builder.into(imgView);

        }
    }

    public void loadPic(String url,final IPicDownLoadListener mListener){
        Context mContext = Global.getContext();
    }

    public void showPic(ImageView imgView,String url){
        showPic(imgView,url,true);
    }

    public void showPicRect(ImageView imgView,String url){
        showPic(imgView,url,false);
    }

    public interface IPicDownLoadListener{
        public void downloadSucc(Bitmap bitmap);
        public void downLoadFailure();
    }
}
