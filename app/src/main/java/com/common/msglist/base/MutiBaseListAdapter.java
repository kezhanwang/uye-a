package com.common.msglist.base;

import android.view.View;
import android.view.ViewGroup;

import com.common.msglist.MsgPageBottomView;

import java.util.List;

/***
 * 多个Item数据类型
 * @date 2015/10/14
 */
public abstract class MutiBaseListAdapter extends BaseListAdapter<BaseItemListener>{
	private final String TAG = "MutiBaseListAdapter";
	
	public static final int TYPE_FOOTER = 0;
	public static final int TYPE_HOME_HEADERVIEW = TYPE_FOOTER + 1;
	public static final int TYPE_HOME_ORDERIINFO = TYPE_HOME_HEADERVIEW + 1;
	public static final int TYPE_HOME_LOC = TYPE_HOME_ORDERIINFO + 1;

	public static final int[] TYPE_HOME = {TYPE_FOOTER,TYPE_HOME_HEADERVIEW,TYPE_HOME_ORDERIINFO,TYPE_HOME_LOC};

	public static final int TYPE_QA_HEADER = TYPE_FOOTER + 1;
	public static final int TYPE_QA_ITEM = TYPE_QA_HEADER + 1;
	public static final int TYPE_QA_BTN = TYPE_QA_ITEM + 1;
	public static final int[] TYPE_QA = {TYPE_FOOTER,TYPE_QA_HEADER,TYPE_QA_ITEM,TYPE_QA_BTN};

	private int[] mTypeArray = null;

	public static final int ADAPTER_TYPE_HOME = 1;
	public static final int ADAPTER_TYPE_QA = 2;

	public MutiBaseListAdapter(List<BaseItemListener> mList, int mTypeAdapter) {
		super(mList);
		// TODO Auto-generated constructor stub
		init(mTypeAdapter);
	}
	
	private void init(int mType){
		switch(mType){
			case ADAPTER_TYPE_HOME:
				mTypeArray = TYPE_HOME;
				break;
			case ADAPTER_TYPE_QA:
				mTypeArray = TYPE_QA;
				break;
		}
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		BaseItemListener item = (BaseItemListener) getItem(position);
		int mType;
		if(item != null){
			mType =  item.getType();
		}else{
			mType = TYPE_FOOTER;
		}
		return mType;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return mTypeArray.length;
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View mView = null;
		BaseItemListener itemEntity = (BaseItemListener) getItem(pos);
		int mType = getItemViewType(pos);
		BaseItemView<BaseItemListener> mItemView = null;
//		switch(mType){
//			case TYPE_FOOTER:
//				MsgPageBottomView bottomView = getBottomView();
//				mView = bottomView;
//				break;
//			case TYPE_PIC_BIG:
//			case TYPE_NORMAL:
//				if(convertView == null){
//					mItemView = getItemView(itemEntity);
//				}else{
//					mItemView = (BaseItemView<BaseItemEntity>) convertView;
//				}
//				mItemView.setPos(pos);
//				mItemView.setSize(getCount());
//				mItemView.setMsg(itemEntity);
//				mView = mItemView;
//				break;
//		}
		if(mType==TYPE_FOOTER){
			MsgPageBottomView bottomView = getBottomView();
			mView = bottomView;
		}else{
			if(convertView == null){
					mItemView = getItemView(itemEntity);
				}else{
					mItemView = (BaseItemView<BaseItemListener>) convertView;
				}
				mItemView.setPos(pos);
				mItemView.setSize(getCount());
				mItemView.setMsg(itemEntity);
				mView = mItemView;
		}
		return mView;
	}
}
