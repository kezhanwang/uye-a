package com.bjzt.uye.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.TabHost;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.adapter.TabsAdapter;
import com.bjzt.uye.controller.OtherController;
import com.bjzt.uye.fragments.FragmentHome;
import com.bjzt.uye.fragments.FragmentMain;
import com.bjzt.uye.fragments.FragmentMyInfo;
import com.bjzt.uye.fragments.FragmentUYe;
import com.bjzt.uye.global.Global;
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
    private String KEY_IDNEX = "key_index";

    public static final int REQ_CODE_LOGIN = 0x10;
    public static final int REQ_SEARCH = 0x11;

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

        Global.postDelay(rDelay);
    }

    private Runnable rDelay = new Runnable() {
        @Override
        public void run() {
            //请求客服电话
            OtherController.getInstance().requestKFContactInfo();
        }
    };

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
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabName).setIndicator(mViewUYe),FragmentUYe.class, null);
            curTabNames.add(tabName);
        }else if(tabName.equals(getString(R.string.tab_mine))){
            mViewMine = new MainTabView(getApplicationContext());
            mViewMine.setTabName(tabName);
            mViewMine.setIcon(R.drawable.tab_myinfo_selector);
            mTabsAdapter.addTab(mTabHost.newTabSpec(tabName).setIndicator(mViewMine),FragmentMyInfo.class, null);
            curTabNames.add(tabName);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Global.removeDelay(rDelay);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int index = mViewPager.getCurrentItem();
        outState.putInt(KEY_IDNEX,index);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            int index = savedInstanceState.getInt(KEY_IDNEX);
            mViewPager.setCurrentItem(index);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case REQ_SEARCH:
                    OtherController.getInstance().notifyRefresh();
                    break;
            }
        }
    }
}
