package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.bjzt.uye.R;
import com.common.listener.NoConfusion;
import com.tencent.smtt.sdk.WebView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/11/2.
 */

public class OrgDetailCourseInfoView extends RelativeLayout implements NoConfusion{

    @BindView(R.id.webview)
    WebView mWebView;

    public OrgDetailCourseInfoView(Context context) {
        super(context);
        init();
    }

    public OrgDetailCourseInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.org_detail_course_info_layout,this,true);
        ButterKnife.bind(this);

    }

    public void setOrgInfo(String url){
        mWebView.loadUrl(url);
    }
}
