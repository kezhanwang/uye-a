package com.common.msglist;

import com.common.msglist.entity.PPageEntity;

public class PageType {
	//拉取首页数据
	public static final int FIRST_PAGE = 1;
	
	/***
	 * 获取下页
	 * @param page
	 * @return
	 */
	public static final int getNextPage(PPageEntity page){
		if(page != null){
			return page.p + 1;
		}
		return FIRST_PAGE;
	}
}
