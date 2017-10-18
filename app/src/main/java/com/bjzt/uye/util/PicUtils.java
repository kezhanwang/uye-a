package com.bjzt.uye.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.bjzt.uye.entity.PicResultEntity;
import com.common.common.MyLog;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/18.
 */

public class PicUtils {
    private static final String TAG = "PicUtils";

    /***
     * 获取图片返回照
     * @param mContext
     * @param data
     * @param mCammerImgPath
     * @return
     */
    public static final PicResultEntity onPicCammerResult(Activity mContext, Intent data, String mCammerImgPath){
        PicResultEntity entity = new PicResultEntity();
        String path = "";
        boolean isEmpty = false;
        boolean isCamera = false;
        int size = 0;
        ArrayList<String> mPaths = null;
        if(data != null){	//由Gallery返回的相册
            boolean isGallery = ImageUtil.isFromGallery(mContext,data);
            if(isGallery){
                mPaths = ImageUtil.getGalleryImgPath(mContext,data);
                if(mPaths.size() > 0){
                    path = mPaths.get(0);
                    size = mPaths.size();
                }else{
                    isEmpty = true;
                }
            }else{
                isCamera = true;
                path = PicUtils.getCammerResultImgPath(mContext,data,mCammerImgPath);
            }
        }else{
            isCamera = true;
            path = PicUtils.getCammerResultImgPath(mContext,data,mCammerImgPath);
        }
        entity.path = path;
        entity.mPaths = mPaths;
        entity.size = size;
        entity.isCammera = isCamera;
        entity.isEmpty = isEmpty;
        return entity;
    }


    public static final String getCammerResultImgPath(final Context mContext, final Intent intent, String imgPath){
        String path = null;
        if(!TextUtils.isEmpty(imgPath)){
            File file = new File(imgPath);
            if(file.exists()){
                path = imgPath;
            }
        }
        if(MyLog.isDebugable()){
            MyLog.debug(TAG,"[getCammerResultImgPath]" + " imgPath:" + imgPath);
        }
        if(TextUtils.isEmpty(path)){
            path = ImageUtil.getCammerLatestPath();
        }
        return path;
    }

    
}
