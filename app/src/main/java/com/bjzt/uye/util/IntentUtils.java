package com.bjzt.uye.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.bjzt.uye.activity.ApplyContactInfoActivity;
import com.bjzt.uye.activity.ApplyEmployProActivity;
import com.bjzt.uye.activity.ApplyEmployProAddActivity;
import com.bjzt.uye.activity.ApplyFirstTransferActivity;
import com.bjzt.uye.activity.ApplyIDActivity;
import com.bjzt.uye.activity.ApplyMyExperienceBaseActivity;
import com.bjzt.uye.activity.ApplyMyExperienceDegreeAddActivity;
import com.bjzt.uye.activity.ApplyMyExperienceOccDegreeActivity;
import com.bjzt.uye.activity.ApplyMyExperienceOccAddActivity;
import com.bjzt.uye.activity.DataCheckActivity;
import com.bjzt.uye.activity.LoginActivity;
import com.bjzt.uye.activity.MainActivity;
import com.bjzt.uye.activity.MapActivity;
import com.bjzt.uye.activity.OrderInfoActivity;
import com.bjzt.uye.activity.OrgDetailActivity;
import com.bjzt.uye.activity.QAActivity;
import com.bjzt.uye.activity.RegisterActivity;
import com.bjzt.uye.activity.SearchActivity;
import com.bjzt.uye.activity.WebViewActivity;
import com.bjzt.uye.entity.PExperiEntity;
import com.bjzt.uye.http.rsp.RspExperiListEntity;
import com.bjzt.uye.http.rsp.RspQACfgEntity;
import com.bjzt.uye.photo.activity.LoanPhotoAlblumActivity;
import com.bjzt.uye.photo.activity.LoanPicScanActivity;
import com.common.common.MyLog;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/12.
 */

public class IntentUtils {
    private static final String TAG = "IntentUtils";

    public static final String PARA_KEY_PUBLIC = "key_public";
    public static final String PARA_KEY_DATA = "key_data";
    public static final String KEY_WEB_URL = "key_web_url";
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_PHONE = "key_phone";
    public static final String PARA_KEY_SIZE = "key_size";
    public static final String PARA_KEY_POS = "key_pos";
    public static final String PARA_KEY_TYPE = "key_type";
    public static final String PARA_KEY_LIST = "key_list";
    public static final String PARA_KEY_RSP = "key_rsp";
    public static final String LNG = "lng";
    public static final String LAT = "lat";
    public static final String SNAME = "sname";
    public static final String ADDRESS = "addrss";

    /**
     * 打开首页
     * @param mContext
     * @param clearTop
     */
    public static final void startMainActivity(Context mContext,boolean clearTop){
        Intent intent = new Intent(mContext,MainActivity.class);
        if(clearTop){
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }


    /***
     * 打开首页
     * @param mContext
     */
    public static final void startMainActivity(Context mContext){
        startMainActivity(mContext,false);
    }

    /***
     * 打开登录页卡
     * @param mContext
     * @param requestCode
     */
    public static final void startLoginActivity(Activity mContext,int type,int requestCode){
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,type);
        mContext.startActivityForResult(intent,requestCode);
    }

    /***
     * 打开注册页卡
     * @param mContext
     * @param requestCode
     */
    public static final void startRegisterActivity(Activity mContext,int requestCode){
        Intent intent = new Intent(mContext, RegisterActivity.class);
        mContext.startActivityForResult(intent,requestCode);
    }

    /***
     * 个人资料页卡
     * @param mContext
     * @param requestCode
     */
    public static final void startDataCheckActivity(Activity mContext,String orgId,int requestCode){
        Intent intent = new Intent(mContext, DataCheckActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,requestCode);
    }


    /**
     * 打开系统UI
     * @param mContext
     * @param url
     */
    public static final void startSysActivity(Context mContext,String url){
        if(!TextUtils.isEmpty(url)){
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }catch(Exception ee){
                MyLog.error(TAG,ee);
            }
        }
    }

    /***
     * 打开WebViewActivity
     * @param mContext
     */
    public static final void startWebViewActivity(Context mContext,String url){
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(IntentUtils.KEY_WEB_URL,url);
        mContext.startActivity(intent);
    }

    /***
     * 打开身份认证UI
     * @param mContext
     * @param requestCode
     */
    public static final void startApplyIDActivity(Activity mContext,int requestCode){
        Intent intent = new Intent(mContext, ApplyIDActivity.class);
        mContext.startActivityForResult(intent,requestCode);
    }

    /**
     * 打开搜索页卡
     * @param mContext
     */
    public static final void startSearchActivity(Activity mContext,int reqCode){
        Intent intent = new Intent(mContext, SearchActivity.class);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 打开系统照相机
     * @param context
     * @param requestCode
     */
    public static final void startMediaStore(Activity context,int requestCode,String filePath){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 100);
        cameraIntent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivityForResult(cameraIntent, requestCode);
    }

    /***
     * 打开系统相机
     * @param context
     * @param requestCode
     * @param filePath
     */
    public static final void startSysCammera(Activity context,int requestCode,String filePath){
        startMediaStore(context, requestCode, filePath);
    }

    /***
     * 获取订单配置信息
     * @param orgID
     */
    public static final void startOrderInfoActivity(Activity mContext,String orgID,int reqCode){
        Intent intent = new Intent(mContext, OrderInfoActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgID);
        mContext.startActivityForResult(intent,reqCode);
    }

    /**
     * 跳转到系统拨号界面
     * @param mContext
     * @param number
     */
    public static final boolean startSysCallActivity(Context mContext,String number){
        boolean isSucc = false;
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            isSucc = true;
        }catch(Exception ee){
            ee.printStackTrace();;
        }
        return isSucc;
    }

    /***
     * 打开就业调查
     * @param mContext
     * @param requestCode
     */
    public static final void startQAActivity(Activity mContext,String orgId,int requestCode){
        Intent intent = new Intent(mContext, QAActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,requestCode);
    }

    /**
     * 跳转到调查问卷
     * @param mContext
     * @param orgId
     * @param rspEntity
     * @param reqCode
     */
    public static final void startQAActivity(Activity mContext, String orgId, RspQACfgEntity  rspEntity,int reqCode){
        Intent intent = new Intent(mContext, QAActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        intent.putExtra(IntentUtils.PARA_KEY_DATA,rspEntity);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 打开系统相册
     */
    public static final void startSysGallery(Activity context,int requestCode,int size,ArrayList<String> mSelectPath){
//		Intent intent = new Intent();
//		intent.setType("image/*");
//		intent.setAction(Intent.ACTION_GET_CONTENT);
//		context.startActivityForResult(intent,requestCode);
        Intent intent = new Intent(context,LoanPhotoAlblumActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_SIZE, size);
        intent.putStringArrayListExtra(IntentUtils.PARA_KEY_PUBLIC, mSelectPath);
        context.startActivityForResult(intent, requestCode);
    }

    /***
     * 本地相册图片浏览
     * @param mContext
     * @param requestCode
     * @param mList
     * @param pos
     */
    public static final void startPicScanAblueActivity(Context mContext,int requestCode,ArrayList<String> mList,int pos){
        startPicScaneActivity(mContext, requestCode, mList, pos, LoanPicScanActivity.TYPE_ABLUM_LOC);
    }

    public static final void startPicScaneActivity(Context mContext,int requestCode,ArrayList<String> mList,int pos,int type){
        Intent intent = new Intent(mContext,LoanPicScanActivity.class);
        intent.putStringArrayListExtra(IntentUtils.PARA_KEY_PUBLIC, mList);
        intent.putExtra(IntentUtils.PARA_KEY_POS, pos);
        intent.putExtra(IntentUtils.PARA_KEY_TYPE, type);
        if(mContext instanceof Activity){
            Activity activity = (Activity) mContext;
            activity.startActivityForResult(intent,requestCode);
        }else{
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    /***
     * 开始进入到申请流程
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startApplyFirstTransActivity(Activity mContext,String orgId,int reqCode){
        Intent intent = new Intent(mContext, ApplyFirstTransferActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,reqCode);
    }


    /*****
     * 下载apk
     * @param mContext
     * @param downLoadUrl
     */
    public static final void startDownLoadApk(Context mContext,String downLoadUrl){
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(downLoadUrl);
        intent.setData(content_url);
        mContext.startActivity(intent);
    }

    /***
     * 职业进展
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startEmployProActivity(Activity mContext,String orgId,int reqCode){
        Intent intent = new Intent(mContext, ApplyEmployProActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,reqCode);
    }

    /****
     * 新增就业进展
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startEmployProAddActivity(Activity mContext,String orgId,int reqCode){
        Intent intent = new Intent(mContext, ApplyEmployProAddActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 联系信息
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startContactInActivity(Activity mContext,String orgId,int reqCode){
        Intent intent = new Intent(mContext, ApplyContactInfoActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 打开机构详情
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startOrgDetailActivity(Activity mContext,String orgId,int reqCode){
        Intent intent = new Intent(mContext, OrgDetailActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 打开个人经历
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startMyExperienceBaseActivity(Activity mContext,String orgId,int reqCode){
        Intent intent = new Intent(mContext, ApplyMyExperienceBaseActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 个人经历-职业列表
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startMyExperienceOccActivity(Activity mContext, String orgId, int reqCode, boolean isOcc, RspExperiListEntity rspEntity){
        Intent intent = new Intent(mContext, ApplyMyExperienceOccDegreeActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        if(isOcc){
            intent.putExtra(IntentUtils.PARA_KEY_TYPE,ApplyMyExperienceOccDegreeActivity.TYPE_OCC);
        }else{
            intent.putExtra(IntentUtils.PARA_KEY_TYPE,ApplyMyExperienceOccDegreeActivity.TYPE_DEGREE);
        }
        if(rspEntity != null){
            intent.putExtra(IntentUtils.PARA_KEY_RSP,rspEntity);
        }
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 个人经历-职业添加
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startMyExperienceOccAddActivity(Activity mContext,String orgId,int reqCode){
        Intent intent = new Intent(mContext, ApplyMyExperienceOccAddActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 个人经历-职业添加
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startMyExperienceOccAddActivity(Activity mContext, String orgId, int reqCode, PExperiEntity pEntity){
        Intent intent = new Intent(mContext, ApplyMyExperienceOccAddActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        intent.putExtra(IntentUtils.PARA_KEY_DATA,pEntity);
        intent.putExtra(IntentUtils.PARA_KEY_TYPE,ApplyMyExperienceOccAddActivity.TYPE_EDIT);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 个人经历-学历列表
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startMyExperienceDegreeActivity(Activity mContext,String orgId,int reqCode){
        Intent intent = new Intent(mContext, ApplyMyExperienceOccDegreeActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        intent.putExtra(IntentUtils.PARA_KEY_TYPE,ApplyMyExperienceOccDegreeActivity.TYPE_DEGREE);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 个人经历-学历添加
     * @param mContext
     * @param orgId
     * @param reqCode
     */
    public static final void startMyExperienceDegreeAddActivity(Activity mContext,String orgId,int reqCode){
        Intent intent = new Intent(mContext, ApplyMyExperienceDegreeAddActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        mContext.startActivityForResult(intent,reqCode);
    }

    public static final void startMyExperienceDegreeAddActivity(Activity mContext,String orgId,int reqCode,PExperiEntity pEntity){
        Intent intent = new Intent(mContext, ApplyMyExperienceDegreeAddActivity.class);
        intent.putExtra(IntentUtils.PARA_KEY_PUBLIC,orgId);
        intent.putExtra(IntentUtils.PARA_KEY_TYPE,ApplyMyExperienceDegreeAddActivity.TYPE_EDIT);
        intent.putExtra(IntentUtils.PARA_KEY_DATA,pEntity);
        mContext.startActivityForResult(intent,reqCode);
    }

    /***
     * 打开百度定位
     * @param mContext
     * @param lng
     * @param lat
     * @param orgName
     * @param address
     */
    public static final void startBaiduMapActivity(Context mContext,double lng,double lat,String orgName,String address){
        Intent intent=new Intent(mContext,MapActivity.class);
        intent.putExtra(LNG, lng);
        intent.putExtra(LAT, lat);
        intent.putExtra(SNAME, orgName);
        intent.putExtra(ADDRESS, address);
        mContext.startActivity(intent);
    }
}
