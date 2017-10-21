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
}
