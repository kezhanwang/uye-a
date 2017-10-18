package com.bjzt.uye.views.component.datepicker.adapter;

import android.content.Context;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by diaosi on 2016/4/27.
 */
public class LoanStrAdapter extends AbstractWheelTextAdapter {
    private List<String> mList;

    public LoanStrAdapter(Context context, List<String> mList) {
        super(context);
        this.mList = mList;
    }

    @Override
    protected CharSequence getItemText(int index) {
        String str = getItemByPos(index);
        return str;
    }

    public String getItemByPos(int pos){
        String str = "";
        if(mList != null && pos < mList.size()){
            str = mList.get(pos);
        }
        return str;
    }

    @Override
    public int getItemsCount() {
        if(mList != null){
            return mList.size();
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
}
