package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjzt.uye.R;

/**
 * Created by billy on 2017/9/21.
 */

public class MainTabView extends FrameLayout {
    private TextView mTabNameView;
    private ImageView mTabIcon;

    public MainTabView(Context context) {
        this(context, null);
    }

    public MainTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
        init();
    }

    private void initView(Context context) {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.tab_item_content, this);
        mTabNameView = ((TextView) findViewById(R.id.tab_item_content_text));
        mTabIcon = ((ImageView) findViewById(R.id.tab_btn_icon));
    }

    private void init() {

    }

    public void setTabName(String tabName) {
        if (mTabNameView != null) {
            mTabNameView.setText(tabName);
        }
    }

    public void setIcon(int resId) {
        if (mTabIcon != null) {
            mTabIcon.setImageResource(resId);
        }
    }

}
