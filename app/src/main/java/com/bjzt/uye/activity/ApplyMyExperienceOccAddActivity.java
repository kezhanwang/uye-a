package com.bjzt.uye.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.YHeaderView;

import butterknife.BindView;

/**
 * Created by billy on 2017/10/30.
 * 个人经历-职业添加
 */
public class ApplyMyExperienceOccAddActivity extends BaseActivity{

    @BindView(R.id.header)
    YHeaderView mHeader;

    @BindView(R.id.item_cpname)
    ItemView mItemViewCpName;
    @BindView(R.id.item_entrytime)
    ItemView mItemViewEntryTime;
    @BindView(R.id.item_turnovertime)
    ItemView mItemViewTurnOverTime;
    @BindView(R.id.item_pos)
    ItemView mItemViewPos;
    @BindView(R.id.item_income)
    ItemView mItemViewIncome;

    @BindView(R.id.btn_ok)
    Button btnOk;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_myexperience_occ_add_layout;
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
        String title = "添加职业";
        mHeader.setTitle(title);

        title = "单位名称";
        String hint = "请输入您的单位名称";
        mItemViewCpName.setTitle(title);
        mItemViewCpName.setHint(hint);

        title = "入职时间";
        hint = "请选择您的入职时间";
        mItemViewEntryTime.setTitle(title);
        mItemViewEntryTime.setHint(hint);
        mItemViewEntryTime.setEditAble(false);
        mItemViewEntryTime.showArrow();
        mItemViewEntryTime.setEditTxtBtnListener(mItemListener);

        title = "离职时间";
        hint = "请选择您的离职时间";
        mItemViewTurnOverTime.setTitle(title);
        mItemViewTurnOverTime.setHint(hint);
        mItemViewTurnOverTime.setEditAble(false);
        mItemViewTurnOverTime.showArrow();
        mItemViewTurnOverTime.setEditTxtBtnListener(mItemListener);

        title = "职位";
        hint = "请选择职位";
        mItemViewPos.setTitle(title);
        mItemViewPos.setHint(hint);
        mItemViewPos.setEditAble(false);
        mItemViewPos.showArrow();
        mItemViewPos.setEditTxtBtnListener(mItemListener);

        title = "薪资";
        hint = "请选择您的薪资范围";
        mItemViewIncome.setTitle(title);
        mItemViewIncome.setHint(hint);
        mItemViewIncome.setEditAble(false);
        mItemViewIncome.showArrow();
        mItemViewIncome.setEditTxtBtnListener(mItemListener);
    }

    private View.OnClickListener mItemListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void initExtras(Bundle bundle) {

    }
}
