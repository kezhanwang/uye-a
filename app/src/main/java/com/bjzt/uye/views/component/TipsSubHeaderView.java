package com.bjzt.uye.views.component;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/21.
 */

public class TipsSubHeaderView extends RelativeLayout implements NoConfusion{

    @BindView(R.id.txt_tips)
    TextView mTxtTips;

    public static final int TYPE_QA = 1;
    private int mType;

    public TipsSubHeaderView(Context context) {
        super(context);
        init();
    }

    public TipsSubHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.tips_subheader_layout,this,true);
        ButterKnife.bind(this);
    }

    public void updateType(int mType){
        this.mType = mType;
        String tips = null;
        switch(this.mType){
            case TYPE_QA:
                tips = "请您配置问卷调查，我们会为您提供更好的服务";
                break;
        }
        if(!TextUtils.isEmpty(tips)){
            this.mTxtTips.setText(tips);
        }
    }

}
