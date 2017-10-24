package com.bjzt.uye.activity.dialog;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bjzt.uye.R;
import com.bjzt.uye.adapter.DCourseListAdapter;
import com.bjzt.uye.entity.PCourseEntity;
import com.common.msglist.base.BaseListAdapter;
import com.common.util.DeviceUtil;

import java.util.List;

/**
 * Created by billy on 2017/10/19.
 */
public class DialogCourseList extends DialogBankList{

    private DCourseListAdapter mAdapter;

    public DialogCourseList(Context context, int theme) {
        super(context, theme);
    }

    public void setCourseList(List<PCourseEntity> mList,PCourseEntity pSelectCourse){
        int cnt = 0;
        if(mList != null){
            cnt = mList.size();
            if(pSelectCourse != null){
                for(int i = 0;i < mList.size();i++){
                    PCourseEntity pEntity = mList.get(i);
                    if(pEntity != null && pEntity.c_id.equals(pSelectCourse.c_id)){
                        pEntity.vIsSelected = true;
                    }
                }
            }
        }
        if(mAdapter == null){
            mAdapter = new DCourseListAdapter(mList);
            mAdapter.setListener(mItemListener);
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
        h += (cnt*getContext().getResources().getDimension(R.dimen.cash_bind_card_item_height));
        FrameLayout.LayoutParams llp = (FrameLayout.LayoutParams) mRelaMain.getLayoutParams();
        llp.width = mWidth;
        llp.height = (int) h;
        mRelaMain.setLayoutParams(llp);

        String title = "选择课程";
        this.mTxtTitle.setText(title);
    }
}
