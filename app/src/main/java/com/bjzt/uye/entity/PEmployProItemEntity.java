package com.bjzt.uye.entity;

import com.common.msglist.entity.PPageEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by billy on 2017/11/20.
 */

public class PEmployProItemEntity implements Serializable{
    public ArrayList<PEmployProListItemEntity> lists;
    public PPageEntity pages;
}
