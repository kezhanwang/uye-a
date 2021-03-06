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

import java.io.Serializable;
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
    public static final int SRC_DEL = 2;

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

        String title = getResources().getString(R.string.myexperience_base_locdialog_title);
        mTxtTitle.setText(title);
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

    private boolean hasSameAreaName(PLocItemEntity mLocPro,PLocItemEntity mLocCity,PLocItemEntity mLocArea){
        boolean hasSame = false;
        if(mList != null){
            for(BLocEntity bLocEntity : mList){
                if(bLocEntity != null && !bLocEntity.isFake){
                    if(bLocEntity.mLocArea.name.equals(mLocArea.name)){
                        hasSame = true;
                        break;
                    }
                }
            }
        }
        return hasSame;
    }

    private boolean isJustFake(){
        if(mList != null && mList.size() == 1){
            BLocEntity bEntity = mList.get(0);
            if(bEntity.isFake){
                return true;
            }
        }
        return false;
    }

    public boolean removeEntity(BLocEntity mEntity){
        boolean isSucc = false;
        mList.remove(mEntity);
        catView.setLocInfo(mList,mCatViewListener);
        if(isJustFake()){
            mList.clear();
            catView.setVisibility(View.GONE);
            mTxtContent.setVisibility(View.VISIBLE);
        }
        return isSucc;
    }

    public boolean appendLocEntity(PLocItemEntity mLocPro,PLocItemEntity mLocCity,PLocItemEntity mLocArea){
        boolean succ = false;
        if(hasSameAreaName(mLocPro,mLocCity,mLocArea)){
            succ = false;
        }else{
            catView.setVisibility(View.VISIBLE);
            mTxtContent.setVisibility(View.GONE);

            BLocEntity bEntity = new BLocEntity();
            bEntity.mLocPro = mLocPro;
            bEntity.mLocCity = mLocCity;
            bEntity.mLocArea = mLocArea;
            mList.add(bEntity);
            catView.setLocInfo(mList,mCatViewListener);

            succ = true;
        }
        return succ;
    }

    private IItemListener mCatViewListener = new IItemListener() {
        @Override
        public void onItemClick(Object obj, int tag) {
            if(mListener != null){
                mListener.onItemClick(obj,SRC_DEL);
            }
        }
    };

    public void setList(List<String> sList){
        if(sList != null){
            catView.setVisibility(View.VISIBLE);
            mTxtContent.setVisibility(View.GONE);
            for(int i = 0;i < sList.size();i++){
                String strInfo = sList.get(i);
                BLocEntity bEntity = new BLocEntity();
                bEntity.mLocPro = new PLocItemEntity();
                bEntity.mLocCity = new PLocItemEntity();
                bEntity.mLocArea = new PLocItemEntity();
                bEntity.mLocArea.name = strInfo;
                mList.add(bEntity);
            }
            catView.setLocInfo(mList,mCatViewListener);
        }
    }

    public void reSetLocList(List<EmployArea.BLocEntity> mList){
        if(mList != null && mList.size() > 0){
            this.mList = mList;
            catView.setVisibility(View.VISIBLE);
            mTxtContent.setVisibility(View.GONE);
            catView.setLocInfo(mList,mCatViewListener);
        }
    }

    public List<BLocEntity> getLocList() {
        return this.mList;
    }

    public static class BLocEntity implements Serializable{
        public PLocItemEntity mLocPro;
        public PLocItemEntity mLocCity;
        public PLocItemEntity mLocArea;
        public boolean isFake;
    }
}
