package com.bjzt.uye.activity;

import android.os.Bundle;
import android.view.View;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.YHeaderView;
import butterknife.BindView;

/**
 * Created by billy on 2017/10/30.
 * 个人经历-基本信息
 */
public class ApplyMyExperienceBaseActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;

    @BindView(R.id.item_high_edu)
    ItemView mItemViewHighEdu;
    @BindView(R.id.item_occ)
    ItemView mItemViewOcc;
    @BindView(R.id.item_income)
    ItemView mItemViewIncome;
    @BindView(R.id.item_house)
    ItemView mItemViewHouse;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_myexperience_layout;
    }

    @Override
    protected void initLayout(Bundle bundle) {
        mHeader.updateType(YHeaderView.TYPE_ABOUT);
        mHeader.setIListener(new IHeaderListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });
        String title = getResources().getString(R.string.myexperience_base_title);
        mHeader.setTitle(title);

        title = "最高学历";
        String hint = "请选择您的学历";
        mItemViewHighEdu.setTitle(title);
        mItemViewHighEdu.setHint(hint);
        mItemViewHighEdu.setEditAble(false);
        mItemViewHighEdu.showArrow();
        mItemViewHighEdu.setEditTxtBtnListener(mItemClickListener);

        title = "职业";
        hint = "请选择您的职业";
        mItemViewOcc.setTitle(title);
        mItemViewOcc.setHint(hint);
        mItemViewOcc.setEditAble(false);
        mItemViewOcc.showArrow();
        mItemViewOcc.setEditTxtBtnListener(mItemClickListener);

        title = "月收入";
        hint = "请选择您的月收入";
        mItemViewIncome.setTitle(title);
        mItemViewIncome.setHint(hint);
        mItemViewIncome.setEditAble(false);
        mItemViewIncome.showArrow();
        mItemViewIncome.setEditTxtBtnListener(mItemClickListener);

        title = "住房情况";
        hint = "请选择您的住房情况";
        mItemViewHouse.setTitle(title);
        mItemViewHouse.setHint(hint);
        mItemViewHouse.setEditAble(false);
        mItemViewHouse.showArrow();
        mItemViewHouse.setEditTxtBtnListener(mItemClickListener);
    }

    View.OnClickListener mItemClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void initExtras(Bundle bundle) {

    }
}
