package com.bjzt.uye.entity;

import com.bjzt.uye.msglist.itemview.QAListItemView;

import java.io.Serializable;

/**
 * Created by billy on 2017/10/21.
 */

public class VQAItemEntity implements Serializable{
    public String data;
    public boolean isSelect;
    public boolean isBottom;
    public char c;
    public int id;
    public int vQAId;
    public int vType;
    transient public QAListItemView itemView;

    public VQAItemEntity(){
        this.isSelect = false;
        this.isBottom = false;
    }
}
