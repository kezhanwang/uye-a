package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.entity.VQAItemEntity;
import com.bjzt.uye.listener.IItemListener;
import com.common.msglist.base.BaseItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/21.
 */

public class QAListItemView extends BaseItemView<VQAItemEntity> implements View.OnClickListener{
    private VQAItemEntity mEntity;

    @BindView(R.id.rela_main)
    RelativeLayout mRelaMain;
    @BindView(R.id.txt_alpha)
    TextView mTxtAlpha;
    @BindView(R.id.txt_content)
    TextView mTxtContent;
    @BindView(R.id.img_select)
    ImageView mImgSelect;
    @BindView(R.id.qa_listview_line)
    View mViewLine;

    private IItemListener mListener;
    private Drawable dSelect;
    private Drawable dSelectUn;

    public QAListItemView(Context context) {
        super(context);
        dSelect = getResources().getDrawable(R.drawable.qa_img_select_ed);
        dSelectUn = getResources().getDrawable(R.drawable.qa_img_select_un);
    }

    @Override
    public void setMsg(VQAItemEntity vqaItemEntity) {
        this.mEntity = vqaItemEntity;
        //set alpha
        char c = this.mEntity.c;
        String strC = Character.toString(c);
        mTxtAlpha.setText(strC+".");
        //set content
        String str = this.mEntity.data;
        if(!TextUtils.isEmpty(str)){
            mTxtContent.setText(str);
        }else{
            mTxtContent.setText("");
        }
        //is select
        boolean isSelect = this.mEntity.isSelect;
        if(isSelect){
            this.mImgSelect.setImageDrawable(dSelect);
        }else{
            this.mImgSelect.setImageDrawable(dSelectUn);
        }
        //is bottom
        boolean isBottom = this.mEntity.isBottom;
        int leftMargin;
        if(!isBottom){
            leftMargin = (int) getResources().getDimension(R.dimen.common_margin);
        }else{
            leftMargin =(int) getResources().getDimension(R.dimen.common_margin_2);
        }
        RelativeLayout.LayoutParams llp = (LayoutParams) mViewLine.getLayoutParams();
        llp.leftMargin = leftMargin;
        mViewLine.setLayoutParams(llp);
    }

    @Override
    public VQAItemEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.qa_listitem_layout, this, true);
        ButterKnife.bind(this);

        this.mRelaMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            if(v == this.mRelaMain){
                this.mListener.onItemClick(this,-1);
            }
        }
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }
}
