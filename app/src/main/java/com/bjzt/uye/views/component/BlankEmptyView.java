package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.common.listener.NoConfusion;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/16.
 */

public class BlankEmptyView extends RelativeLayout implements NoConfusion{
    private BlankBtnListener mListener;
    private boolean isSucc;
    @BindView(R.id.img_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.neterror_img)
    ImageView netErrorImg;
    @BindView(R.id.neterror_text)
    TextView netErrorTxt;

    public BlankEmptyView(Context context) {
        super(context);
        init();
    }

    public BlankEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.blank_empty_layout,this,true);
        ButterKnife.bind(this);

        netErrorImg.setOnClickListener(mViewListener);
        netErrorTxt.setOnClickListener(mViewListener);
    }


    public static abstract class BlankBtnListener{
        public void btnRefresh(){};
    }

    private OnClickListener mViewListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if(mListener != null){
                mListener.btnRefresh();
            }
        }
    };

    public void setErrorTips(String str){
        netErrorTxt.setText(str);
    }

    public void showLoadingState() {
        if (netErrorTxt != null) netErrorTxt.setVisibility(View.GONE);
        if (netErrorImg != null) netErrorImg.setVisibility(View.GONE);

        if (mProgressBar != null) mProgressBar.setVisibility(View.VISIBLE);
    }

    public void showErrorState(){
        if(isSucc){
            return;
        }
        mProgressBar.setVisibility(View.GONE);

        netErrorTxt.setVisibility(View.VISIBLE);
        netErrorImg.setVisibility(View.VISIBLE);
    }

    public void reSetState(){
        isSucc = false;
    }

    public boolean isSucc(){
        return isSucc;
    }

    /***
     * 加载成功状态
     */
    public void loadSucc(){
        isSucc = true;
        setVisibility(View.GONE);
    }

    public void setBlankListener(BlankBtnListener mListener){
        this.mListener = mListener;
    }


    private void recyle(){
        mListener = null;
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
        recyle();
    }

}
