package com.bjzt.uye.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjzt.uye.R;
import com.bjzt.uye.fragments.base.BaseFragment;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.YHeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/20.
 */

public class FragmentUYe extends BaseFragment{

    @BindView(R.id.uheader)
    YHeaderView mHeader;

    @BindView(R.id.u_emptyview)
    BlankEmptyView mEmptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uye_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        mHeader.updateType(YHeaderView.TYPE_RIGHT_TXT_ONLY);
        String title = getResources().getString(R.string.tab_uye);
        mHeader.setTitle(title);
        String txtRight = "0/0";
        mHeader.setRightTxt(txtRight);

        mEmptyView.updateType(BlankEmptyView.TYPE_EMPTY_ORDER);
    }
}
