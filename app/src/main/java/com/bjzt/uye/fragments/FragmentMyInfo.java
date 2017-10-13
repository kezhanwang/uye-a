package com.bjzt.uye.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.views.component.MyInfoHeader;
import com.bjzt.uye.fragments.base.BaseFragment;
import com.bjzt.uye.views.component.MyInfoHeaderView;
import com.bjzt.uye.views.component.MyInfoItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人中心
 * Created by billy on 2017/10/13.
 */

public class FragmentMyInfo extends BaseFragment{

    @BindView(R.id.myinfo_header)
    MyInfoHeader mHeader;
    @BindView(R.id.myinfo_headerview)
    MyInfoHeaderView mHeaderView;
    @BindView(R.id.item_clean_cache)
    MyInfoItemView mItemCleanCache;
    @BindView(R.id.item_contact_us)
    MyInfoItemView mItemContactUs;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myinfo_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onRightClick() {

            }
        });

        mHeaderView.setIListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });

        mItemCleanCache.updateType(MyInfoItemView.TYPE_CACHE_CLEAN);
        mItemCleanCache.setIListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });
        mItemCleanCache.showBottomLine(true);

        mItemContactUs.updateType(MyInfoItemView.TYPE_CONTACT_US);
        mItemContactUs.setIListener(new IItemListener() {
            @Override
            public void onItemClick(Object obj, int tag) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });

    }

}
