package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

    private View mIconNewTip;

    private TextView mTxtNewMsgCount;//新消息数字

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
        LayoutInflater.from(context).inflate(R.layout.wz_tab_item_content, this);
        mTabNameView = ((TextView) findViewById(R.id.tab_item_content_text));
        mTabIcon = ((ImageView) findViewById(R.id.tab_btn_icon));
        mIconNewTip = findViewById(R.id.new_msg_tips);
        mTxtNewMsgCount = (TextView) findViewById(R.id.new_msg_counter);
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

    public void showNewTip(boolean isShow) {
        if (mIconNewTip != null) {
            if (isShow) {
                mIconNewTip.setVisibility(VISIBLE);
            } else {
                mIconNewTip.setVisibility(GONE);
            }
        }
    }

    /**
     * 消息栏中的新消息数字
     *
     * @param count
     */
    public void setTxtNewMsgCount(int count) {
        if (count <= 0) {
            mTxtNewMsgCount.setVisibility(GONE);
            return;
        } else {
            mTxtNewMsgCount.setVisibility(VISIBLE);
        }

        String strCount;
        if (count > 99) {
            strCount = "99+";
        } else {
            strCount = count + "";
        }

        mTxtNewMsgCount.setText(strCount);
    }
}
