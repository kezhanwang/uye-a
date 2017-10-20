package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/19.
 */
public class PHomeEntity implements Serializable{
    public String loaction;
    public String count_order;
    public PHomeOrderEntity insured_order;
    public List<POrganizeEntity> organize;
    public ArrayList<PAdEntity> ad_list;
}
