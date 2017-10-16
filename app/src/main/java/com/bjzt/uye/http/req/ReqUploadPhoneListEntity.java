package com.bjzt.uye.http.req;

import com.bjzt.uye.entity.PhoneUserEntity;
import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;
import com.common.common.MyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by billy on 2017/10/16.
 */

public class ReqUploadPhoneListEntity extends ReqBaseEntity{

    public String mobile;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_UPLOAD_CONTACTLIST;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("mobile",mobile);
        return mReqMap;
    }

    public static final String parseContactData(List<PhoneUserEntity> mList){
        String str = "";
        if(mList != null && mList.size() > 0){
            Gson gson = new Gson();
            Type type = new TypeToken<List<PhoneUserEntity>>(){}.getType();
            str = gson.toJson(mList,type);
            if(MyLog.isDebugable()){
                MyLog.debug("ReqUploadPhoneListEntity","[getReqData]" + " str:" + str);
            }
        }
        return str;
    }
}
