package com.bjzt.uye.photo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.entity.PicEntity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.photo.adapter.LoanAdapterPicScane;
import com.bjzt.uye.photo.listener.LoanIPublishListener;
import com.bjzt.uye.util.FileUtil;
import com.bjzt.uye.util.IntentUtils;
import com.bjzt.uye.util.SDCardUtil;
import com.bjzt.uye.views.component.YHeaderView;
import com.common.common.MyLog;
import com.common.thread.ThreadPool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/22.
 */

public class LoanPicScanActivity extends BaseActivity implements View.OnClickListener {
    private List<String> mList = null;
    private List<String> mDelList = new ArrayList<String>();
    private int curPos;
    private ViewPager mViewPager;
    private YHeaderView mHeader;
    private LoanAdapterPicScane mAdapter;
    private Button mBottomBtn;

    private boolean isHeaderShow = true;
    public static final int TYPE_LOC = 1;
    public static final int TYPE_NET = 2;
    public static final int TYPE_ABLUM_LOC = 3;

    private final int FLAG_FINISH = 0x100;
    private final int FLAG_DEL_ACTION = 0x101;
    private final int FLAG_FILE_SAVE = 0x102;

    private int mType;
//    private LoanDialogExist mDialogExit;
//    private LoanDialogExist mDialogSave;
    private String mPathSave;
    private ArrayList<PicEntity> mPicList;

    @Override
    protected int getLayoutID() {
        return R.layout.loan_activity_pic_scan_layout;
    }

    @Override
    protected void initExtras(Bundle bundle) {
        Intent intent = getIntent();
        curPos = intent.getIntExtra(IntentUtils.PARA_KEY_POS,0);
        mType = intent.getIntExtra(IntentUtils.PARA_KEY_TYPE,TYPE_LOC);
        mList = intent.getStringArrayListExtra(IntentUtils.PARA_KEY_PUBLIC);
        MyLog.d(TAG,"[initExtras]" + " mList:" + mList);
//        if(mType == TYPE_NET){
//            mPicList = (ArrayList<PicEntity>) intent.getSerializableExtra(IntentUtils.PARA_KEY_LIST);
//            if(mPicList != null){
//                mList = new ArrayList<String>();
//                if(mPicList != null){
//                    for(int i = 0;i < mPicList.size();i++){
//                        PicEntity vEntity = mPicList.get(i);
//                        String path = vEntity.path;
//                        mList.add(path);
//                    }
//                }
//            }
//        }else{
//            mList = intent.getStringArrayListExtra(IntentUtils.PARA_KEY_PUBLIC);
//        }
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader = (YHeaderView) this.findViewById(R.id.header);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                checkDataGoBack();
                finish();
            }

            @Override
            public void onRightClick() {
                if(mType == TYPE_NET){
                    String str = mAdapter.getPicUrl(curPos);
                    mDelList.add(str);
                    mAdapter.deleteItemByPos(curPos);
                    refreshHeader(null);
                    if(mAdapter.getCount() <= 0){
                        checkDataGoBack();
                        finish();
                    }
                }else if(mType == TYPE_LOC){
                    showDialogExist();
                }
            }
        });

        String str = "标题";
        mHeader.setTitle(str);
        if(mType == TYPE_LOC || mType == TYPE_NET){
            mHeader.updateType(YHeaderView.TYPE_PIC_SCANNE);
            mHeader.setRightImage(R.drawable.loan_icon_del);
        }else if(mType == TYPE_ABLUM_LOC){
            mHeader.updateType(YHeaderView.TYPE_IMAGE_RIGHT_ABLUME);
            mHeader.setRightImage(R.drawable.loan_pic_ablum_flag_selected);
        }
        mViewPager = (ViewPager) this.findViewById(R.id.mviewpager);
        mAdapter = new LoanAdapterPicScane(this,mList,mType);
        mAdapter.setIPublishListener(mListener);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int pos){
//				View view = mViewPager.getChildAt(pos);
//				MyLog.debug(TAG,"[onPageSelected]" + " view:" + view + " pos:" + pos);
                curPos = pos;
                refreshHeader(null);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}
            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
        mViewPager.setCurrentItem(curPos);
        mBottomBtn = (Button) this.findViewById(R.id.btn_confirm);
        mBottomBtn.setOnClickListener(this);

        refreshHeader(null);
    }

    private void refreshBtn(){
        if(mType == TYPE_NET){
            mBottomBtn.setVisibility(View.VISIBLE);
            int size = mList.size() - mDelList.size();
            Drawable d = null;
            if(size <= 0){
                d = getResources().getDrawable(R.drawable.loan_uploadpic_sure_not);
            }else{
                d = getResources().getDrawable(R.drawable.loan_btn_ablume_selector);
            }
            mBottomBtn.setBackgroundDrawable(d);
            String str = getResources().getString(R.string.complete);
            str += getResources().getString(R.string.loan_pic_scanne_brk,size);
            mBottomBtn.setText(str);
        }else{
            mBottomBtn.setVisibility(View.GONE);
        }
    }

    private int findDelItem(List<String> mList){
        int index = -1;
        if(mList != null){
            if(mPicList != null){
                for(int i = 0;i < mPicList.size();i++){
                    PicEntity entity = mPicList.get(i);
                    String url = entity.path;
                    if(!mList.contains(url)){
                        index = i;
                        break;
                    }
                }
            }
        }
        return index;
    }

    private void refreshHeader(List<String> rList){
        if(mType == TYPE_ABLUM_LOC){
            String str = mAdapter.getPicUrl(curPos);
            if(mDelList.contains(str)){
                mHeader.setRightImage(R.drawable.loan_pic_ablum_flag_nor);
            }else{
                mHeader.setRightImage(R.drawable.loan_pic_ablum_flag_selected);
            }
        }else if(mType == TYPE_NET){
            int sum = mAdapter.getCount();
            int cur = curPos + 1;
            String str = "" + cur + "/" + sum;
            mHeader.setTitle(str);
        }else{
            int sum = mAdapter.getCount();
            int cur = curPos + 1;
            String str = "" + cur + "/" + sum;
            mHeader.setTitle(str);
        }
    }


    private LoanIPublishListener mListener = new LoanIPublishListener() {
        @Override
        public void onPicClickDown() {
            isHeaderShow = !isHeaderShow;
            if(isHeaderShow){
                mHeader.setVisibility(View.VISIBLE);
                if(mType == TYPE_ABLUM_LOC){
                    mBottomBtn.setVisibility(View.VISIBLE);
                }
            }else{
                mHeader.setVisibility(View.GONE);
                if(mType == TYPE_ABLUM_LOC){
                    mBottomBtn.setVisibility(View.GONE);
                }
            }
        };

        @Override
        public void onPicLongClick(String path) {
            MyLog.debug(TAG,"[onPicLongClick]" + " path:" + path);
            showDialogSave(path);
        };
    };

    private void checkDataGoBack(){
        Intent intent = new Intent();
        intent.putStringArrayListExtra(IntentUtils.PARA_KEY_PUBLIC,(ArrayList<String>)mAdapter.getList());
        setResult(Activity.RESULT_OK,intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            checkDataGoBack();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    };

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if(view == this.mBottomBtn){
            checkDataGoBack();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        hideDialogExist();
        hideDialogSave();
        ThreadPool.getInstance().removeJob(rSave);
    }

    private void hideDialogExist(){
//        if(mDialogExit != null){
//            mDialogExit.dismiss();
//            mDialogExit = null;
//        }
    }

    private void hideDialogSave(){
//        if(mDialogSave != null){
//            mDialogSave.dismiss();
//            mDialogSave = null;
//        }
    }

    @Override
    protected void handleMsg(Message msg) {
        // TODO Auto-generated method stub
        int what = msg.what;
        switch(what){
            case FLAG_FINISH:
                checkDataGoBack();
                finish();
                break;
            case FLAG_DEL_ACTION:
                hideLoadingDialog();
                hideToast();
                int pos = msg.arg1;
                mAdapter.deleteItemByPos(pos);
                List<String> rList = mAdapter.getList();
                refreshHeader(rList);
                break;
            case FLAG_FILE_SAVE:
                String str = (String) msg.obj;
                showToast(str);
                break;
        }
    }

    private void showDialogExist(){
//        hideDialogExist();
//        mDialogExit = new LoanDialogExist(this,R.style.Loan_MyDialogBg);
//        mDialogExit.setExistBtnListener(new LoanDialogExist.ExistBtnListener() {
//            @Override
//            public void btnExisit() {
//                int count = mAdapter.getCount();
//                String str = getResources().getString(R.string.loan_pic_scanne_tips_del_succ);
//                showToast(str,100);
//                if(count - 1 <= 0){
//                    //del last pic
//                    mAdapter.deleteItemByPos(curPos);
//                    refreshHeader(null);
//
//                    Message msg = Message.obtain();
//                    msg.what = FLAG_FINISH;
//                    msg.arg1 = curPos;
//                    sendMsgDelay(msg,1000);
//                }else{
//                    Message msg = Message.obtain();
//                    msg.what = FLAG_DEL_ACTION;
//                    msg.arg1 = curPos;
//                    sendMsgDelay(msg,1000);
//                }
//            }
//        });
//        mDialogExit.show();
//        mDialogExit.updateType(LoanDialogExist.TYPE_DEL);
    }

    private void showDialogSave(final String filePath){
        hideDialogSave();
//        mDialogSave = new LoanDialogExist(this,R.style.Loan_MyDialogBg);
//        mDialogSave.setExistBtnListener(new LoanDialogExist.ExistBtnListener() {
//            @Override
//            public void btnExisit() {
//                LoanPicScanActivity.this.mPathSave = filePath;
//                ThreadPool.getInstance().submmitJob(rSave);
//            }
//        });
//        mDialogSave.show();
//        mDialogSave.updateType(LoanDialogExist.TYPE_SAVE);
    }

    private Runnable rSave = new Runnable() {
        @Override
        public void run() {
            String filePath = SDCardUtil.getSavePath();
            String fileName = filePath + "/kezhanmsg" + System.currentTimeMillis() + ".jpg";
            boolean isSucc = FileUtil.copyBigFile(mPathSave,fileName);
            Message msg = Message.obtain();
            msg.what = FLAG_FILE_SAVE;
            String str = "";
            if(isSucc){
                str = "保存成功";
            }else{
                str = "保存失败";
            }
            msg.obj = str;
            sendMsg(msg);

            //更新图片集
            File file = new File(fileName);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            LoanPicScanActivity.this.sendBroadcast(intent);
        }
    };

    
}
