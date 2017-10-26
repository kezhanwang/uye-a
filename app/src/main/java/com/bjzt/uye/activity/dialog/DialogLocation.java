package com.bjzt.uye.activity.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.adapter.LoanAdapterSelectAddr;
import com.bjzt.uye.entity.PLocItemEntity;
import com.bjzt.uye.http.ProtocalManager;
import com.bjzt.uye.http.base.RspBaseEntity;
import com.bjzt.uye.http.listener.ICallBack;
import com.bjzt.uye.http.rsp.RspLocAreaEntity;
import com.bjzt.uye.http.rsp.RspLocCityEntity;
import com.bjzt.uye.http.rsp.RspLocProEntity;
import com.bjzt.uye.views.component.BlankEmptyView;
import com.common.msglist.base.BaseListAdapter;
import com.common.util.DeviceUtil;
import com.common.util.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diaosi on 2017/10/26.
 * 地址选择器
 */
public class DialogLocation extends Dialog implements  View.OnClickListener{
    private final String TAG = getClass().getSimpleName();

    private LinearLayout[] mLinearArray;
    private TextView[] mTxtArray;
    private View[] mViewArray;
    private List<Integer> mReqList = new ArrayList<Integer>();
    private LoanAdapterSelectAddr mAdapterPro;
    private LoanAdapterSelectAddr mAdapterCity;
    private LoanAdapterSelectAddr mAdapterArea;
    private ListView mListView;
    private ImageView imgClose;
    private BlankEmptyView mEmptyView;

    private final int INDEX_PROVINCE = 0;
    private final int INDEX_CITY = 1;
    private final int INDEX_AREA = 2;

    private PLocItemEntity mEntityPro;
    private PLocItemEntity mEntityCity;
    private PLocItemEntity mEntityArea;
    private int mIndex = -1;
    private LoanIDialogLocListener mListener;

    private PLocItemEntity mSelectPro;
    private PLocItemEntity mSelectCity;
    private PLocItemEntity mSelectArea;

    public DialogLocation(Context context, int theme) {
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


    public void setSelectInfo(PLocItemEntity mSelectPro, PLocItemEntity mSelectCity, PLocItemEntity mSelectArea){
        this.mSelectPro = mSelectPro;
        this.mSelectCity = mSelectCity;
        this.mSelectArea = mSelectArea;
        //reset status
        reSetStatus(INDEX_AREA);
        //set text
        String strPro = this.mSelectPro.joinname;
        if(!TextUtils.isEmpty(strPro)){
            TextView mTxtView = getTxtByIndex(INDEX_PROVINCE);
            mTxtView.setText(strPro);
        }
        String strCity = this.mSelectCity.name;
        if(!TextUtils.isEmpty(strCity)){
            TextView mTxtView = getTxtByIndex(INDEX_CITY);
            mTxtView.setText(strCity);
        }
        String strArea = this.mSelectArea.name;
        if(!TextUtils.isEmpty(strArea)){
            TextView mTxtView = getTxtByIndex(INDEX_AREA);
            mTxtView.setText(strArea);
        }
        //set select
        setIndex(INDEX_PROVINCE,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = li.inflate(R.layout.loan_location_dialog_layout, null);
        this.imgClose = (ImageView) mView.findViewById(R.id.img_close);
        this.imgClose.setOnClickListener(this);
        LinearLayout mLinearFirst = (LinearLayout) mView.findViewById(R.id.linear_first);
        TextView mTxtFirst = (TextView) mView.findViewById(R.id.txt_loc_first);
        View mViewFirst = mView.findViewById(R.id.line_first);
        LinearLayout mLinearSecond = (LinearLayout) mView.findViewById(R.id.linear_second);
        TextView mTxtSecond = (TextView) mView.findViewById(R.id.txt_loc_second);
        View mViewSecond = mView.findViewById(R.id.line_second);
        LinearLayout mLinearThird = (LinearLayout) mView.findViewById(R.id.linear_third);
        TextView mTxtThird = (TextView) mView.findViewById(R.id.txt_loc_third);
        View mViewThird = mView.findViewById(R.id.line_third);

        this.mListView = (ListView) mView.findViewById(R.id.listview);
        this.mEmptyView = (BlankEmptyView) mView.findViewById(R.id.emptyview);

        LinearLayout[] tempArray = {mLinearFirst,mLinearSecond,mLinearThird};
        TextView[] tempTxtArray = {mTxtFirst,mTxtSecond,mTxtThird};
        View[] tempViewArray = {mViewFirst,mViewSecond,mViewThird};
        this.mLinearArray = tempArray;
        this.mTxtArray = tempTxtArray;
        this.mViewArray = tempViewArray;

        //init linear
        for(int i = 0;i < mLinearArray.length;i++){
            LinearLayout mLinear = mLinearArray[i];
            mLinear.setOnClickListener(this);
        }

        int mWidth = DeviceUtil.mWidth;
        ViewGroup.LayoutParams llp = new ViewGroup.LayoutParams(mWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(mView,llp);

        int reqNo= ProtocalManager.getInstance().reqLocProList(callBack);
        mReqList.add(reqNo);
        showLoadingDialog();

        reSetStatus(INDEX_PROVINCE);
    }

    private void reSetStatus(int mIndex){
        TextView mTxtProvince = getTxtByIndex(INDEX_PROVINCE);
        TextView mTxtCity = getTxtByIndex(INDEX_CITY);
        TextView mTxtArea = getTxtByIndex(INDEX_AREA);
        if(mIndex == INDEX_PROVINCE){
            resetTxtContents(mTxtProvince);
            mTxtCity.setVisibility(View.GONE);
            mTxtArea.setVisibility(View.GONE);
            mTxtProvince.setVisibility(View.VISIBLE);
        }else if(mIndex == INDEX_CITY){
            resetTxtContents(mTxtCity);
            resetTxtContents(mTxtArea);
            mTxtProvince.setVisibility(View.VISIBLE);
            mTxtCity.setVisibility(View.VISIBLE);
            mTxtArea.setVisibility(View.GONE);
        }else if(mIndex == INDEX_AREA){
            resetTxtContents(mTxtArea);
            mTxtProvince.setVisibility(View.VISIBLE);
            mTxtCity.setVisibility(View.VISIBLE);
            mTxtArea.setVisibility(View.VISIBLE);
        }
    }

    private void showLoadingDialog(){
        mListView.setVisibility(View.GONE);
        mEmptyView.reSetState();
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.showLoadingState();
    }

    private void hideLoadingDialog(){
        mListView.setVisibility(View.VISIBLE);
        mEmptyView.reSetState();
        mEmptyView.loadSucc();
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v == this.imgClose){
            dismiss();
        }else{
            int mIndex = -1;
            for(int i = 0;i < mLinearArray.length;i++){
                if(v == mLinearArray[i]){
                    mIndex = i;
                    break;
                }
            }
            if(mIndex >= 0){
                setIndex(mIndex,true);
            }
        }
    }

    private void setIndex(int mIndex,boolean changeAdapter){
        this.mIndex = mIndex;
        if(changeAdapter){
            switch(mIndex){
                case INDEX_PROVINCE:
                    if(mAdapterPro != null){
                        hideLoadingDialog();
                        mListView.setAdapter(mAdapterPro);
                    }
                    break;
                case INDEX_CITY:
                    if(mAdapterCity != null){
                        hideLoadingDialog();
                        mListView.setAdapter(mAdapterCity);
                    }else{
                        if(mSelectCity != null){
                            String proId = null;
                            if(mEntityPro != null){
                                proId = mEntityPro.id;
                            }else if(mSelectPro != null){
                                proId = mSelectPro.id;
                            }
                            if(!TextUtils.isEmpty(proId)){
                                showLoadingDialog();
                                int reqNo= ProtocalManager.getInstance().reqLocCityList(proId,callBack);
                                mReqList.add(reqNo);
                            }
                        }
                    }
                    break;
                case INDEX_AREA:
                    if(mAdapterArea != null){
                        hideLoadingDialog();
                        mListView.setAdapter(mAdapterArea);
                    }else{
                        if(mSelectArea != null){
                            String cityId = null;
                            if(mEntityCity != null){
                                cityId = mEntityCity.id;
                            }else if(mSelectCity != null){
                                cityId = mSelectCity.id;
                            }
                            if(!TextUtils.isEmpty(cityId)){
                                showLoadingDialog();
                                int reqNo= ProtocalManager.getInstance().reqLocAreaList(cityId,callBack);
                                mReqList.add(reqNo);
                            }
                        }
                    }
                    break;
            }
        }
        int cBlack = getContext().getResources().getColor(R.color.common_font_3);
        int cRed = getContext().getResources().getColor(R.color.common_red);
        for(int i = 0;i < mTxtArray.length;i++){
            TextView txtView = mTxtArray[i];
            View mViewLine = mViewArray[i];
            if(i == mIndex){
                txtView.setTextColor(cRed);
                mViewLine.setVisibility(View.VISIBLE);
            }else{
                txtView.setTextColor(cBlack);
                mViewLine.setVisibility(View.GONE);
            }
        }
    }

    private void handleRspError(RspBaseEntity mRspEntity){
        String tips = getContext().getResources().getString(R.string.common_request_error);
        if(!TextUtils.isEmpty(mRspEntity.msg)){
            tips = mRspEntity.msg;
        }
        Utils.toast(tips,true);
        mEmptyView.reSetState();
        this.mEmptyView.showErrorState();
        this.mEmptyView.setErrorTips(tips);
    }

    private TextView getTxtByIndex(int mIndex){
        TextView mTxt = null;
        if(mIndex >= 0 && mIndex < mTxtArray.length){
            mTxt = mTxtArray[mIndex];
        }
        return mTxt;
    }

    private void resetTxtContents(TextView mTxtView){
        mTxtView.setText("请选择");
    }

    //省点击
    private LoanISelectAddressItemClickListener mItemProListener = new LoanISelectAddressItemClickListener(){
        @Override
        public void selectAddressItemClick(PLocItemEntity entity) {
            boolean flag = false;
            if(mEntityPro != null){
                mEntityPro.vIsSelect = false;
                if(!mEntityPro.id.equals(entity.id)){
                    TextView txtCity = getTxtByIndex(INDEX_CITY);
                    resetTxtContents(txtCity);
                    TextView txtArea = getTxtByIndex(INDEX_AREA);
                    resetTxtContents(txtArea);
                    //clear data
                    mAdapterArea = null;
                    mAdapterCity = null;
                    reSetStatus(INDEX_CITY);
                    flag = true;
                }
            }
            entity.vIsSelect = true;
            mEntityPro = entity;
            if(!flag && mEntityCity == null){
                reSetStatus(INDEX_CITY);
            }
            String strName = mEntityPro.name;
            if(!TextUtils.isEmpty(strName)){
                TextView mTxtView = getTxtByIndex(INDEX_PROVINCE);
                mTxtView.setText(strName);
            }
            setIndex(INDEX_CITY,false);
            showLoadingDialog();
            String id = mEntityPro.id;
            int reqNo= ProtocalManager.getInstance().reqLocCityList(id,callBack);
            mReqList.add(reqNo);
        }
    };

    //市
    private LoanISelectAddressItemClickListener mItemCityListener = new LoanISelectAddressItemClickListener(){
        @Override
        public void selectAddressItemClick(PLocItemEntity entity) {
            if(mEntityCity != null){
                mEntityCity.vIsSelect = false;
                if(!mEntityCity.id.equals(entity.id)){
                    TextView txtArea = getTxtByIndex(INDEX_AREA);
                    resetTxtContents(txtArea);
                    //clear data
                    mAdapterArea = null;
                    reSetStatus(INDEX_AREA);
                }
            }
            entity.vIsSelect = true;
            mEntityCity = entity;
            if(mEntityArea == null){
                reSetStatus(INDEX_AREA);
            }
            String strName = mEntityCity.name;
            if(!TextUtils.isEmpty(strName)){
                TextView mTxtView = getTxtByIndex(INDEX_CITY);
                mTxtView.setText(strName);
            }
            setIndex(INDEX_AREA,false);
            showLoadingDialog();
            String id = mEntityCity.id;
            int reqNo= ProtocalManager.getInstance().reqLocAreaList(id,callBack);
            mReqList.add(reqNo);
        }
    };

    //地区列表点击
    private LoanISelectAddressItemClickListener mItemAreaListener = new LoanISelectAddressItemClickListener(){
        @Override
        public void selectAddressItemClick(PLocItemEntity entity) {
            if(mEntityArea != null){
                mEntityArea.vIsSelect = false;
            }
            entity.vIsSelect = true;
            mAdapterArea.notifyDataSetChanged();

            mEntityArea = entity;
            String strName = mEntityArea.joinname;
            if(!TextUtils.isEmpty(strName)){
                TextView mTxtView = getTxtByIndex(INDEX_AREA);
                mTxtView.setText(strName);
            }
            setIndex(INDEX_AREA,false);

            if(mListener != null){
                if(mEntityCity == null && mSelectCity != null){
                    mEntityCity = mSelectCity;
                }
                if(mEntityArea == null && mSelectArea != null){
                    mEntityArea = mSelectArea;
                }
                mListener.onLocSelect(mEntityPro,mEntityCity,mEntityArea);
            }
            dismiss();
        }
    };

    private ICallBack callBack = new ICallBack() {
        @Override
        public void getResponse(Object rsp, boolean isSucc, int errorCode, int seqNo, int src) {
            if(isShowing()){
                if(mReqList.contains(Integer.valueOf(seqNo))){
                    if(rsp instanceof RspLocProEntity){
                        RspLocProEntity rspEntity = (RspLocProEntity) rsp;
                        if(isSucc){
                            if(mAdapterPro == null){
                                mAdapterPro = new LoanAdapterSelectAddr(rspEntity.list);
                                mAdapterPro.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
                                mAdapterPro.setmSelectAddressItemListener(mItemProListener);
                                mListView.setAdapter(mAdapterPro);
                            }else{
                                mAdapterPro.reSetList(rspEntity.list);
                            }
                            hideLoadingDialog();
                            //初始化相关
                            if(mSelectPro != null){
                                PLocItemEntity rEntity = mAdapterPro.getSelectItem(mSelectPro);
                                if(rEntity != null){
                                    rEntity.vIsSelect = true;
                                    mEntityPro = rEntity;
                                }
                                mSelectPro = null;
                            }
                            if(mIndex == INDEX_PROVINCE){
                                mListView.setAdapter(mAdapterPro);
                                setIndex(INDEX_PROVINCE,false);
                            }
                        }else{
                            handleRspError(rspEntity);
                        }
                    }else if(rsp instanceof RspLocCityEntity){
                        RspLocCityEntity rspEntity = (RspLocCityEntity) rsp;
                        if(isSucc){
                            if(mAdapterCity == null){
                                mAdapterCity = new LoanAdapterSelectAddr(rspEntity.list);
                                mAdapterCity.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
                                mAdapterCity.setmSelectAddressItemListener(mItemCityListener);
                                mListView.setAdapter(mAdapterCity);
                            }else{
                                mAdapterCity.reSetList(rspEntity.list);
                                setIndex(INDEX_CITY,false);
                            }
                            hideLoadingDialog();
                            //初始化相关
                            if(mSelectCity != null){
                                PLocItemEntity rEntity = mAdapterCity.getSelectItem(mSelectCity);
                                if(rEntity != null){
                                    rEntity.vIsSelect = true;
                                    mEntityCity = rEntity;
                                }
                                mSelectCity = null;
                            }
                        }else{
                            handleRspError(rspEntity);
                        }
                        if(mIndex == INDEX_CITY){
                            mListView.setAdapter(mAdapterCity);
                        }
                    }else if(rsp instanceof RspLocAreaEntity){
                        RspLocAreaEntity rspEntity = (RspLocAreaEntity) rsp;
                        if(isSucc){
                            if(mAdapterArea == null){
                                mAdapterArea = new LoanAdapterSelectAddr(rspEntity.list);
                                mAdapterArea.setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
                                mAdapterArea.setmSelectAddressItemListener(mItemAreaListener);
                                mListView.setAdapter(mAdapterArea);
                            }else{
                                mAdapterArea.reSetList(rspEntity.list);
                                setIndex(INDEX_AREA,false);
                            }
                            hideLoadingDialog();
                            //初始化相关
                            if(mSelectArea != null){
                                PLocItemEntity rEntity = mAdapterArea.getSelectItem(mSelectArea);
                                if(rEntity != null){
                                    rEntity.vIsSelect = true;
                                    mEntityArea = rEntity;
                                }
                                mSelectArea = null;
                            }
                        }else{
                            handleRspError(rspEntity);
                        }
                        if(mIndex == INDEX_AREA){
                            mListView.setAdapter(mAdapterArea);
                        }
                    }
                }
            }
        }
    };

    public void setIListener(LoanIDialogLocListener mListener){
        this.mListener = mListener;
    }

    public interface LoanIDialogLocListener{
        public void onLocSelect(PLocItemEntity mEntityPro, PLocItemEntity mEntityCity, PLocItemEntity mEntityArea);
    }

    public static interface  LoanISelectAddressItemClickListener{
        void selectAddressItemClick(PLocItemEntity entity);
    }
}
