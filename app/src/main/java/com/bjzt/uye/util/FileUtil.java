package com.bjzt.uye.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.common.common.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileUtil {
	
	private final static String TAG = "FileUtil";

	public static final long KB = 1024;
	public static final long MB = KB * 1024;

	/***
	 * 保存图片
	 * @param bitmap
	 * @return
	 */
	public static boolean saveBitmap(Bitmap bitmap, String url){
		boolean flag = false;
		if(SDCardUtil.isSDCardExist() && !TextUtils.isEmpty(url) && bitmap != null){
			String sdCardPath = SDCardUtil.getSDCardPath_Pic();
			String fileName = MD5.getMd5(url.getBytes());
			String filePath = sdCardPath + "/" + fileName;
			File file = new File(filePath);
			if(file != null && file.exists()){
				file.deleteOnExit();
			}
			FileOutputStream out = null;
			try{
				out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				flag = true;
			}catch(Exception ee){
				MyLog.error(TAG,"",ee);
			}finally{
				if(out != null){
					try {
						out.flush();
						out.close();
						out = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return flag;
	}

	public static Bitmap getThumbBitmap(String filePath){
		return getThumbBitmap(filePath,false);
	}

	public static Bitmap getThumbBitmap(String filePath, boolean isRect){
//		Bitmap bitmap = BitmapFactory.decodeFile(filePath);
		BitmapFactory.Options options = new BitmapFactory.Options(); 
		options.inPreferredConfig = Bitmap.Config.RGB_565;  
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inSampleSize = 6;
		Bitmap bitmap = null;
		try{
			bitmap = BitmapFactory.decodeFile(filePath,options);
		}catch(OutOfMemoryError error){
			MyLog.error(TAG,error);
			System.gc();
		}
//		Matrix m = null;
		if(bitmap != null){
//			ExifInterface exifInterface;
//			try {
//				exifInterface = new ExifInterface(filePath);
//				int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
//				//然后旋转：
//				int degree = 0;
//				if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
//					degree = 90;
//				} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
//					degree = 180;
//				} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
//					degree = 270;
//				}
//				if (degree != 0 && bitmap != null) {
//					m = new Matrix();
//					m.setRotate(degree,(float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
//				}
//			} catch (IOException e) {
//				MyLog.error(TAG,"[getThumbBitmap]",e);
//			}
			Bitmap result = null;
			if(bitmap != null && !bitmap.isRecycled()){
//				result = ImageUtil.reduce(bitmap,200,200,true,m);
				int width;
				int height = 200;
				if(isRect){
					width = 300;
				}else{
					width = 200;
				}
				try {
					result = BitmapUtil.compressImageByQuailty(bitmap, width, height, filePath, true);
				}catch(OutOfMemoryError ee){
					MyLog.error(TAG,ee);
					System.gc();
				}
				return result;
			}
		}
		return null;
	}
	
	/***
	 * 从文件中读取
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapFromSDCard(String url){
		Bitmap bitmap = null;
		if(SDCardUtil.isSDCardExist() && !TextUtils.isEmpty(url)){
			String sdCardPath = SDCardUtil.getSDCardPath_Pic();
			String fileName = MD5.getMd5(url.getBytes());
			String filePath = sdCardPath + "/" + fileName;
			File file = new File(filePath);
			if(file != null && file.exists()){
				try{
					bitmap = BitmapFactory.decodeFile(filePath);
				}catch(OutOfMemoryError ee){
					MyLog.error(TAG, ee);
					System.gc();
				}
			}
		}
		return bitmap;
	}
	
	public static String getPicFilePath(String url){
		String filePath = "";
		String fileName = MD5.getMd5(url.getBytes());
		filePath = SDCardUtil.getSDCardPath_Pic() + "/" + fileName;
		return filePath;
	}
	
	
	/***
	 * 获取照相机文件路径
	 * @return
	 */
	public static final String getCammeraImgPath(){
		String picName = "";
		picName = System.currentTimeMillis()+".jpg";
		String filePath = SDCardUtil.getSDCardCamerPath() + "/"+picName;
		return filePath;
	}

	/***
	 * 获取照相机文件夹
	 * @return
	 */
	public static final String getCammerFolderPath(){
		String filePath = SDCardUtil.getSDCardCamerPath();
		return filePath;
	}
	
	public static final String saveBitmapToCammera(Bitmap bitmap, String fileName,int quality){
		String filePath = "";
		if(!TextUtils.isEmpty(fileName)){
			filePath = getCammerFolderPath() + "/" + fileName;
		}else{
			filePath = getCammerFolderPath() + "/" + System.currentTimeMillis() + ".jpg";
		}
		File file = new File(filePath);
		if(file != null && file.exists()){
			file.deleteOnExit();
		}
		FileOutputStream out = null;
		boolean flag = false;
		try{
			out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
			flag = true;
		}catch(Exception ee){
			MyLog.error(TAG,"",ee);
		}finally{
			if(out != null){
				try {
					out.flush();
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(!flag){
			filePath = "";
		}
		return filePath;
	}

	private File mSrcFile;
	
	/****
	 * 获取图片保存路径
	 * @return
	 */
	public static final String getImgSavePath(){
		String fileName = System.currentTimeMillis() + ".jpg";
		return SDCardUtil.getSavePath() + "/" + fileName;
	}

	/***
	 * 文件拷贝
	 * @param srcPath
	 * @param destFilePath
	 */
	public static final void copyFile(String srcPath, String destFilePath){
		if(!TextUtils.isEmpty(destFilePath)){
			File file = new File(destFilePath);
			file.delete();
			InputStream inStream = null;
			FileOutputStream outStream = null;
			try {
				file.createNewFile();
				File sFile = new File(srcPath);
				byte[] buffer = new byte[1024];
				if(sFile.exists()){
					inStream = new FileInputStream(sFile);
					outStream = new FileOutputStream(file);
					int cnt = 0;
					while((cnt = inStream.read(buffer,0,buffer.length)) != -1){
						outStream.write(buffer,0,cnt);
						outStream.flush();
					}
				}
			} catch (IOException e) {
				MyLog.error(TAG,e);
			}finally {
				if(inStream != null){
					try {
						inStream.close();
					} catch (IOException e) {
						MyLog.error(TAG,e);
					}
				}
				if(outStream != null){
					try {
						outStream.flush();
						outStream.close();
					} catch (IOException e) {
						MyLog.error(TAG,e);
					}
				}
			}
		}
	}
	
	
	
	 public static boolean copyBigFile(String src, String des) {
	        boolean flag = true;
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	            File f1 = new File(src);
	            in = new FileInputStream(f1);
	            File f = new File(des);
	            if(!f.exists()){
	            	f.createNewFile();
	            }else{
	            	f.deleteOnExit();
	            	f.createNewFile();
	            }
	            out = new FileOutputStream(f);
	            byte[] b = new byte[2048];
	            int n;
	            long bytes = 0;
	            while ((n = in.read(b)) >= 0) {
	                bytes += n;
	                out.write(b, 0, n);
	            }
	        } catch (Exception e1) {
	            e1.printStackTrace();
	            flag = false;
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                    flag = false;
	                    e.printStackTrace();
	                }
	            }
	            if (out != null) {
	                try {
	                    out.close();
	                } catch (IOException e) {
	                    flag = false;
	                    e.printStackTrace();
	                }
	            }
	        }
	        return flag;
	    }


	public static final String getFileSizeStr(long size){
		String str = "";
		if(size >= FileUtil.MB){
			str = (size / (float)FileUtil.MB) + "MB";
		}else{
			str = (size/(float)KB) + "KB";
		}
		return str;
	}
}
