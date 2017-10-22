package com.bjzt.uye.photo.pic;

import java.util.LinkedHashMap;

/***
 * 图片缓存
 * @date 2015/08/14
 */
public class LoanImageCache extends LinkedHashMap<String,LoanPicEntityShow> {
	private static final long serialVersionUID = 1L;
	private int maxSize;
	
	public LoanImageCache(int maxSize) {
		// TODO Auto-generated constructor stub
		this.maxSize = maxSize;
	}
	
	@Override
	protected boolean removeEldestEntry(Entry<String, LoanPicEntityShow> eldest) {
		// TODO Auto-generated method stub
		boolean isOver = size() > maxSize;
		if(isOver){
			LoanPicEntityShow entity = eldest.getValue();
			if(entity != null && entity.bitmap != null && !entity.bitmap.isRecycled()){
				entity.bitmap = null;
			}
		}
		return isOver;
	}
}
