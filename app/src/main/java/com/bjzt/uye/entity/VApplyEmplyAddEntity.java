package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by billy on 2017/11/21.
 */

public class VApplyEmplyAddEntity implements Serializable{
    public String strTime;
    public PLocItemEntity mLocEntityPro;
    public PLocItemEntity mLocEntityCity;
    public PLocItemEntity mLocEntityArea;
    public String strAddr;
    public String cpName;
    public String cpPos;
    public String salary;
    public String employStatus;
    public List<String> mPicList;

    public boolean isSucc;
    public String tips;
}
