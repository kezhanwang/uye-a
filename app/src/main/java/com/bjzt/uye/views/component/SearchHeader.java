package com.bjzt.uye.views.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bjzt.uye.R;
import com.bjzt.uye.listener.ITextListener;
import com.common.listener.NoConfusion;
import com.common.util.KeyBoardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by billy on 2017/10/17.
 */

public class SearchHeader extends RelativeLayout implements NoConfusion, View.OnClickListener{

    @BindView(R.id.txt_cancle)
    TextView mTxtCancle;

    @BindView(R.id.edit_find)
    ExtendEditText editTxt;

    private ISearchListener mListener;

    public SearchHeader(Context context) {
        super(context);
        init();
    }

    public SearchHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.search_header_layout,this,true);
        ButterKnife.bind(this);

        mTxtCancle.setOnClickListener(this);

        editTxt.setTxtChangeListener(new ITextListener() {
            @Override
            public void onTxtState(boolean isEmpty) {
                if(!isEmpty){
                    String strInfo = editTxt.getText();
                    if(mListener != null){
                        mListener.onTxtChanged(strInfo);
                    }
                }else{
                    if(mListener != null){
                        mListener.onTxtChanged(null);
                    }
                }
            }
        });
        editTxt.needSmallLeftMargin();

        editTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    if(mListener != null){
                        String str = editTxt.getText().toString();
                        mListener.onSearch(str);
                    }
                    return true;
                }
                return false;
            }
        });

        editTxt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(mListener != null){
                    mListener.onTxtFocus(hasFocus);
                }
            }
        });
        //设置文本大小
        float txtSize = getResources().getDimension(R.dimen.common_font_size_14);
        editTxt.setTextSize(txtSize);
        //设置键盘为搜索样式
        editTxt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //隐藏键盘
        hideKeyBoard();
    }

    public String getContentTxt(){
        return this.editTxt.getText();
    }

    public void setContentTxt(String str){
        this.editTxt.setText(str);
    }

    public void setHint(String strHint){
        editTxt.setHint(strHint);
    }

    /***
     * 键盘显示
     */
    public void showKeyBoard(){
        EditText edit = editTxt.getEditTxt();
        KeyBoardUtils.showKeyBoardLater(this.getContext(),edit);
    }

    /***
     * 隐藏键盘
     */
    public void hideKeyBoard(){
        EditText edit = editTxt.getEditTxt();
        KeyBoardUtils.hideSoftKeyBroad(this.getContext(),edit);
    }

    @Override
    public void onClick(View view) {
        if(this.mListener != null){
            if(view == this.mTxtCancle){
                mListener.onTxtCancle();
            }
        }
    }

    public void setISearchListener(ISearchListener mListener){
        this.mListener = mListener;
    }

    public interface ISearchListener{
        public void onSearch(String msg);
        public void onTxtCancle();
        public void onTxtChanged(String info);
        public void onTxtFocus(boolean focus);
    }
}
