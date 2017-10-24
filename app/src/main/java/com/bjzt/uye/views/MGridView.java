package com.bjzt.uye.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import com.common.listener.NoConfusion;

/**
 * Created by billy on 2017/10/24
 */
public class MGridView extends GridView implements NoConfusion {

    public MGridView(Context context) {
        super(context);
    }

    public MGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
