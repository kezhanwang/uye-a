package com.bjzt.uye.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

import com.bjzt.uye.entity.EScaleType;
import com.common.common.MyLog;

public class BitmapUtil {
	private static final String TAG = "BitmapUtil";

	public static final int BITMAP_SIZE_320 = 320;
	public static final int BITMAP_SIZE_480 = 480;
	public static final int BITMAP_SIZE_560 = 560;
	public static final int BITMAP_SIZE_720 = 720;

	public static final boolean isPicNetUrlLegel(String imageUrl){
		boolean flag = false;
		if(!TextUtils.isEmpty(imageUrl) && (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))){
			flag = true;
		}
		return flag;
	}

	/** 水平方向模糊度 */
	private static float hRadius = 4;
	/** 竖直方向模糊度 */
	private static float vRadius = 4;
	/** 模糊迭代度 */
	private static int iterations = 2;

	public static Bitmap BoxBlurFilter(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
		try {
			for (int i = 0; i < iterations; i++) {
				blur(inPixels, outPixels, width, height, hRadius);
				blur(outPixels, inPixels, height, width, vRadius);
			}
		} catch (IndexOutOfBoundsException ee) {
			MyLog.error("ImageUtils", "", ee);
		}
		blurFractional(inPixels, outPixels, width, height, hRadius);
		blurFractional(outPixels, inPixels, height, width, vRadius);
		bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);

		// 去掉边边
		int offset = 6;
		Bitmap destBitmap = Bitmap.createBitmap(bitmap, offset, offset, width
				- offset * 2, height - offset * 2);
		return destBitmap;
	}

	private static void blur(int[] in, int[] out, int width, int height,
			float radius) {
		int widthMinus1 = width - 1;
		int r = (int) radius;
		int tableSize = 2 * r + 1;
		int divide[] = new int[256 * tableSize];
		for (int i = 0; i < 256 * tableSize; i++)
			divide[i] = i / tableSize;
		int inIndex = 0;
		for (int y = 0; y < height; y++) {
			int outIndex = y;
			int ta = 0, tr = 0, tg = 0, tb = 0;
			for (int i = -r; i <= r; i++) {
				int rgb = in[inIndex + clamp(i, 0, width - 1)];
				ta += (rgb >> 24) & 0xff;
				tr += (rgb >> 16) & 0xff;
				tg += (rgb >> 8) & 0xff;
				tb += rgb & 0xff;
			}
			for (int x = 0; x < width; x++) {
				out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
						| (divide[tg] << 8) | divide[tb];

				int i1 = x + r + 1;
				if (i1 > widthMinus1)
					i1 = widthMinus1;
				int i2 = x - r;
				if (i2 < 0)
					i2 = 0;
				int rgb1 = in[inIndex + i1];
				int rgb2 = in[inIndex + i2];

				ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
				tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
				tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
				tb += (rgb1 & 0xff) - (rgb2 & 0xff);
				outIndex += height;
			}
			inIndex += width;
		}
	}

	private static void blurFractional(int[] in, int[] out, int width,
			int height, float radius) {
		radius -= (int) radius;
		float f = 1.0f / (1 + 2 * radius);
		int inIndex = 0;
		for (int y = 0; y < height; y++) {
			int outIndex = y;
			out[outIndex] = in[0];
			outIndex += height;
			for (int x = 1; x < width - 1; x++) {
				int i = inIndex + x;
				int rgb1 = in[i - 1];
				int rgb2 = in[i];
				int rgb3 = in[i + 1];
				int a1 = (rgb1 >> 24) & 0xff;
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;
				int a2 = (rgb2 >> 24) & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;
				int a3 = (rgb3 >> 24) & 0xff;
				int r3 = (rgb3 >> 16) & 0xff;
				int g3 = (rgb3 >> 8) & 0xff;
				int b3 = rgb3 & 0xff;
				a1 = a2 + (int) ((a1 + a3) * radius);
				r1 = r2 + (int) ((r1 + r3) * radius);
				g1 = g2 + (int) ((g1 + g3) * radius);
				b1 = b2 + (int) ((b1 + b3) * radius);
				a1 *= f;
				r1 *= f;
				g1 *= f;
				b1 *= f;
				out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
				outIndex += height;
			}
			if (outIndex < out.length && (width - 1) < in.length) {
				out[outIndex] = in[width - 1];
			}
			inIndex += width;
		}
	}

	private static int clamp(int x, int a, int b) {
		return (x < a) ? a : (x > b) ? b : x;
	}

	/***
	 * 图片按照质量进行压缩
	 * 
	 * @param image
	 *            500K后在进行上传
	 * @return
	 */
	public static final Bitmap compressImageByQuailty(Bitmap image, int width, int height, String filePath, boolean isZoom) {
		Bitmap result = null;
		Canvas canvas = null;
		Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
		if(isZoom){
			Rect srcRect = calculateSrcRect(image.getWidth(), image.getHeight(), width, height, EScaleType.CROP);
			Rect dstRect = calculateDstRect(image.getWidth(), image.getHeight(), width, height, EScaleType.CROP);
			result = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.RGB_565);
			canvas = new Canvas(result);
			canvas.drawBitmap(image, srcRect, dstRect, paint);
		}else{
			result = Bitmap.createBitmap(width,height, Bitmap.Config.RGB_565);
			canvas = new Canvas(result);
			canvas.drawBitmap(image,
					new Rect(0, 0, image.getWidth(), image.getHeight()), 
					new Rect(0, 0, width, height), paint);
		}
		//翻转
//		Matrix m = null;
//		if(result != null && !TextUtils.isEmpty(filePath)){
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
//				if (degree != 0 && result != null) {
//					m = new Matrix();
////					m.setRotate(degree,(float) result.getWidth() / 2, (float) result.getHeight() / 2);
//					m.postRotate(degree);
//				}
//			} catch (IOException e) {
//				MyLog.error(TAG,"[getThumbBitmap]",e);
//			}
//		}
//
		if (image != null && !image.isRecycled()) {
			image.recycle();
		}
//
//		Bitmap rBitmap = null;
//		if(m != null){
//			rBitmap = Bitmap.createBitmap(result, 0, 0, result.getWidth(),result.getHeight(),m,false);
//			if(result != null && !result.isRecycled()){
//				result.recycle();
//			}
//			return rBitmap;
//		}
		return result;
	}

	public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, EScaleType scaleType) {
		if (scaleType == EScaleType.CROP) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				final int srcRectWidth = (int) (srcHeight * dstAspect);
				final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
				return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
			} else {
				final int srcRectHeight = (int) (srcWidth / dstAspect);
				final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
				return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
			}
		} else {
			return new Rect(0, 0, srcWidth, srcHeight);
		}
	}

	public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, EScaleType scaleType) {
		if (scaleType == EScaleType.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
			} else {
				return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
			}
		} else {
			return new Rect(0, 0, dstWidth, dstHeight);
		}
	}
	
	/***
	 * 根据宽度进行压缩
	 * 
	 * @param image
	 * @param width
	 * @return
	 */
	public static final Bitmap compressImageByWidth(Bitmap image, int width, String filePath) {
		return compressImageByWidth(image,width,0,filePath);
	}
	
	public static final Bitmap compressImageByWidth(Bitmap image, int width, int height, String filePath) {
		Bitmap result = null;
		if (image != null && !image.isRecycled()) {
			if(height <= 0){
				height = (image.getHeight() * width) / image.getWidth();
			}
			result = compressImageByQuailty(image, width, height,filePath,true);
		}
		return result;
	}


	public static Bitmap getBitmap(byte[] data){
		return BitmapFactory.decodeByteArray(data, 0, data.length);//从字节数组解码位图
	}

}
