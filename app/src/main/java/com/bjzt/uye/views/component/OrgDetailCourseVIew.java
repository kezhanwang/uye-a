package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.bjzt.uye.R;
import com.bjzt.uye.adapter.OrgDetailCourseAdapter;
import com.bjzt.uye.entity.PCourseEntity;
import com.common.listener.NoConfusion;
import com.common.msglist.base.BaseListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/11/2.
 */

public class OrgDetailCourseView extends RelativeLayout implements NoConfusion{

    @BindView(R.id.gridview)
    GridView mGridView;

    private OrgDetailCourseAdapter mAdapter;

    public OrgDetailCourseView(Context context) {
        super(context);
        init();
    }

    public OrgDetailCourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.org_detail_courseview_layout,this,true);
        ButterKnife.bind(this);
    }

    public void setCourseList(List<PCourseEntity> mList){
        List<PCourseEntity> rList = new ArrayList<>();
        if(mList != null && mList.size() > 10){
            for(int i = 0;i < 10;i++){
                rList.add(mList.get(i));
            }
        }else{
            rList = mList;
        }
        if(mAdapter == null){
            mAdapter = new OrgDetailCourseAdapter(rList);
            mAdapter.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
            mGridView.setAdapter(mAdapter);
        }else{
            mAdapter.reSetList(rList);
        }
    }
}
