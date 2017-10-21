package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.bjzt.uye.R;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/21.
 */

public class InsureOrderItemView extends LinearLayout{

    public InsureOrderItemView(Context context) {
        super(context);
        init();
    }

    public InsureOrderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.insure_order_itemview_layout,this,true);
        ButterKnife.bind(this);

    }

    public void recyle(){

    }
}
