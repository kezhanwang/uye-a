package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/24.
 */

public class VOrderInfoEntity implements Serializable{
    public String tuition;
    public String clazz;
    public String class_start;
    public String class_end;
    public String course_consultant;
    public String group_pic;
    public List<String> training_pic = new ArrayList<>();
    public String insured_type;

    public boolean isSucc;
    public String msg;
    public PCourseEntity vCourseEntity;

    public VOrderInfoEntity(){
        this.isSucc = false;
    }

    public PCourseEntity getSelectCourseEntity(ArrayList<PCourseEntity> mList,PCourseEntity sCourseEntity){
        PCourseEntity pEntity = null;
        if(mList != null && mList.size() > 0){
            for(int i = 0;i < mList.size();i++){
                PCourseEntity tempEntity = mList.get(i);
                if(tempEntity != null && tempEntity.c_id.equals(sCourseEntity.c_id)){
                    pEntity = tempEntity;
                    break;
                }
            }
        }
        return pEntity;
    }
}
