package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.entity.PLocItemEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/30
 */
public class EmployArea extends RelativeLayout implements View.OnClickListener,NoConfusion{

    @BindView(R.id.title)
    TextView mTxtTitle;
    @BindView(R.id.txt_content)
    TextView mTxtContent;
    @BindView(R.id.img_add)
    ImageView imgAdd;
    @BindView(R.id.catview)
    QAPublishCatView catView;

    private IItemListener mListener;

    public static final int SRC_ADD = 1;
    private List<BLocEntity> mList = new ArrayList<BLocEntity>();

    public EmployArea(Context context) {
        super(context);
        init();
    }

    public EmployArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.employ_area_layout,this,true);
        ButterKnife.bind(this);

        mTxtTitle.setText("就业地区");
        imgAdd.setOnClickListener(this);
        catView.setRowCnt(3);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.imgAdd){
                mListener.onItemClick(null,SRC_ADD);
            }
        }
    }

    public void appendLocEntity(PLocItemEntity mLocPro,PLocItemEntity mLocCity,PLocItemEntity mLocArea){
        catView.setVisibility(View.VISIBLE);
        mTxtContent.setVisibility(View.GONE);

        BLocEntity bEntity = new BLocEntity();
        bEntity.mLocPro = mLocPro;
        bEntity.mLocCity = mLocCity;
        bEntity.mLocArea = mLocArea;
        mList.add(bEntity);
        catView.setLocInfo(mList,null);
    }

    public List<BLocEntity> getLocList() {
        return this.mList;
    }

    public static class BLocEntity{
        public PLocItemEntity mLocPro;
        public PLocItemEntity mLocCity;
        public PLocItemEntity mLocArea;
    }
}
