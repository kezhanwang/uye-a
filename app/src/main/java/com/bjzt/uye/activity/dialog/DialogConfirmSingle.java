package com.bjzt.uye.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.util.DeviceUtil;

/**
 * Created by diaosi on 2016/4/26.
 */
public class DialogConfirmSingle extends Dialog implements View.OnClickListener{

    private TextView txtViewContents;
    private TextView txtTitle;
    private Button btnOk;
    private IItemListener mListener;

    public static final int TYPE_CONTACTLIST_CFG = 0x10;

    public int mType;

    public DialogConfirmSingle(Context context, int theme) {
        super(context, theme);
        init();
    }

    public DialogConfirmSingle(Context context) {
        super(context);
        init();
    }

    private void init(){
        Window window = getWindow();
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void setMCancleable(boolean enable) {
        setCancelable(enable);
        setCanceledOnTouchOutside(enable);
    }

    public void setIBtnListener(IItemListener mListener){
        this.mListener = mListener;
    }

    public void updateType(int mType){
        this.mType = mType;
        String btnStr = null;
        String contents = null;
        switch(mType){
            case TYPE_CONTACTLIST_CFG:
                btnStr = getContext().getResources().getString(R.string.dialog_btn_str_okay);
                contents = getContext().getResources().getString(R.string.dialog_contactlist_failure);
                break;
        }

        if(!TextUtils.isEmpty(btnStr)){
            this.btnOk.setText(btnStr);
        }

        if(!TextUtils.isEmpty(contents)){
            txtViewContents.setText(contents);
        }
    }

    public void setContents(String strInfo, String btnStr){
        if(!TextUtils.isEmpty(strInfo)){
            txtViewContents.setText(strInfo);
        }
        if(!TextUtils.isEmpty(btnStr)){
            this.btnOk.setText(btnStr);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.dialog_confirm_single_layout,null);

        txtViewContents = (TextView) mView.findViewById(R.id.txt_tips);
        txtTitle = (TextView) mView.findViewById(R.id.update_title);
        this.btnOk = (Button)mView.findViewById(R.id.btn_ok);
        this.btnOk.setOnClickListener(this);

        int mWidth = DeviceUtil.mWidth;
        int lrMargin = (int) getContext().getResources().getDimension(R.dimen.common_margin);
        mWidth -= 5*lrMargin;
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(mWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(mView,llp);
    }

    @Override
    public void onClick(View view) {
        if(mListener != null){
            if(view == this.btnOk){
                mListener.onItemClick(view,-1);
            }
            dismiss();
        }
    }
}
