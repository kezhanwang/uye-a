package com.bjzt.uye.views.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.common.MyLog;
import com.common.listener.NoConfusion;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/25.
 */
public class ProtocalItemView extends RelativeLayout implements NoConfusion, View.OnClickListener{
    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.pro_img_flag)
    ImageView imgIcon;
    @BindView(R.id.pro_txt_content)
    TextView mTxtContent;

    public static final int TYPE_SERVICE = 1;   //服务协议
    public static final int TYPE_AUTH = 2;      //授权

    public static final int SRC_IMG_ICON = 1;
    public static final int SRC_TXT_CLICK = 2;

    private int mType;
    private IItemListener mListener;
    private boolean isSelect = false;

    public ProtocalItemView(Context context) {
        super(context);
        init();
    }

    public ProtocalItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.protocal_itemview_layout,this,true);
        ButterKnife.bind(this);

        this.imgIcon.setOnClickListener(this);
        this.mTxtContent.setOnClickListener(this);
    }

    public void updateType(int mType){
        this.mType = mType;
        List<String> mList = new ArrayList<>();
        String str = "我同意";
        switch(this.mType){
            case TYPE_AUTH:
                String s = getResources().getString(R.string.order_info_protocal_auth);
                String strPro = getResources().getString(R.string.order_info_protocal_formate,s);
                mList.add(str);
                mList.add(strPro);
                break;
            case TYPE_SERVICE:
                s = getResources().getString(R.string.order_info_protocal_service);
                strPro = getResources().getString(R.string.order_info_protocal_formate,s);
                mList.add(str);
                mList.add(strPro);
                break;
        }
        //xxx
        int start = mList.get(0).length();
        int middle = start + mList.get(1).length();
//        int end = middle + mList.get(2).length();
        str = "";
        for(String s : mList){
            str += s;
        }
        final SpannableStringBuilder sb = new SpannableStringBuilder(str);
        int cBlue = getResources().getColor(R.color.common_green);
        int cBlack = getResources().getColor(R.color.common_font_black);
        ForegroundColorSpan fBlue = new ForegroundColorSpan(cBlue);         // Span to set text color to some RGB value
        ForegroundColorSpan fBlack = new ForegroundColorSpan(cBlack);
        ForegroundColorSpan fBlackEnd = new ForegroundColorSpan(cBlack);
        //set txt color
        try {
            sb.setSpan(fBlack, 0, start, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            sb.setSpan(fBlue, start, middle, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            sb.setSpan(fBlackEnd, middle, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }catch(Exception ee){
            MyLog.error(TAG,ee);
        }
        //set txt size
        int txtSizeSmall = getResources().getDimensionPixelSize(R.dimen.pro_font_small);
        int txtSizeLarge = getResources().getDimensionPixelSize(R.dimen.pro_font_large);
        AbsoluteSizeSpan fSmall = new AbsoluteSizeSpan(txtSizeSmall,false);
        AbsoluteSizeSpan fLarge = new AbsoluteSizeSpan(txtSizeLarge,false);
        AbsoluteSizeSpan fSmallEnd = new AbsoluteSizeSpan(txtSizeSmall,false);
        try {
            sb.setSpan(fSmall, 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
            sb.setSpan(fLarge, start, middle, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
//            sb.setSpan(fSmallEnd, middle, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
        }catch(Exception ee){
            MyLog.error(TAG,ee);
        }
        this.mTxtContent.setText(sb);
    }

    public void setIItemListener(IItemListener mListener){
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        if(this.mListener != null){
            if(v == this.imgIcon){
                isSelect = !isSelect;
                refresh();
                mListener.onItemClick(this,SRC_IMG_ICON);
            }else if(v == this.mTxtContent){
                mListener.onItemClick(this,SRC_TXT_CLICK);
            }
        }
    }

    public boolean getIsSelect(){
        return this.isSelect;
    }

    public void setIsSelect(boolean isSelect){
        this.isSelect = isSelect;
        refresh();
    }

    private void refresh(){
        Drawable d;
        if(isSelect){
            d = getResources().getDrawable(R.drawable.img_protocal_flag_selected);
        }else{
            d = getResources().getDrawable(R.drawable.img_protocal_flag_normal);
        }
        if(d != null){
            imgIcon.setImageDrawable(d);
        }
    }
}
