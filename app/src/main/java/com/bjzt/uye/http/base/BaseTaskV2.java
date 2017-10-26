package com.bjzt.uye.http.base;

import android.text.TextUtils;

import com.bjzt.uye.controller.LBSController;
import com.bjzt.uye.controller.WifiController;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.global.MConfiger;
import com.bjzt.uye.util.StrUtil;
import com.common.common.MyLog;
import com.common.common.NetCommon;
import com.common.controller.LoginController;
import com.common.entity.BWifiEntity;
import com.common.entity.PHttpHeader;
import com.common.http.HttpBaseTask;
import com.common.http.HttpUtils;
import com.common.util.APNUtils;
import com.common.util.DeviceUtil;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 框架改造版本2使用
 * Created by diaosi on 2015/12/29.
 */
public class BaseTaskV2<T> extends HttpBaseTask implements ITaskListener {
    private final String TAG = "BaseTaskV2";

    public BaseTaskV2(Object o) {
        super(o);
    }

    @Override
    public void doTask() {
        if(MyLog.isDebugable()){
            MyLog.debug(TAG,"[doTask]");
        }
        taskPre();
        Object o = getReq();
        if(o instanceof ReqBaseEntity){
            ReqBaseEntity reqBaseEntity = (ReqBaseEntity) o;
            String url = reqBaseEntity.getReqUrl();
            //url转换为小写
            if(!TextUtils.isEmpty(url)){
                url = url.toLowerCase();
            }
//            CacheCommon mCache = new CacheCommon();
            final int seqNo = reqBaseEntity.seqNo;
            //数据库缓存请求
//            if(reqBaseEntity.needCache){
//                String key = getReqKey();
//                JSONObject jsonObj = mCache.loadRsp(key);
//                if(jsonObj != null){
//                    getResponse(jsonObj,true,NetCommon.ERROR_CODE_SUCC,seqNo,ITaskListener.TYPE_SRC_CACHE);
//                }
//            }
            //网络请求
            try {
                //带有文件上传的表单提交
                MultipartEntity multiEntity = new MultipartEntity();
                //设置form表单数据
                Map<String,Object> mMap = reqBaseEntity.getReqData();
                if(mMap == null){
                    mMap = new HashMap<String, Object>();
                }
                //每个请求添加一个时间戳,防止运营商的缓存
//                if(reqBaseEntity._t <= 0){
//                    mMap.put("_t", System.currentTimeMillis());
//                }
                //添加经纬度
                String la = LBSController.getInstance().getLa();
                String lo = LBSController.getInstance().getLo();
                if(!TextUtils.isEmpty(la) && !TextUtils.isEmpty(lo)){
                    mMap.put("map_lat",la);
                    mMap.put("map_lng",lo);
                }
                //添加wifi信息
//                boolean isWifi = APNUtils.isWifi();
//                if(isWifi){
                    BWifiEntity bEntity = WifiController.getInstance().getWifiEntity();
                    if(bEntity != null && !TextUtils.isEmpty(bEntity.mac)){
                        mMap.put("mac",bEntity.mac);
                        mMap.put("ssid",bEntity.ssid);
                    }
//                }
//                //添加areaid
//                PHotCityEntity pHotEntity = CityListController.getInstance().getCurCityEntity();
//                if(pHotEntity != null && !TextUtils.isEmpty(pHotEntity.areaid)){
//                    mMap.put("areaid",pHotEntity.areaid);
//                }

                //phone ID
                String strPhoneId = DeviceUtil.getIMEI();
                if(!TextUtils.isEmpty(strPhoneId)){
                    mMap.put("phoneid",strPhoneId);
                }
                //version
                mMap.put("version",MConfiger.VERSION);

                if(mMap != null && mMap.size() > 0){
                    for(Map.Entry<String, Object> entry:mMap.entrySet()){
                        String key = entry.getKey();
                        Object val = entry.getValue();
                        if(val instanceof ReqFileEntity){
                            ReqFileEntity reqFileEntity = (ReqFileEntity) val;
                            if(!TextUtils.isEmpty(reqFileEntity.filePath)){
                                File file = new File(reqFileEntity.filePath);
                                if(MyLog.isDebugable()){
                                    MyLog.debug(TAG,"[doTask]" + " file.exist:" + file.exists());
                                }
                                FileBody fileBody = new FileBody(file);
                                multiEntity.addPart(key,fileBody);
                            }
                        }else{
                            //value过滤掉空格
                            if(val instanceof String){
                                String strVal = (String) val;
                                if(!TextUtils.isEmpty(strVal+"")){
                                    val = StrUtil.trimAll(strVal);
                                }
                            }
                            StringBody strBody = new StringBody(val+"", Charset.forName(HTTP.UTF_8));
                            multiEntity.addPart(key,strBody);
                        }
                    }
                }
                PHttpHeader pHeader = reqBaseEntity.pHeader;
                //start timeout timer
                Global.postDelay(rTimeout, MConfiger.TIME_OUT);
                byte[] buffer = HttpUtils.postJsonV2(url, reqBaseEntity.interfaceType, multiEntity, reqBaseEntity.useAllUrl,pHeader);
                //remove timeout timer
                Global.removeDelay(rTimeout);
                if(buffer != null && buffer.length > 2){
                    String str = new String(buffer, HTTP.UTF_8);
                    JSONObject jsonObj = new JSONObject(str);
                    if(MyLog.isDebugable()){
                        String tempStr = jsonObj.toString();
                        if(MyLog.isDebugable()){
                            MyLog.debug(TAG,"[doTask]" + " rsp str:" + tempStr);
                        }
                    }
                    getResponse(jsonObj,true, NetCommon.ERROR_CODE_SUCC,seqNo,ITaskListener.TYPE_SRC_NET);

                    //延迟一下，防止文件读写造成卡顿
//                    if(reqBaseEntity.needCache){
//                        Thread.sleep(50);
//                        String key = getReqKey();
//                        mCache.saveRsp(key,str);
//                    }
                }else{
                    if(buffer != null && buffer.length == 1){
                        int code = buffer[0];
                        getResponse(null, false,code,seqNo,ITaskListener.TYPE_SRC_NET);
                    }else{
                        getResponse(null, false,NetCommon.ERROR_CODE_EXCEPTION,seqNo,ITaskListener.TYPE_SRC_NET);
                    }
                }
            } catch (UnsupportedEncodingException ee) {
                MyLog.error(TAG, "", ee);
                getResponse(null, false, NetCommon.ERROR_HTTP_UNSUPPORT,seqNo,ITaskListener.TYPE_SRC_NET);
            } catch (Exception ee) {
                // TODO Auto-generated catch block
                MyLog.error(TAG, "", ee);
                getResponse(null, false, NetCommon.ERROR_HTTP_OTHERWISE,seqNo,ITaskListener.TYPE_SRC_NET);
            }
        }
    }

    //超时操作
    private Runnable rTimeout = new Runnable() {
        @Override
        public void run() {
            Object obj = getReq();
            if(obj instanceof  ReqBaseEntity){
                ReqBaseEntity reqEntity = (ReqBaseEntity) obj;
                if(reqEntity != null){
                    int seqNo = reqEntity.seqNo;
                    getResponse(null, false, NetCommon.ERROR_CODE_TIME_OUT,seqNo,ITaskListener.TYPE_SRC_NET);
                }
            }
        }
    };


    @Override
    public void recyle() {

    }

    @Override
    public void getResponse(JSONObject jsonObj, boolean isSucc, int errorCode, int seqNo, int src) {

    }

    @Override
    public Object getReq() {
        return this.t;
    }

    /***
     * doTask任务之前需要做的事情
     */
    protected void taskPre(){}

    private String getReqKey(){
        String key = "";
        Object obj = getReq();
        if(obj instanceof  ReqBaseEntity){
            ReqBaseEntity reqBaseEntity = (ReqBaseEntity) obj;
            key = reqBaseEntity.getReqUrl() + "_" + reqBaseEntity.getID();
            key += ("_" + LoginController.getInstance().getUid());
        }
        return key;
    }
}
