package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.common.listener.NoConfusion;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/15.
 */

public class ItemView extends RelativeLayout implements NoConfusion{

    public ItemView(Context context) {
        super(context);
        init();
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        ButterKnife.bind(this);
    }


}
