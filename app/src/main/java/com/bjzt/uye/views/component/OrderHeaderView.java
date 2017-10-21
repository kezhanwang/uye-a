package com.bjzt.uye.views.component;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.entity.POrganizeEntity;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单headerview
 * Created by billy on 2017/10/18
 */
public class OrderHeaderView extends RelativeLayout implements NoConfusion{
    private POrganizeEntity mEntity;

    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_course_name)
    TextView mTxtName;
    @BindView(R.id.txt_cat)
    TextView mTxtCat;
    @BindView(R.id.txt_tips_price)
    TextView mTxtTution;

    public OrderHeaderView(Context context) {
        super(context);
        init();
    }

    public OrderHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.order_headerview_layout,this,true);
        ButterKnife.bind(this);

    }

    public void setInfo(POrganizeEntity pEntity){
        this.mEntity = pEntity;
        String name = this.mEntity.org_name;
        if(!TextUtils.isEmpty(name)){
            mTxtName.setText(name);
        }else{
            mTxtName.setText("");
        }
        String imgUrl = this.mEntity.logo;
        if(!TextUtils.isEmpty(imgUrl)){
            PicController.getInstance().showPic(imgIcon,imgUrl);
        }
        String strCat = this.mEntity.category;
        if(!TextUtils.isEmpty(strCat)){
            mTxtCat.setText(strCat);
        }else{
            mTxtCat.setText("");
        }
        String strTution = this.mEntity.avg_course_price;
        if(!TextUtils.isEmpty(strTution)){
            mTxtTution.setText(strTution);
        }else{
            mTxtTution.setText("");
        }
    }
}