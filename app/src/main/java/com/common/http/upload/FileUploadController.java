package com.common.http.upload;

import com.bjzt.uye.global.MyLog;
import com.common.thread.ThreadPool;
import java.io.File;
import java.util.Vector;

public class FileUploadController extends Thread {
	private final String TAG = "FileUploadController";
	private static FileUploadController instance;
	private Vector<UploadEntity> mVector = new Vector<UploadEntity>();
	
	private FileUploadController() {
		// TODO Auto-generated constructor stub
	}
	
	public synchronized static final FileUploadController getInstance(){
		if(instance == null){
			instance = new FileUploadController();
		}
		return instance;
	}
	
	@Override
	public void run() {
		super.run();
		while(true){
			synchronized (mVector) {
				while(mVector.size() <= 0){
				     try {
				    	 mVector.wait();
				     	} catch (InterruptedException e) {
				     		e.printStackTrace();
				     	}
					 }
				 if(mVector.size() > 0){
					 final UploadEntity entity = mVector.remove(0);
				     ThreadPool.getInstance().submmitJob(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(20);
								} catch (InterruptedException ee) {
									MyLog.error(TAG, "", ee);
								}
								UploadUtils util = new UploadUtils();
								File file = new File(entity.filePath);
								util.uploadFile = file;
								util.setFileUploadListener(entity.mUploadListener);
								util.upload();
								util.setFileUploadListener(null);
							}
						});
				 }
			}
		}
	}
}
