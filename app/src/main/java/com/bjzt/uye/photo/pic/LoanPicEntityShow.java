package com.bjzt.uye.photo.pic;

import android.graphics.Bitmap;
import android.view.View;

public class LoanPicEntityShow {
	public Bitmap bitmap;
	public View imgView;
	public String url;
	public String pageName;
	public int picType;
	public int showType;
	public LoanIPicDownLoadListener mPicDownListener;
	
	public static final int SHOW_TYPE_NORMAL = 0;
	public static final int SHOW_TYPE_SLOW = 10;
}
