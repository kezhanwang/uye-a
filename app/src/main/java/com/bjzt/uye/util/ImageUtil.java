package com.bjzt.uye.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.bjzt.uye.comparator.FileComparator;
import com.bjzt.uye.entity.VAblumItemEntity;
import com.bjzt.uye.photo.entity.LoanVAblumItemEntity;
import com.common.common.MyLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

/***
 * 图片工具类
 */
public class ImageUtil {
	private static final String TAG = "ImageUtil";
	
	public static final int TYPE_BITMAP_ROUND_CORNER = 1;	//圆角图片
	public static final int TYPE_BITMAP_ROUND = 2;			//圆形图片
	
	/***
	 * 获得圆形bitmap,图片操作是耗时操作，适合放置在异步线程处理
	 * @param bitmap
	 * @return
	 */
	public static Bitmap GetRoundedCornerBitmap(final Bitmap bitmap, int type) {
	    try {  
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);
	        final Paint paint = new Paint();
	        int offsetX = 1;
	        final Rect rect = new Rect(0, 0, bitmap.getWidth()-offsetX,bitmap.getHeight()-offsetX);
	        final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth()-offsetX,bitmap.getHeight()-offsetX));
	        float roundPx = 0;
	        if(type == TYPE_BITMAP_ROUND){
	        	roundPx = bitmap.getWidth()/2;
	        }else if(type == TYPE_BITMAP_ROUND_CORNER){
	        	roundPx = 12;
	        }
	        paint.setAntiAlias(true);  
	        canvas.drawARGB(0, 0, 0, 0);  
	        paint.setColor(Color.BLACK);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        final Rect src = new Rect(0, 0, bitmap.getWidth()-offsetX,bitmap.getHeight()-offsetX);
	        canvas.drawBitmap(bitmap, src, rect, paint);
	        
	        //
	        Paint mPaint = new Paint();
	        mPaint.setColor(Color.WHITE);
	        mPaint.setStyle(Style.STROKE);
	        mPaint.setStrokeWidth(2);
	        mPaint.setAntiAlias(true);
	        float cx = bitmap.getWidth()/2;
	        float cy = bitmap.getHeight()/2;
	        float radius = bitmap.getWidth()/2-1;
	        canvas.drawCircle(cx, cy, radius,mPaint);
	        return output;
	    } catch (Exception e) {
	        return bitmap;  
	    }finally{
//	    	//原始图片释放资源
//	    	if(bitmap != null && !bitmap.isRecycled()){
//	    		bitmap.recycle();
//	    	}
	    }
	}
	
	
	
	/** 
     * 压缩图片 
     * @param bitmap 源图片 
     * @param width 想要的宽度 
     * @param height 想要的高度 
     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩 
     * @return Bitmap 
     */  
    public static Bitmap reduce(Bitmap bitmap, int width, int height, boolean isAdjust, Matrix m) {
        // 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图  
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) {return bitmap;}  
        // 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor, int scale, int roundingMode);  
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃  
        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸  
            sx = (sx < sy ? sx : sy);sy = sx;// 哪个比例小一点，就用哪个比例  
        }  
        Matrix matrix = null;
        if(m == null){
        	matrix = new Matrix();
        }else{
        	matrix = m;
        }
        matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    
    /***
     * 保存图片到其他接口
     * @return
     */
    public static boolean saveBitmap2File(String filePath, Bitmap bitmap){
    	File file = new File(filePath);
		if(file != null && file.exists()){
			file.deleteOnExit();
		}
		FileOutputStream out = null;
		boolean flag = false;
		try{
			out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG,90,out);
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
		return flag;
    }
    
    /***
     * 获取照相机返回的地址路径
     * @param mContext
     * @param data
     * @return
     */
    public static final String getUriPath(Activity mContext, Intent data){
    	Uri uri = data.getData();
        String[] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.managedQuery( uri,
                proj,                 // Which columns to return  
                null,       // WHERE clause; which rows to return (all rows)  
                null,       // WHERE clause selection arguments (none)  
                null);                 // Order-by clause (ascending by name)  
          
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();  
        String path = cursor.getString(column_index);
        return path;
    }
    
    /***
     * 获取相机最新的图片地址
     * @return
     */
    public static final String getCammerLatestPath(){
    	String path = "";
    	String result = "";
    	path = FileUtil.getCammerFolderPath();
    	File file = new File(path);
		ArrayList<File> array = new ArrayList<>();
    	if(file.exists()){
    		File[] tempArray = file.listFiles();
    		for(int i = 0;i < tempArray.length;i++){
    			array.add(tempArray[i]);
    		}
    		//按照创建时间排序
    		FileComparator fileC = new FileComparator();
    		Collections.sort(array,fileC);
    	}
    	if(array.size() > 0){
    		file = array.get(array.size()-1);
    		result = file.getAbsolutePath();
    	}
    	return result;
    }

	public static final boolean isFromGallery(Context mContext, Intent intent){
		boolean flag = false;
		if(intent != null && intent.getSerializableExtra(IntentUtils.PARA_KEY_PUBLIC) != null){
			flag = true;
		}
		return flag;
	}
    /***
     * 获取相册返回的图片地址
     * @param mContext
     * @return
     */
    public static final ArrayList<String> getGalleryImgPath(Context mContext, Intent intent){
//    	String path = "";
//		path = uri.getPath();
//		path = uri.toString();
//		String[] filePathColumns={MediaStore.Images.Media.DATA};
//		Cursor c = mContext.getContentResolver().query(uri,filePathColumns, null,null, null);
//		c.moveToFirst();
//		int columnIndex = c.getColumnIndex(filePathColumns[0]);
//		String picturePath= c.getString(columnIndex);
//		path = picturePath;
//		c.close();
    	ArrayList<String> mList = new ArrayList<String>();
    	ArrayList<LoanVAblumItemEntity> resList = null;
    	if(intent != null){
			Object o = intent.getSerializableExtra(IntentUtils.PARA_KEY_PUBLIC);
			ArrayList<Object> rList = (ArrayList<Object>) o;
			if(rList != null && rList.size() > 0){
				Object obj = rList.get(0);
				if(obj instanceof String){
					ArrayList<String> strList = (ArrayList<String>) o;
					resList = new ArrayList<LoanVAblumItemEntity>();
					for(int i = 0;i < strList.size();i++){
						LoanVAblumItemEntity vEntity = new LoanVAblumItemEntity();
						vEntity.isSelect = false;
						vEntity.url = strList.get(i);
						resList.add(vEntity);
					}
				}else if(obj instanceof LoanVAblumItemEntity){
					resList = (ArrayList<LoanVAblumItemEntity>) o;
				}
			}

			if(resList != null){
    			for(int i = 0;i < resList.size();i++){
					LoanVAblumItemEntity entity = resList.get(i);
    				mList.add(entity.url);
    			}
    		}
    	}
    	return mList;
    }

    /***
     * 是否是GIF图片
     * @param path
     * @return
     */
    public static final boolean isGif(String path){
    	if(!TextUtils.isEmpty(path)){
    		return path.endsWith(".gif");
    	}
    	return false;
    }
}
