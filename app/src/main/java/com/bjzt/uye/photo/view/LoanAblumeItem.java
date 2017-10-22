package com.bjzt.uye.photo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.photo.entity.LoanVAblumItemEntity;
import com.bjzt.uye.photo.listener.LoanIAblumBtnListener;
import com.bjzt.uye.photo.pic.LoanPicListViewManager;
import com.common.listener.NoConfusion;
import com.common.util.DeviceUtil;

/**
 * Created by billy on 2017/10/22.
 */

public class LoanAblumeItem extends RelativeLayout implements NoConfusion,View.OnClickListener {
    private RelativeLayout relaMain;
    private ImageView imgView;
    private RelativeLayout relaRight;
    private ImageView imgFlag;
    private int pos;
    private LoanIAblumBtnListener mListener;

    public LoanAblumeItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public LoanAblumeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public LoanAblumeItem(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.loan_ablum_item_layout,this,true);

        relaMain = (RelativeLayout) this.findViewById(R.id.rela_main);
        imgView = (ImageView) this.findViewById(R.id.img);
        relaRight = (RelativeLayout) this.findViewById(R.id.rela_right);
        relaRight.setOnClickListener(this);
        imgFlag = (ImageView) this.findViewById(R.id.img_select);
        imgFlag.setOnClickListener(this);
    }

    public void setInfo(boolean isPageStop, int pos, LoanVAblumItemEntity entity, LoanIAblumBtnListener mListener){
        this.pos = pos;
        this.mListener = mListener;
        String url = entity.url;
        //reset imgview
//		GridView.LayoutParams llp = null;
        LayoutParams llp = null;
        int mW = (int) Global.mContext.getResources().getDimension(R.dimen.loan_pic_local_imgview_margin);
        int w = (DeviceUtil.getDeviceWidth() - (4 * mW)) / 3;
        llp = new LayoutParams(w, w);
        relaMain.setLayoutParams(llp);
        if(isPageStop){
            LoanPicListViewManager.getInstance().reqLocalImgOnStop(imgView,url);
        }else{
            imgView.setImageBitmap(null);
            LoanPicListViewManager.getInstance().reqLocalImgOnScroll(imgView,url);
        }
        boolean isSelect = entity.isSelect;
        Drawable d = null;
        if(isSelect){
            d = Global.mContext.getResources().getDrawable(R.drawable.qa_img_select_ed);
        }else{
            d = Global.mContext.getResources().getDrawable(R.drawable.qa_img_select_un);
        }
        imgFlag.setBackgroundDrawable(d);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if(mListener != null){
            if(view == this.relaRight || view == imgFlag){
                mListener.itemClick(pos);
            }
        }
    }
}
