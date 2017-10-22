package com.bjzt.uye.photo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bjzt.uye.photo.listener.LoanIPublishListener;
import com.bjzt.uye.photo.view.LoanPicScaneItemView;

import java.util.List;

/**
 * Created by billy on 2017/10/22.
 */

public class LoanAdapterPicScane extends PagerAdapter {

    private List<String> mList;
    private Context mContext;
    private LoanIPublishListener mListener;
    private int mType;

    public LoanAdapterPicScane(Context mContext,List<String> mList,int mType) {
        // TODO Auto-generated constructor stub
        this.mList = mList;
        this.mContext = mContext;
        this.mType = mType;
    }

    public void deleteItemByPos(int pos){
        if(pos >= 0 && pos < mList.size()){
            mList.remove(pos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(mList != null){
            return mList.size();
        }
        return 0;
    }

    public String getPicUrl(int pos){
        if(mList != null){
            return mList.get(pos);
        }
        return null;
    }

    public List<String> getList(){
        return mList;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        LoanPicScaneItemView view = null;
        String picUrl = mList.get(position);
        view = new LoanPicScaneItemView(mContext,mType);
        view.setPicInfo(picUrl);
        view.setIPublishListener(this.mListener);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        if(object instanceof LoanPicScaneItemView){
            LoanPicScaneItemView viewItem = (LoanPicScaneItemView) object;
            viewItem.recyle();
        }
        container.removeView((View)object);
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return POSITION_NONE;
    }

    public void setIPublishListener(LoanIPublishListener mListener){
        this.mListener = mListener;
    }


}
