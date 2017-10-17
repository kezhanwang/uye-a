package com.bjzt.uye.views.component;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import com.bjzt.uye.listener.ITextListener;
import com.common.common.MyLog;
import com.common.listener.NoConfusion;

/***
 * 银行卡号的输入格式 xxxx xxxx xxxx xxxx xxx
 * @date 2017/10/17
 */
public class BankInputEditTxt extends EditText implements NoConfusion {
	private final String TAG = "BankInputEditTxt";
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_BANKCARD = 1;
	public static final int TYPE_ID = 2;		//身份证
	public static final int TYPE_TUTION = 3;	//输入金额

	private int mType;
	private ITextListener mTxtListener;

	public BankInputEditTxt(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public BankInputEditTxt(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public BankInputEditTxt(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){}
	
	public void setType(int mType){
		switch(mType){
			case TYPE_BANKCARD:
				removeTextChangedListener(txtWatcher);
				addTextChangedListener(txtWatcher);
				setInputType(InputType.TYPE_CLASS_NUMBER);
				break;
			case TYPE_ID:
				removeTextChangedListener(mIdWatcher);
				addTextChangedListener(mIdWatcher);
				break;
			case TYPE_TUTION:
				removeTextChangedListener(mIdWatcher);
				addTextChangedListener(mIdWatcher);
				break;
		}
	}

	private TextWatcher mIdWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override
		public void afterTextChanged(Editable editable) {
			if(mTxtListener != null){
				mTxtListener.onTxtState(false);
			}
		}
	};

	public void setITxtListener(ITextListener mTxtListener){
		this.mTxtListener = mTxtListener;
	}

	//6228 4815 5288 7309 119 
	private TextWatcher txtWatcher = new TextWatcher() {
		private int beforeLen;
		private int afterLen;
		private int mSelectIndex = 0;
		
		@Override
		public void onTextChanged(CharSequence arg0, int start, int before, int count) {
			// TODO Auto-generated method stub
			String str = getText().toString();
			afterLen = str.length();
			if(MyLog.isDebugable()){
				MyLog.debug(TAG,"[onTextChanged]" + " before:" + before + " start:" + start + " count:" + count);
			}
			if(beforeLen != afterLen){
				String result = "";
				if(!TextUtils.isEmpty(str)){
						String strInput = getInputTxt();
						StringBuilder builder = new StringBuilder();
						for(int i = 0;i < strInput.length();i++){
							char c = strInput.charAt(i);
							if(i != 0 && i % 4 == 0){
								if(c != ' '){
									builder.append(" ");
								}
							}
							builder.append(strInput.charAt(i));
						}
						result = builder.toString();
				}
				
				if(!TextUtils.isEmpty(result)){
					mSelectIndex = BankInputEditTxt.this.getSelectionStart();
					removeTextChangedListener(txtWatcher);
					BankInputEditTxt.this.setText(result);
					try{
						int len = result.length();
						if(mSelectIndex + 1 >= len){
							BankInputEditTxt.this.setSelection(len);
						}else{
							BankInputEditTxt.this.setSelection(mSelectIndex);
						}
					}catch(Exception ee){
						MyLog.error(TAG,ee);
					}
					addTextChangedListener(txtWatcher);
				}
			}
			if(MyLog.isDebugable()){
				MyLog.debug(TAG,"[onTextChanged]" + " start:" + start + " before:" + before + " count:" + count + " indexSelect:" + mSelectIndex);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int start, int before, int count) {
			// TODO Auto-generated method stub
			this.beforeLen = BankInputEditTxt.this.getText().toString().length();
			if(MyLog.isDebugable()){
				MyLog.debug(TAG,"[beforeTextChanged]" + " beforeLen:" + beforeLen);
			}
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {	//最终
			// TODO Auto-generated method stub
		}
	};
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		removeTextChangedListener(txtWatcher);
		removeTextChangedListener(mIdWatcher);
	};

	/***
	 * 获取输入的内容,过滤空格数据
	 * @return
	 */
	public String getInputTxt(){
		String str = "";
		str = getText().toString();
		str = str.replaceAll(" ","");
		return str;
	}
}
