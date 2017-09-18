package com.common.http;

/***
 * Http请求单元
 * @date 2015/06/27
 * Params T --> 请求数据体
 */
public abstract class HttpBaseTask<T>{
	protected T t;
	
	public HttpBaseTask(T t){
		this.t = t;
	} 
	
	/***
	 * 整个请求业务都放置在这个地方执行
	 */
	public abstract void doTask();

	/***
	 * 内部引用的资源要进行释放掉
	 */
	public abstract void recyle();
}
