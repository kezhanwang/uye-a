package com.bjzt.uye.entity;

import com.common.msglist.entity.PPageEntity;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/21.
 */

public class PInsureOrderEntity implements Serializable{
    public PInsureOrderItemEntity insured_order;
    public PPageEntity page;
    public boolean isFake = true;
    public int p;
}
