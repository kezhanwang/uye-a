package com.bjzt.uye.views.component;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.entity.POrganizeEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/27.
 */

public class OrgDetailHeaderView extends RelativeLayout implements NoConfusion, View.OnClickListener{

    @BindView(R.id.rela_header)
    RelativeLayout mRelaMain;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_name)
    TextView mTxtName;
    @BindView(R.id.txt_cat)
    TextView mTxtCat;
    @BindView(R.id.score_view)
    ScoreView mScoreView;

    private IItemListener mListener;

    public static final int SRC_RELA = 1;
    public static final int SRC_BTN = 2;

    private POrganizeEntity mEntity;
    public OrgDetailHeaderView(Context context) {
        super(context);
        init();
    }

    public OrgDetailHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.org_detail_headerview_layout,this,true);
        ButterKnife.bind(this);

        mRelaMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.mRelaMain){
                this.mListener.onItemClick(null,SRC_RELA);
            }
        }
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    public void setInfo(POrganizeEntity pEntity){
        //init img icon
        String logo = pEntity.logo;
        if(!TextUtils.isEmpty(logo)){
            PicController.getInstance().showPic(imgIcon,logo);
        }
        //init course name
        String oName = pEntity.org_name;
        if(!TextUtils.isEmpty(oName)){
            mTxtName.setText(oName);
        }
        //init cat
        String strCat = pEntity.category;
        if(!TextUtils.isEmpty(strCat)){
            mTxtCat.setText(strCat);
        }
    }
}
