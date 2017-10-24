package com.bjzt.uye.adapter;

import android.text.TextUtils;

import com.bjzt.uye.entity.VPicFileEntity;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.msglist.itemview.SelectPicItemView;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by billy on 2017/10/24.
 */

public class AdapterSelectPic extends BaseListAdapter<VPicFileEntity> {

    public AdapterSelectPic(List<VPicFileEntity> mList) {
        super(mList);
    }

    @Override
    protected BaseItemView<VPicFileEntity> getItemView(VPicFileEntity vPicFileEntity) {
        SelectPicItemView itemView = new SelectPicItemView(Global.mContext);
        return itemView;
    }


    public void insertLastBefore(VPicFileEntity entity){
        List<VPicFileEntity> mList = getList();
        if(mList.size() > 0){
            mList.add(mList.size()-1,entity);
        }else{
            mList.add(entity);
        }
        notifyDataSetChanged();
    }

    public void checkMaxCnt(int maxSize){
        if(getCount() > maxSize){
            deleteLastOne();
        }
    }

    private void deleteLastOne(){
        VPicFileEntity entity = (VPicFileEntity) getItem(getCount() - 1);
        removeItem(entity);
    }

    public List<String> getPublishList(){
        List<String> mList = new ArrayList<String>();
        for(int i = 0;i < getCount();i++){
            VPicFileEntity entity = (VPicFileEntity) getItem(i);
            if(!entity.isAddPic){
                String filePath=entity.filePath;
                if (!TextUtils.isEmpty(filePath)) {
                    mList.add(filePath);
                }
            }
        }
        return mList;
    }
    public List<String> getNetUrlList(){
        List<String> mList = new ArrayList<String>();
        for(int i = 0;i < getCount();i++){
            VPicFileEntity entity = (VPicFileEntity) getItem(i);
            if(!entity.isAddPic){
                String url=entity.url;
                if (!TextUtils.isEmpty(url)) {
                    mList.add(url);
                }
            }
        }
        return mList;
    }
    public void modifyPic(VPicFileEntity entity,int pos){
        getList().set(pos,entity);
        notifyDataSetChanged();
    }

}
