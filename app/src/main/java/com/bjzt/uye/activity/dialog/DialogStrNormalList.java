package com.bjzt.uye.activity.dialog;

import android.content.Context;
import android.widget.FrameLayout;

import com.bjzt.uye.R;
import com.bjzt.uye.adapter.DCourseListAdapter;
import com.bjzt.uye.adapter.DStrNorAdapter;
import com.bjzt.uye.entity.BDialogStrEntity;
import com.common.msglist.base.BaseListAdapter;
import com.common.util.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/26.
 */
public class DialogStrNormalList extends DialogBankList{

    private DStrNorAdapter mAdapter;

    public DialogStrNormalList(Context context, int theme) {
        super(context, theme);
    }

    public void setStrInfo(List<BDialogStrEntity> mList,BDialogStrEntity mEntitySelect,String title){
        int cnt = mList.size();
        //set select
        if(mList != null && mEntitySelect != null){
            for(BDialogStrEntity bEntity : mList){
                if(bEntity != null && bEntity.str.equals(mEntitySelect.str)){
                    bEntity.isSelect = true;
                    break;
                }
            }
        }
        if(mAdapter == null){
            mAdapter = new DStrNorAdapter(mList);
            mAdapter.setIItemListener(mItemListener);
            mAdapter.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
            mListView.setAdapter(mAdapter);
        }else{
            mAdapter.reSetList(mList);
        }
        if(cnt >= 6){
            cnt = 6;
        }
        int mWidth = DeviceUtil.mWidth;
        float h = getContext().getResources().getDimension(R.dimen.cash_bind_card_title_height);
        h += getContext().getResources().getDimension(R.dimen.cash_bind_card_divider);
        h += (cnt*getContext().getResources().getDimension(R.dimen.cash_bind_card_item_height_normal));
        FrameLayout.LayoutParams llp = (FrameLayout.LayoutParams) mRelaMain.getLayoutParams();
        llp.width = mWidth;
        llp.height = (int) h;
        mRelaMain.setLayoutParams(llp);

        this.mTxtTitle.setText(title);
    }

    public static final List<BDialogStrEntity> buildNormalList(List<String> mList){
        List<BDialogStrEntity> rList = new ArrayList<BDialogStrEntity>();
        if(mList != null){
            for(int i = 0;i < mList.size();i++){
                BDialogStrEntity bEntity = new BDialogStrEntity();
                bEntity.str = mList.get(i);
                rList.add(bEntity);
            }
        }
        return rList;
    }
}
