package com.bjzt.uye.views.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.common.listener.NoConfusion;

/**
 * Created by diaosi on 2017/2/21.
 */
public class DataCheckItemView extends RelativeLayout implements NoConfusion,View.OnClickListener{

    private int mType;
    public static final int TYPE_IDDENTITY = 1; //身份
    public static final int TYPE_CONTACT = 2;   //联系方式
    public static final int TYPE_BANKCARD = 3;  //绑定银行卡
    public static final int TYPE_FRIEND = 4;    //联系人信息
    public static final int TYPE_DEGREE = 5;    //学历
    public static final int TYPE_CONTACT_LIST = 6;  //通讯录
    public static final int TYPE_SESAME = 7;        //芝麻信用分
    public static final int TYPE_EXPERIENCE = 8;    //个人经历

    private RelativeLayout mRelaMain;
    private ImageView imgLogo;
    private TextView txtTitle;
    private TextView txtContent;
    private TextView txtTail;

    private IItemListener mItemListener;

    public DataCheckItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DataCheckItemView(Context context) {
        super(context);
        init();
    }

    public void setIItemListener(IItemListener mListener){
        this.mItemListener = mListener;
    }

    public void updateType(int mType){
        this.mType = mType;
        Drawable dLogo = null;
        String strTitle = "";
        String strContent = "";

        switch(this.mType){
            case TYPE_EXPERIENCE:
                dLogo = getResources().getDrawable(R.drawable.datacheck_img_experience);
                strTitle = getResources().getString(R.string.data_check_experience_title);
                strContent = getResources().getString(R.string.data_check_experience_content);
                break;
            case TYPE_IDDENTITY:
                dLogo = getResources().getDrawable(R.drawable.datacheck_img_id);
                strTitle = getResources().getString(R.string.cash_data_check_identity_title);
                strContent = getResources().getString(R.string.cash_data_check_identity_content);
                break;
            case TYPE_CONTACT:
                dLogo = getResources().getDrawable(R.drawable.data_check_firend);
                strTitle = getResources().getString(R.string.cash_data_check_contact_title);
                strContent = getResources().getString(R.string.cash_data_check_contact_content);
                break;
            case TYPE_BANKCARD:
                dLogo = getResources().getDrawable(R.drawable.data_check_bankcard);
                strTitle = getResources().getString(R.string.cash_data_check_bankcard_title);
                strContent = getResources().getString(R.string.cash_data_check_bankcard_content);
                break;
            case TYPE_FRIEND:
                dLogo = getResources().getDrawable(R.drawable.data_check_firend);
                strTitle = getResources().getString(R.string.cash_data_check_friend_title);
                strContent = getResources().getString(R.string.cash_data_check_friend_content);
                break;
            case TYPE_DEGREE:
                dLogo = getResources().getDrawable(R.drawable.data_check_degree);
                strTitle = getResources().getString(R.string.cash_data_check_degree_title);
                strContent = getResources().getString(R.string.cash_data_check_degree_content);
                break;
            case TYPE_CONTACT_LIST:
                dLogo = getResources().getDrawable(R.drawable.data_check_contactlist);
                strTitle = getResources().getString(R.string.cash_data_check_contactlist_title);
                strContent = getResources().getString(R.string.cash_data_check_contactlist_content);
                break;
            case TYPE_SESAME:
                dLogo = getResources().getDrawable(R.drawable.data_check_sesame);
                strTitle = getResources().getString(R.string.cash_data_check_sesame_title);
                strContent = getResources().getString(R.string.cash_data_check_sesame_content);
                break;
        }
        if(dLogo != null){
            imgLogo.setBackgroundDrawable(dLogo);
        }
        if(!TextUtils.isEmpty(strTitle)){
            txtTitle.setText(strTitle);
        }
        if(!TextUtils.isEmpty(strContent)){
            txtContent.setText(strContent);
        }
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.data_check_itemview_layout,this,true);

        this.mRelaMain = (RelativeLayout) this.findViewById(R.id.rela_main);
        this.mRelaMain.setOnClickListener(this);
        this.imgLogo = (ImageView) this.findViewById(R.id.cash_data_check_logo);
        this.txtTitle = (TextView) this.findViewById(R.id.cash_data_checking_title);
        this.txtContent = (TextView) this.findViewById(R.id.cash_data_checking_content);
        this.txtTail = (TextView) this.findViewById(R.id.cash_data_checking_tail);
    }

    @Override
    public void onClick(View v) {
        if(this.mItemListener != null){
            if(v == this.mRelaMain){
                this.mItemListener.onItemClick(v,-1);
            }
        }
    }

    /***
     * 更新尾部认证标志
     * @param isVerify
     */
    public void updateTailContent(boolean isVerify){
        String str = "";
        int c = 0;
        if(isVerify){
            str = "已完成";
            c = getResources().getColor(R.color.common_font_3);
        }else{
            if(mType == TYPE_SESAME){
                str = "可认证";
            }else{
                str = "待认证";
            }
            c = getResources().getColor(R.color.data_check_color_font_red);
        }
        if(!TextUtils.isEmpty(str)){
            txtTail.setText(str);
            txtTail.setTextColor(c);
        }
    }
}
