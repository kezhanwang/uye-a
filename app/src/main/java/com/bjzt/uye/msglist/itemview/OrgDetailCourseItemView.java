package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.dialog.DialogStrNormalList;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.entity.PCourseEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.msglist.base.BaseItemView;
import com.common.util.DeviceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/11/2.
 */

public class OrgDetailCourseItemView extends BaseItemView<PCourseEntity> implements View.OnClickListener{
    private PCourseEntity mEntity;

    @BindView(R.id.rela_main)
    RelativeLayout mRelaMain;
    @BindView(R.id.txt_content)
    TextView mTxtContent;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    private IItemListener mListener;
    private Drawable dBg = null;

    public OrgDetailCourseItemView(Context context) {
        super(context);
        dBg = getResources().getDrawable(R.drawable.rect_grey_shape);
    }

    @Override
    public void setMsg(PCourseEntity pCourseEntity) {
        this.mEntity = pCourseEntity;
        //set title
        String name = this.mEntity.c_name;
        if(!TextUtils.isEmpty(name)){
            mTxtContent.setText(name);
        }else{
            mTxtContent.setText("");
        }
        //set img logo
        String imgUrl = this.mEntity.logo;
        if(!TextUtils.isEmpty(imgUrl)){
            PicController.getInstance().showPicRect(imgLogo,imgUrl);
        }else{
            imgLogo.setImageDrawable(dBg);
        }
    }

    @Override
    public PCourseEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.org_detail_course_itemview_layout,this,true);
        ButterKnife.bind(this);

        int margin = (int) getResources().getDimension(R.dimen.common_margin);
        int offset = margin;
        RelativeLayout.LayoutParams llp = (LayoutParams) mRelaMain.getLayoutParams();
        int width = (DeviceUtil.mWidth / 2) - (int)(1.5*margin);
        llp.width = width;
        llp.height = LayoutParams.WRAP_CONTENT;

        RelativeLayout.LayoutParams llpImg = (LayoutParams) imgLogo.getLayoutParams();
        llpImg.width = width;
        llpImg.height = (width * 3)/4 - offset;
        imgLogo.setLayoutParams(llpImg);

        mRelaMain.setLayoutParams(llp);

        mRelaMain.setOnClickListener(this);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }


    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.mRelaMain){
                this.mListener.onItemClick(null,-1);
            }
        }
    }
}
