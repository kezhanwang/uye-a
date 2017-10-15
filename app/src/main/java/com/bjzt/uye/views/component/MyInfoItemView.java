package com.bjzt.uye.views.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/13.
 */

public class MyInfoItemView extends RelativeLayout implements NoConfusion,View.OnClickListener{

    @BindView(R.id.rela_main)
    RelativeLayout mRela;
    @BindView(R.id.img_view)
    ImageView imgIcon;
    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    @BindView(R.id.viewline)
    View mViewLine;

    private int mType;
    public static final int TYPE_CACHE_CLEAN = 1;
    public static final int TYPE_CONTACT_US = 2;
    private IItemListener mListener;

    public MyInfoItemView(Context context) {
        super(context);
        init();
    }

    public MyInfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.myinfo_itemview_layout,this,true);
        ButterKnife.bind(this);

        mRela.setOnClickListener(this);
    }

    public void updateType(int mType){
        this.mType = mType;
        String title = "";
        Drawable d = null;
        switch(this.mType){
            case TYPE_CACHE_CLEAN:
                title = getResources().getString(R.string.myinfo_itemview_cache_clean);
                d = getResources().getDrawable(R.drawable.myinfo_clean_cache);
                break;
            case TYPE_CONTACT_US:
                title = getResources().getString(R.string.myinfo_itemview_contact_us);
                d = getResources().getDrawable(R.drawable.myinfo_contact_us);
                break;
        }
        if(!TextUtils.isEmpty(title)){
            mTxtTitle.setText(title);
        }
        if(d != null){
            imgIcon.setImageDrawable(d);
        }
    }

    public void setIListener(IItemListener mListener){
        this.mListener = mListener;
    }

    public void showBottomLine(boolean isShow){
        if(isShow){
            mViewLine.setVisibility(View.VISIBLE);
        }else{
            mViewLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if(mListener != null){
            if(view == this.mRela){
                mListener.onItemClick(null,-1);
            }
        }
    }
}
