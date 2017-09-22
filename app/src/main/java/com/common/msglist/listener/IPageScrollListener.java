package com.common.msglist.listener;

public abstract class IPageScrollListener {
	public static final int PAGE_MAX_OFFET = 10000;
	
	public void onPageScroll(int curY){};

	public void onScrollChangeState(int state){};
}
