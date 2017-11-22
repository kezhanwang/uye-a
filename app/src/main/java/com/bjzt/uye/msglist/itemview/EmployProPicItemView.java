package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.common.msglist.base.BaseItemView;
import com.common.util.DeviceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/11/22.
 */

public class EmployProPicItemView extends BaseItemView<String>{
    private String mUrl;
    @BindView(R.id.rela_main)
    RelativeLayout mRelaMain;
    @BindView(R.id.imgview)
    ImageView imgView;


    public EmployProPicItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(String s) {
        this.mUrl = s;
        PicController.getInstance().showPicRect(imgView,this.mUrl);
    }

    @Override
    public String getMsg() {
        return this.mUrl;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.employpro_pic_itemview_layout,this,true);
        ButterKnife.bind(this);

        int picWidth = (int) getResources().getDimension(R.dimen.employ_pro_img_width);
        RelativeLayout.LayoutParams llp = (LayoutParams) imgView.getLayoutParams();
        llp.width = picWidth;
        llp.height = picWidth;
        imgView.setLayoutParams(llp);
    }
}
