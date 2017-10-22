package com.bjzt.uye.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bjzt.uye.entity.PInsureOrderEntity;
import com.bjzt.uye.entity.PInsureOrderItemEntity;
import com.bjzt.uye.msglist.itemview.InsureOrderItemView;
import java.util.List;

/**
 * Created by billy on 2017/10/21.
 */
public class UYeAdapter extends PagerAdapter{
    private List<PInsureOrderEntity> mList;
    private Context mContext;

    public UYeAdapter(Context mContext,List<PInsureOrderEntity> mList){
        this.mList = mList;
        this.mContext = mContext;
    }

    public List<PInsureOrderEntity> getList(){
        return this.mList;
    }

    public PInsureOrderEntity getItem(int pos){
        if(mList != null && mList.size() > pos){
            return mList.get(pos);
        }
        return null;
    }

    public void reSetList(List<PInsureOrderEntity> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if(this.mList != null){
            return this.mList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        PInsureOrderEntity mEntity = mList.get(position);
        InsureOrderItemView view = new InsureOrderItemView(this.mContext);
        view.setInfo(mList,mEntity);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        if(object instanceof InsureOrderItemView){
            InsureOrderItemView viewItem = (InsureOrderItemView) object;
            viewItem.recyle();
        }
        container.removeView((View)object);
    }

}
