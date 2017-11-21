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
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.adapter.DStrSelectAdapter;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.views.component.datepicker.view.LoanWheelView;
import com.common.util.DeviceUtil;

import java.util.ArrayList;

/**
 * Created by billy on 2017/11/21
 */
public class DialogStrSelect extends Dialog implements View.OnClickListener{
    private DStrSelectAdapter mAdapter;
    private TextView btnOk;
    private TextView txtTitle;
    private TextView txtCancel;
    private LoanWheelView mWheel;
    private IItemListener mListener;

    private int mType;
    public static final int TYPE_EMPLOY_PRO_ADD = 10;
    public static final int TYPE_SALARY = 11;

    public DialogStrSelect(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.loan_popwin_animation);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void updateType(int mType){
        this.mType = mType;
        String title = "";
        switch(this.mType){
            case TYPE_EMPLOY_PRO_ADD:
                title = "工作状态";
                break;
            case TYPE_SALARY:
                title = "月薪范围";
                break;
        }
        txtTitle.setText(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater li= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView=li.inflate(R.layout.single_select_dialog, null);

        btnOk= (TextView) mView.findViewById(R.id.text_ok);
        btnOk.setOnClickListener(this);
        txtTitle= (TextView) mView.findViewById(R.id.text_title);
        txtCancel= (TextView) mView.findViewById(R.id.text_cancel);
        txtCancel.setOnClickListener(this);

        this.mWheel = (LoanWheelView) mView.findViewById(R.id.wheel_first);
        this.mWheel.setDrawShadows(true);
        int mWidth = DeviceUtil.mWidth;
        int mHeight = (int) getContext().getResources().getDimension(R.dimen.loan_datepicker_height);
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(mWidth, mHeight);
        setContentView(mView, llp);
        this.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.btnOk){
                int index = this.mWheel.getCurrentItem();
                String strInfo = mAdapter.getItemByPos(index);
                this.mListener.onItemClick(strInfo,IItemListener.SRC_BTN_OK);
            }else if(v == this.txtCancel){
                this.mListener.onItemClick(null,IItemListener.SRC_BTN_CANCLE);
            }
        }
    }

    public void setListInfo(ArrayList<String> mList){
        this.mAdapter = new DStrSelectAdapter(getContext(),mList);
        mWheel.setViewAdapter(this.mAdapter);
    }

    public void setSelectInfo(String strInfo){
        int index = -1;
        if(!TextUtils.isEmpty(strInfo) && mAdapter != null){
            index = mAdapter.getIndexByInfo(strInfo);
        }
        if(index >= 0){
            mWheel.setCurrentItem(index);
        }
    }
}
