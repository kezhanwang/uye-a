package com.bjzt.uye.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.common.util.DeviceUtil;

/**
 * Created by billy on 2017/10/18.
 */

public class DialogPicSelect extends Dialog implements  View.OnClickListener{

    public static final int TYPE_ID_CARD = 1;//身份证
    public static final int TYPE_BANK_CARD = 2;//银行卡
    public static final int TYPE_ID_CARD_BACK = 3;//身份证件背面
    public static final int TYPE_HOLD = 4;    //手持身份证照
    public static final int TYPE_PROTOCAL = 5;  //协议照
    public static final int TYPE_EMPLOY_TRACKS = 6; //足迹

    private ImageView img_loanDialog;
    private TextView textView_loanDialog_item1;
    private TextView textView_loanDialog_item2;
    private TextView textView_loanDialog_cancel;
    private Drawable mDrawable;
    private DialogClickListener mListener;

    public DialogPicSelect(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.loan_popwin_animation);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.dialog_picselect_layout, null);
        img_loanDialog=(ImageView) mView.findViewById(R.id.img_loanDialog);
        textView_loanDialog_item1=(TextView) mView.findViewById(R.id.textView_loanDialog_item1);
        textView_loanDialog_item1.setOnClickListener(this);
        textView_loanDialog_item2=(TextView) mView.findViewById(R.id.textView_loanDialog_item2);
        textView_loanDialog_item2.setOnClickListener(this);
        textView_loanDialog_cancel=(TextView) mView.findViewById(R.id.textView_loanDialog_cancel);
        textView_loanDialog_cancel.setOnClickListener(this);
        if(mDrawable!=null){
            img_loanDialog.setBackgroundDrawable(mDrawable);
        }

        int mWidth = DeviceUtil.mWidth;
        mWidth = mWidth - 50;
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(mWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(mView,llp);
        this.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void setListener(DialogClickListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View view) {
        if(mListener!=null){
            if(view==textView_loanDialog_item1){
                mListener.ItemTopClick();
                dismiss();
            }else if(view==textView_loanDialog_item2){
                mListener.ItemMiddleClick();
                dismiss();
            }else if(view==textView_loanDialog_cancel){
                dismiss();
            }
        }
    }

    public void updateType(int mType) {
        Context mContext = Global.getContext();
        switch (mType) {
            case TYPE_ID_CARD:
                mDrawable= mContext.getResources().getDrawable(R.drawable.dk_tc_pic01);
                break;
            case TYPE_ID_CARD_BACK:
                mDrawable = mContext.getResources().getDrawable(R.drawable.dk_tc_pic01_back);
                break;
            case TYPE_BANK_CARD:
                mDrawable = mContext.getResources().getDrawable(R.drawable.dk_tc_pic02);
                break;
            case TYPE_HOLD:
                mDrawable = mContext.getResources().getDrawable(R.drawable.loan_dk_tc_pic_hold);
                break;
            case TYPE_PROTOCAL:
                mDrawable = mContext.getResources().getDrawable(R.drawable.loan_dk_tc_pro);
                break;
            default:
                break;
        }
    }


    public static abstract class DialogClickListener{
        //上侧item点击
        public void ItemTopClick(){};
        //中间Item点击
        public void ItemMiddleClick(){};
        //底部item点击
        public void ItemBottomClick(){};
    }
}
