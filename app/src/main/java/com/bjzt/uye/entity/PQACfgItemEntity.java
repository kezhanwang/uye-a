package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/21.
 */

public class PQACfgItemEntity implements Serializable{
    public int id;
    public String question;
    public int type;
    public ArrayList<String> answer;
    public ArrayList<VQAItemEntity> vAnswer;

    public static final int TYPE_SINGLE = 1;    //单向选择题
    public static final int TYPE_MUTI = 2;      //多项选择题
}
