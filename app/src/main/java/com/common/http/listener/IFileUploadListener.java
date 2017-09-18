package com.common.http.listener;

public interface IFileUploadListener {
	public void uploadSucc();
	public void uploadProgress(int progress, int total);
	public void uploadError(int errorCode);
}
