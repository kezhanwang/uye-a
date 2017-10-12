package com.bjzt.uye.http.base;

import com.bjzt.uye.global.Global;
import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.listener.ICallBack;
import com.common.common.MyLog;
import com.common.common.NetCommon;
import org.json.JSONObject;

/**
 * 基础Task对应的业务全部提出来
 * Created by diaosi on 2015/12/28.
 */
public class TaskCommonV2<REQ> extends BaseTaskV2 implements ITaskListener{
    private final String TAG = "TaskCommonV2";

    private ICallBack<Object> callBack;

    public TaskCommonV2(ReqBaseEntity req,ICallBack<Object> listener) {
        super(req);
        // TODO Auto-generated constructor stub
        callBack = listener;
    }


    @Override
    public void getResponse(JSONObject jsonObj, final boolean isSucc, final int errorCode, final int seqNo, final int src) {
        super.getResponse(jsonObj, isSucc, errorCode, seqNo, src);
        final ReqBaseEntity req = (ReqBaseEntity) getReq();
        String url = req.getReqUrl();
        Class<?> clazz = HttpCommon.mMap.get(url);
        if(clazz != null){
            try {
                Object obj = clazz.newInstance();
                if(obj instanceof RspBaseEntity){
                    final RspBaseEntity rsp = (RspBaseEntity) obj;
                    rsp.seqNo = seqNo;
                    if(isSucc){
                        //解析操作
                        rsp.preParseV2(jsonObj, seqNo,req.isJsonArray,req.interfaceType,req);
                    }else{
                        //返回错误,不解析
                        rsp.code = errorCode;
                    }

                    //data notify to ui
                    Global.post2UI(new Runnable() {
                        @Override
                        public void run() {
                            if (callBack != null) {
                                ICallBack call = callBack;
                                if (call != null) {
                                    boolean succ = false;
                                    if(req.interfaceType == NetCommon.NET_INTERFACE_TYPE_UYE){
                                        succ = (rsp.code == NetCommon.ERROR_CODE_SUCC);
                                    }
                                    call.getResponse(rsp,succ,errorCode, seqNo, src);
                                }

                                if(src == ITaskListener.TYPE_SRC_NET){
                                    callBack = null;
                                }
                            }
                        }
                    });

                }else{
                    MyLog.error(TAG,"Rsp need extends RspBaseEntity...");
                    //data notify to ui
                    Global.post2UI(new Runnable() {
                        @Override
                        public void run() {
                            if (callBack != null) {
                                ICallBack call = callBack;
                                if (call != null) {
                                    call.getResponse(null, false, errorCode, seqNo, src);
                                }
                                if(src == ITaskListener.TYPE_SRC_NET){
                                    callBack = null;
                                }
                            }
                        }
                    });
                }
            } catch (InstantiationException e) {
                MyLog.error(TAG,e);
                //data notify to ui
                Global.post2UI(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            ICallBack call = callBack;
                            if (call != null) {
                                call.getResponse(null, false, errorCode, seqNo, src);
                            }
                            if(src == ITaskListener.TYPE_SRC_NET){
                                callBack = null;
                            }
                        }
                    }
                });
            } catch (IllegalAccessException e) {
            	MyLog.error(TAG, e);
                //data notify to ui
            	Global.post2UI(new Runnable() {
                    @Override
                    public void run() {
                        if (callBack != null) {
                            ICallBack call = callBack;
                            if (call != null) {
                                call.getResponse(null, false, errorCode, seqNo, src);
                            }
                            if(src == ITaskListener.TYPE_SRC_NET){
                                callBack = null;
                            }
                        }
                    }
                });
            }
        }
    }
}
