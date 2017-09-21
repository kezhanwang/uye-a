package com.bjzt.uye.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.adapter.TabsAdapter;
import com.bjzt.uye.fragments.FragmentMain;
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
    private MainTabView mViewCommunity;
    private MainTabView mViewDiscovery;
    private MainTabView mViewMine;
    private ArrayList<String> curTabNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initLayout();
    }

    private void initLayout(){
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
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

    private void createTabView(String tabName){
        if(tabName.equals(getString(R.string.tab_msg))){
            mViewMsg = new MainTabView(getApplicationContext());
            mViewMsg.setTabName(tabName);
//            mViewMsg.setIcon(R.drawable.wz_tab_icon_msg_selector);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabName).setIndicator(mViewMsg),FragmentMain.class, null);
            curTabNames.add(tabName);
        }else if(tabName.equals(getString(R.string.tab_discover))){
            mViewMsg = new MainTabView(getApplicationContext());
            mViewMsg.setTabName(tabName);
//            mViewMsg.setIcon(R.drawable.wz_tab_icon_msg_selector);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabName).setIndicator(mViewMsg),FragmentMain.class, null);
            curTabNames.add(tabName);
        }else if(tabName.equals(getString(R.string.tab_scene))){
            mViewMsg = new MainTabView(getApplicationContext());
            mViewMsg.setTabName(tabName);
//            mViewMsg.setIcon(R.drawable.wz_tab_icon_msg_selector);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabName).setIndicator(mViewMsg),FragmentMain.class, null);
            curTabNames.add(tabName);
        }else if(tabName.equals(getString(R.string.tab_mine))){
            mViewMsg = new MainTabView(getApplicationContext());
            mViewMsg.setTabName(tabName);
//            mViewMsg.setIcon(R.drawable.wz_tab_icon_msg_selector);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabName).setIndicator(mViewMsg),FragmentMain.class, null);
            curTabNames.add(tabName);
        }
    }
}
