package com.bjzt.uye.views.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.controller.PicController;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/17.
 */

public class BindCardPhotoView extends RelativeLayout implements NoConfusion,View.OnClickListener{
    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.img_bankcard)
    ImageView imgView;
    @BindView(R.id.bind_card_rela)
    RelativeLayout mRelaTitle;
    @BindView(R.id.rela_imgbitmap)
    RelativeLayout mRelaContent;
    @BindView(R.id.bind_card_img_close)
    ImageView imgClose;
    @BindView(R.id.bind_card_img_content)
    ImageView imgContent;
    @BindView(R.id.txt_bottom_tips)
    TextView mTxtBottomTips;
    @BindView(R.id.cash_bindcard_phototview_title)
    TextView mTxtTitle;
    @BindView(R.id.cash_bindcard_txt_star)
    TextView mTxtStar;
    @BindView(R.id.line_bottom)
    View mViewLineBottom;

    IItemListener mListener;
    public static final int TYPE_BINDCARD = 1;
    public static final int TYPE_IMG_SITUATION = 2;
    public static final int TYPE_IMG_STATEMENT = 3;
    public static final int TYPE_IMG_PERSONAL_HOLD = 4;
    public static final int TYPE_IMG_ID = 5;
    public static final int TYPE_IMG_ID_BACK = 6;
    public static final int TYPE_IMG_PROTOCAL = 7;

    public static final int TAG_TYPE_IMG_ADD = 1;
    public static final int TAG_TYPE_CLOSE = 2;
    public static final int TAG_TYPE_CONTENT = 3;

    private String mUrl;
    private boolean mIsClickable = true;


    public BindCardPhotoView(Context context) {
        super(context);
        init();
    }

    public BindCardPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.cash_bindcard_photoview_layout,this,true);
        ButterKnife.bind(this);

        this.imgView.setOnClickListener(this);
        this.imgContent.setOnClickListener(this);
        this.imgClose.setOnClickListener(this);
        this.imgView.setVisibility(View.VISIBLE);
        this.mRelaContent.setVisibility(View.GONE);
    }

    public void updateType(int mType){
        String title = "";
        String bottomTips = "";
        Drawable d = null;
        int imgWidth = -1;
        int imgHeight = -1;
        switch (mType){
            case TYPE_BINDCARD:
                mTxtBottomTips.setVisibility(View.GONE);
                break;
            case TYPE_IMG_SITUATION:
                mTxtBottomTips.setVisibility(View.VISIBLE);
                break;
            case TYPE_IMG_STATEMENT:
                mTxtBottomTips.setVisibility(View.VISIBLE);
                break;
            case TYPE_IMG_PERSONAL_HOLD:
                mTxtBottomTips.setVisibility(View.VISIBLE);
                mViewLineBottom.setVisibility(View.VISIBLE);
                title = "手持身份证";
                bottomTips = "注: 请持本人身份证,在有机构LOGO的地方与课程顾问一起拍摄,要求照片清晰无遮挡。";
                imgWidth = (int) getResources().getDimension(R.dimen.photo_view_img_width);
                imgHeight = (int) getResources().getDimension(R.dimen.photo_view_img_height);
                d = getResources().getDrawable(R.drawable.img_add);
                break;
            case TYPE_IMG_PROTOCAL:
                mTxtBottomTips.setVisibility(View.VISIBLE);
                mViewLineBottom.setVisibility(View.VISIBLE);
                title = "培训协议";
                bottomTips = "注: 请上传培训协议或收据,需要包括机构名称、课程、价格、时间等重要信息。";
                imgWidth = (int) getResources().getDimension(R.dimen.photo_view_img_width);
                imgHeight = (int) getResources().getDimension(R.dimen.photo_view_img_height);
                d = getResources().getDrawable(R.drawable.img_add);
                break;
            case TYPE_IMG_ID_BACK:
            case TYPE_IMG_ID:
                mTxtBottomTips.setVisibility(View.GONE);
                mTxtTitle.setVisibility(View.GONE);
                mRelaTitle.setVisibility(View.GONE);
                int relaWidth = (int) getResources().getDimension(R.dimen.photo_view_rela_width_s);
                int relaHeight = (int) getResources().getDimension(R.dimen.photo_view_rela_height_s);
                RelativeLayout.LayoutParams llp = (LayoutParams) mRelaContent.getLayoutParams();
                llp.width = relaWidth;
                llp.height = relaHeight;
                llp.leftMargin = 0;
                mRelaContent.setLayoutParams(llp);
                if(mType == TYPE_IMG_ID_BACK){
                    d = getResources().getDrawable(R.drawable.bind_card_back);
                }else{
                    d = getResources().getDrawable(R.drawable.bind_card_front);
                }
                mViewLineBottom.setVisibility(View.GONE);
                break;
        }

        if(imgWidth > 0 && imgHeight > 0){
            RelativeLayout.LayoutParams llp = (LayoutParams) this.imgView.getLayoutParams();
            llp.width = imgWidth;
            llp.height = imgHeight;
            imgView.setLayoutParams(llp);
        }

        if(d != null){
            this.imgView.setBackgroundDrawable(d);
        }

        if(!TextUtils.isEmpty(title)){
            mTxtTitle.setText(title);
        }
        if(!TextUtils.isEmpty(bottomTips)){
            mTxtBottomTips.setText(bottomTips);
        }
    }


    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.imgView){
                this.mListener.onItemClick(this,TAG_TYPE_IMG_ADD);
            }else if(v == this.imgClose) {
                this.mListener.onItemClick(this, TAG_TYPE_CLOSE);
            }else if(v == this.imgContent){
                if(mIsClickable){
                    this.mListener.onItemClick(this,TAG_TYPE_CONTENT);
                }
            }
        }
    }

    public void showStar(boolean isShow){
        if(isShow){
            mTxtStar.setVisibility(View.VISIBLE);
        }else{
            mTxtStar.setVisibility(View.INVISIBLE);
        }
    }

    public void updateBitmap(Bitmap bitmap, String url, boolean isEditable){
        if(bitmap != null){
            this.mUrl = url;
            this.imgView.setVisibility(View.GONE);
            this.mRelaContent.setVisibility(View.VISIBLE);
            this.imgContent.setImageBitmap(bitmap);
        }else{
            this.mUrl = null;
            this.imgView.setVisibility(View.VISIBLE);
            this.mRelaContent.setVisibility(View.GONE);
        }
        if(isEditable){
            this.imgView.setOnClickListener(this);
            this.imgClose.setVisibility(View.VISIBLE);
        }else{
            this.imgView.setOnClickListener(null);
            this.imgClose.setVisibility(View.GONE);
        }
    }

    public void updateBitmapAdd(Bitmap bitmap,String url){
        if(bitmap != null){
            this.mUrl = url;
            this.imgView.setVisibility(View.VISIBLE);
            this.imgView.setImageBitmap(bitmap);
        }else{
            this.mUrl = null;
        }
    }

    public void updateBitmap(Bitmap bitmap,boolean isEditable){
        updateBitmap(bitmap,null,isEditable);
    }

    public void updatePicInfo(Context mContext,String pic,boolean isEdiable){
        this.mUrl = pic;
        if(!TextUtils.isEmpty(pic)){
            this.imgView.setVisibility(View.GONE);
            this.mRelaContent.setVisibility(View.VISIBLE);
            this.imgContent.setVisibility(View.VISIBLE);
            PicController.getInstance().showPic(imgView,pic);
        }else{
            this.imgView.setVisibility(View.VISIBLE);
            this.mRelaContent.setVisibility(View.GONE);
        }
        if(!isEdiable){
            this.imgView.setOnClickListener(null);
            this.imgClose.setVisibility(View.GONE);
            this.mIsClickable = false;
        }else{
            this.imgView.setOnClickListener(this);
            this.imgClose.setVisibility(View.VISIBLE);
            this.mIsClickable = true;
        }
    }

    public String getUrl(){
        return this.mUrl;
    }

}
