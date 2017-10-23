package com.bjzt.uye.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/18.
 */

public class PIDentityInfoEntity implements Serializable{
    public String uid;
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

    public boolean isSucc;
    public String vMsg;
    public String vCode;

    public PBankEntity buildBankEntity(){
        PBankEntity mEntity = null;
        if(!TextUtils.isEmpty(open_bank) && !TextUtils.isEmpty(open_bank_code)){
            mEntity = new PBankEntity();
            mEntity.open_bank = this.open_bank;
            mEntity.open_bank_code = this.open_bank_code;
        }
        return mEntity;
    }

    public boolean isOkay(){
        return !TextUtils.isEmpty(uid) && !TextUtils.isEmpty(full_name) && !TextUtils.isEmpty(id_card);
    }
}
