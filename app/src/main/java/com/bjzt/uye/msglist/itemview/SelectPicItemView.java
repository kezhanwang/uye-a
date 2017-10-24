package com.bjzt.uye.msglist.itemview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.entity.VPicFileEntity;
import com.bjzt.uye.util.BitmapUtil;
import com.common.msglist.base.BaseItemView;

/**
 * Created by billy on 2017/10/24.
 */

public class SelectPicItemView extends BaseItemView<VPicFileEntity> {
    private ImageView img_itemView;
    private ImageView img_add;
    private VPicFileEntity mEntity;

    public SelectPicItemView(Context context) {
        super(context);
    }

    @Override
    public void setMsg(VPicFileEntity vPicFileEntity) {
        mEntity=vPicFileEntity;
        if (!mEntity.isAddPic) {
            img_itemView.setVisibility(VISIBLE);
            img_add.setVisibility(GONE);
            String url=mEntity.url;
            if (!TextUtils.isEmpty(url)){
                PicController.getInstance().showPic(img_itemView,url);
            }else{
                if (mEntity.mBitmapByte != null && mEntity.mBitmapByte.length>2){
                    mEntity.mBitmap= BitmapUtil.getBitmap(mEntity.mBitmapByte);
                }
                img_itemView.setImageBitmap(mEntity.mBitmap);
            }
        }else {
            img_itemView.setVisibility(GONE);
            img_add.setVisibility(VISIBLE);
        }
    }

    @Override
    public VPicFileEntity getMsg() {
        return this.mEntity;
    }

    @Override
    public void onInflate() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.select_pic_item_view,this,true);
        img_itemView= (ImageView) this.findViewById(R.id.img_itemView);
        img_add= (ImageView) this.findViewById(R.id.img_add);
    }
}
