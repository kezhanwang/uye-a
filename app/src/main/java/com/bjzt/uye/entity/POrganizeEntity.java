package com.bjzt.uye.entity;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/18.
 */

public class POrganizeEntity implements Serializable{
    public String org_id;
    public String org_short_name;
    public String org_name;
    public String org_type;
    public String parent_id;
    public String status;
    public int is_shelf;
    public int is_delete;
    public int is_employment;
    public int is_high_salary;
    public long created_time;
    public long updated_time;
    public String logo;
//    public String description;
    public String province;
    public String city;
    public String area;
    public String address;
    public String category_1;
    public String phone;
    public String employment_index;
    public String avg_course_price;
    public String category;
    public String distance;
}
