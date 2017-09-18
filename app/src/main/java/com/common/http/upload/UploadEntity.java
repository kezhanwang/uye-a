package com.common.http.upload;

import com.common.http.listener.IFileUploadListener;

public class UploadEntity {
	public String filePath;
	public long fileLength;
	public IFileUploadListener mUploadListener;
}
