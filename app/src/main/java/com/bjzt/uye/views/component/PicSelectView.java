package com.bjzt.uye.views.component;

import android.content.Context;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.adapter.AdapterSelectPic;
import com.bjzt.uye.entity.VPicFileEntity;
import com.common.listener.NoConfusion;
import com.common.msglist.base.BaseListAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/24.
 */

public class PicSelectView extends RelativeLayout implements NoConfusion{

    @BindView(R.id.rela_title)
    RelativeLayout mRelaTitle;
    @BindView(R.id.txt_title)
    TextView text_name;
    @BindView(R.id.txt_star)
    TextView mTxtDot;
    @BindView(R.id.gridview)
    GridView gridView;
    @BindView(R.id.txt_tips)
    TextView mTxtTip;

    private AdapterSelectPic mAdapter;
    public static final int TYPE_PROTOCAL = 1;
    private int mType;
    private ArrayList<VPicFileEntity> mList;
    private ISelectPicItemClickListener mListener;

    public PicSelectView(Context context) {
        super(context);
        init();
    }

    public PicSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.pic_select_view_layout,this,true);
        ButterKnife.bind(this);

        gridView.setOnItemClickListener(itemListener);
    }

    public void updateType(int mType){
        this.mType = mType;
        switch(this.mType){
            case TYPE_PROTOCAL:
                break;
        }
    }

    public void clearData(){
        if(this.mList != null){
            this.mList.clear();
            VPicFileEntity entity = new VPicFileEntity();
            entity.isAddPic = true;
            this.mList.add(entity);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void initData(int type){
        mType=type;
        ArrayList<VPicFileEntity> mPicList = new ArrayList<VPicFileEntity>();
        mList=mPicList;
        VPicFileEntity entity = new VPicFileEntity();
        entity.isAddPic = true;
        mPicList.add(entity);
        mAdapter = new AdapterSelectPic(mPicList);
        this.mAdapter.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
        gridView.setAdapter(mAdapter);
        String title = null;
        String tips = null;
        switch(type){
            case TYPE_PROTOCAL:
                title = getResources().getString(R.string.pic_select_title_protocal);
                tips = getResources().getString(R.string.pic_select_tips_protocal);
                break;
        }
        text_name.setText(title);
        mTxtTip.setText(tips);
    }

    public void insertLastBefore(VPicFileEntity entity){
        mAdapter.insertLastBefore(entity);
    }

    public void checkMaxCnt(int cnt){
        mAdapter.checkMaxCnt(cnt);
    }
    public List<String> getPublishList(){
        return mAdapter.getPublishList();
    }
    public List<String> getNetUrlList(){
        return mAdapter.getNetUrlList();
    }
    public void setOnItemClickListener(ISelectPicItemClickListener mListener){
        this.mListener=mListener;
    }
    public void setTxt(String str){
        text_name.setText(str);
    }
    public int getPicSize(){
        return mAdapter.getCount()-1;
    }
    public void modifyPic(VPicFileEntity entity,int pos){
        mAdapter.modifyPic(entity,pos);
    }


    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View arg1, int pos,long arg3) {
            // TODO Auto-generated method stub
            ListAdapter listAdapter= (ListAdapter) adapterView.getAdapter();
            if (listAdapter instanceof HeaderViewListAdapter){
                HeaderViewListAdapter headerViewListAdapter= (HeaderViewListAdapter) listAdapter;
                listAdapter=headerViewListAdapter.getWrappedAdapter();
            }
            if (listAdapter!=null) {
                AdapterSelectPic mAdapter= (AdapterSelectPic) listAdapter;
                VPicFileEntity entity = (VPicFileEntity) mAdapter.getItem(pos);
                if (entity.isAddPic) {
                    if (mListener!=null){
                        mListener.selectPic(mType);
                    }
                } else {
                    ArrayList<VPicFileEntity> mList = new ArrayList<VPicFileEntity>();
                    if (mAdapter != null) {
                        for (int i = 0; i < mAdapter.getCount(); i++) {
                            VPicFileEntity itemEntity = (VPicFileEntity) mAdapter.getItem(i);
                            if (entity != null && !itemEntity.isAddPic) {
                                mList.add(itemEntity);
                            }
                        }
                    }
                    if (mListener!=null){
                        mListener.showBigPic(mType,mList,pos);
                    }
                }
            }
        }
    };



    public static interface ISelectPicItemClickListener{
        void selectPic(int type);
        void showBigPic(int type, ArrayList<VPicFileEntity> mList, int pos);
    }
}
