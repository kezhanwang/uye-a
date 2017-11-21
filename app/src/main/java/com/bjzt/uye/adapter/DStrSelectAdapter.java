package com.bjzt.uye.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.bjzt.uye.views.component.datepicker.adapter.AbstractWheelTextAdapter;
import java.util.List;

/**
 * Created by billy on 2017/11/21.
 */

public class DStrSelectAdapter extends AbstractWheelTextAdapter {
    private List<String> mList;

    public DStrSelectAdapter(Context context,List<String> mList) {
        super(context);
        this.mList = mList;
    }

    @Override
    protected CharSequence getItemText(int index) {
        if(this.mList != null && index >= 0 && this.mList.size() > index){
            return this.mList.get(index);
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        if(this.mList != null){
            return this.mList.size();
        }
        return 0;
    }

    /***
     * 根据Info获取index下标
     * @param strInfo
     * @return
     */
    public int getIndexByInfo(String strInfo){
        int mIndex = -1;
        if(this.mList != null){
            for(int i = 0;i < mList.size();i++){
                String strData = mList.get(i);
                if(!TextUtils.isEmpty(strData) && strData.equals(strInfo)){
                    mIndex = i;
                    break;
                }
            }
        }
        return mIndex;
    }

    public String getItemByPos(int pos){
        String entity = null;
        if(mList != null && pos < mList.size()){
            entity = mList.get(pos);
        }
        return entity;
    }

}
