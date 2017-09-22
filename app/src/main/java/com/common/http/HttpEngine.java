package com.common.http;

import com.common.common.MyLog;
import com.common.common.NetCommon;
import com.common.file.SharePreDev;
import com.common.thread.ThreadPool;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/***
 * HttpEngin
 * 添加对应的任务到这个引擎中
 * @date 2015/06/27
 * @author billywen
 */
public class HttpEngine extends Thread {
	private final String TAG = "HttpEngine";
	
	private static HttpEngine instace;
	private Vector<HttpBaseTask<Object>> mVector = new Vector<HttpBaseTask<Object>>();
	private boolean isRunning = true;
//	private MyThread mThread;
	private Map<Integer,String> mMapUrl = new HashMap<Integer,String>();
	private Map<Integer,String> mSaasMapUrl = new HashMap<Integer,String>();
	private Map<Integer,String> mMapLoanUrl = new HashMap<Integer,String>();

	private int mDevType = 0;
	
	private HttpEngine() {
		// TODO Auto-generated constructor stub
		super("HttpEnginePool");
		//0->正式域名
		mMapUrl.put(NetCommon.NET_TYPE_INDEX_OFFICAL,"http://api2.kezhanwang.cn");
		//1->开发域名
		mMapUrl.put(NetCommon.NET_TYPE_INDEX_DEV,"http://dev.kezhanwang.cn");
		//2->测试域名 .app "http://test1.app.kezhanwang.cn/saas";
		mMapUrl.put(NetCommon.NET_TYPE_INDEX_TEST1,"http://dev.kezhanwang.cn");
		mMapUrl.put(NetCommon.NET_TYPE_INDEX_TEST3,"http://test3.app.kezhanwang.cn");
		mMapUrl.put(NetCommon.NET_TYPE_INDEX_TEST4,"http://test4.app.kezhanwang.cn");
		//3->预发布环境
		mMapUrl.put(NetCommon.NET_TYPE_PRE, "http://uat.kezhanwang.cn");


		setPriority(Thread.MAX_PRIORITY);
	}

	/***
	 * 启动Http引擎
	 */
	public void startEngine(){
		this.start();
	}
	
	@Override
	public void run() {
		super.run();
		while(isRunning){
			synchronized (mVector) {
				while(mVector.size() <= 0){
				     try {
				    	 mVector.wait();
				     	} catch (InterruptedException e) {
				     		e.printStackTrace();
				     	}
					 }
				 if(mVector.size() > 0){
					 final HttpBaseTask task = mVector.remove(0);
					 try {
						mVector.wait(10);
					 } catch (InterruptedException ee) {
							MyLog.error(TAG,ee);
					 }
				     ThreadPool.getInstance().submmitJob(new Runnable() {
							@Override
							public void run() {
								task.doTask();
								task.recyle();
							}
						});
				 }
			}
		}
	}
	
	public static final HttpEngine getInstance(){
		if(instace == null){
			instace = new HttpEngine();
		}
		return instace;
	}

	/***
	 * 添加Http任务
	 * @param task
	 */
	public void addTask(HttpBaseTask<Object> task){
		synchronized (mVector) {
			if(MyLog.isDebugable()){
				MyLog.debug(TAG,"[addTask]" + " size:" + mVector.size());
			}


			mVector.add(task);
			mVector.notify();

		}
	}
	
	public void stopAll(){
		isRunning = false;
	}
	
	public String getRefer(int interfaceType){
		if(interfaceType == NetCommon.NET_INTERFACE_TYPE_KEZHAN) {
			return mMapUrl.get(mDevType);
		} else if(interfaceType == NetCommon.NET_INTERFACE_TYPE_SAAS){
			return mSaasMapUrl.get(mDevType);
		} else if(interfaceType == NetCommon.NET_INTERFACE_TYPE_LOAN){
			return mMapLoanUrl.get(mDevType);
		}else{
			return mMapUrl.get(mDevType);
		}
	}

	/***
	 * 更改开发测试环境
	 * 0 --> 正式环境
	 * 1 --> 开发环境
	 * 2 --> 测试环境
	 * @param index
	 */
	public void changeDev(int index){
		this.mDevType = index;
		SharePreDev mSharePre = new SharePreDev();
		mSharePre.saveDevIndex(index);
	}

	/***
	 * 在开始启动时候进行，修改状态
	 */
	public void loadDev(){
		SharePreDev mSharePre = new SharePreDev();
		int index = mSharePre.getDevIndex();
		this.mDevType = index;
	}

	public boolean isOffical(){
		if(this.mDevType == NetCommon.NET_TYPE_INDEX_OFFICAL){
			return true;
		}
		return false;
	}
}
