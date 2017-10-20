package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.entity.VHomeLocEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;
import com.common.msglist.base.BaseItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/20
 */
public class HomeLocView extends BaseItemView<VHomeLocEntity> implements NoConfusion, View.OnClickListener{

    @BindView(R.id.home_btn_ok)
    Button btnOk;
    @BindView(R.id.home_loc_txt_clickme)
    TextView txtBtnClick;

    private IItemListener mListener;

    public static final int TAG_BTN_SIGN = 1;
    public static final int TAG_TXT_BTN_LOC = 2;

    private VHomeLocEntity mEntity;

    public HomeLocView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(VHomeLocEntity vHomeLocEntity) {
        this.mEntity = vHomeLocEntity;
    }

    @Override
    public VHomeLocEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        init();
    }

    public HomeLocView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.home_loc_layout,this,true);
        ButterKnife.bind(this);

        this.btnOk.setOnClickListener(this);
        this.txtBtnClick.setOnClickListener(this);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            if(v == this.btnOk){
                this.mListener.onItemClick(this,TAG_BTN_SIGN);
            }else if(v == this.txtBtnClick){
                this.mListener.onItemClick(this,TAG_TXT_BTN_LOC);
            }
        }
    }
}
