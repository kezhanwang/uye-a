package com.bjzt.uye.photo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.photo.listener.LoanIAblumBtnListener;
import com.common.listener.NoConfusion;

/**
 * Created by billy on 2017/10/22.
 */
public class LoanAblumButtonArea  extends RelativeLayout implements NoConfusion,View.OnClickListener {

    private TextView btnOk;
    private RelativeLayout relaScane;
    private LoanIAblumBtnListener mListener;
    private TextView txtLeft;

    public LoanAblumButtonArea(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public LoanAblumButtonArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public LoanAblumButtonArea(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.loan_ablum_button_layout,this,true);

        btnOk = (TextView) this.findViewById(R.id.btn_right);
        btnOk.setOnClickListener(this);
        btnOk.setEnabled(false);

        relaScane = (RelativeLayout) this.findViewById(R.id.rela_left);
        this.relaScane.setOnClickListener(this);

        txtLeft = (TextView) this.findViewById(R.id.txt_left);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if(mListener != null){
            if(view == this.btnOk){
                mListener.btnOk();
            }else if(view == this.relaScane){
                mListener.picScane();
            }
        }
    }

    public void setIAblumBtnListener(LoanIAblumBtnListener mListener){
        this.mListener = mListener;
    }

    public void setBtnEnable(boolean enable){
        int cFont = 0;
        btnOk.setEnabled(enable);
        Drawable dBackGround = getResources().getDrawable(R.drawable.loan_ablum_rela_bg);
        if(enable){
            cFont = getResources().getColor(R.color.loan_ablum_font_color_normal);
            relaScane.setBackgroundDrawable(dBackGround);
            relaScane.setOnClickListener(this);
        }else{
            cFont = getResources().getColor(R.color.loan_ablum_font_color_nor);
            relaScane.setBackgroundDrawable(null);
            relaScane.setOnClickListener(null);
        }
        txtLeft.setTextColor(cFont);
    }

    public void setButtonTxt(int cnt){
        String str = "";
        if(cnt <= 0){
            str = "确定";
        }else{
            str = "确定";
            str += getResources().getString(R.string.loan_pic_scanne_brk,cnt);
        }
        btnOk.setText(str);
    }
}
