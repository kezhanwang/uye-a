package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/18.
 */

public class POrderInfoEntity implements Serializable{
    public String contract;
    public String authcontract;
    public POrganizeEntity organize;
    public ArrayList<PCourseEntity> courses;
}
