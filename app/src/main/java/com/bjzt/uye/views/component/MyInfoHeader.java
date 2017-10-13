package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.IHeaderListener;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/13.
 */

public class MyInfoHeader extends RelativeLayout implements NoConfusion{

    @BindView(R.id.title)
    TextView mTxtTitle;
    @BindView(R.id.rela_right)
    RelativeLayout relaRight;

    private IHeaderListener mListener;

    public MyInfoHeader(Context context) {
        super(context);
        init();
    }

    public MyInfoHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.myinfo_header_layout,this,true);
        ButterKnife.bind(this);

        relaRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.onRightClick();
                }
            }
        });

        String str = getResources().getString(R.string.myinfo_title);
        mTxtTitle.setText(str);
    }

    public void setIListener(IHeaderListener mListener){
        this.mListener = mListener;
    }
}
