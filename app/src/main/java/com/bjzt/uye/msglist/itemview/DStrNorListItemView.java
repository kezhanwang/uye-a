package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.entity.BDialogStrEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.msglist.base.BaseItemView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/26.
 */

public class DStrNorListItemView extends BaseItemView<BDialogStrEntity> implements  View.OnClickListener{

    @BindView(R.id.dialog_rela_main)
    RelativeLayout mRelaMain;
    @BindView(R.id.txtview_name)
    TextView mTxtTitle;
    @BindView(R.id.imgview_select)
    ImageView mImgIcon;

    private BDialogStrEntity mEntity;
    private IItemListener mListener;

    public DStrNorListItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(BDialogStrEntity bDialogStrEntity) {
        this.mEntity = bDialogStrEntity;
        String title = this.mEntity.str;
        if(!TextUtils.isEmpty(title)){
            mTxtTitle.setText(title);
        }
        //set select img flag
        boolean isSelect = this.mEntity.isSelect;
        if(isSelect){
            this.mImgIcon.setVisibility(View.VISIBLE);
        }else{
            this.mImgIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public BDialogStrEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.dialog_str_itemview_layout,this,true);
        ButterKnife.bind(this);

        mRelaMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            if(v == this.mRelaMain){
                mListener.onItemClick(this.mEntity,-1);
            }
        }
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
