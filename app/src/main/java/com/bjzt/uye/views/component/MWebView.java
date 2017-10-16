package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import com.common.listener.NoConfusion;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by billy on 2017/10/16.
 */

public class MWebView extends WebView implements NoConfusion{

    public MWebView(Context context) {
        super(context);
    }

    public MWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

}
