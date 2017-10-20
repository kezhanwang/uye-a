package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.bjzt.uye.R;
import com.common.listener.NoConfusion;

/**
 * Created by billy on 2017/10/20.
 */

public class DividerItemView extends RelativeLayout implements NoConfusion{

    public DividerItemView(Context context) {
        super(context);
        init();
    }

    public DividerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.divider_itemview_layout,this,true);
    }
}
