package com.common.msglist;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bjzt.uye.R;
import com.bjzt.uye.global.Global;
import com.bjzt.uye.global.MConfiger;
import com.common.common.Common;
import com.common.common.MyLog;
import com.common.listener.NoConfusion;
import com.common.msglist.base.BaseItemView;
import com.common.msglist.base.BaseListAdapter;
import com.common.msglist.listener.IListAdapterListener;
import com.common.msglist.listener.IPageScrollListener;
import com.common.msglist.listener.IPageScrollUpListener;
import com.common.msglist.listener.IRefreshListener;
import com.common.msglist.swipe.SwipeMenu;
import com.common.msglist.swipe.SwipeMenuAdapter;
import com.common.msglist.swipe.SwipeMenuCreator;
import com.common.msglist.swipe.SwipeMenuItem;
import com.common.msglist.swipe.SwipeMenuListView;
import com.common.msglist.views.ListViewEmptyView;
import com.common.util.Utils;

/***
 * 下拉刷新ListView控件控件
 * @date 2015/07/16
 */
public class MsgPage extends RelativeLayout implements NoConfusion {
	private final String TAG = "MsgPage";
	
	private ListView mListView = null;
	private SwipeMenuListView mSwipeListView;
	private NLPullRefreshView mRefreshView = null;
	private IRefreshListener mRefreshListener = null;
	private ListViewEmptyView listView_Empty;
	private boolean needPageScroll;	//是否需要监听列表滑动事件
	private boolean autoLoadMore;	//滑动到底部自动加载更多
	private boolean mLockLoadMore;
	private int mType;
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_SWIPE = 1;
	private int autoLoadCnt = MConfiger.AUTO_LOAD_ITEM_CNT;
	private boolean isShowToast = true;
	private IPageScrollListener mPageScrollListener;

	public MsgPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(attrs);
	}

	public MsgPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(attrs);
	}
	
	public MsgPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(null);
	}

	public void setAutoloadItemCnt(int cnt){
		this.autoLoadCnt = cnt;
	}

	public void setIsShowToast(boolean isShowToast){
		this.isShowToast = isShowToast;
	}

	public void setRefreshListener(IRefreshListener refreshListener){
		this.mRefreshListener = refreshListener;
		if(mRefreshView != null){
			mRefreshView.setRefreshListener(refreshListener);
		}
		ListAdapter adapter = mListView.getAdapter();
		if(adapter != null && adapter instanceof BaseListAdapter){
			BaseListAdapter<?> baseLA = (BaseListAdapter<?>) adapter;
			baseLA.setRefreshListener(refreshListener);
		}
	}
	
	/***
	 * 多个Adapter对应一个MsgPage
	 * 不同Adapter需要不同是否需要LoadMore状态信息
	 * @param autoLoadMore
	 */
	public void reSetAutoLoadMore(boolean autoLoadMore){
		this.autoLoadMore = autoLoadMore;
		ListAdapter adapter = mListView.getAdapter();
		if(adapter != null && adapter instanceof BaseListAdapter){
			BaseListAdapter<?> baseLA = (BaseListAdapter<?>) adapter;
			baseLA.setRefreshListener(this.mRefreshListener);
		}
	}
	
	public void setNeedPageScrollListener(boolean needPageScroll){
		this.needPageScroll = needPageScroll;
	}
	
	public void setAutoLoadMore(boolean autoLoadMore){
		this.autoLoadMore = autoLoadMore;
	}
	
	private void init(AttributeSet attrs){
		if(attrs != null){
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.mytype);
			int mType = typedArray.getInt(R.styleable.mytype_mType,0);
			this.mType = mType;
			typedArray.recycle();
		}
		LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(mType == TYPE_SWIPE){
			li.inflate(R.layout.msg_page_swipe,this,true);
		}else{
			li.inflate(R.layout.msg_page,this,true);
		}
		initListView();
	}

	public ListView getListView(){
		return mListView;
	}

	/***
	 * 不挂有延迟的刷新操作
	 * @param isNoDelayFinish
	 */
	public void setNoDelayFinish(boolean isNoDelayFinish){
		mRefreshView.setNoDelayFinish(isNoDelayFinish);
	}
	
	/***
	 * 是否打开下拉刷新功能
	 * @param canPullDown 默认为true可以下拉刷新，false为不能下拉刷新操作
	 */
	public void setEnablePullDown(boolean canPullDown){
		mRefreshView.setEnablePullDown(canPullDown);
	}
	
	public void setDividerHeight(int height){
		getListView().setDividerHeight(height);
	}
	
	public void setDivider(Drawable d){
		getListView().setDivider(d);
	}

	public IPageScrollUpListener mScrollDownListener = new IPageScrollUpListener() {
		@Override
		public void onScrollUp() {
			// TODO Auto-generated method stub
			mRefreshView.onPageScrollUp();
		}
	};
	
	/***
	 * 设置滚动条是否显示
	 * @param isEnable
	 */
	public void setListViewScrollBar(boolean isEnable){
		mListView.setVerticalScrollBarEnabled(isEnable);
	}
	
	private void refreshData(){
		int childCnt = mListView.getChildCount();
		for(int i = 0;i < childCnt;i++){
			//停止后，在进行滑动列表，不进行设置数据
			if(!Common.isPageStop){
				return;
			}
			View view = mListView.getChildAt(i);
			if(view != null && view instanceof BaseItemView){
				BaseItemView<Object> baseItemView = (BaseItemView<Object>) view;
				Object o = baseItemView.getMsg();
				baseItemView.setMsg(o);
			}
		}
	}
	
	private void initListView(){
		mRefreshView = (NLPullRefreshView) this.findViewById(R.id.refresh_root);
		
		mListView = (ListView) this.findViewById(R.id.listview);
//		View emptyView = this.findViewById(R.id.listview_emptyview);
//		mListView.setEmptyView(emptyView);
		listView_Empty=(ListViewEmptyView) this.findViewById(R.id.listView_Empty);
		mListView.setDividerHeight(0);
		if(mType == TYPE_NORMAL){
			mListView.setAdapter(null);
		}
		mListView.setVerticalScrollBarEnabled(false);
		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) {
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					Common.isPageStop = true;
					if(mRefreshListener != null){
						mRefreshListener.onPageStop();
					}
					refreshData();
					//滚动到底部事件通知
					if(autoLoadMore && !mLockLoadMore && listView.getCount() >= autoLoadCnt){
						if(listView.getLastVisiblePosition() == listView.getCount() - 1){
							if(mRefreshListener != null){
								int state = -1;
								ListAdapter mAdapter = getListView().getAdapter();
								if(mAdapter instanceof HeaderViewListAdapter){
									HeaderViewListAdapter mHeaderListAdapter = (HeaderViewListAdapter) mAdapter;
									mAdapter = mHeaderListAdapter.getWrappedAdapter();
								}
								if(mAdapter != null && mAdapter instanceof BaseListAdapter){
									BaseListAdapter<?> bLA = (BaseListAdapter<?>) mAdapter;
									state = bLA.getBottomState();
								}
								mLockLoadMore = true;
								mRefreshListener.bottomClick(state);
							}
						}
					}
				}else{
					Common.isPageStop = false;
					if(needPageScroll){
						mRefreshListener.onPageScroll();
					}
				}
				//
				if(mPageScrollListener != null){
					mPageScrollListener.onScrollChangeState(scrollState);
				}
			}
			
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
		if(mListView instanceof ListViewV6){
			ListViewV6 listViewV6 = (ListViewV6) mListView;
			listViewV6.setPageScrollUpListener(mScrollDownListener);
		}
	}
	
	/***
	 * 页面滑动停止
	 */
	public void onPageStop(){
		refreshData();
	}
	
	public void setIScrollListener(IPageScrollListener mListener){
		if(mListView instanceof ListViewV6){
			ListViewV6 listViewV6 = (ListViewV6) mListView;
			listViewV6.setIPageListener(mListener);
		}
		this.mPageScrollListener = mListener;
	}
	
	public void setListAdapter(BaseAdapter baseAdapter){
		mListView.setAdapter(baseAdapter);
		if(mRefreshListener != null && baseAdapter instanceof BaseListAdapter){
			BaseListAdapter<?> baseLA = (BaseListAdapter<?>) baseAdapter;
			baseLA.setRefreshListener(mRefreshListener);
		}
		if(autoLoadMore && baseAdapter instanceof BaseListAdapter){
			BaseListAdapter<?> baseLA = (BaseListAdapter<?>) baseAdapter;
			baseLA.setListAdapterListener(mAdapterListener);
		}
	}
	
	private IListAdapterListener mAdapterListener = new IListAdapterListener(){
		@Override
		public void resetLock() {
			mLockLoadMore = false;
		}
		@Override
		public void lockPage() {
			mLockLoadMore = true;
		}
	};
	
	public void completeRefresh(boolean isSucc){
		if(mRefreshView != null){
			mRefreshView.finishRefresh(isSucc,isShowToast);
		}
		//如果只有两三项时候，手动强制刷新
		ListAdapter baseAdapter = mListView.getAdapter();
		if(baseAdapter != null){
			int cnt = baseAdapter.getCount();
			//不满一屏幕,经验值判断
			if(cnt <= 4){
				refreshData();
			}
		}
	}
	
	public void recyle(){
		if(mRefreshView != null){
			mRefreshView.recyle();
		}
		mRefreshListener = null;
		mAdapterListener = null;
	}
	
	/***
	 * 添加HeaderView
	 * @param view
	 */
	public void addHeaderView(View view){
		getListView().addHeaderView(view);
	}
	
	/****
	 * 添加FooterView
	 * @param view
	 */
	public void addFooterView(View view){
		getListView().addFooterView(view);
	}
	
	/***
	 * 更新底部状态
	 * @param state
	 */
	public void updateState(int state){
		ListAdapter mAdapter = getListView().getAdapter();
		if(mAdapter instanceof HeaderViewListAdapter){
			HeaderViewListAdapter mHeaderListAdapter = (HeaderViewListAdapter) mAdapter;
			mAdapter = mHeaderListAdapter.getWrappedAdapter();
		}
		if(mAdapter != null && mAdapter instanceof BaseListAdapter){
			BaseListAdapter<?> bLA = (BaseListAdapter<?>) mAdapter;
			bLA.updateState(state);
		}
	}
	
	public void setEmpty(int type){
		ListAdapter mAdapter = mListView.getAdapter();
		if(mAdapter instanceof HeaderViewListAdapter){
			HeaderViewListAdapter headerAdapter = (HeaderViewListAdapter) mAdapter;
			mAdapter = headerAdapter.getWrappedAdapter();
		}
		if(mAdapter != null && mAdapter instanceof BaseListAdapter){
			BaseListAdapter<?> bLA = (BaseListAdapter<?>)mAdapter;
			bLA.reSetList(null);
		}else if(mAdapter != null && mAdapter instanceof SwipeMenuAdapter){
			SwipeMenuAdapter swAdapter = (SwipeMenuAdapter) mAdapter;
			if(swAdapter != null && swAdapter.getAdapter() instanceof BaseListAdapter<?>){
				BaseListAdapter<?> mBaseListAdapter = (BaseListAdapter<?>) swAdapter.getAdapter();
				if(mBaseListAdapter != null){
					mBaseListAdapter.reSetList(null);
				}
			}
		}
		listView_Empty.setType(type);
		mListView.setEmptyView(listView_Empty);
	}
	
	public void removeEmptyView(){
		mListView.removeView(listView_Empty);
	}
	
	public void initCreator(){
		if(mType == TYPE_SWIPE){
			SwipeMenuListView mList = (SwipeMenuListView) getListView();
			SwipeMenuCreator creator = getCreator();
			mList.setMenuCreator(creator);
		}else{
			Exception e = new Exception("mType should TYPE_SWIPE");
			MyLog.error(TAG,e);
		}
	}
	
	private SwipeMenuCreator getCreator(){
		SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(Global.mContext.getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
//                // set item width
//                openItem.setWidth(Utils.dp2px(90));
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(Global.mContext.getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Utils.dp2px(60));
                // set a icon
                deleteItem.setIcon(R.drawable.icon_del);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        return creator;
	}
	
	/****
	 * 点击右侧删除按钮
	 * @param onMenuListener
	 */
	public void setOnMenuItemClickListener(SwipeMenuListView.OnMenuItemClickListener onMenuListener){
		if(mType == TYPE_SWIPE){
			SwipeMenuListView mList = (SwipeMenuListView) getListView();
			mList.setOnMenuItemClickListener(onMenuListener);
		}
	}
}
