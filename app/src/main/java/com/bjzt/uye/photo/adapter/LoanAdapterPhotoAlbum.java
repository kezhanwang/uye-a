package com.bjzt.uye.photo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.photo.entity.LoanVAblumItemEntity;
import com.bjzt.uye.photo.listener.LoanIAblumBtnListener;
import com.bjzt.uye.photo.view.LoanAblumeItem;
import java.util.List;

/**
 * Created by billy on 2017/10/22
 */
public class LoanAdapterPhotoAlbum extends BaseAdapter {
    private List<LoanVAblumItemEntity> mList;
    private LayoutInflater li;
    private boolean isPageStop = true;
    private LoanIAblumBtnListener mListener;
    private Context mContext;

    public LoanAdapterPhotoAlbum(List<LoanVAblumItemEntity> mList) {
        // TODO Auto-generated constructor stub
        this.mList = mList;
        li = (LayoutInflater) Global.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = Global.mContext;
    }

    public void setPageFlag(boolean isPageStop){
        this.isPageStop = isPageStop;
    }

    public void setIAblumeListener(LoanIAblumBtnListener mListener){
        this.mListener = mListener;
    }

    public void setPicFlagFalse(List<String> urls){
        for(int k = 0;k < urls.size();k++){
            String url = urls.get(k);
            for(int i = 0;i < mList.size();i++){
                LoanVAblumItemEntity entity = mList.get(i);
                if(entity.url.equals(url)){
                    entity.isSelect = false;
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(mList != null){
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if(mList != null){
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        LoanAblumeItem view = null;
        LoanVAblumItemEntity entity = (LoanVAblumItemEntity) getItem(pos);
        if(convertView == null){
            view = new LoanAblumeItem(mContext);
        }else{
            view = (LoanAblumeItem) convertView;
        }
        view.setInfo(isPageStop, pos, entity,mListener);
        return view;
    }

    public void onPageStop(AbsListView girdView){
        if(girdView != null){
            int index = girdView.getFirstVisiblePosition();
            int cnt = girdView.getChildCount();
            for(int i = 0;i < cnt;i++){
                View view = girdView.getChildAt(i);
                if(view instanceof LoanAblumeItem){
                    LoanAblumeItem ablumeItem = (LoanAblumeItem) view;
                    LoanVAblumItemEntity entity = (LoanVAblumItemEntity) getItem(index+i);
                    ablumeItem.setInfo(true, index+i, entity,mListener);
                }
            }
        }
    }

}
