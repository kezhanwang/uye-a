package com.bjzt.uye.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.util.DeviceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/23.
 */

public class DialogIDGuide extends Dialog implements View.OnClickListener{

    @BindView(R.id.img_icon)
    ImageView imgLogo;
    @BindView(R.id.btn_know)
    ImageView btnKnow;
    @BindView(R.id.btn_try)
    ImageView btnTry;

    private IItemListener mListener;

    public DialogIDGuide(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.dialog_applyid_layout, null);
        ButterKnife.bind(this,mView);

        btnKnow.setOnClickListener(this);
        btnTry.setOnClickListener(this);

        int width = DeviceUtil.mWidth;
        int height = DeviceUtil.mHeight;
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(width,height);
        setContentView(mView,llp);
    }

    public void setListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            if(v == this.btnTry){
                this.mListener.onItemClick(null,-1);
            }
        }
        dismiss();
    }
}
