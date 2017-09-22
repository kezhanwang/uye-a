package com.common.msglist.entity;

import java.io.Serializable;
/**
 * Created by billy on 2017/9/22.
 */
public class PPageEntity implements Serializable {
    public int p;
    public int ps;
    public long begin;
    public int end;
    public int total;
    public int hasnext;
}
