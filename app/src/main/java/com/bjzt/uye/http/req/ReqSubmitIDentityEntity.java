package com.bjzt.uye.http.req;

import android.text.TextUtils;

import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.base.ReqBaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by billy on 2017/10/18.
 */

public class ReqSubmitIDentityEntity extends ReqBaseEntity{
    public String full_name;
    public String id_card;
    public String id_card_start;
    public String id_card_end;
    public String id_card_address;
    public String id_card_info_pic;
    public String id_card_nation_pic;
    public String auth_mobile;
    public String bank_card_number;
    public String open_bank_code;
    public String open_bank;
    public String code;
    public String udcredit_order;

    @Override
    public String getReqUrl() {
        return HttpCommon.URL_IDENTITY_SUBMIT;
    }

    @Override
    public Map<String, Object> getReqData() {
        Map<String,Object> mReqMap = new HashMap<>();
        mReqMap.put("full_name",full_name);
        mReqMap.put("id_card",id_card);
        mReqMap.put("id_card_start",id_card_start);
        mReqMap.put("id_card_end",id_card_end);
        mReqMap.put("id_card_address",id_card_address);
        mReqMap.put("id_card_info_pic",id_card_info_pic);
        mReqMap.put("id_card_nation_pic",id_card_nation_pic);
        mReqMap.put("auth_mobile",auth_mobile);
        mReqMap.put("bank_card_number",bank_card_number);
        mReqMap.put("open_bank_code",open_bank_code);
        mReqMap.put("open_bank",open_bank);
        mReqMap.put("code",code);
        if(!TextUtils.isEmpty(udcredit_order)){
            mReqMap.put("udcredit_order",udcredit_order);
        }
        return mReqMap;
    }
}
