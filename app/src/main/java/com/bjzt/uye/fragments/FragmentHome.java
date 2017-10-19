package com.bjzt.uye.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjzt.uye.R;
import com.bjzt.uye.fragments.base.BaseFragment;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.views.component.HomeHeader;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 * Created by billy on 2017/10/19
 */
public class FragmentHome extends BaseFragment{

    @BindView(R.id.header)
    HomeHeader mHeader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        mHeader.setIItemListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });

    }
}
