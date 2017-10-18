package com.bjzt.uye.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import com.bjzt.uye.R;
import com.bjzt.uye.adapter.BankListAdapter;
import com.bjzt.uye.entity.PBankEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.msglist.base.BaseListAdapter;
import com.common.util.DeviceUtil;
import java.util.List;

/**
 * Created by diaosi on 2017/2/27.
 * 银行列表dialog
 */
public class DialogBankList extends Dialog {
    private ListView mListView;
    private BankListAdapter mAdapter;
    private IItemListener mItemListener;

    public DialogBankList(Context context, int theme) {
        super(context, theme);
        init();
    }

    private void init(){
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.loan_popwin_animation);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.cash_dialog_banklist_layout,null);

        this.mListView = (ListView) mView.findViewById(R.id.dialog_bank_listview);
        int mWidth = DeviceUtil.mWidth;
        int mHeight = (int) getContext().getResources().getDimension(R.dimen.cash_bind_card_dialog_height);
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(mWidth, mHeight);
        setContentView(mView, llp);
        this.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void setInfo(List<PBankEntity> mList){
        if(mAdapter == null){
            this.mAdapter = new BankListAdapter(mList);
            this.mAdapter.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
            this.mAdapter.setIItemListener(this.mItemListener);
            mListView.setAdapter(this.mAdapter);
        }else{
            this.mAdapter.reSetList(mList);
        }
    }

    public void setIItemListener(IItemListener mItemListener){
        this.mItemListener = mItemListener;
    }
}
