package com.bjzt.uye.entity;

import com.common.msglist.entity.PPageEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/17.
 */

public class PSearchAgencyEntity implements Serializable{
    public PPageEntity page;
    public ArrayList<PAgencyEntity> organizes;
}
