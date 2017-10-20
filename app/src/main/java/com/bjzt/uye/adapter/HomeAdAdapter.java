package com.bjzt.uye.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.bjzt.uye.entity.PAdEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IViewPagerListener;
import com.bjzt.uye.msglist.itemview.AdItemView;
import java.util.ArrayList;

/**
 * Created by billy on 2017/10/20.
 */

public class HomeAdAdapter extends PagerAdapter {
    private ArrayList<PAdEntity> mList;
    private IViewPagerListener mListener;

    public HomeAdAdapter(ArrayList<PAdEntity> mList) {
        // TODO Auto-generated constructor stub
        this.mList = mList;
    }

    public void resetList(ArrayList<PAdEntity> mList){
        this.mList = mList;
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
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(container instanceof ViewPager){
            if(object instanceof AdItemView){
                AdItemView view = (AdItemView) object;
                view.recyle();
                container.removeView(view);
            }
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AdItemView view = null;
        final PAdEntity entity = mList.get(position);
        view = new AdItemView(Global.mContext);
        view.setIPagerListener(this.mListener);
        view.setAdInfo(entity);
        container.addView(view,0);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        if(isRefresh){
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    public void setIPagerListener(IViewPagerListener viewPagerListener){
        this.mListener = viewPagerListener;
    }

    public void refreshAll(){
        isRefresh = true;
        notifyDataSetChanged();
        isRefresh = false;
    }

    private boolean isRefresh = false;
}
