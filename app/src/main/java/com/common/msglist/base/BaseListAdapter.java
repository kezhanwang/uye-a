package com.common.msglist.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.bjzt.uye.global.Global;
import com.common.msglist.MsgPageBottomView;
import com.common.msglist.entity.PPageEntity;
import com.common.msglist.listener.IListAdapterListener;
import com.common.msglist.listener.IRefreshListener;
import java.util.List;


public abstract class BaseListAdapter<T> extends BaseAdapter {
	private final String TAG = "BaseListAdapter";
	private List<T> mList;
	private final int TYPE_FOOTER = 0;
	private final int TYPE_ITEM_NORMAL = 1;
	private MsgPageBottomView mBottomView;
	private PPageEntity mPageEntity;	//翻页的标记
	private IRefreshListener mRefreshListener;
	private IListAdapterListener mAdapterListener;
	
	private int mType = ADAPTER_TYPE_NORMAL;
	
	public static final int ADAPTER_TYPE_NORMAL = 10;
	public static final int ADAPTER_TYPE_NO_BOTTOM = 11;
	
	public BaseListAdapter(List<T> mList) {
		this.mList = mList;
	}

	public void appendList(List<T> mList){
		if(this.mList == null){
			this.mList = mList;
		}else{
			this.mList.addAll(mList);
		}
		notifyDataSetChanged();
	}
	
	public void reSetList(List<T> mList){
		this.mList = mList;
		notifyDataSetChanged();
		if(mAdapterListener != null){
			mAdapterListener.resetLock();
		}
	}

	public void addItem(T t){
		if(this.mList != null){
			mList.add(t);
			notifyDataSetChanged();
		}
	}
	
	public void removeItem(T t){
		if(this.mList != null){
			this.mList.remove(t);
			notifyDataSetChanged();
		}
	}
	
	public void removeItem(T t,boolean needNotify){
		if(this.mList != null){
			this.mList.remove(t);
		}
	}
	
	public List<T> getList(){
		return this.mList;
	}
	
	public void setRefreshListener(IRefreshListener mListener){
		this.mRefreshListener = mListener;
	}
	
	public void setListAdapterListener(IListAdapterListener mListener){
		this.mAdapterListener = mListener;
	}
	
	public void recyle(){
		this.mAdapterListener = null;
		this.mRefreshListener = null;
	}

	/***
	 * 设置分页标志位
	 * @param entity
	 */
	public void updatePageFlag(PPageEntity entity){
		this.mPageEntity = entity;
		boolean hasNext = this.mPageEntity.hasNext();
		if(hasNext){
			setType(BaseListAdapter.ADAPTER_TYPE_NORMAL);
			updateState(MsgPageBottomView.STATE_LISTVIEW_INIT);
		}else{
			setType(BaseListAdapter.ADAPTER_TYPE_NO_BOTTOM);
			updateState(MsgPageBottomView.STATE_LISTVIEW_NOMORE);
		}
	}

	/***
	 * 获取分页标志位
	 * @return
	 */
	public PPageEntity getPageFlag(){
		return this.mPageEntity;
	}
	
	public void setType(int mType){
		this.mType = mType;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(this.mList != null){		//footer也计算在内
			if(mType == ADAPTER_TYPE_NO_BOTTOM){
				return this.mList.size();
			}else{
				return this.mList.size() + 1;
			}
		}
		return 0;
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		if(pos >= 0 && this.mList != null && mList.size() > 0 && pos < this.mList.size()){
			return this.mList.get(pos);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	protected MsgPageBottomView getBottomView(){
		if(mBottomView == null){
			mBottomView = new MsgPageBottomView(Global.mContext);
			mBottomView.setListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(mAdapterListener != null){
						mAdapterListener.lockPage();
					}
					if(mRefreshListener != null){
						mRefreshListener.bottomClick(mBottomView.getState());
					}
				}
			});
		}
		return mBottomView;
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if(mType == ADAPTER_TYPE_NO_BOTTOM){
			return TYPE_ITEM_NORMAL;
		}else{
			if (position == getCount() - 1) {
				return TYPE_FOOTER;
			}
		}
		return TYPE_ITEM_NORMAL;
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View mView = null;
		if(getItemViewType(pos) == TYPE_ITEM_NORMAL){
			T t = (T) getItem(pos);
			BaseItemView<T> mItemView = null;
			if(convertView == null){
				mItemView = getItemView(t);
			}else{
				mItemView = (BaseItemView) convertView;
			}
			mItemView.setPos(pos);
			mItemView.setSize(getCount());
			mItemView.setMsg(t);
			mView = mItemView;
		}else {
			//footer item
			if(mType == ADAPTER_TYPE_NORMAL) {
				MsgPageBottomView bottomView = getBottomView();
				mView = bottomView;
			}
		}
		return mView;
	}
	
	/***
	 * 更新底部状态
	 * @param state
	 */
	public void updateState(int state){
		if(mBottomView != null){
			mBottomView.updateState(state);
		}
		//防止两次loading状态
		if(state != MsgPageBottomView.STATE_LISTVIEW_LOADING){
			if(mAdapterListener != null){
				mAdapterListener.resetLock();
			}
		}
	}
	
	/****
	 * 获取底部状态
	 * @return
	 */
	public int getBottomState(){
		if(mBottomView != null){
			return mBottomView.getState();
		}
		return -1;
	}
	
	protected abstract BaseItemView<T> getItemView(T t);
}
