package com.bjzt.uye.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.adapter.TabsAdapter;
import com.bjzt.uye.fragments.FragmentHome;
import com.bjzt.uye.fragments.FragmentMain;
import com.bjzt.uye.fragments.FragmentMyInfo;
import com.bjzt.uye.views.component.MainTabView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager mViewPager;
    TabHost mTabHost;
    private TabsAdapter mTabsAdapter;
    private MainTabView mViewMsg;
    private MainTabView mViewUYe;
    private MainTabView mViewMine;
    private ArrayList<String> curTabNames = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initLayout(){
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        String[] tabNames = getResources().getStringArray(R.array.main_tab_name);
        for (String tabName : tabNames) {
            createTabView(tabName);
        }
        String[] initPageTabs = getResources().getStringArray(R.array.init_page_tab);
        for (String tabName : initPageTabs) {
            if (curTabNames.contains(tabName)) {
                mTabHost.setCurrentTabByTag(tabName);
                break;
            }
        }
        //缓存几个页面
        mViewPager.setOffscreenPageLimit(4);
    }

    @Override
    protected void initExtras(Bundle bundle) {

    }


    private void createTabView(String tabName){
        if(tabName.equals(getString(R.string.tab_main))){
            mViewMsg = new MainTabView(getApplicationContext());
            mViewMsg.setTabName(tabName);
            mViewMsg.setIcon(R.drawable.tab_main_selector);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabName).setIndicator(mViewMsg),FragmentHome.class, null);
            curTabNames.add(tabName);
        }else if(tabName.equals(getString(R.string.tab_uye))){
            mViewUYe = new MainTabView(getApplicationContext());
            mViewUYe.setTabName(tabName);
            mViewUYe.setIcon(R.drawable.tab_uye_selector);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabName).setIndicator(mViewUYe),FragmentMain.class, null);
            curTabNames.add(tabName);
        }else if(tabName.equals(getString(R.string.tab_mine))){
            mViewMine = new MainTabView(getApplicationContext());
            mViewMine.setTabName(tabName);
            mViewMine.setIcon(R.drawable.tab_myinfo_selector);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabName).setIndicator(mViewMine),FragmentMyInfo.class, null);
            curTabNames.add(tabName);
        }
    }
}
