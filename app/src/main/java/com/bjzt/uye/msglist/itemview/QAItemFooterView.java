package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.BaseItemView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/12/12.
 */

public class QAItemFooterView extends BaseItemView<BaseItemListener> implements View.OnClickListener{
    private BaseItemListener mEntity;

    @BindView(R.id.btn_ok)
    Button btnOk;

    private IItemListener mListener;

    public QAItemFooterView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(BaseItemListener baseItemListener) {
        this.mEntity = baseItemListener;
    }

    @Override
    public BaseItemListener getMsg() {
        return this.mEntity;
    }

    public void setListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.itemview_qa_footerview_layout,this,true);
        ButterKnife.bind(this);

        this.btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.btnOk){
                this.mListener.onItemClick(this,-1);
            }
        }
    }
}
