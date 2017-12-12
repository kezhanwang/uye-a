package com.common.download;

/**
 * Created by billy on 2017/12/12.
 */

public interface IDownloadListener {
    public void downloadError(String tips);
    public void downloadSucc(String filePath);
    public void downloading(int process);

}
