package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/19.
 */
public class PHomeEntity implements Serializable{
    public String loaction;
    public String count_order;
    public PHomeOrderEntity insured_order;
    public POrganizeEntity organize;
    public ArrayList<PAdEntity> ad_list;
}
