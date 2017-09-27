package com.bjzt.uye.views.fragments;

import android.support.v4.app.Fragment;

import com.common.common.MyLog;

/**
 * Created by billy on 2017/9/21.
 */

public class BaseFragment extends Fragment {
    protected String TAG = this.getClass().getSimpleName();

    /**
     * ViewPager页面选择的，会调用被选中的Fragment 页面；
     */
    public void fragmentSelected() {
        MyLog.d(TAG, "页面被选选中了");
    }
}
