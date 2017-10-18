package com.bjzt.uye.views.component.datepicker.adapter;

import android.content.Context;
import com.bjzt.uye.entity.VTimeEntity;
import java.util.List;

/**
 * Created by diaosi on 2016/6/3.
 */
public class LoanTimeAdapter extends AbstractWheelTextAdapter {

    private List<VTimeEntity> mList;

    public LoanTimeAdapter(Context context, List<VTimeEntity> mList) {
        super(context);
        this.mList = mList;
    }

    @Override
    protected CharSequence getItemText(int index) {
        if(mList != null && index < mList.size()){
            VTimeEntity vEntity = mList.get(index);
            return vEntity.buildTime();
        }
        return "";
    }

    @Override
    public int getItemsCount() {
        if(mList != null){
            return mList.size();
        }
        return 0;
    }
}
