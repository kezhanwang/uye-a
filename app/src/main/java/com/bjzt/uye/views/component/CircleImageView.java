package com.bjzt.uye.views.component;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bjzt.uye.R;
import com.common.listener.NoConfusion;

public class CircleImageView extends ImageView implements NoConfusion {
	
    private int mResource, mPressedResourceID;                  // 图片id和 按下态的 图片id...
    private Drawable mDrawable;                      //   核心的原图和 vip标记的图片。。。
    private Drawable mPressedDrawable, mTempStore;            //    目前只支持的按下态 图片。。。

    private boolean isVip = false;                   //  是否在右下角显示 会员图标。。。现在又改成在 名字后边显示vip 这里就没有必要再显示 vip标记了...

    private ScaleType mScaleType;
    private boolean specifyWidth = false;          // 容器是否强制 指定了 本控件 的大小。。。

    private static final int gapDip = 8;     // 默认是 8 dp
    private static int gap = 0;                //   需要根据 displayMetrix 来算 实际的大小。。。
    
    private Matrix matx = new Matrix();
    private int mWidth;
    private int mHeight;
    
    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        init(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);
//        mPressedResourceID = a.getResourceId(R.styleable.RoundedImageView_pressedSrc, 0);
//        mResource = a.getResourceId(R.styleable.RoundedImageView_normalSrc, 0);
//        a.recycle();

        if (mResource <= 0) mResource = R.drawable.user_icon;           // 默认的头像图片. 是一个指定的图...
        mDrawable = CircleDrawable.fromDrawable(getResources().getDrawable(mResource));
        updateDrawableAttrs();
    }

    public void setImageDrawable(Drawable drawable) {
        mResource = 0;
        mDrawable = CircleDrawable.fromDrawable(drawable);
        updateDrawableAttrs();
    }

    public void setImageBitmap(Bitmap bm) {
        mResource = 0;
        mDrawable = CircleDrawable.fromBitmap(bm);
        updateDrawableAttrs();
    }

    public void setImageResource(int resId) {
        if (mResource != resId) {
            mResource = resId;
            mDrawable = resolveResource();
            updateDrawableAttrs();
        }
    }

    private Drawable resolveResource() {
        Resources rsrc = getResources();
        if (rsrc == null) {
            return null;
        }

        Drawable d = null;

        if (mResource != 0) {
            try {
                d = rsrc.getDrawable(mResource);
            } catch (Exception e) {
                // Don't try again.
                mResource = 0;
            }
        }
        return CircleDrawable.fromDrawable(d);
    }

    private void updateDrawableAttrs() {
        updateAttrs(mDrawable);
    }

    private void updateAttrs(Drawable drawable) {
        if (drawable == null) {
            return;
        }

        if (drawable instanceof CircleDrawable) {
            ((CircleDrawable) drawable).setScaleType(mScaleType);
        } else if (drawable instanceof LayerDrawable) {
            // loop through layers to and set drawable attrs
            LayerDrawable ld = ((LayerDrawable) drawable);
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i));
            }
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (mDrawable == null) return;

        float scale = -1;
        int actualWidth = getMeasuredWidth();
        int actualHeight = getMeasuredHeight();

        int availableWidth = actualWidth;
        int drawableOffsetY = 0;        // 当 measure 为 exactly的时候， 需要移动的 Y轴 偏移量。。。。
        if (specifyWidth) {
            availableWidth = availableWidth - gap;
        }
        boolean needAdjust = false;             //   是否需要调整图片大小....
//        Matrix matx = null;
        if (availableWidth < mDrawable.getIntrinsicWidth()) {             //  实际 可用 宽度不够 mdrawable 的话， 需要进行缩小处理。。。
            needAdjust = true;
//            matx = new Matrix();
            matx.reset();
            scale = ((float) availableWidth) / ((float) mDrawable.getIntrinsicWidth());
            matx.setScale(scale, scale);
        }
        canvas.save();
        mDrawable.setBounds(0, 0,mWidth,mHeight);
        if (needAdjust) {
            //  还要看 ， 是否 需要位移。。。 不过这里只需要移动 Y 轴。。。
//            float scaledDrawableHeight = matx.mapRadius(mDrawable.getIntrinsicHeight());
//            drawableOffsetY = (int) (actualHeight - scaledDrawableHeight) >>> 1;
//            canvas.translate(0, drawableOffsetY);
//            canvas.concat(matx);
            mDrawable.draw(canvas);
        } else {
            mDrawable.draw(canvas);
        }
        canvas.restore();

        if (!isVip) return;

        //  需要调整大小， 采用 平移 canvas 的方式。。。  效率较高。。。
        canvas.save();
        //  因为后续有缩放的影响， 所以需要移动的距离是 缩放后图片的宽度和高度。。。而不是 原图尺寸大小的位移。。。
//        float scaledWidth = matx.mapRadius(mVipDrawable.getIntrinsicWidth());    // 得到缩放后 的 长宽 大小。。。
//        float scaledHeight = matx.mapRadius(mVipDrawable.getIntrinsicHeight());  // 得到缩放后 的 长宽 大小。。。这种方式看源码要 比 mapPoints 效率要高... 少了一次java层的复杂调用， 这个直接执行native方法
        canvas.translate(actualWidth,actualHeight);     //  直接移动到 右下角 的坐标位置上。。。
        canvas.restore();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	// TODO Auto-generated method stub
    	super.onSizeChanged(w, h, oldw, oldh);
    	mWidth = w;
    	mHeight = h;
    	if(mWidth <= mHeight){
    		mWidth = mHeight;
    	}else{
    		mHeight = mWidth;
    	}
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mDrawable == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heighSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int finalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;

        if (widthSpecMode == MeasureSpec.EXACTLY) {
            specifyWidth = true;
            width = finalWidth;
        } else {
            width = mDrawable.getIntrinsicWidth() + gap;
        }
        if (heighSpecMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mDrawable.getIntrinsicHeight();
        }

        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), height + getPaddingTop() + getPaddingBottom());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPressedResourceID == 0 || mPressedDrawable == null) {
            return super.onTouchEvent(event);
        }
        super.onTouchEvent(event);
        if (mPressedDrawable == null) {          //  只有第一次才会这样了。。。
            mPressedDrawable = getResources().getDrawable(mPressedResourceID);
            mTempStore = mDrawable;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setImageDrawable(mPressedDrawable);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            setImageDrawable(mTempStore);
        }
        return true;
    }

    @Override
    public void setPressed(boolean pressed) {
        if (pressed && getParent() != null &&
                getParent() instanceof View && ((View) getParent()).isPressed()) {
            return;
        }
        super.setPressed(pressed);
    }

}
