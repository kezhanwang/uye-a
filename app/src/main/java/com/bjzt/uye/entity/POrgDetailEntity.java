package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by billy on 2017/10/27.
 */
public class POrgDetailEntity implements Serializable{
    public POrganizeEntity organize;
    public List<PCourseEntity> courses;
}
