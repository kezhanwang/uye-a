package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.activity.dialog.DialogLocation;
import com.bjzt.uye.entity.PLocItemEntity;
import com.common.msglist.base.BaseItemView;

/**
 * Created by ningdan on 2016/9/28.
 */
public class LoanSelectAddressItem extends BaseItemView<PLocItemEntity> implements View.OnClickListener {
    private Context mContext;
    private TextView name;
    private ImageView arrow;
    private ImageView mImgViewSelect;
    private RelativeLayout rl_item;
    private DialogLocation.LoanISelectAddressItemClickListener mSelectAddressItemListener;
    private PLocItemEntity entity;

    public LoanSelectAddressItem(Context context) {
        super(context);
        mContext=context;
//        cBlack = getContext().getResources().getColor(R.color.loan_common_font_black_v2);
//        cRed = getContext().getResources().getColor(R.color.loan_common_font_blue);
    }

    @Override
    public void setMsg(PLocItemEntity item) {
        this.entity = item;
        int mType = entity.mType;
        switch(mType){
            case PLocItemEntity.TYPE_CITY:
                name.setText(entity.name);
                break;
            case PLocItemEntity.TYPE_AREA:
                name.setText(entity.name);
                arrow.setVisibility(View.INVISIBLE);
                break;
            case PLocItemEntity.TYPE_PROVINCE:
                name.setText(entity.name);
                break;
        }
        boolean isSelect = this.entity.vIsSelect;
        if(isSelect){
//            name.setTextColor(cBlack);
            mImgViewSelect.setVisibility(View.VISIBLE);
        }else{
//            name.setTextColor(cBlack);
            mImgViewSelect.setVisibility(View.GONE);
        }
    }

    @Override
    public PLocItemEntity getMsg() {
        return entity;
    }

    @Override
    public void onInflate() {
        LayoutInflater inflater= (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.loan_select_address_province_item,this,true);
        name= (TextView) this.findViewById(R.id.loan_select_address_name);
        this.mImgViewSelect = (ImageView) this.findViewById(R.id.loan_img_select);
        arrow= (ImageView) this.findViewById(R.id.loan_select_address_arrow);
        rl_item= (RelativeLayout) this.findViewById(R.id.loan_select_address_rl_item);
        rl_item.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view==rl_item){
            if(mSelectAddressItemListener!=null){
                mSelectAddressItemListener.selectAddressItemClick(entity);
            }
        }
    }
    public void setSelectAddressItemListener(DialogLocation.LoanISelectAddressItemClickListener mSelectAddressItemListener){
        this.mSelectAddressItemListener=mSelectAddressItemListener;
    }

}
