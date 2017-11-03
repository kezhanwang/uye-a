package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/11/3.
 */

public class ProfileItemView extends RelativeLayout implements View.OnClickListener,NoConfusion{

    @BindView(R.id.rela_main)
    RelativeLayout mRelaMain;
    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    @BindView(R.id.txt_tail)
    TextView mTxtTail;

    private IItemListener mListener;

    public ProfileItemView(Context context) {
        super(context);
        init();
    }

    public ProfileItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.profile_itemview_layout,this,true);
        ButterKnife.bind(this);

        mRelaMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.mRelaMain){
                this.mListener.onItemClick(this,-1);
            }
        }
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    public void setTitle(String strTitle){
        mTxtTitle.setText(strTitle);
    }

    public void setTail(String strTail){
        mTxtTail.setText(strTail);
    }
}
