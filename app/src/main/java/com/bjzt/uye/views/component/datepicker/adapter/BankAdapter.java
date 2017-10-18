package com.bjzt.uye.views.component.datepicker.adapter;

import android.content.Context;

import com.bjzt.uye.entity.PBankEntity;
import java.util.List;

/**
 * Created by diaosi on 2016/4/27.
 */
public class BankAdapter extends AbstractWheelTextAdapter {
    private List<PBankEntity> mList;

    public BankAdapter(Context context, List<PBankEntity> mList) {
        super(context);
        this.mList = mList;
    }

    @Override
    protected CharSequence getItemText(int index) {
        String str = "";
        PBankEntity pEntity = getItemByPos(index);
        if(pEntity != null){
            str = pEntity.open_bank;
        }
        return str;
    }

    public PBankEntity getItemByPos(int pos){
        PBankEntity entity = null;
        if(mList != null && pos < mList.size()){
            entity = mList.get(pos);
        }
        return entity;
    }

    @Override
    public int getItemsCount() {
        if(this.mList != null){
            return this.mList.size();
        }
        return 0;
    }

    public int getIndexByInfo(String strInfo){
        int mIndex = -1;
        if(this.mList != null){
            for(int i = 0;i < mList.size();i++){
                PBankEntity mEntity = mList.get(i);
                if(mEntity != null && mEntity.open_bank.equals(strInfo)){
                    mIndex = i;
                    break;
                }
            }
        }
        return mIndex;
    }
}
