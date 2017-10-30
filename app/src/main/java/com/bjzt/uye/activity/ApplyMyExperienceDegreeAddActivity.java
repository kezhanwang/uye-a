package com.bjzt.uye.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import com.bjzt.uye.R;
import com.bjzt.uye.activity.base.BaseActivity;
import com.bjzt.uye.listener.IHeaderListener;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.bjzt.uye.views.component.ItemView;
import com.bjzt.uye.views.component.YHeaderView;
import butterknife.BindView;

/**
 * Created by billy on 2017/10/30.
 */

public class ApplyMyExperienceDegreeAddActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;
    @BindView(R.id.item_degree)
    ItemView mItemViewDegree;
    @BindView(R.id.item_schname)
    ItemView mItemViewSchName;
    @BindView(R.id.item_schaddr)
    ItemView mItemViewSchAddr;
    @BindView(R.id.item_major)
    ItemView mItemViewMajor;
    @BindView(R.id.item_entrytime)
    ItemView mItemViewEntryTime;
    @BindView(R.id.item_graduation)
    ItemView mItemViewGraduTime;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_myexperience_degree_add_layout;
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
        String title = "添加学历";
        mHeader.setTitle(title);

        //degree
        title = "学历";
        String hint = "请选择您的学历";
        mItemViewDegree.setTitle(title);
        mItemViewDegree.setHint(hint);
        mItemViewDegree.setEditAble(false);
        mItemViewDegree.showArrow();

        //sch name
        title = "学校名称";
        hint = "请输入您的学校名称";
        mItemViewSchName.setTitle(title);
        mItemViewSchName.setHint(hint);

        //sch addr
        title = "学校地址";
        hint = "请输入地址，精确到门牌号";
        mItemViewSchAddr.setTitle(title);
        mItemViewSchAddr.setHint(hint);

        //major
        title = "专业";
        hint = "请输入您的专业名称";
        mItemViewMajor.setTitle(title);
        mItemViewMajor.setHint(hint);

        //entry time
        title = "入学时间";
        hint = "请选择入学时间";
        mItemViewEntryTime.setTitle(title);
        mItemViewEntryTime.setHint(hint);
        mItemViewEntryTime.setEditAble(false);
        mItemViewEntryTime.showArrow();

        //graduate time
        title = "毕业时间";
        hint = "请选择您的毕业时间";
        mItemViewGraduTime.setTitle(title);
        mItemViewGraduTime.setHint(hint);
        mItemViewGraduTime.setEditAble(false);
        mItemViewGraduTime.showArrow();

        btnOk.setOnClickListener(this);
    }

    @Override
    protected void initExtras(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        if(v == this.btnOk){

        }
    }
}
