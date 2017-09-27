package com.bjzt.uye.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import com.bjzt.uye.views.fragments.BaseFragment;
import com.common.common.MyLog;
import java.util.ArrayList;

/**
 * Created by billy on 2017/9/21.
 */

public class TabsAdapter extends FragmentPagerAdapter implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private ArrayList<TabInfo> mTabs = new ArrayList<>();
    private FragmentManager fm;

    static final class TabInfo {
        private final String tag;
        private final Class<?> clazz;
        private final Bundle args;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clazz = _class;
            args = _args;
        }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        fm = activity.getSupportFragmentManager();
        mContext = activity;
        mTabHost = tabHost;
        mViewPager = pager;
        mTabHost.setOnTabChangedListener(this);
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(this);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clazz, Bundle args) {
        tabSpec.setContent(new DummyTabFactory(mContext));
        String tag = tabSpec.getTag();

        TabInfo info = new TabInfo(tag, clazz, args);
        mTabs.add(info);
        mTabHost.addTab(tabSpec);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        MyLog.d(TAG,"[getItem]"+ "clazz:"+ info.clazz.getName());
        return Fragment.instantiate(mContext, info.clazz.getName(), info.args);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void onTabChanged(String tabId) {
        int position = mTabHost.getCurrentTab();
        mViewPager.setCurrentItem(position,false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        TabWidget widget = mTabHost.getTabWidget();
        int oldFocusability = widget.getDescendantFocusability();
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);
        widget.setDescendantFocusability(oldFocusability);

        int viewId = mViewPager.getId();
        String tag = makeFragmentName(viewId, position);

        MyLog.d(TAG, "[onPageSelected]" + tag);

        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment != null) {
            if (fragment instanceof BaseFragment) {
                ((BaseFragment) fragment).fragmentSelected();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
