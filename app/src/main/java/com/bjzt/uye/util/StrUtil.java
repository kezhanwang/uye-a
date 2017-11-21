package com.bjzt.uye.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.bjzt.uye.R;
import com.bjzt.uye.entity.BStrFontEntity;
import com.bjzt.uye.entity.PLocItemEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.common.common.NetCommon;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by billy on 2017/10/12.
 */

public class StrUtil {


    /***
     * 删除所有空格相关
     * @param str
     * @return
     */
    public static final String trimAll(String str){
        String strResult = "";
        if(!TextUtils.isEmpty(str)){
            strResult = str.replaceAll("\\s*","");
        }
        return strResult;
    }

    public static final String trimQuotes(String strInfo){
        String strResult = "";
        if(!TextUtils.isEmpty(strInfo)){
            strResult = strInfo.replace("\"","");
        }
        return strResult;
    }

    /***
     * 判断手机号码是否合法
     * @param phone
     * @return
     */
    public static final boolean isPhotoLegal(String phone){
        if(!TextUtils.isEmpty(phone) && phone.length() == 11){
            return true;
        }
        return false;
    }

    public static final String getErrorTipsByCode(int errorCode){
        String str = "";
        Context mContext = Global.getContext();
        if(errorCode == NetCommon.ERROR_CODE_TIME_OUT){
            str = mContext.getResources().getString(R.string.common_timeout);
        }else{
            str = mContext.getResources().getString(R.string.common_request_error_code,errorCode);
        }
        return str;
    }


    public static final String getErrorTipsByCode(int errorCode, RspBaseEntity rspEntity){
        String str = getErrorTipsByCode(errorCode);
        if(rspEntity != null && !TextUtils.isEmpty(rspEntity.msg)){
            str = rspEntity.msg;
        }
        return str;
    }

    /***
     * 获取百分比进度
     * @param cur
     * @param total
     * @return
     */
    public static final String getProgressStr(long cur,long total){
        double result = ((double)cur / (double)total);
        int data = (int) (result * 100);
        return data+"%";
    }


    public static final List<BStrFontEntity> buildStrNumList(String strInfo){
        List<BStrFontEntity> mList = new ArrayList<BStrFontEntity>();
        Context mContext = Global.getContext();
        int start = 0;
        int fontC = mContext.getResources().getColor(R.color.common_orange);
        int index = -1;
        int fontSize = mContext.getResources().getDimensionPixelSize(R.dimen.common_font_size_16);
        String strReg = "\\d+\\.?\\d*";
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(strInfo);
        String result = m.replaceAll("");
        index = strInfo.indexOf(result);
        if(index >= 0){
            BStrFontEntity bEntity = new BStrFontEntity();
            bEntity.fontColor = fontC;
            bEntity.start = index;
            bEntity.len = result.length();
            bEntity.fontSize = fontSize;
            mList.add(bEntity);
        }
        return mList;
    }


    /***
     * 文本设置字段颜色
     * @param str
     * @param mList
     * @return
     */
    public static final SpannableStringBuilder getSpansStrBuilder(String str, List<BStrFontEntity> mList){
        SpannableStringBuilder builder = null;
        if(!TextUtils.isEmpty(str)){
            builder = new SpannableStringBuilder(str);
            if(mList != null && mList.size() > 0){
                try {
                    for (int i = 0; i < mList.size(); i++) {
                        BStrFontEntity bEntity = mList.get(i);
                        int start = bEntity.start;
                        int len = bEntity.len;
                        int fontSize = bEntity.fontSize;
                        int fontColor = bEntity.fontColor;
                        //set font color
                        ForegroundColorSpan fCS = new ForegroundColorSpan(fontColor);
                        builder.setSpan(fCS, start, start + len, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        //set font size
                        AbsoluteSizeSpan aSP = new AbsoluteSizeSpan(fontSize, false);
                        builder.setSpan(aSP, start, start + len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }catch(Exception ee){
                    builder = null;
                }
            }
        }
        return builder;
    }

    public static final String getInsureOrderSimple(String strInfo){
        String result = strInfo;
//        if(!TextUtils.isEmpty(strInfo) && strInfo.length() > 20){
//            int startIndex = strInfo.length() - 10;
//            result = strInfo.substring(startIndex);
//        }
        return result;
    }

    public static final String getMoneyInfoByFen(long tution){
        return "¥" + (tution / 100);
    }

    public static final String getHomeTutionStr(long tution){
        String str = "";
        StringBuilder builder = new StringBuilder();
        if(tution > 0){
            String s = tution+"";
            s = new StringBuilder(s).reverse().toString();
            for(int i = 0;i < s.length();i++){
                char c = s.charAt(i);
                builder.append(c);
                if(i == 1){
                    builder.append(".");
                }else if((i - 2) > 0 && (i - 2) %  3 == 0){
                    builder.append(",");
                }
            }
            str = builder.reverse().toString();
        }else{
            str = "0.00";
        }
        return str;
    }

    /***
     * 获取位置信息
     * @param mLocPro
     * @param mLocCity
     * @param mLocArea
     * @return
     */
    public static final String getLocAddrStr(PLocItemEntity mLocPro,PLocItemEntity mLocCity,PLocItemEntity mLocArea){
        String str = "";
        if(mLocPro != null){
            str += mLocPro.name;
        }
        if(mLocCity != null && !TextUtils.isEmpty(mLocCity.name)){
            str += "-" + mLocCity.name;
        }
        if(mLocArea != null){
            str += "-" + mLocArea.name;
        }
        return str;
    }

    public static final String getTimeShowStyle(String strTime){
        String strResult;
        strResult = strTime.replaceAll("-","/");
        return strResult;
    }

    public static final boolean isWebUrlLegal(String url){
        if(!TextUtils.isEmpty(url) && url.startsWith("http")){
            return true;
        }
        return false;
    }
}
