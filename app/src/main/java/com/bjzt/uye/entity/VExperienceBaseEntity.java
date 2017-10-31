package com.bjzt.uye.entity;

import com.bjzt.uye.views.component.EmployArea;
import java.io.Serializable;
import java.util.List;

/**
 * Created by billy on 2017/10/31.
 */

public class VExperienceBaseEntity implements Serializable{
    public String strDegree;
    public String strOcc;
    public String strIncome;
    public String strHouse;
    public List<EmployArea.BLocEntity> mLocList;

    public boolean isSucc;
    public String msg;
}
