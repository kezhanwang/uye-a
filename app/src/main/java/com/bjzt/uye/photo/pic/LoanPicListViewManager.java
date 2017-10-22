package com.bjzt.uye.photo.pic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.util.BitmapUtil;
import com.common.common.MyLog;
import com.common.thread.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class LoanPicListViewManager {
	private final String TAG = "PicListViewManager";
	
	public static final int PIC_TYPE_DEFAULT = -1;		//默认	rect
	public static final int PIC_TYPE_ROUND_CORNER = 1;	//圆角
	public static final int PIC_TYPE_ROUND = 2;			//圆形  圆头像专用
	public static final int PIC_TYPE_RECT_BIG = 3;		//矩形大图
	public static final int PIC_TYPE_NEWSINFO_NORMAL = 4;		//资讯小图片
	
	private static LoanPicListViewManager instance = null;
	private LoanImageCache imgCache;	//图片缓存
	private LoanImageCache localCache;	//本地图片缓存
	private final int CACHE_SIZE_BITMAP = 120;
	private final int CACHE_SIZE_BITMAP_LOCAL = 80;
	private Handler uiHandler = null;
	private final int FLAG_SHOW = 0x100;
	private static Bitmap mUserBitmapDefault;
	public static final int FLAG_BITMAP = 1000;
	private Vector<String> mUrlDownloading = new Vector<String>();
	private final byte CACHE_TYPE_SMALL = 10;
	private final byte CACHE_TYPE_MIDDLE = 11;
	private final byte CACHE_TYPE_ABLUME = 12;
	private static Drawable picDefaultRound;
	private static Drawable picDefaultRect;
	private static Drawable picDefaultRectBig;
	private static Drawable picDefaultNewsNormal;
	private Drawable picDefaultUserIcon;

	private LoanPicListViewManager() {
//		mList = new Vector<PicEntityShow>();
		imgCache = new LoanImageCache(CACHE_SIZE_BITMAP);
		localCache = new LoanImageCache(CACHE_SIZE_BITMAP_LOCAL);
		initHandler();
		mUserBitmapDefault = getDefalutUserBitmap();
	}

	private Bitmap getDefalutUserBitmap(){
		Context mContext = Global.mContext;
		return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.circle_grey_bg_color);
	}
	
	public static LoanPicListViewManager getInstance(){
		if(instance == null){
			instance = new LoanPicListViewManager();
		}
		return instance;
	}
	
	private void initHandler(){
		uiHandler = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				int what = msg.what;
				if(what == FLAG_SHOW){
					Object obj = msg.obj;
					LoanPicEntityShow entity = (LoanPicEntityShow) obj;
					if(entity.bitmap != null && !entity.bitmap.isRecycled()){
						View imgView = entity.imgView;
						if(imgView instanceof ImageView){
							ImageView img = (ImageView) imgView;
							img.setBackgroundDrawable(null);
							img.setImageBitmap(entity.bitmap);
						}
						if(entity.imgView != null){
							entity.imgView.setTag(R.id.loan_img_url_id,entity.url);
						}
					}else{
						if(entity.imgView != null){
							entity.imgView.setTag(R.id.loan_img_url_id,entity.url);
						}
					}
					if(entity != null && entity.mPicDownListener != null){
						entity.mPicDownListener.downloadSucc(entity.url);
					}
					//MyLog.debug(TAG,"[handleMessage]" + " hashCode:" + entity.hashCode() + " set img view = null...:" + entity.imgView.hashCode());
					entity.imgView = null;
					entity.url = null;
					//MyLog.debug(TAG,"[handleMessage]" + " hashCode:" + entity.imgView);
				}
			}
		};
	}
	
	/***
	 * 如果在内存中就加载
	 * 否则显示默认
	 * @param imgView
	 * @param url
	 */
	public synchronized void onListScroll(Drawable dBg, ImageView imgView, String url, final int picType){
		imgView.setTag(url);
		LoanPicEntityShow entity = getPicEntityFromCache(url,picType,CACHE_TYPE_MIDDLE);
		if(entity != null && entity.bitmap != null && !entity.bitmap.isRecycled()){
			Message msg = Message.obtain();
			LoanPicEntityShow entityShow = new LoanPicEntityShow();
			entityShow.imgView = imgView;
			entityShow.bitmap = entity.bitmap;
			msg.what = FLAG_SHOW;
			msg.obj = entityShow;
			uiHandler.sendMessage(msg);
//			MyLog.debug(MyLog.Billy,"[onListScroll]" + " 内存show... bitmap:" + entity.bitmap);
		}else{
			if(picType == PIC_TYPE_ROUND){
				if(mUserBitmapDefault == null || mUserBitmapDefault.isRecycled()){
					mUserBitmapDefault = getDefalutUserBitmap();
				}
				imgView.setImageBitmap(mUserBitmapDefault);
			}else{
				imgView.setBackgroundDrawable(dBg);
				imgView.setImageBitmap(null);
			}
		}
	}

	public synchronized void onListScroll(ImageView imgView, String url){
		if(!TextUtils.isEmpty(url)){
			LoanPicEntityShow entity = getPicEntityFromCache(url,PIC_TYPE_DEFAULT,CACHE_TYPE_MIDDLE);
			if(entity != null && entity.bitmap != null){
				Message msg = Message.obtain();
				LoanPicEntityShow entityShow = new LoanPicEntityShow();
				entityShow.imgView = imgView;
				entityShow.bitmap = entity.bitmap;
				msg.what = FLAG_SHOW;
				msg.obj = entityShow;
				uiHandler.sendMessage(msg);
//				MyLog.debug(MyLog.Billy,"[onListScroll]" + " 内存show... bitmap:" + entity.bitmap);
			}else{
				imgView.setImageBitmap(null);
			}
		}
	}

	private Drawable getDrawableByType(int picType){
		Drawable d = null;
		switch(picType){
			case PIC_TYPE_ROUND_CORNER:
				if(picDefaultRound == null){
					picDefaultRound = Global.getContext().getResources().getDrawable(R.drawable.loan_bg_img_round_border);
				}
				d = picDefaultRound;
				break;
			case PIC_TYPE_RECT_BIG:
				if(picDefaultRectBig == null){
					picDefaultRectBig = Global.getContext().getResources().getDrawable(R.drawable.loan_bg_img_rect_border);
				}
				d = picDefaultRectBig;
				break;
			case PIC_TYPE_NEWSINFO_NORMAL:
				if(picDefaultNewsNormal == null){
					picDefaultNewsNormal = Global.getContext().getResources().getDrawable(R.drawable.loan_bg_img_round_border);
				}
				d = picDefaultNewsNormal;
				break;
			case PIC_TYPE_DEFAULT:
				if(picDefaultRect == null){
					picDefaultRect = Global.getContext().getResources().getDrawable(R.drawable.loan_bg_img_rect_border);
				}
				d = picDefaultRect;
				break;
			default:
				if(picDefaultRect == null){
					picDefaultRect = Global.getContext().getResources().getDrawable(R.drawable.loan_bg_img_rect_border);
				}
				d = picDefaultRect;
				break;
		}
		return d;
	}

	
	
	public void onPagePause(final String pageName){
		ThreadPool.getInstance().submmitJob(new Runnable() {
			@Override
			public void run() {
				synchronized (imgCache) {
//					List<String> mList = new ArrayList<String>();
//					for(Map.Entry<String,PicEntityShow> entry : imgCache.entrySet()){
//						PicEntityShow entity = entry.getValue();
//						if(entity != null && entity.pageName != null && entity.pageName.equals(pageName)){
//							if(entity.bitmap != null && !entity.bitmap.isRecycled()){
//								entity.bitmap.recycle();
//							}
//							mList.add(entity.url);
//							//
//							entity.imgView = null;
//							entity.bitmap = null;
//							entity.pageName = null;
//							entity.url = null;
//						}
//					}
//					for(int i = 0;i < mList.size();i++){
//						String url = mList.get(i);
//						imgCache.remove(url);
//					}
				}
			}
		});
	}
	
	
	private LoanPicEntityShow getPicEntityFromCache(String url, int picType, int cacheType){
		LoanPicEntityShow picEntityShow = null;
		switch(cacheType){
			case CACHE_TYPE_MIDDLE:
				picEntityShow = imgCache.get(url);
				break;
			case CACHE_TYPE_ABLUME:
				picEntityShow = localCache.get(url);
				break;
		}
		return picEntityShow;
	}
	
	/***
	 * 添加到内存缓存
	 * @param temp
	 */
	private synchronized void addToMemCache(String url, LoanPicEntityShow temp, int cacheType){
		switch(cacheType){
			case CACHE_TYPE_MIDDLE:
				//MyLog.debug(TAG,"[addToMemCache]" + " picEntityHashCode:" + temp.hashCode());
				imgCache.put(url,temp);
				break;
			case CACHE_TYPE_ABLUME:
				localCache.put(url,temp);
				break;
		}
	}
	
	public Bitmap getBitmapFromCache(String url){
		Bitmap bitmap = null;
		LoanPicEntityShow entity = getPicEntityFromCache(url,PIC_TYPE_DEFAULT,CACHE_TYPE_MIDDLE);
		if(entity != null){
			bitmap = entity.bitmap;
		}
		return bitmap;
	}
	

	public void reqLocalImgOnScroll(ImageView imgView, String url){
		if(!TextUtils.isEmpty(url)){
			LoanPicEntityShow entity = getPicEntityFromCache(url,PIC_TYPE_DEFAULT,CACHE_TYPE_ABLUME);
			imgView.setTag(url);
			if(entity != null){
				LoanPicEntityShow showEntity = new LoanPicEntityShow();
				showEntity.bitmap = entity.bitmap;
				showEntity.url = url;
				showEntity.imgView = imgView;
				//send msg to handler
				Message msg = Message.obtain();
				msg.what = FLAG_SHOW;
				msg.obj = showEntity;
				uiHandler.sendMessage(msg);
			}
		}
	}
	
	public void reqLocalImgOnStop(final ImageView imgView, final String url){
		if(!TextUtils.isEmpty(url)){
			LoanPicEntityShow entity = getPicEntityFromCache(url,PIC_TYPE_DEFAULT,CACHE_TYPE_ABLUME);
			imgView.setTag(url);
			if(entity != null){			
				//内存查看
				LoanPicEntityShow showEntity = new LoanPicEntityShow();
				showEntity.imgView = imgView;
				showEntity.url = url;
				showEntity.bitmap = entity.bitmap;
				//send msg to handler
				Message msg = Message.obtain();
				msg.what = FLAG_SHOW;
				msg.obj = showEntity;
				uiHandler.sendMessage(msg);
			}else{
				//进行本地decode图片
				ThreadPool.getInstance().submmitJob(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						//decode图片
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inPreferredConfig = Bitmap.Config.RGB_565;
						options.inPurgeable = true;
						options.inInputShareable = true;
						options.inSampleSize = 3;
						Bitmap bitmap = null;
						try {
							bitmap = BitmapFactory.decodeFile(url, options);
							bitmap = BitmapUtil.compressImageByWidth(bitmap, 300, 300, null);
						} catch (OutOfMemoryError ee) {
							MyLog.error(TAG, ee);
							localCache.clear();
							int cacheSize = CACHE_SIZE_BITMAP_LOCAL  / 2;
							localCache = new LoanImageCache(cacheSize);
							System.gc();
						}
						if (bitmap != null && imgView.getTag() != null && imgView.getTag().equals(url)) {
							//show
							LoanPicEntityShow entity = new LoanPicEntityShow();
							entity.bitmap = bitmap;
							entity.imgView = imgView;
							entity.url = url;
							entity.pageName = "";
							//添加到缓存中
							addToMemCache(url, entity, CACHE_TYPE_ABLUME);
							//show bitmap
							Message msg = Message.obtain();
							msg.what = FLAG_SHOW;
							msg.obj = entity;
							uiHandler.sendMessage(msg);
						}
					}
				});
			}
		}
	}

	/***
	 * 获取图片缓存数据
	 * @return
	 */
	public List<LoanPicEntityShow> getImgCache(){
		List<LoanPicEntityShow> mList = new ArrayList<LoanPicEntityShow>();
		for(Map.Entry<String, LoanPicEntityShow> entry:imgCache.entrySet()){
			LoanPicEntityShow val = entry.getValue();
			mList.add(val);
		}
		return mList;
	}
	
}
