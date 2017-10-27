package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/27.
 */

public class ItemViewTitle extends RelativeLayout implements NoConfusion{

    @BindView(R.id.txt_title)
    TextView mTxtTitle;

    public ItemViewTitle(Context context) {
        super(context);
        init();
    }

    public ItemViewTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.item_view_title_layout,this,true);
        ButterKnife.bind(this);
    }

    public void setTitle(String  title){
        mTxtTitle.setText(title);
    }
}
