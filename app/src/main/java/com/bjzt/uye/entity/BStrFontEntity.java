package com.bjzt.uye.entity;

/**
 * Created by billy on 2017/10/20.
 */

public class BStrFontEntity {
    public int start;
    public int len;
    public int fontSize;
    public int fontColor;

    public int getNextStart(){
        return start + len;
    }

    public BStrFontEntity getNextFontEntity(int length){
        BStrFontEntity mEntity = new BStrFontEntity();
        mEntity.start = getNextStart();
        mEntity.len = length;
        mEntity.fontSize = this.fontSize;
        mEntity.fontColor = this.fontColor;
        return mEntity;
    }
}
