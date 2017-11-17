package com.bjzt.uye.views.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bjzt.uye.R;
import com.bjzt.uye.listener.ITextListener;
import com.bjzt.uye.util.ReflectUtils;
import com.common.listener.NoConfusion;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/15.
 */

public class ExtendEditText extends LinearLayout implements NoConfusion, View.OnClickListener, TextWatcher {
    private final String TAG = "ExtendEditText";
    @BindView(R.id.edit)
    EditText text;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.img_eyes)
    ImageView imgEyes;
    @BindView(R.id.linear_main)
    LinearLayout mLinear;

    private boolean isEmpty;
    private ITextListener mTxtListener;
    private int mType;
    public static final int TYPE_PWD = 0x10;
    public static final int TYPE_PWD_OPEN = 0x11;

    public ExtendEditText(Context context) {
        super(context);
        init();
    }

    public ExtendEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.extend_edittext,this,true);
        ButterKnife.bind(this);
        ReflectUtils.setEditCursorColor(text, 0);
        this.text.setHintTextColor(getContext().getResources().getColor(R.color.common_dc));
        this.text.addTextChangedListener(this);
        this.img.setOnClickListener(this);
        this.imgEyes.setOnClickListener(this);
    }

    /***
     * 设置文本大小
     * @param txtSize
     */
    public void setTextSize(float txtSize){
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX,txtSize);
    }

    public void setBGDrawable(Drawable d){
        this.mLinear.setBackgroundDrawable(d);
        this.text.setBackgroundDrawable(null);
    }

    public void setTxtChangeListener(ITextListener mListener){
        this.mTxtListener = mListener;
    }

    public String getText(){
        String str=text.getText().toString();
        return str;
    }
    public void setText(String str){
        text.setText(str);
    }
    public void setHint(String str){
        text.setHint(str);
    }
    public void setInputType(int type){
        text.setInputType(type);
    }
    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if(view == this.img){
            this.text.setText("");
        }else if(view == this.imgEyes){
            if(mType == TYPE_PWD){
                updateType(TYPE_PWD_OPEN);
            }else{
                updateType(TYPE_PWD);
            }
        }
    }

    public boolean isEmpty(){
        return isEmpty;
    }
    @Override
    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub
        if(mTxtListener != null){
            String str = getText().toString();
            if(TextUtils.isEmpty(str)){
                mTxtListener.onTxtState(true);
            }else{
                mTxtListener.onTxtState(false);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        String str=text.getText().toString();
        if(!TextUtils.isEmpty(str)){
            isEmpty=false;
            img.setVisibility(View.VISIBLE);
        }else{
            isEmpty=true;
            img.setVisibility(View.GONE);
        }
    }

    /***
     * 获取编辑输入控件
     * @return
     */
    public EditText getEditTxt(){
        return this.text;
    }

    /****
     * 设置input类型
     * @param inputType
     */
    public void setImeOptions(int inputType){
        text.setImeOptions(inputType);
    }

    /***
     * 键盘的搜索按钮
     * @param mListener
     */
    public void setOnEditorActionListener(TextView.OnEditorActionListener mListener){
        text.setOnEditorActionListener(mListener);
    }

    /***
     * 左边距减小
     */
    public void needSmallLeftMargin(){
        int leftMargin = (int) getResources().getDimension(R.dimen.common_margin_left_small);
        LinearLayout.LayoutParams llp = (LayoutParams) text.getLayoutParams();
        llp.leftMargin = leftMargin;
        text.setLayoutParams(llp);
    }


    public void setMaxLength(int max){
        text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max)});
    }

    public void updateType(int mType){
        this.mType = mType;
        Drawable dEye = null;
        int index = text.getSelectionStart();
        switch(mType){
            case TYPE_PWD:
                setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                dEye = getResources().getDrawable(R.drawable.login_img_eye_close);
                break;
            case TYPE_PWD_OPEN:
                setInputType(InputType.TYPE_CLASS_TEXT);
                dEye = getResources().getDrawable(R.drawable.login_img_eye_open);
                break;
        }
        if(dEye != null){
            this.imgEyes.setVisibility(View.VISIBLE);
            this.imgEyes.setImageDrawable(dEye);
        }
        if(index >= 0){
            text.setSelection(index);
        }
    }
}
