package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.adapter.EmployProAdapter;
import com.bjzt.uye.adapter.EmployProPicAdapter;
import com.bjzt.uye.entity.PEmployProListItemEntity;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 就业进展列表
 * Created by billy on 2017/10/25
 */
public class EmployProItemView extends BaseItemView<PEmployProListItemEntity>{
    private PEmployProListItemEntity mEntity;

    @BindView(R.id.txt_time)
    TextView mTxtTime;
    @BindView(R.id.txt_content)
    TextView mTxtContent;
    @BindView(R.id.gridview)
    GridView mGridView;

    private EmployProPicAdapter mPicAdapter;

    public EmployProItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(PEmployProListItemEntity pEmployProEntity) {
        this.mEntity = pEmployProEntity;
        //set time
        String strTime = this.mEntity.date;
        if(!TextUtils.isEmpty(strTime)){
            mTxtTime.setText(strTime);
        }else{
            mTxtTime.setText("");
        }
        //set content
        String strContent = this.mEntity.desp;
        if(!TextUtils.isEmpty(strContent)){
            mTxtContent.setText(strContent);
        }else{
            mTxtContent.setText("");
        }
        if(this.mEntity.pic != null && this.mEntity.pic.size() > 0){
            mGridView.setVisibility(View.VISIBLE);
            if(mPicAdapter == null){
                mPicAdapter = new EmployProPicAdapter(this.mEntity.pic);
                mPicAdapter.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
                mGridView.setAdapter(mPicAdapter);
            }else{
                mPicAdapter.reSetList(this.mEntity.pic);
            }
        }else{
            mGridView.setVisibility(View.GONE);
        }
    }

    @Override
    public PEmployProListItemEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.employ_itemview_layout,this,true);

        ButterKnife.bind(this);
    }
}
