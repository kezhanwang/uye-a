package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.bjzt.uye.R;
import com.common.listener.NoConfusion;

import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/19.
 */

public class HomeHeaderView extends RelativeLayout implements NoConfusion{

    public HomeHeaderView(Context context) {
        super(context);
        init();
    }

    public HomeHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.home_headerview_layout,this,true);
        ButterKnife.bind(this);

    }
}
