package com.bjzt.uye.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.bjzt.uye.entity.BComResultEntity;
import com.bjzt.uye.entity.PUploadEntity;
import com.bjzt.uye.entity.PicEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.listener.IUploadListener;
import com.bjzt.uye.util.BitmapUtil;
import com.bjzt.uye.util.FileUtil;
import com.bjzt.uye.util.UploadFileUtil;
import com.common.common.MyLog;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

/**
 * 图片上传控制器
 * Created by diaosi on 2016/9/24.
 */
public class UploadPicController extends Thread {
    private final String TAG = "LoanUploadPicController";
    private static UploadPicController instance;
    private Vector<PicEntity> mVector;
    private Vector<PicEntity> mVSucc;
    private IUploadListener mListener;
    private final int FLAG_TIME_OUT = 0x10;

    private final int SIZE = 6; //文件大小为6MB
    private final int TIME_OUT = 1000*120;   //图片超时时间

    private UploadPicController(){
        mVector = new Vector<PicEntity>();
        mVSucc = new Vector<PicEntity>();
        setPriority(Thread.MIN_PRIORITY);
        start();
    }

    public static final UploadPicController getInstance(){
        if(instance == null){
            instance = new UploadPicController();
        }
        return instance;
    }

    public void buildIDListInfoAuthFace(String idCard, String idBack){
        //网络图片身份证件
        PicEntity vEntity = new PicEntity();
        vEntity.key = PicEntity.KEY_IDCARD;
        vEntity.mType = PicEntity.TYPE_ID1;
        vEntity.mNetPath = idCard;
        vEntity.isAuthFace = true;
        mVSucc.add(vEntity);

        vEntity = new PicEntity();
        vEntity.key = PicEntity.KEY_IDCARD_BACK;
        vEntity.mType = PicEntity.TYPE_ID2;
        vEntity.mNetPath = idBack;
        vEntity.isAuthFace = true;
        mVSucc.add(vEntity);
    }

    public void initSuccList(Vector<PicEntity> mList){
        mVSucc.clear();
        mVector.clear();
        mVSucc = mList;
    }

    public void appendItem2SuccList(PicEntity mPicEntity){
        if(mVSucc == null){
            mVSucc = new Vector<>();
        }
        mVSucc.add(mPicEntity);
    }

    public void setUploadListener(IUploadListener mListener){
        this.mListener = mListener;
    }
    
    /***
     * 添加图片到上传队列
     * @param mEntity
     */
    public void addUploadPicQueue(PicEntity mEntity){
        synchronized (mVector){
            mVector.add(mEntity);
            mVector.notifyAll();
        }
    }

    @Override
    public void run() {
        while(true){
            synchronized (mVector) {
                while(mVector.size() <= 0){
                    try {
                        mVector.wait();
                    } catch (InterruptedException ee) {
                        MyLog.error(TAG,ee);
                    }
                }
                if(mVector.size() > 0){
                    PicEntity vEntity  = mVector.remove(0);
                    String filePath = vEntity.path;
                    int mType = vEntity.mType;
                    //uploading pic
                    if(!TextUtils.isEmpty(filePath)){
                        File f = new File(filePath);
                        if(f.exists()){
                            long len = f.length();
                            BComResultEntity cEntity = null;
                            //test code
                            if(len >= FileUtil.MB * SIZE){
                                cEntity = comBitmap(filePath,mType);
                                if(MyLog.isDebugable()){
                                    MyLog.debug(TAG,"[run]" + " compress:" + cEntity.path + " size:" + len);
                                }
                            }
                            UploadFileUtil util = new UploadFileUtil();
                            String fileName = PicEntity.getFileName(filePath);
                            PUploadEntity rEntity = null;
                            //time out 启动
                            Message msg = Message.obtain();
                            msg.what = FLAG_TIME_OUT;
                            msg.obj = vEntity;
                            bizHandler.sendMessageDelayed(msg,TIME_OUT);
                            if(cEntity != null && !TextUtils.isEmpty(cEntity.path)){
                                File file = new File(cEntity.path);
                                if(file.exists()){
                                    rEntity = util.upload(file,vEntity.key,fileName,vEntity.mType,mUploadListener);
                                }else{
                                    rEntity = util.upload(f,vEntity.key,fileName,vEntity.mType,mUploadListener);
                                }
                            }else{
                                rEntity = util.upload(f,vEntity.key,fileName,vEntity.mType,mUploadListener);
                            }
                            //time out remove
                            bizHandler.removeMessages(FLAG_TIME_OUT);
                            //删除压缩文件
                            if(cEntity != null && !TextUtils.isEmpty(cEntity.path)){
                                File file = new File(cEntity.path);
                                if(file.exists()){
                                    len = file.length();
                                    file.delete();
                                    if(MyLog.isDebugable()){
                                        MyLog.debug(TAG,"[run]" + " file del:" + cEntity.path + " len:" + len);
                                    }
                                }
                            }
                            if(rEntity.isSucc){
                                vEntity.mNetPath = rEntity.url;
                                mVSucc.add(vEntity);
                                if(mListener != null){
                                    mListener.uploadItemSucc(vEntity.mType,vEntity.mNetPath);
                                }
                            }else{
                                if(mListener != null){
                                    mListener.uploadError(vEntity.mType,vEntity.mNetPath,rEntity.msg);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Handler bizHandler = new Handler(Global.getLooper()){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch(what){
                case FLAG_TIME_OUT:
                    PicEntity vEntity = (PicEntity) msg.obj;
                    String tips = "网络超时~";
                    if(vEntity != null){
                        if(mListener != null){
                            mListener.uploadError(vEntity.mType,vEntity.mNetPath,tips);
                        }
                    }
                    break;
            }
        }
    };

    private BComResultEntity comBitmap(String path, int codeSrc){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 2;
        BComResultEntity bEntity = new BComResultEntity();
        String filePath = null;
        try {
            Bitmap bitmap = null;
            try{
                bitmap = BitmapFactory.decodeFile(path, options);
            }catch(OutOfMemoryError ee){
                MyLog.error(TAG,ee);
                bEntity.code = codeSrc + 1;
            }
            if(bitmap != null){
                int width = 720;
                int height = (bitmap.getHeight() * width) / bitmap.getWidth();
                Bitmap destBitmap = null;
                destBitmap = BitmapUtil.compressImageByQuailty(bitmap, width, height, path, false);
                if(destBitmap == null){
                    bEntity.code = codeSrc + 4;
                }
                filePath = FileUtil.saveBitmapToCammera(destBitmap, null);
                if(TextUtils.isEmpty(filePath)){
                    bEntity.code = codeSrc + 5;
                }
                if(bitmap != null && !bitmap.isRecycled()){
                    bitmap.recycle();
                    bitmap = null;
                }
                if(destBitmap != null && !destBitmap.isRecycled()){
                    destBitmap.recycle();
                    destBitmap = null;
                }
            }else{
                bEntity.code = codeSrc + 2;
            }
        }catch (OutOfMemoryError error){
            MyLog.error(TAG,error);
            bEntity.code = codeSrc + 3;
            System.gc();
        }
        bEntity.path = filePath;
        return bEntity;
    }


    private IUploadListener mUploadListener = new IUploadListener() {
        @Override
        public void upload(long curLen, long maxLen, String key, int mType) {
            if(mListener != null){
                mListener.upload(curLen,maxLen,key,mType);
            }
        }
    };

    /***
     * 获取网络地址
     * @param mType
     * @return
     */
    public String getNetPathByType(int mType){
        String url = "";
        for(int i = mVSucc.size() - 1;i >= 0;i--){
            PicEntity mEntity = mVSucc.get(i);
            if(mEntity != null){
                if(mEntity.mType == mType){
                    url = mEntity.mNetPath;
                    break;
                }
            }
        }
        return url;
    }

    public ArrayList<PicEntity> getSuccList(){
        ArrayList<PicEntity> mList = new ArrayList<PicEntity>();
        if(mVSucc != null){
            for(int i = 0;i < mVSucc.size();i++){
                mList.add(mVSucc.get(i));
            }
        }
        return mList;
    }

    public ArrayList<PicEntity> getIngList(){
        ArrayList<PicEntity> mList = new ArrayList<PicEntity>();
        if(mVector != null){
            for(int i = 0;i < mVector.size();i++){
                mList.add(mVector.get(i));
            }
        }
        return mList;
    }

    public void reStoreSuccList(ArrayList<PicEntity> mSuccList){
        /***
         * 添加上传成功数据
         */
        if(mSuccList != null){
            for(int i = 0;i < mSuccList.size();i++){
                mVSucc.add(mSuccList.get(i));
            }
        }
    }

    public void reStoreList(ArrayList<PicEntity> mList){
        /***
         * 添加正在上传中数据
         */
        if(mList != null){
            for(int i = 0;i < mList.size();i++){
                addUploadPicQueue(mList.get(i));
            }
        }
    }

    public void clearSuccList(){
        mVSucc.clear();
    }

    public PicEntity getItemBySuccList(int mType){
        PicEntity mEntity = null;
        if(mVSucc != null && mVSucc.size() > 0){
            for(int i = mVSucc.size() - 1;i >= 0;i--){
                PicEntity entity = mVSucc.get(i);
                if(entity != null && entity.mType == mType){
                    mEntity = entity;
                    break;
                }
            }
        }
        return mEntity;
    }

    public boolean removeItemFromSuccList(int mType){
        boolean succ = false;
        if(mVSucc != null){
            for(int i = 0;i < mVSucc.size();i++){
                PicEntity vEntity = mVSucc.get(i);
                if(vEntity != null && vEntity.mType == mType){
                    mVSucc.remove(i);
                    i--;
                }
            }
        }
        return succ;
    }

    public boolean findLocPicBySuccList(String path){
        boolean find = false;
        if(mVSucc != null && mVSucc.size() > 0){
            for(PicEntity picEntity : mVSucc){
                if(picEntity != null && !TextUtils.isEmpty(picEntity.path) && picEntity.path.equals(path)){
                    find = true;
                    break;
                }
            }
        }
        return find;
    }
}
