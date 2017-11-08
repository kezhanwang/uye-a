package com.bjzt.uye.adapter;

import android.content.Context;
import com.bjzt.uye.entity.PHomeEntity;
import com.bjzt.uye.entity.PHomeOrderEntity;
import com.bjzt.uye.entity.VHomeHeaderViewEntity;
import com.bjzt.uye.entity.VHomeLocEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.listener.IViewPagerListener;
import com.bjzt.uye.views.component.HomeHeaderView;
import com.bjzt.uye.views.component.HomeLocView;
import com.bjzt.uye.views.component.HomeOrderInfoView;
import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.MutiBaseListAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/20.
 */

public class AdapterHome extends MutiBaseListAdapter{

    private IItemListener mListener;
    private IViewPagerListener mPagerListener;

    public AdapterHome(List<BaseItemListener> mList, int mTypeAdapter) {
        super(mList, mTypeAdapter);
    }

    @Override
    protected BaseItemView<BaseItemListener> getItemView(BaseItemListener baseItemEntity) {
        int mType = baseItemEntity.getType();
        BaseItemView baseItemView = null;
        Context mContext = Global.getContext();
        if(mType == MutiBaseListAdapter.TYPE_HOME_HEADERVIEW){
            HomeHeaderView finItemView = new HomeHeaderView(mContext);
            finItemView.setIPageListener(this.mPagerListener);
            baseItemView = finItemView;
        }else if(mType == MutiBaseListAdapter.TYPE_HOME_ORDERIINFO){
            HomeOrderInfoView finItemView = new HomeOrderInfoView(mContext);
            finItemView.setIItemListener(this.mListener);
            baseItemView = finItemView;
        }else if(mType == MutiBaseListAdapter.TYPE_HOME_LOC){
            HomeLocView adItemView = new HomeLocView(Global.getContext());
            adItemView.setIItemListener(this.mListener);
            baseItemView = adItemView;
        }
        return baseItemView;
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    public void setIPagerListener(IViewPagerListener mPagerListener){
        this.mPagerListener = mPagerListener;
    }

    public static final ArrayList<BaseItemListener> buildList(PHomeEntity pEntity){
        ArrayList<BaseItemListener> rList = new ArrayList<BaseItemListener>();
        //build headerview entity
        VHomeHeaderViewEntity headerViewEntity = new VHomeHeaderViewEntity();
        headerViewEntity.mList = pEntity.ad_list;
        headerViewEntity.strOrderCnt = pEntity.count_order;
        rList.add(headerViewEntity);
        //build orderinfo entity
        PHomeOrderEntity orderEntity = pEntity.insured_order;
        rList.add(orderEntity);
        //build loc entity
        VHomeLocEntity locEntity = new VHomeLocEntity();
        locEntity.strLoc = pEntity.loaction;
        locEntity.organize = pEntity.organize;
        rList.add(locEntity);
        return rList;
    }
}
