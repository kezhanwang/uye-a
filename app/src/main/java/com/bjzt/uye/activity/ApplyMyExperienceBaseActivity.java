package com.bjzt.uye.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
public class ApplyMyExperienceBaseActivity extends BaseActivity implements  View.OnClickListener{

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
    @BindView(R.id.btn_ok)
    Button btnOk;

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

        title = getResources().getString(R.string.myexperience_base_edu_title);
        String hint = getResources().getString(R.string.myexperience_base_edu_hint);
        mItemViewHighEdu.setTitle(title);
        mItemViewHighEdu.setHint(hint);
        mItemViewHighEdu.setEditAble(false);
        mItemViewHighEdu.showArrow();
        mItemViewHighEdu.setEditTxtBtnListener(mItemClickListener);

        title = getResources().getString(R.string.myexperience_base_occ_title);
        hint = getResources().getString(R.string.myexperience_base_occ_hint);
        mItemViewOcc.setTitle(title);
        mItemViewOcc.setHint(hint);
        mItemViewOcc.setEditAble(false);
        mItemViewOcc.showArrow();
        mItemViewOcc.setEditTxtBtnListener(mItemClickListener);

        title = getResources().getString(R.string.myexperience_base_income_title);
        hint = getResources().getString(R.string.myexperience_base_income_hint);
        mItemViewIncome.setTitle(title);
        mItemViewIncome.setHint(hint);
        mItemViewIncome.setEditAble(false);
        mItemViewIncome.showArrow();
        mItemViewIncome.setEditTxtBtnListener(mItemClickListener);

        title = getResources().getString(R.string.myexperience_base_house_title);
        hint = getResources().getString(R.string.myexperience_base_house_hint);
        mItemViewHouse.setTitle(title);
        mItemViewHouse.setHint(hint);
        mItemViewHouse.setEditAble(false);
        mItemViewHouse.showArrow();
        mItemViewHouse.setEditTxtBtnListener(mItemClickListener);

        btnOk.setOnClickListener(this);
    }

    View.OnClickListener mItemClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void initExtras(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        if(v == this.btnOk){

        }
    }
}
