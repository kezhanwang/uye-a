package com.bjzt.uye.util;

import android.text.TextUtils;

import com.bjzt.uye.entity.PUploadEntity;
import com.bjzt.uye.http.HttpCommon;
import com.bjzt.uye.http.listener.IUploadListener;
import com.common.common.MyLog;
import com.common.controller.LoginController;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * 图片上传工具类
 * Created by diaosi on 2016/9/24.
 */
public class UploadFileUtil {
    private static final String TAG = "uploadFile";
    private final String NET_URL_UPLOADFILE = HttpCommon.getUploadPicUrl();
    private IUploadListener mListener;

    private final int TIME_OUT = 1000 * 10;

    /* 上传文件至Server的方法 */
    public PUploadEntity upload(File uploadFile, String key, String picName, int mType, IUploadListener mListener){
        this.mListener = mListener;
        long totalSize = uploadFile.length();

        PUploadEntity mEntity = new PUploadEntity();
        String end ="\r\n";
        String twoHyphens ="--";
        String boundary ="*****";
        try
        {
            URL url =new URL(NET_URL_UPLOADFILE);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            /**设置连接超时**/
            con.setConnectTimeout(TIME_OUT);
            con.setReadTimeout(TIME_OUT);
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            /* 设置传送的method=POST */
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
            //设置cookie
            String cookie = LoginController.getInstance().getCookie();
            if(!TextUtils.isEmpty(cookie)){
                con.setRequestProperty("Cookie",cookie);
            }
            /* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\""+key+"\";filename=\"" + picName +"\""+ end);
            ds.writeBytes("Content-Type: image/jpeg" + end);
            ds.writeBytes(end);
            /* 取得文件的FileInputStream */
            FileInputStream fStream =new FileInputStream(uploadFile);
            /* 设置每次写入1024bytes */
            int bufferSize =1024;
            byte[] buffer = new byte[bufferSize];
            int length =-1;
            int curSize = 0;
            /* 从文件读取数据至缓冲区 */
            while((length = fStream.read(buffer)) != -1){
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
                curSize += length;
                if(mListener != null){
                    mListener.upload(curSize,totalSize,key,mType);
                }
                //防止假卡死现象
//                Thread.sleep(5);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) !=-1 ){
                b.append( (char)ch );
            }
            String str = new String(b.toString().getBytes(),"UTF-8");
            /* 将Response显示于Dialog */
            if(!TextUtils.isEmpty(str)){
                JSONObject jsonObj = new JSONObject(str);
                String msg = jsonObj.getString("msg");
                mEntity.msg = msg;
                String code = jsonObj.getString("code");
                if(!TextUtils.isEmpty(code) && code.equals("0")){
                    mEntity.isSucc = true;
                }else{
                    mEntity.isSucc = false;
                }
                JSONObject jsonData = jsonObj.getJSONObject("data");
                Iterator<?> it = jsonData.keys();
                if(it != null){
                    if(it.hasNext()){
                        key = it.next().toString();
                        String val = (String) jsonData.get(key);
                        mEntity.key = key;
                        mEntity.url = val;
                    }
                }
                //判断链接是否是合法链接
                if(TextUtils.isEmpty(mEntity.url)){
                   mEntity.isSucc = false;
                   if(TextUtils.isEmpty(mEntity.msg)){
                       mEntity.msg = "上传图片获取图片链接异常~";
                   }
                }
                if(MyLog.isDebugable()){
                    MyLog.debug(TAG,"[upload]" + jsonData.toString() + " key:" + mEntity.key + " val:" + mEntity.url);
                }
            }else{
                mEntity = new PUploadEntity();
                mEntity.isSucc = false;
                mEntity.msg = "上传图片失败~";
            }
            /* 关闭DataOutputStream */
            ds.close();
        }
        catch(Exception e) {
            MyLog.debug(TAG,"[uploadFile]" + "文件上传失败:"  + e.getMessage());
            mEntity.msg = "文件上传失败[" + e.getMessage() + "]";
            mEntity.isSucc = false;
            MyLog.error(TAG,e);
        }
        return mEntity;
    }

//    public void upload(File uploadFile,String picName){
//        if(uploadFile != null){
//            maxSize = uploadFile.length();
//        }
//        long curSize = 0;
//        DataOutputStream fileOutput = null;
//        String boundary = UUID.randomUUID().toString();
//        String PREFIX = "--" , LINE_END = "\r\n";
//        String CONTENT_TYPE = "multipart/form-data"; //内容类型
//
//        try{
//            URL url = new URL(NET_URL_UPLOADFILE);
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            /* 允许Input、Output，不使用Cache */
//            con.setDoInput(true);
//            con.setDoOutput(true);
//            con.setUseCaches(false);
//	        /* 设定传送的method=POST */
//            con.setRequestMethod("POST");
//	        /* setRequestProperty */
//            con.setRequestProperty("Connection", "Keep-Alive");
//            con.setRequestProperty("Charset", "UTF-8");
////            con.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
//            con.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + boundary);
////            con.setRequestProperty("Content-Type","image/jpeg");
////            con.setRequestProperty("Content-Disposition","form-data;name=school_pic;filename=" + picName);
//
//            //文件设置在流里面
//            StringBuffer sb = new StringBuffer();
//            sb.append(PREFIX);
//            sb.append(boundary);
//            sb.append(LINE_END);
//
//            /**
//             * 这里重点注意：
//             * name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
//             * filename是文件的名字，包含后缀名的 比如:abc.png
//             */
//            sb.append("Content-Disposition: form-data; name=\"" + "school_pic" + "\"" + "; filename=\""+"aa.jpg"+"\"" + LINE_END);
//            sb.append("Content-Type: application/octet-stream; charset=utf-8; " + LINE_END);
////            sb.append("Content-Tye: image/jpeg" + LINE_END);
//            sb.append(LINE_END.getBytes());
//
//	        /* 设定DataOutputStream */
//            fileOutput = new DataOutputStream(con.getOutputStream());
//            //文件头推到服务器端
//            fileOutput.write(sb.toString().getBytes());
//
//	        /* 取得文件的FileInputStream */
//            FileInputStream fStream = new FileInputStream(uploadFile);
//	        /* 设定每次写入1024bytes */
//            int bufferSize = 1024;
//            byte[] buffer = new byte[bufferSize];
//            int length = -1;
//	        /* 从文件读取数据到缓冲区 */
//            while ((length = fStream.read(buffer)) != -1)
//            {
//	            /* 将数据写入DataOutputStream中 */
//                fileOutput.write(buffer, 0, length);
//                curSize += length;
//                if(mListener != null){
//                    mListener.upload(curSize,maxSize);
//                }
//                MyLog.debug(TAG,"[upload]" + " 文件上传:" + curSize + " maxSize:" + maxSize);
//            }
//
//            fileOutput.write(LINE_END.getBytes());
//            byte[] end_data = (PREFIX + boundary + PREFIX + LINE_END).getBytes();
//            fileOutput.write(end_data);
//
//            fileOutput.flush();
//            fStream.close();
//	        /* 取得Response内容 */
//            InputStream is = con.getInputStream();
//            int ch;
//            StringBuffer b = new StringBuffer();
//            while ((ch = is.read()) != -1){
//                b.append((char) ch);
//            }
//            String str = b.toString();
//            MyLog.debug(TAG,"[uploadFile]" + "上传成功...str:" + str);
//            //}else{
////            MyLog.debug(TAG,"[uploadFile]" + " upload error...");
//            //}
//        } catch (Exception e){
//            MyLog.error(TAG,"[uploadFile]" + "上传失败..." + e.getMessage(),e);
//        }finally{
//            if(fileOutput != null){
//                try {
//                    fileOutput.close();
//                } catch (IOException e) {
//                    MyLog.error(TAG, e);
//                }
//            }
//        }
//    }
}
