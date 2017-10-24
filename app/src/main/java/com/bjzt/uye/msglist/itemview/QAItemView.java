package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.adapter.QAItemAdapter;
import com.bjzt.uye.entity.PQACfgItemEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/21.
 */

public class QAItemView extends BaseItemView<PQACfgItemEntity> implements View.OnClickListener{
    private PQACfgItemEntity mEntity;

    @BindView(R.id.txt_title)
    TextView mTxtTitle;
    @BindView(R.id.txt_question_type)
    TextView mTxtQAType;

    @BindView(R.id.listview)
    ListView mListView;

    private IItemListener mListener;
    private QAItemAdapter mAdapter;

    public QAItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(PQACfgItemEntity pqaCfgItemEntity) {
        this.mEntity = pqaCfgItemEntity;
        //init title
        String title = this.mEntity.question;
        if(!TextUtils.isEmpty(title)){
            mTxtTitle.setText(title);
        }else{
            mTxtTitle.setText("");
        }
        int mType = this.mEntity.type;
        if(mType == PQACfgItemEntity.TYPE_SINGLE){
            mTxtQAType.setVisibility(View.GONE);
        }else{
            mTxtQAType.setVisibility(View.VISIBLE);
        }
        //init adapter
        if(mAdapter == null){
            mAdapter = new QAItemAdapter(this.mEntity.vAnswer);
            mAdapter.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
            mAdapter.setIItemListener(this.mListener);
            mListView.setAdapter(mAdapter);
        }else{
            mAdapter.reSetList(this.mEntity.vAnswer);
        }
    }

    @Override
    public PQACfgItemEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.qa_itemview_layout,this,true);
        ButterKnife.bind(this);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {

    }
}
