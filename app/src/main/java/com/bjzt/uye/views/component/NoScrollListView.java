package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import com.common.listener.NoConfusion;

/**
 * Created by billy on 2017/10/21.
 */
public class NoScrollListView extends ListView implements NoConfusion{

    public NoScrollListView(Context context) {
        super(context);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
