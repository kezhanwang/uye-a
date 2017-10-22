package com.bjzt.uye.photo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.common.listener.NoConfusion;

/**
 * Created by billy on 2017/10/22.
 */

public class LoadingView extends RelativeLayout implements NoConfusion {

    private TextView txtView;
    private ImageView imgView;

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public LoadingView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.loan_dialog_loading,this,true);
        imgView = (ImageView) mView.findViewById(R.id.loading_img);
        txtView = (TextView) mView.findViewById(R.id.load_tips);
    }

    /***
     * 展示转菊花
     * @param str
     */
    public void showLoading(String str){
        Animation ani = AnimationUtils.loadAnimation(getContext(),R.anim.loading_anim_rotate);
        ani.setRepeatCount(Animation.INFINITE);
        imgView.startAnimation(ani);
        txtView.setText(str);
        int cWhite = getResources().getColor(R.color.common_white);
        txtView.setTextColor(cWhite);
        txtView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        imgView.clearAnimation();
    }
}
