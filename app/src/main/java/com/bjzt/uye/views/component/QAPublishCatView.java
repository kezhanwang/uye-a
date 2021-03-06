package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/17.
 */
public class QAPublishCatView extends LinearLayout implements NoConfusion {
    private List<String> mList;
    private int ROW_CNT = 4;
    private List<QARowItem> mQAList;
    private List<LocRowItem> mLocViewList;
    private List<EmployArea.BLocEntity> mLocList;

    public QAPublishCatView(Context context) {
        super(context);
        init();
    }

    public QAPublishCatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        this.setOrientation(LinearLayout.VERTICAL);
        LayoutParams llp = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
        setLayoutParams(llp);
    }

    public void setRowCnt(int cnt){
        this.ROW_CNT = cnt;
    }

    private boolean hasFakeInfo(){
        if(this.mLocList != null){
            for(EmployArea.BLocEntity mEntity : mLocList){
                if(mEntity != null && mEntity.isFake){
                    return true;
                }
            }
        }
        return false;
    }

    public void setLocInfo(List<EmployArea.BLocEntity> mList,IItemListener mListener){
        //clear row
        removeAllViews();
        this.mLocList = mList;

        if(this.mLocList != null) {
            int size = this.mLocList.size();
            if(!hasFakeInfo() && size > 2){   //add fake
                EmployArea.BLocEntity bEntity = new EmployArea.BLocEntity();
                bEntity.isFake = true;
                this.mLocList.add(2,bEntity);
                size++;
            }
            int row = 0;
            if (size > 0) {
                if (size % ROW_CNT == 0) {
                    row = size / ROW_CNT;
                } else {
                    row = size / ROW_CNT + 1;
                }
            }
            mLocViewList = new ArrayList<>();
            for (int i = 0; i < row; i++) {
                LocRowItem item = new LocRowItem(getContext());
                item.setIItemListner(mListener);
                mLocViewList.add(item);
                LayoutParams llp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
                addView(item, llp);
            }

            List<EmployArea.BLocEntity> tempList = null;

            int i = 0;
            for (i = 0; i < size; i++) {
                EmployArea.BLocEntity entity = mList.get(i);
                if (i % ROW_CNT == 0) {
                    if (tempList != null) {
                        int index = i / ROW_CNT - 1;
                        LocRowItem item = mLocViewList.get(index);
                        item.setIndex(index);
                        item.setInfoLoc(tempList);
                    }
                    tempList = new ArrayList<>();
                    tempList.add(entity);
                } else {
                    tempList.add(entity);
                }
            }

            if (tempList != null && (i-1) >= 0) {
                int index = (i-1) / ROW_CNT;
                LocRowItem item = mLocViewList.get(index);
                item.setIndex(index);
                item.setInfoLoc(tempList);
            }
        }
    }

    public void setInfo(List<String> mList, IItemListener mListener){
        //clear row
        removeAllViews();

        this.mList = mList;
        if(this.mList != null){
            int size = this.mList.size();
            int row = 0;
            if(size > 0){
                if(size % ROW_CNT == 0){
                    row = size / ROW_CNT;
                }else{
                    row = size / ROW_CNT + 1;
                }
            }
            mQAList = new ArrayList<>();
            for(int i = 0;i < row;i++){
                QARowItem item = new QARowItem(getContext());
                item.setIItemListner(mListener);
                mQAList.add(item);
                LayoutParams llp = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
                addView(item,llp);
            }

            List<String> tempList = null;

            int i = 0;
            for(i = 0;i < size;i++){
                String entity = mList.get(i);
                if(i % ROW_CNT == 0){
                    if(tempList != null){
                        int index = i / ROW_CNT - 1;
                        QARowItem item = mQAList.get(index);
                        item.setIndex(index);
                        item.setInfo(tempList);
                    }
                    tempList = new ArrayList<>();
                    tempList.add(entity);
                }else{
                    tempList.add(entity);
                }
            }

            if(tempList != null && (i-1) >= 0){
                int index = (i-1) / ROW_CNT;
                QARowItem item = mQAList.get(index);
                item.setIndex(index);
                item.setInfo(tempList);
            }
        }
    }
}
