package com.common.thread;

import com.bjzt.uye.global.Global;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/***
 * 图片线程池
 * @date 2015/10/06
 */
public class PicThreadPool {
	private static final String TAG = "ThreadPool";
	private static PicThreadPool instance;
	private ThreadPoolExecutor threadPool = null;
	private static final int CORE_POOL_SIZE = 3;
	private static final int MAX_POOL_SIZE = 6;
	private static final int KEEP_ALIVE_TIME = 10; // 10 seconds
	
	private PicThreadPool() {
		threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                new PriorityThreadFactory("picthread-pool",
                android.os.Process.THREAD_PRIORITY_BACKGROUND));
	}
	
	public static PicThreadPool getInstance(){
		if(instance == null){
			instance = new PicThreadPool();
		}
		return instance;
	}
	
	public void removeJob(Runnable task){
		threadPool.remove(task);
	}
	
	/***
	 * 线程池执行command
	 * @param r
	 */
	public void submmitJob(Runnable r){
		threadPool.execute(r);
	}
	
	/***
	 * 线程池delay millis执行
	 * @param r
	 * @param delayMillis
	 */
	public void submmitJobDelay(final Runnable r, long delayMillis){
		if(r != null){
			Global.postDelay(new Runnable() {
				@Override
				public void run() {
					threadPool.execute(r);
				}
			}, delayMillis);
		}
	}
}
