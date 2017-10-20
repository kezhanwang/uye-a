package com.common.msglist.entity;

import java.io.Serializable;
/**
 * Created by billy on 2017/9/22.
 */
public class PPageEntity implements Serializable {
    public int page;
    public int pageSize;
    public long totalCount;
    public int totalPage;

    public boolean hasNext(){
        if(totalCount > page*pageSize){
            return true;
        }
        return false;
    }
}
