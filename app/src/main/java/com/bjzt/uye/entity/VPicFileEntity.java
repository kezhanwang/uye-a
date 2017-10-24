package com.bjzt.uye.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/24.
 */

public class VPicFileEntity implements Serializable{
    //文件路径
    public String filePath;
    //缩略图
    transient public Bitmap mBitmap;
    public byte[] mBitmapByte;
    //是否是添加按钮
    public boolean isAddPic;
    //网络图片URL
    public String url;
}
