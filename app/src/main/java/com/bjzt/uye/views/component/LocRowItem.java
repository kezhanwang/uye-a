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
public class LocRowItem extends RelativeLayout implements NoConfusion,View.OnClickListener {
    private TextView[] txtArray;
    private RelativeLayout[] relaArray;
    private List<String> mList;
    private List<EmployArea.BLocEntity> mLocList;
    private int mRowIndex;
    private int selectIndex = -1;
    private int mIndex;

    private IItemListener mListener;

    public LocRowItem(Context context) {
        super(context);
        init();
    }

    public LocRowItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.loc_rowitem_layout, this, true);

        RelativeLayout mRelaFirst = this.findViewById(R.id.rela_first);
        RelativeLayout mRelaSecond = this.findViewById(R.id.rela_second);
        RelativeLayout mRelaThird = this.findViewById(R.id.rela_third);
        RelativeLayout mRelaForth = this.findViewById(R.id.rela_forth);

        RelativeLayout[] tArray = {mRelaFirst,mRelaSecond,mRelaThird,mRelaForth};
        this.relaArray = tArray;

        TextView txtFirst = (TextView) this.findViewById(R.id.txt_first);
        TextView txtSecond = (TextView) this.findViewById(R.id.txt_second);
        TextView txtThird = (TextView) this.findViewById(R.id.txt_third);
        TextView txtForth = (TextView) this.findViewById(R.id.txt_forth);
        TextView[] array = {txtFirst,txtSecond,txtThird,txtForth};
        this.txtArray = array;

        for(int i = 0;i < this.txtArray.length;i++){
            View view = txtArray[i];
            RelativeLayout mRela = relaArray[i];
            view.setOnClickListener(this);
            mRela.setOnClickListener(this);
        }
    }

    public void setIndex(int index){
        this.mRowIndex = index;
    }

    public void setInfo(List<String> mList){
        this.mList = mList;
        for(int i = 0;i < txtArray.length;i++){
            TextView txtView = txtArray[i];
            RelativeLayout mRela = relaArray[i];
            if(i < mList.size()){
                String entity = mList.get(i);
                txtView.setVisibility(View.VISIBLE);
                txtView.setText(entity);
                mRela.setVisibility(View.VISIBLE);
            }else{
                txtView.setVisibility(View.GONE);
                mRela.setVisibility(View.GONE);
            }
        }
    }

    public void setInfoLoc(List<EmployArea.BLocEntity> mList){
        this.mLocList = mList;
        for(int i = 0;i < txtArray.length;i++){
            TextView txtView = txtArray[i];
            RelativeLayout mRela = relaArray[i];
            if(i < mList.size()){
                EmployArea.BLocEntity entity = mList.get(i);
                if(entity.isFake){
                    txtView.setVisibility(View.INVISIBLE);
                    txtView.setText("...");
                    mRela.setVisibility(View.INVISIBLE);
                }else{
                    txtView.setVisibility(View.VISIBLE);
                    txtView.setText(entity.mLocArea.name);
                    mRela.setVisibility(View.VISIBLE);
                }
            }else{
                txtView.setVisibility(View.GONE);
                mRela.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        this.mIndex = -1;
        for(int i = 0;i < this.txtArray.length;i++){
            TextView txtView = this.txtArray[i];
            RelativeLayout mRela = this.relaArray[i];
            if(txtView == view || view == mRela){
                this.mIndex = i;
                selectIndex = mRowIndex;
                if(mListener != null){
                    if(mLocList != null){
                        mListener.onItemClick(getLocInfo(this.mIndex),this.mIndex);
                    }else{
                        mListener.onItemClick(getCatInfo(this.mIndex),this.mIndex);
                    }
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
        if(mList != null){
            if(index >= 0){
                return mList.get(index);
            }
        }
        return null;
    }

    public EmployArea.BLocEntity getLocInfo(int index){
        if(mLocList != null){
            if(index >= 0){
                return mLocList.get(index);
            }
        }
        return null;
    }
}
