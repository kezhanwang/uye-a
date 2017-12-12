package com.common.download;

import com.bjzt.uye.util.MD5;
import com.bjzt.uye.util.SDCardUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by billy on 2017/12/12
 */
public class DownloadController {
    private final String TAG = getClass().getSimpleName();
    private static DownloadController instance;
    private IDownloadListener mListener;

    private DownloadController(){

    }

    public static DownloadController getInstance(){
        if(instance == null){
            instance = new DownloadController();
        }
        return instance;
    }

    public void setListener(IDownloadListener mListener){
        this.mListener = mListener;
    }

    public void startDownloadFile(String url){
        String md5 = MD5.getMd5(url);
        String filePath = SDCardUtil.getSavePath() + "/" + md5;
        File file = new File(filePath);
        if(file.exists()){
            if(mListener != null) {
                mListener.downloadSucc(filePath);
            }
        }else{
            //开始下载
            startDownload(filePath,url);
        }
    }

    private void startDownload(String filePath,String url){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            RandomAccessFile savedFile = new RandomAccessFile(filePath,"rw");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {
                float totalSize = connection.getContentLength();
                if (totalSize <= 0) {
                    if(mListener != null){
                        mListener.downloadError("获取文件失败~");
                    }
                    return;
                }
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                byte[] buffer = new byte[1024];
                int hasDownload = 0;
                int read;
                int percent = 0;
                while ((read = bufferedInputStream.read(buffer)) != -1) {
                    savedFile.write(buffer, 0, read);
                    hasDownload += read;
                    int temp = (int) (((hasDownload) / totalSize) * 100);
                    if (percent != temp) {
                        percent = temp;
                        if(mListener != null){
                            mListener.downloading(percent);
                        }
                    }
                }
                bufferedInputStream.close();
                savedFile.close();
                if(mListener != null){
                    mListener.downloadSucc(filePath);
                }
            } else {
                if(mListener != null){
                    mListener.downloadError("下载失败:responseCode" + responseCode);
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            if(mListener != null){
                mListener.downloadError("下载失败:" + e.getMessage());
            }
        }
    }
}
