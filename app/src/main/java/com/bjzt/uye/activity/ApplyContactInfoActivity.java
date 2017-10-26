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
 * 联系信息
 * Created by billy on 2017/10/26.
 */
public class ApplyContactInfoActivity extends BaseActivity implements  View.OnClickListener{

    @BindView(R.id.header)
    YHeaderView mHeader;
    @BindView(R.id.scrollview)
    ScrollView mScrollView;

    @BindView(R.id.item_addr)
    ItemView mItemAddr;
    @BindView(R.id.item_addr_detail)
    ItemView mItemAddrDetail;
    @BindView(R.id.item_marriage)
    ItemView mItemMarriage;
    @BindView(R.id.item_email)
    ItemView mItemMail;
    @BindView(R.id.item_wechat)
    ItemView mItemWeChat;
    @BindView(R.id.item_qq)
    ItemView mItemQQ;
    @BindView(R.id.item_sec_rela)
    ItemView mItemSecRela;              //第二联系人关系
    @BindView(R.id.item_sec_name)
    ItemView mItemSecName;
    @BindView(R.id.item_sec_phone)
    ItemView mItemSecPhone;
    @BindView(R.id.btn_ok)
    Button btnOk;

    @BindView(R.id.emptyview)
    BlankEmptyView mEmptyView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_contactinfo_layout;
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
        String title = getResources().getString(R.string.contact_info_title);
        mHeader.setTitle(title);

        title = getResources().getString(R.string.contact_info_addr);
        String hint = getResources().getString(R.string.contact_info_addr_hint);
        mItemAddr.setTitle(title);
        mItemAddr.setHint(hint);
        mItemAddr.setEditAble(false);
        mItemAddr.showArrow();

        hint = getResources().getString(R.string.contact_info_addr_detail_hint);
        mItemAddrDetail.hideTitle();
        mItemAddrDetail.setHint(hint);

        title = getResources().getString(R.string.contact_info_marriage);
        hint = getResources().getString(R.string.contact_info_marriage_hint);
        mItemMarriage.setTitle(title);
        mItemMarriage.setHint(hint);
        mItemMarriage.setEditAble(false);
        mItemMarriage.showArrow();

        title = getResources().getString(R.string.contact_info_email);
        hint = getResources().getString(R.string.contact_info_email_hint);
        mItemMail.setTitle(title);
        mItemMail.setHint(hint);

        title = getResources().getString(R.string.contact_info_wechat);
        hint = getResources().getString(R.string.contact_info_wechat_hint);
        mItemWeChat.setTitle(title);
        mItemWeChat.setHint(hint);

        title = getResources().getString(R.string.contact_info_qq);
        hint = getResources().getString(R.string.contact_info_qq_hint);
        mItemQQ.setTitle(title);
        mItemQQ.setHint(hint);
        mItemQQ.setInputTypeNumber(ItemView.INPUT_TYPE_NUMBER);

        title = getResources().getString(R.string.contact_info_sec_rela);
        hint = getResources().getString(R.string.contact_info_sec_hint);
        mItemSecRela.setTitle(title);
        mItemSecRela.setHint(hint);
        mItemSecRela.setEditAble(false);
        mItemSecRela.showArrow();

        title = getResources().getString(R.string.name);
        hint = getResources().getString(R.string.contact_info_sec_name_hint);
        mItemSecName.setTitle(title);
        mItemSecName.setHint(hint);

        title = getResources().getString(R.string.contact_info_sec_phone);
        hint = getResources().getString(R.string.contact_info_sec_phone_hint);
        mItemSecPhone.setTitle(title);
        mItemSecPhone.setHint(hint);
        mItemSecPhone.setInputTypeNumber(ItemView.INPUT_TYPE_NUMBER);

        btnOk.setOnClickListener(this);

        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    protected void initExtras(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }
}
