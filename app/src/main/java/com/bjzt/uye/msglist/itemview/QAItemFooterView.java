package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.util.AttributeSet;

import com.common.msglist.base.BaseItemListener;
import com.common.msglist.base.BaseItemView;

/**
 * Created by billy on 2017/12/12.
 */

public class QAItemFooterView extends BaseItemView<BaseItemListener>{
    private BaseItemListener mEntity;

    public QAItemFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    }
}
