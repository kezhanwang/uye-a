package com.bjzt.uye.views.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.bjzt.uye.R;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.common.common.Common;
import com.common.common.MyLog;

/***
 * 圆角并且带渐变
 * @date 2015/08/14
 */
public class RoundImageView extends ImageView {
	private final String TAG="RoundImageView";
	private static final int BODER_RADIUS_DEFAULT=10;	//圆角大小的默认值
	private int mBorderRadius;	//圆角大小
	private Paint mBitmapPaint;	//绘图的paint
	private Paint mPaint;
	private Matrix mMatrix;
	private BitmapShader mBitmapShader;
	private RectF mRoundRect;
	private Bitmap mBitmap;
	private final int STATUS_STOP = 1;
	private final int STATUS_DOING = 2;
	private final int STATUS_BEGIN = 3;
	private int status = STATUS_BEGIN;
	private String mUrl;
	private PaintFlagsDrawFilter mFilter;
	
	public RoundImageView(Context context) {
		super(context);
		init();
		// TODO Auto-generated constructor stub
	}
	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		// TODO Auto-generated constructor stub
	}
	public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		this.mBitmap = bm;
		mBitmapPaint.setAlpha(255);
		invalidate();

		MyLog.d(TAG,"[setImageBitmap]" + " setImageBitmap:" + this.mBitmap);
	}
	
	public void setImageBitmap(Bitmap bm, boolean isDelay, String url) {
		// TODO Auto-generated method stub
		boolean isInVaildate = true;
		if(isDelay){
			if(this.mUrl == null || !this.mUrl.equals(url)){
				this.mBitmap = bm;
				this.mUrl = url;
				this.mBitmapPaint.setAlpha(0);
				removeCallbacks(r);
				post(r);
			}else{
				this.mBitmap = bm;
				this.mUrl = url;
				isInVaildate = false;
			}
		}else{
			this.mBitmap = bm;
			this.mUrl = url;
			if(status == STATUS_BEGIN || status == STATUS_STOP){
				this.mBitmapPaint.setAlpha(255);
			}
		}
		
		if(isInVaildate){
			invalidate();
		}
	}

	private void init(){
		mMatrix = new Matrix();
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);
		mBorderRadius = BODER_RADIUS_DEFAULT;
		mRoundRect = new RectF();
		mBitmapPaint.setAlpha(255);
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		int c = getResources().getColor(R.color.common_border_color);
		mPaint.setColor(c);
		mPaint.setStrokeWidth(1f);
		mPaint.setStyle(Paint.Style.STROKE);
		
		mFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Drawable d = getDrawable();
		Drawable backGroundDrawable = getBackground();
		if(d != null && d instanceof GlideBitmapDrawable){
			GlideBitmapDrawable  bitmapDrawable = (GlideBitmapDrawable) d;
			boolean isEnd = bitmapDrawable.isAnimated();
			if(bitmapDrawable.getBitmap() != null){
				this.mBitmap = bitmapDrawable.getBitmap();
			}
		}

//		if(mBitmap == null || mBitmap.isRecycled()){
//			return;
//		}
		if(mRoundRect == null){
			mRoundRect = new RectF();
		}
		mRoundRect.left = 0;
		mRoundRect.right = getWidth();
		mRoundRect.top = 0;
		mRoundRect.bottom = getHeight();

		if(mBitmap != null){
			mBitmapShader = new BitmapShader(mBitmap, TileMode.CLAMP, TileMode.CLAMP);
			float scaleX=getWidth()*1.0f/mBitmap.getWidth();
			float scaleY=getHeight()*1.0f/mBitmap.getHeight();
			mMatrix.setScale(scaleX, scaleY);
			mBitmapShader.setLocalMatrix(mMatrix);
			mBitmapPaint.setShader(mBitmapShader);


			canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius, mBitmapPaint);
		}
		//绘制边框
		canvas.drawRoundRect(mRoundRect, mBorderRadius-1, mBorderRadius-1, mPaint);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		removeCallbacks(r);
	}
	
	private Runnable r = new Runnable() {
		@Override
		public void run() {
			int alpha = mBitmapPaint.getAlpha();
			if(alpha < 255){
				alpha+=12;
				if(alpha >= 255){
					alpha = 255;
				}
				mBitmapPaint.setAlpha(alpha);
				invalidate();
				if(Common.isPageStop){
					post(r);
					status = STATUS_DOING;
				}else{
					alpha = 255;
					mBitmapPaint.setAlpha(alpha);
					invalidate();
					status = STATUS_STOP;
					removeCallbacks(r);
				}
			}else{
				removeCallbacks(r);
				status = STATUS_STOP;
			}
		}
	};
	
}
