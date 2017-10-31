package com.bjzt.uye.controller;

import com.bjzt.uye.entity.PExperiBaseCfgEntity;

import java.util.List;

/**
 * Created by billy on 2017/10/31.
 */

public class ExperiController {
    private final String TAG = getClass().getSimpleName();
    private static ExperiController instance;
    private PExperiBaseCfgEntity mEntity;

    private ExperiController(){

    }

    public static final ExperiController getInstance(){
        if(instance == null){
            instance = new ExperiController();
        }
        return instance;
    }

    public void setCfgEntity(PExperiBaseCfgEntity mEntity){
        this.mEntity = mEntity;
    }

    public PExperiBaseCfgEntity getCfgEntity(){
        return this.mEntity;
    }

    public List<String> getDegreeList(){
        if(this.mEntity != null){
            return this.mEntity.highest_education;
        }
        return null;
    }

    public List<String> getPostList(){
        if(this.mEntity != null){
            return this.mEntity.profession;
        }
        return null;
    }

    public List<String> getIncomeList(){
        if(this.mEntity != null){
            return this.mEntity.monthly_income;
        }
        return null;
    }
}
