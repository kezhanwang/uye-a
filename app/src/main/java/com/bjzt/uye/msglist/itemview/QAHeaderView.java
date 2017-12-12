package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.view.LayoutInflater;

import com.common.listener.NoConfusion;
import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.BaseItemView;

/**
 * Created by billy on 2017/12/12
 */
public class QAHeaderView extends BaseItemView<BaseItemListener> implements NoConfusion{
    private BaseItemListener mEntity;

    public QAHeaderView(Context context) {
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

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
}
