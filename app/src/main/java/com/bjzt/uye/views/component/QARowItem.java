package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;
import java.util.List;

/**
 * Created by billy on 2017/10/17
 */
public class QARowItem extends RelativeLayout implements NoConfusion,View.OnClickListener {
    private TextView[] txtArray;
    private List<String> mList;
    private int mRowIndex;
    private int selectIndex = -1;
    private int mIndex;

    private IItemListener mListener;

    public QARowItem(Context context) {
        super(context);
        init();
    }

    public QARowItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.qa_cat_rowitem_layout, this, true);

        TextView txtFirst = (TextView) this.findViewById(R.id.txt_first);
        TextView txtSecond = (TextView) this.findViewById(R.id.txt_second);
        TextView txtThird = (TextView) this.findViewById(R.id.txt_third);
        TextView txtForth = (TextView) this.findViewById(R.id.txt_forth);
        TextView[] array = {txtFirst,txtSecond,txtThird,txtForth};
        this.txtArray = array;

        for(int i = 0;i < this.txtArray.length;i++){
            View view = txtArray[i];
            view.setOnClickListener(this);
        }
    }

    public void setIndex(int index){
        this.mRowIndex = index;
    }

    public void setInfo(List<String> mList){
        this.mList = mList;
        for(int i = 0;i < txtArray.length;i++){
            TextView txtView = txtArray[i];
            if(i < mList.size()){
                String entity = mList.get(i);
                txtView.setVisibility(View.VISIBLE);
                txtView.setText(entity);
            }else{
                txtView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        this.mIndex = -1;
        for(int i = 0;i < this.txtArray.length;i++){
            TextView txtView = this.txtArray[i];
            if(txtView == view){
                this.mIndex = i;
                selectIndex = mRowIndex;
                if(mListener != null){
                    mListener.onItemClick(getCatInfo(this.mIndex),this.mIndex);
                }
            }
        }
    }

    public void setIItemListner(IItemListener mListener){
        this.mListener = mListener;
    }

    /***
     * 选中的item项下标
     * @return
     */
    public String getCatInfo(int index){
        if(index >= 0){
            return mList.get(index);
        }
        return null;
    }
}
