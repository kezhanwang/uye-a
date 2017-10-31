package com.bjzt.uye.entity;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/31.
 */

public class PExperiEntity implements Serializable{
    //base
    public String uid;
    public int id;
    public String date_start;
    public String date_end;
    public int type;

    //occ
    public String work_name;
    public String work_position;
    public String work_salary;

    //degree
    public String education;
    public String school_name;
    public String school_profession;
    public String school_address;
}

