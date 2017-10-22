package com.bjzt.uye.photo.pic;

import android.graphics.Bitmap;

/****
 * 图片下载通知观察者
 * @date 2015/07/14
 */
public abstract class LoanIPicDownLoadListener {
	/***
	 * 下载成功
	 */
	public void downloadSucc(String picUrl){};

	public void downloadSucc(Bitmap bitmap){};
	/***
	 * 图片下载失败
	 */
	public void downloadFail(String picUrl){};
}
