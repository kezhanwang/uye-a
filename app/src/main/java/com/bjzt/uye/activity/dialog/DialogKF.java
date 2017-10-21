package com.bjzt.uye.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.common.util.DeviceUtil;

/**
 * 客服对话框
 * Created by billy on 2017/10/20
 */
public class DialogKF extends Dialog implements View.OnClickListener{
    public static final int TYPE_KF = 1;	//修改头衔类型

    private Activity mActivity;
    private TextView textView_dialog_modify_item1;
    private TextView textView_dialog_modify_item2;
    private TextView textView_dialog_modify_item3;
    private TextView textView_dialog_modify_cancel;
    private String str1;
    private String str2;
    private String str3;
    private boolean isGone;		//是否显示item3
    private int color;
    private DialogPicSelect.DialogClickListener mListener;
    private int txtSize = 0;

    public DialogKF(Context context, int themeResId) {
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

    //选择类型
    public void updateType(int mType) {
        Context mContext = Global.getContext();
        switch (mType) {
            case TYPE_KF:
                this.color = mContext.getResources().getColor(R.color.common_grey);
                str1 = mContext.getResources().getString(R.string.myinfo_kf_title);
                str3 = mContext.getResources().getString(R.string.cancle);
                txtSize = (int)mContext.getResources().getDimension(R.dimen.myinfo_textSize_small);
                isGone = true;
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.dialog_kf, null);

        textView_dialog_modify_item1 = (TextView) mView.findViewById(R.id.textView_dialog_modify_item1);
        textView_dialog_modify_item1.setText(str1);
        textView_dialog_modify_item1.setTextColor(color);
        if(txtSize != 0){
            textView_dialog_modify_item1.setTextSize(TypedValue.COMPLEX_UNIT_PX,txtSize);
            textView_dialog_modify_item1.setBackgroundResource(R.drawable.flot_but_1);
        }
        textView_dialog_modify_item1.setOnClickListener(this);

        textView_dialog_modify_item2 = (TextView) mView.findViewById(R.id.textView_dialog_modify_item2);
        textView_dialog_modify_item2.setText(str2);
        textView_dialog_modify_item2.setOnClickListener(this);

        textView_dialog_modify_item3=(TextView) mView.findViewById(R.id.textView_dialog_modify_item3);
        textView_dialog_modify_item3.setText(str3);
        textView_dialog_modify_item3.setOnClickListener(this);
        if(isGone){
            //如果item3不存在，则item2背景为bottom
            textView_dialog_modify_item3.setVisibility(View.GONE);
            textView_dialog_modify_item2.setBackgroundResource(R.drawable.menu_btn_item_bottom_selector);
        }

        textView_dialog_modify_cancel = (TextView) mView.findViewById(R.id.textView_dialog_modify_cancel);
        textView_dialog_modify_cancel.setOnClickListener(this);

        int mWidth = DeviceUtil.mWidth;
        mWidth = mWidth - 50;
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(mWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(mView,llp);
        this.getWindow().setGravity(Gravity.BOTTOM);
    }


    public void setDialogClickListener(DialogPicSelect.DialogClickListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View view) {
        if(view == textView_dialog_modify_item1){
            if(mListener!=null){
                mListener.ItemTopClick();
            }
            dismiss();
        }else if(view == textView_dialog_modify_item2){
            if(mListener!=null){
                mListener.ItemMiddleClick();
            }
            dismiss();
        }else if(view ==textView_dialog_modify_item3){
            if(mListener!=null){
                mListener.ItemBottomClick();
            }
            dismiss();
        }else if(view == textView_dialog_modify_cancel){
            dismiss();
        }
    }

    public void setTel(String tel1,String tel2){
        str2=tel1;
        str3=tel2;
    }
}
