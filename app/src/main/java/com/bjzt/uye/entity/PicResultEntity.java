package com.bjzt.uye.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/18.
 */

public class PicResultEntity implements Serializable{
    public String path;
    public ArrayList<String> mPaths = null;
    public int size;
    public boolean isCammera;
    public boolean isEmpty;
}
