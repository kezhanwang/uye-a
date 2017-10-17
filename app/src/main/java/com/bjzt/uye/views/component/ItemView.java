package com.bjzt.uye.views.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.IItemListener;
import com.bjzt.uye.listener.ITextListener;
import com.bjzt.uye.util.ReflectUtils;
import com.common.listener.NoConfusion;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/15.
 */
public class ItemView extends RelativeLayout implements NoConfusion,View.OnClickListener{

    @BindView(R.id.item_rela)
    RelativeLayout itemRela;
    @BindView(R.id.title)
    TextView txtTitle;
    @BindView(R.id.txt_star)
    TextView mTxtStar;
    @BindView(R.id.img_arrow)
    ImageView imgArrow;
    @BindView(R.id.item_edittxt)
    BankInputEditTxt mEditTxt;
    @BindView(R.id.txt_btn)
    TimerDownTextView txtBtn;
    @BindView(R.id.img_verify)
    ImageView imgVerify;
    @BindView(R.id.txtview_tips_tail)
    TextView txtTail;
    @BindView(R.id.viewline)
    View mViewLine;
    @BindView(R.id.view_line)
    View mViewLineBottom;


    public static final int TYPE_NORMAL = 0;	//普通类型
    public static final int TYPE_VERIFY = 1;	//验证码类型
    public static final int TYPE_IMG_VERIFY = 2;	//图片验证码
    public static final int TYPE_IDCARD = 3;		//身份证件
    public static final int TYPE_CONTACT_USER = 4;	//联系人相关
    public static final int TYPE_TXTTAIL = 5;		//结尾是TxtView提示

    public static final int INPUT_TYPE_NUMBER = 1;	//number数字类型
    public static final int INPUT_TYPE_BANK = 2;	//银行卡类型
    public static final int INPUT_TYPE_TUTION = 3;	//支付金额
    public static final int TYPE_LEFT_TXT_NORMAL = 0;
    public static final int TYPE_LEFT_TXT_L = 1;
    public static final int TYPE_LEFT_TXT_M = 2;
    public static final int TYPE_LEFT_TXT_S = 3;

    private int mInputType;
    private IItemListener mListener;
    private int mType;

    public boolean isCliclable = true;


    public ItemView(Context context) {
        super(context);
        init();
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.itemview_layout,this,true);
        ButterKnife.bind(this);


        this.txtBtn.setVisibility(View.GONE);
        this.imgArrow.setVisibility(View.GONE);
        this.imgVerify.setOnClickListener(this);
        this.txtTail.setVisibility(View.GONE);
        //set cursor color
        ReflectUtils.setEditCursorColor(mEditTxt, 0);

    }


    public void setType(int mType){
        this.mType = mType;
        switch(mType){
            case TYPE_VERIFY:
                txtBtn.setVisibility(View.VISIBLE);
                break;
            case TYPE_IMG_VERIFY:
                this.imgVerify.setVisibility(View.VISIBLE);
                this.txtBtn.setVisibility(View.GONE);
                String title = "图形验证";
                String strHint = "输入右侧验证码";
                this.txtTitle.setText(title);
                this.mEditTxt.setHint(strHint);
                break;
            case TYPE_IDCARD:
                this.mEditTxt.setType(BankInputEditTxt.TYPE_ID);
                //身份证件最多是18位
                this.mEditTxt .setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
                break;
            case TYPE_CONTACT_USER:
                this.imgVerify.setVisibility(View.VISIBLE);
                this.imgVerify.setScaleType(ImageView.ScaleType.CENTER);
                this.txtBtn.setVisibility(View.GONE);
                Drawable d = getContext().getResources().getDrawable(R.drawable.contact_user_ico);
                this.imgVerify.setImageDrawable(d);
                this.mViewLine.setVisibility(View.VISIBLE);
                int height = (int) getResources().getDimension(R.dimen.item_height);
                int width = (int) (height * 1.5);
                RelativeLayout.LayoutParams llp = (LayoutParams) this.imgVerify.getLayoutParams();
                llp.height = height;
                llp.width = width;
                this.imgVerify.setLayoutParams(llp);
                this.mEditTxt.setInputType(InputType.TYPE_CLASS_PHONE);
                this.imgVerify.setBackgroundDrawable(null);
                break;
            case TYPE_TXTTAIL:
                this.txtTail.setVisibility(View.VISIBLE);
                break;
        }
    }



    public void setTxtTail(String strTail){
        if(this.mType == TYPE_TXTTAIL){
            this.txtTail.setText(strTail);
        }
    }

    public void setTitle(String str){
        this.txtTitle.setText(str);
    }

    public void setHint(String strHint){
        this.mEditTxt.setHint(strHint);
    }

    public void setEditTxt(String str){
        this.mEditTxt.setText(str);
    }

    public String getInputTxt(){
        if(mInputType == INPUT_TYPE_BANK){
            return this.mEditTxt.getInputTxt();
        }else{
            return this.mEditTxt.getText().toString();
        }
    }

    public void setInputTypeNumber(int mType){
        this.mInputType = mType;
        switch(mType){
            case INPUT_TYPE_NUMBER:
                this.mEditTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.mEditTxt.setType(BankInputEditTxt.TYPE_TUTION);
                break;
            case INPUT_TYPE_BANK:
                this.mEditTxt.setType(BankInputEditTxt.TYPE_BANKCARD);
                break;
            case INPUT_TYPE_TUTION:
                this.mEditTxt.setInputType(InputType.TYPE_CLASS_NUMBER);
                this.mEditTxt.setType(BankInputEditTxt.TYPE_TUTION);
                break;
        }
    }

    public EditText getEditTxt(){
        return this.mEditTxt;
    }

    public void setEditAble(boolean isEditable){
        this.mEditTxt.setFocusable(isEditable);
    }

    public void showArrow(){
        this.imgArrow.setVisibility(View.VISIBLE);
    }

    public void hideArrow(){
        this.imgArrow.setVisibility(View.GONE);
    }

    public void hideBottomLine(){
        mViewLineBottom.setVisibility(View.INVISIBLE);
    }

    public void setBtnListener(IItemListener mListener){
        this.mListener = mListener;
        this.txtBtn.setIBtnListener(mListener);
    }

    public void startTimerDown(){
        this.txtBtn.startTimer();
    }

    public void setEditTxtBtnListener(OnClickListener mListener){
        this.mEditTxt.setOnClickListener(mListener);
    }

    public void reSetTimer(){
        this.txtBtn.stopTimer();
    }

    /****
     * 设置右图片
     * @param bitmap
     */
    public void updateBitmap(Bitmap bitmap){
        if(bitmap != null && !bitmap.isRecycled()){
            this.imgVerify.setImageBitmap(bitmap);
        }
    }

    /**
     * 设置右图片2
     * @param drawable
     */
    public void updateBitmap(Drawable drawable){
        if(drawable != null ){
            this.imgVerify.setImageDrawable(drawable);
        }
    }

    @Override
    public void onClick(View view) {
        if(this.mListener != null && isCliclable){
            if(view == this.imgVerify || mType == TYPE_IMG_VERIFY || mType == TYPE_CONTACT_USER){
                this.mListener.onItemClick(this,-1);
            }
        }
    }

    public void setIClickable(boolean isCliclable){
        this.isCliclable = isCliclable;
        if(!this.isCliclable){
            this.mEditTxt.setOnClickListener(null);
        }
    }

    /***
     * 设置文本监听器
     * @param mTxtListener
     */
    public void setITxtChangeListener(ITextListener mTxtListener){
        this.mEditTxt.setITxtListener(mTxtListener);
    }

    public void setMaxCntInput(int maxLines){
        this.mEditTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLines)});
    }

    public void showStar(){
        mTxtStar.setVisibility(View.VISIBLE);
    }

    public void hideStar(){
        this.mTxtStar.setVisibility(View.INVISIBLE);
    }

    /***
     * 设置LeftTxt title类型
     * @param mType
     */
    public void setLeftTxtWidthType(int mType){
        int mWidth = 0;
        switch(mType){
            case TYPE_LEFT_TXT_L:
                mWidth = (int) getContext().getResources().getDimension(R.dimen.item_textWidth);
                break;
            case TYPE_LEFT_TXT_M:
                mWidth = (int) getContext().getResources().getDimension(R.dimen.item_textWidth_s);
                break;
            case TYPE_LEFT_TXT_S:
//				mWidth = (int) getContext().getResources().getDimension(R.dimen.loan_item_textWidth_ss);
//				llp = (LayoutParams) this.txtTitle.getLayoutParams();
//				llp.width = mWidth;
//				llp.leftMargin = 0;
//				this.txtTitle.setLayoutParams(llp);
                break;
        }
        if(mWidth > 0){
            RelativeLayout.LayoutParams llp = (LayoutParams) this.txtTitle.getLayoutParams();
            llp.width = mWidth;
            llp.leftMargin = 0;
            this.txtTitle.setLayoutParams(llp);
        }
    }

    /**
     * 设置编辑框文字
     * @param c
     */
    public void setEditTextColor(int c){
        this.mEditTxt.setTextColor(c);
    }

    /***
     * 相关filter
     * @param filters
     */
    public void setEditFilter(InputFilter[] filters){
        this.mEditTxt.setFilters(filters);
    }

    public void setBottomLineMargin(int margin){
        RelativeLayout.LayoutParams llp = (LayoutParams) mViewLineBottom.getLayoutParams();
        int leftMargin = (int) getResources().getDimension(R.dimen.common_margin_18);
        llp.leftMargin = leftMargin;
        mViewLineBottom.setLayoutParams(llp);
    }

    public void hideTitle(){
        this.itemRela.setVisibility(View.INVISIBLE);
    }

    /***
     * title颜色为灰色
     */
    public void setTitleGrey(){
        int c = getResources().getColor(R.color.common_a6);
        this.txtTitle.setTextColor(c);
    }

}
