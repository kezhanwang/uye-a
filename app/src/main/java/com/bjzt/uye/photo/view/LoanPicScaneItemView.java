package com.bjzt.uye.photo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bjzt.uye.R;
import com.bjzt.uye.photo.activity.LoanPicScanActivity;
import com.bjzt.uye.photo.listener.LoanIPublishListener;
import com.bjzt.uye.util.BitmapUtil;
import com.bjzt.uye.util.FileUtil;
import com.common.common.MyLog;
import com.common.thread.ThreadPool;
import com.common.util.DeviceUtil;
import com.common.util.Utils;

/**
 * Created by billy on 2017/10/22.
 */

public class LoanPicScaneItemView extends RelativeLayout implements View.OnClickListener,View.OnLongClickListener {
    private final String TAG = "PicScaneItemView";

    private AnimationImageView imgView;
    private String picUrl;
    private LoadingView mLoadingView;
    private LoanIPublishListener mListener;
    private int mType;

    private final int FLAG_SET_BITMAP_LOC = 0x100;
    private final int FLAG_URL_EMPTY = 0x102;
    private final int FLAG_SUCC = 0x103;
    private final int FLAG_ERROR = 0x105;

    public LoanPicScaneItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public LoanPicScaneItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public LoanPicScaneItemView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public LoanPicScaneItemView(Context context,int mType) {
        super(context);
        this.mType = mType;
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.loan_pic_scane_item_layout,this,true);

        this.imgView = (AnimationImageView) this.findViewById(R.id.img_view);
        this.imgView.setOnClickListener(this);
        this.imgView.setOnLongClickListener(this);
        this.mLoadingView = (LoadingView) this.findViewById(R.id.loadingview);
    }

    public void setPicInfo(String picUrl){
        mLoadingView.setVisibility(View.VISIBLE);
        String str = getResources().getString(R.string.loan_pic_scanne_tips_pic_loading);
        mLoadingView.showLoading(str);
        this.picUrl = picUrl;
        if(mType == LoanPicScanActivity.TYPE_LOC || mType == LoanPicScanActivity.TYPE_ABLUM_LOC || mType == LoanPicScanActivity.TYPE_LOAN){
            ThreadPool.getInstance().removeJob(r);
            ThreadPool.getInstance().submmitJob(r);
        }else if(mType == LoanPicScanActivity.TYPE_NET){
//			ThreadPool.getInstance().removeJob(rNet);
//			ThreadPool.getInstance().submmitJob(rNet);
        }
    }


    private Runnable r = new Runnable() {
        @Override
        public void run() {
            if(!TextUtils.isEmpty(picUrl)){
                try{
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeFile(picUrl,opts);
                    bitmap = BitmapUtil.compressImageByWidth(bitmap, DeviceUtil.getDeviceWidth(),picUrl);
                    Message msg = Message.obtain();
                    msg.obj = bitmap;
                    msg.what = FLAG_SET_BITMAP_LOC;
                    uiHandler.sendMessage(msg);
                }catch(OutOfMemoryError error){
                    MyLog.error(TAG, error);
                }
            }
        }
    };

    private Handler uiHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch(what){
                case FLAG_SET_BITMAP_LOC:
                    mLoadingView.setVisibility(View.GONE);
                    Bitmap bitmap = (Bitmap) msg.obj;
                    if(bitmap != null && !bitmap.isRecycled()){
                        imgView.setImageBitmap(bitmap);
                    }
                    break;
                case FLAG_URL_EMPTY:
                    mLoadingView.setVisibility(View.GONE);
                    String str = getResources().getString(R.string.loan_pic_scanne_tips_url_error);
                    Utils.toast(str,false);
                    break;
                case FLAG_SUCC:
                    mLoadingView.setVisibility(View.GONE);
                    break;
                case FLAG_ERROR:
                    mLoadingView.setVisibility(View.GONE);
                    str = getResources().getString(R.string.loan_pic_scanne_tips_error);
                    Utils.toast(str,false);
                    break;
            }
        };
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ThreadPool.getInstance().removeJob(r);
        uiHandler.removeCallbacksAndMessages(null);
    };

    public void recyle(){

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if(mListener != null){
            if(view == this.imgView){
                mListener.onPicClickDown();
            }
        }
    }

    public void setIPublishListener(LoanIPublishListener mListener){
        this.mListener = mListener;
    }

    @Override
    public boolean onLongClick(View view) {
        // TODO Auto-generated method stub
        if(mListener != null && mType == LoanPicScanActivity.TYPE_NET){
            if(view == this.imgView){
                String localFilePath = FileUtil.getPicFilePath(picUrl);
                if(!TextUtils.isEmpty(localFilePath)){
                    mListener.onPicLongClick(localFilePath);
                }
            }
        }
        return true;
    }

}
