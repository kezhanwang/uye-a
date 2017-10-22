package com.bjzt.uye.photo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

import com.bjzt.uye.photo.view.touchanalizer.DragBehavior;
import com.bjzt.uye.photo.view.touchanalizer.PinchBehavior;
import com.bjzt.uye.photo.view.touchanalizer.RotateBehavior;
import com.bjzt.uye.photo.view.touchanalizer.Shieldable;
import com.bjzt.uye.photo.view.touchanalizer.TouchAnalizer;
import com.bjzt.uye.photo.view.touchanalizer.TouchBehaviorListener;
import com.bjzt.uye.util.ViewUtils;
import com.common.listener.NoConfusion;
import com.common.util.DeviceUtil;

/**
 * Created by billy on 2017/10/22
 */
public class AnimationImageView  extends View implements NoConfusion,TouchBehaviorListener, Animation.AnimationListener {
    private final static long CLICK_DELAY = 550;

    private Bitmap mBitmap;
    private Drawable mDrawable;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mWidth, mHeight;

    // animation attributes.
    private Animation anim;
    private Transformation trans;
    private float[] matrixValue;
    private boolean animFixCenter = false;
    private boolean animAfterLayout = true;
    private int[]   animAttrs;

    // scrollbar attributes.
    private boolean shouldDrawScrollBar = false;
    private Transformation sbTrans;
    private Animation sbAnim;
    private Drawable hScrollBar, vScrollBar;

    // touching attributes.
    private float minScale = 0.5f;
    private float maxScale = 5;
    private float mScale = 1.0f;
    private float mRotate = 0f;
    private float mRotateTmp = 0f;
    private int positionX = 0;
    private int positionY = 0;
    private TouchAnalizer t = new TouchAnalizer();
    private float dragX = -1, dragY = -1;
    private boolean dragging = false;
    private boolean longClicked = false;
    private boolean bControl = true;
    private float pinchX = 0, pinchY = 0;
    private float rotateX = 0, rotateY = 0;
    private OnClickListener onClickListener;
    private LongClickListener onLongClickListener;

    private boolean tounchEnable = true;
    private boolean isNeedZoom = true;	//需要缩放

    public void setTounchEnable(boolean touncheEnable){
        this.tounchEnable = touncheEnable;
    }

    public void setLongClickListener(LongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (onClickListener != null) {
                    onClickListener.onClick(AnimationImageView.this);
                }
            } else if (msg.what == 1) {
                hideScrollBar();
            }
        }

    };


    @Override
    public void setOnClickListener(OnClickListener l) {
        onClickListener = l;
    }

    public AnimationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public AnimationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimationImageView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        paint.setFilterBitmap(true);

        t.setListener(TouchAnalizer.BehaviorType.SINGLE_CLICK, this);
        if (DeviceUtil.getAndroidSDKVersion() <= 4) {
            t.setListener(TouchAnalizer.BehaviorType.SINGLE_DRAG, this);
        } else {
            t.setListener(TouchAnalizer.BehaviorType.DRAG, this);
            t.setListener(TouchAnalizer.BehaviorType.PINCH, this);
//			t.setListener(BehaviorType.ROTATE, this);
        }
        if (CLICK_DELAY > 0) {
            t.setListener(TouchAnalizer.BehaviorType.DOUBLE_CLICK, this);
        }
        t.setListener(TouchAnalizer.BehaviorType.LONG_CLICK, this);
    }

    @Override
    public void startAnimation(Animation animation) {
        startAnimation(animation, false);
    }

    public void startAnimation(Animation animation, boolean fixCenter) {
        anim = animation;
        if (anim != null) {
            anim.start();
            trans = new Transformation();
            matrixValue = new float[9];
            invalidate();
        }
        animFixCenter = fixCenter;
    }

    public void startAnimationAfterLayout(final int[] posContainer) {
        animAfterLayout = true;
        animAttrs = posContainer;
    }


    public void setOriPicPos(int[] pos) {
        animAttrs = pos;
        animAfterLayout = false;
    }

    private void createAndStartAnimation() {
        if (animAttrs == null || animAttrs.length < 4) {
            if (mBitmap != null) {
                int bHeight = (int) (mBitmap.getHeight() * mWidth / mBitmap.getWidth());
                positionY = mHeight > bHeight ? (mHeight - bHeight) / 2 : 0;
                invalidate();
            }
            return;
        }
        int[] myPos = new int[2];
        ViewUtils.getChildPos(AnimationImageView.this, null, myPos);
        myPos[0] = 0;	// left position is 0
        int[] viewPosOri = animAttrs;
        float s = ((float)viewPosOri[2]) / mWidth;
        int bHeight = (int)((float)viewPosOri[3] / s);
        if (mBitmap != null) {
            bHeight = (int) (mBitmap.getHeight() * mWidth / mBitmap.getWidth());
        } else if (mDrawable != null && mDrawable.getIntrinsicWidth() > 0) {
            bHeight = (int) (mDrawable.getIntrinsicHeight() * mWidth / mDrawable.getIntrinsicWidth());
        }
        ScaleAnimation a = new ScaleAnimation(s, 1.0f, s, 1.0f, 0.0f, 0.0f);
        a.setInterpolator(new DecelerateInterpolator());
        a.setDuration(300);
        TranslateAnimation t = new TranslateAnimation(viewPosOri[0] - myPos[0], 0,
                viewPosOri[1] - myPos[1], mHeight > bHeight ? (mHeight - bHeight) / 2 : 0);
        t.initialize(mWidth, mHeight, mWidth, mHeight);
        t.setInterpolator(new DecelerateInterpolator());
        t.setDuration(300);
        AnimationSet am = new AnimationSet(true);
        am.addAnimation(a);
        am.addAnimation(t);
        isNeedZoom = true;
        startAnimation(am);
    }

    public boolean createBackAnimation(Animation.AnimationListener l) {
        if (animAttrs == null || animAttrs.length < 4) {
            if (mBitmap != null) {
                int bHeight = (int) (mBitmap.getHeight() * mWidth / mBitmap.getWidth());
                positionY = mHeight > bHeight ? (mHeight - bHeight) / 2 : 0;
                invalidate();
            }
            return false;
        }
        int[] myPos = new int[2];
        ViewUtils.getChildPos(AnimationImageView.this, null, myPos);
        myPos[0] = 0;	// left position is 0
        int[] viewPosOri = animAttrs;
        float s = ((float)viewPosOri[2]) / mWidth;
        int bHeight = (int)((float)viewPosOri[3] / s);
        if (mBitmap != null && mBitmap.getWidth() > 0) {
            bHeight = (int) (mBitmap.getHeight() * mWidth / mBitmap.getWidth());
        } else if (mDrawable != null && mDrawable.getIntrinsicWidth() > 0) {
            bHeight = (int) (mDrawable.getIntrinsicHeight() * mWidth / mDrawable.getIntrinsicWidth());
        }
        ScaleAnimation a = new ScaleAnimation(1.0f, s, 1.0f, s, 0.0f, 0.0f);
        a.setInterpolator(new DecelerateInterpolator());
        a.setDuration(300);
        TranslateAnimation t = new TranslateAnimation(0, viewPosOri[0] - myPos[0],
                mHeight > bHeight ? (mHeight - bHeight) / 2 : 0, viewPosOri[1] - myPos[1]);
        t.initialize(mWidth, mHeight, mWidth, mHeight);
        t.setInterpolator(new DecelerateInterpolator());
        t.setDuration(300);
        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.3f);
        aa.setInterpolator(new LinearInterpolator());
        aa.setStartOffset(150);
        aa.setDuration(100);
        AnimationSet am = new AnimationSet(true);
        am.addAnimation(a);
        am.addAnimation(t);
        am.addAnimation(aa);
        if (l != null) {
            am.setAnimationListener(l);
        }
        isNeedZoom = true;
        startAnimation(am);
        return true;
    }

    public void setImageBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        bControl = true;
        mDrawable = null;
        isNeedZoom = true;
        if(mBitmap != null){
            mWidth = mBitmap.getWidth();
            mHeight = mBitmap.getHeight();
            requestLayout();
        }
        invalidate();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public void setImageDrawable(Drawable d) {
        mDrawable = d;
        if (mDrawable != null) {
            if(isNeedZoom){
                mDrawable.setBounds(0, 0, getWidth(), getHeight());
            }else{
                int bHeight = mDrawable.getIntrinsicHeight();
                int bWidth = mDrawable.getIntrinsicWidth();
                int left = (getWidth() - bWidth) / 2;
                int right = left + bWidth;
                int top = (getHeight() - bHeight) / 2;
                int bottom = top + bHeight;
                mDrawable.setBounds(left, top, right, bottom);
            }
            mDrawable.setCallback(this);
            if (d.isStateful()) {
                d.setState(getDrawableState());
            }
            mBitmap = null;
            bControl = true;
        }
        invalidate();
    }

    public void setImageDrawable(Drawable d,boolean isNeedZoom){
        this.isNeedZoom = isNeedZoom;
        setImageDrawable(d);
    }

    public void sethScrollBar(Drawable hScrollBar) {
        this.hScrollBar = hScrollBar;
    }

    public void setvScrollBar(Drawable vScrollBar) {
        this.vScrollBar = vScrollBar;
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
        if (mDrawable != null) {
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            mDrawable.setBounds(0+paddingLeft, 0, right - left - paddingRight, bottom - top);
        }
        if (animAfterLayout) {
            createAndStartAnimation();
            animAfterLayout = false;
        }
        tryDrag(0, 0);
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);

        multiCtrl = false;
        dragRet = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dragging = false;
            longClicked = false;
            mHandler.removeMessages(0);
            if (mScale > 1) {
                dragRet = true;
            }
        }
        t.inputTouchEvent(event);
        if (parent != null) {
            parent.setShielded(dragRet || multiCtrl);
        }
        return true;
    }


    @Override
    protected boolean verifyDrawable(Drawable dr) {
        return mDrawable == dr || super.verifyDrawable(dr);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needDrawNextFrame = false;
        int dstX = positionX;
        int dstY = positionY;
        int bWidth = 0;
        int bHeight = 0;
        if (mBitmap != null && mBitmap.isRecycled()) {
            return;
        }
        if (mBitmap != null) {
            bWidth = mBitmap.getWidth();
            bHeight = mBitmap.getHeight();
        } else if (mDrawable != null) {
            bWidth = mDrawable.getIntrinsicWidth();
            bHeight = mDrawable.getIntrinsicHeight();
        }
        float bScale = (float)mWidth / (bWidth > 0 ? bWidth : mWidth);
        int h = (int) (bHeight * bScale * mScale);
        int w = (int) (bWidth * bScale * mScale);
        if (h < mHeight) {
            dstY = (mHeight - h) / 2;
        } else if (h >= mHeight && positionY > 0) {
            dstY = positionY = 0;
        }

        float aScale = mScale;
        if (anim != null) {
            needDrawNextFrame = anim.getTransformation(System.currentTimeMillis(), trans);
            trans.getMatrix().getValues(matrixValue);
            paint.setAlpha((int) (trans.getAlpha() * 255));
            aScale = matrixValue[Matrix.MSCALE_X];
            dstX = (int)(matrixValue[Matrix.MTRANS_X]);
            dstY = (int)(matrixValue[Matrix.MTRANS_Y]);
            if (animFixCenter) {
                h = (int) (bHeight * bScale * aScale);
                if (h < mHeight) {
                    dstY = (mHeight - h) / 2;
                }
                w = (int) (bWidth * bScale * aScale);
                if (dstX > 0 || w <= mWidth) {
                    dstX = (mWidth - w) / 2;
                }
            }
        } else {
            if (dstX > 0 || w <= mWidth) {
                dstX = (mWidth - w) / 2;
            }
            paint.setAlpha(255);
        }
        bWidth = (int) (mWidth * aScale) ;
        bHeight = (int) (bHeight * bScale * aScale);
        canvas.rotate((float) ((mRotate + mRotateTmp) / Math.PI * 180 + 360), rotateX, rotateY);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        if (mBitmap != null) {
            Rect ori = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
            Rect dst = new Rect(dstX+paddingLeft, dstY, bWidth + dstX - paddingRight, bHeight + dstY);
            canvas.drawBitmap(mBitmap, ori, dst, paint);
        } else if (mDrawable != null) {
            if(isNeedZoom){
                mDrawable.setBounds(dstX+paddingLeft, dstY, bWidth + dstX - paddingRight, bHeight + dstY);
            }else{
                int bHeightT = mDrawable.getIntrinsicHeight();
                int bWidthT = mDrawable.getIntrinsicWidth();
                int left = (getWidth() - bWidthT) / 2;
                int right = left + bWidthT;
                int top = (getHeight() - bHeightT) / 2;
                int bottom = top + bHeightT;
                mDrawable.setBounds(left, top, right, bottom);
            }
            mDrawable.draw(canvas);
        }

        positionX = dstX;
        positionY = dstY;

        if (shouldDrawScrollBar && (bWidth > mWidth || bHeight > mHeight)) {
            boolean sbDrawing = false;
            int alpha = 255;
            if (sbAnim != null) {
                sbDrawing = sbAnim.getTransformation(System.currentTimeMillis(), sbTrans);
                alpha = (int) (sbTrans.getAlpha() * 255);
            }
            if (bWidth > mWidth && hScrollBar != null) {
                hScrollBar.setBounds(-dstX * mWidth / bWidth, mHeight - hScrollBar.getIntrinsicHeight(),
                        (-dstX + mWidth) * mWidth / bWidth, mHeight);
                hScrollBar.setAlpha(alpha);
                hScrollBar.draw(canvas);
            }
            if (bHeight > mHeight && vScrollBar != null) {
                vScrollBar.setBounds(mWidth - vScrollBar.getIntrinsicWidth(), -dstY * mHeight / bHeight,
                        mWidth, (-dstY + mHeight) * mHeight / bHeight);
                vScrollBar.setAlpha(alpha);
                vScrollBar.draw(canvas);
            }
            needDrawNextFrame |= sbDrawing;
        }

        if (needDrawNextFrame) {
            invalidate();
        } else {
            anim = null;
        }
    }


    private boolean dragRet = false;
    private boolean multiCtrl = false;
    @Override
    public boolean onInvoke(TouchAnalizer.BehaviorType type, float x, float y, int state) {
        if(!this.tounchEnable){
            if(type == TouchAnalizer.BehaviorType.DOUBLE_CLICK || type == TouchAnalizer.BehaviorType.SINGLE_CLICK){
                mHandler.sendEmptyMessageDelayed(0, CLICK_DELAY);
            }
            return true;
        }
        if (longClicked) {
            return true;
        }
        boolean ret = true;
        switch (type) {
            case SINGLE_CLICK:
                mHandler.sendEmptyMessageDelayed(0, CLICK_DELAY);
                break;
            case DRAG:
            case SINGLE_DRAG:
                if (bControl) {
                    if (!dragging) {
                        dragX = x;
                        dragY = y;
                        dragging = true;
                    } else {
                        dragRet = tryDrag(x - dragX, y - dragY);
                        dragX = x;
                        dragY = y;
                        if (!dragRet) {
                            dragging = false;
                        }
                    }
                    if (state == DragBehavior.DRAG_END) {
                        dragging = false;
                    }
                }
                if (onLongClickListener != null) {
                    onLongClickListener.onCancelMenu();
                }
                break;
            case DOUBLE_CLICK:
                mHandler.removeMessages(0);
                if (bControl) {
                    if (mScale > 1.5) {
                        zoomTo(1.0f, (x - positionX) / (mWidth * mScale), (y - positionY) / (mHeight * mScale));
                    } else {
                        zoomTo(1.8f, (x - positionX) / (mWidth * mScale), (y - positionY) / (mHeight * mScale));
                    }
                }
                break;
            case PINCH:
                if (bControl) {
                    multiCtrl = true;
                    switch (state) {
                        case PinchBehavior.PINCH_START:
                            pinchX = x;
                            pinchY = y;
                            break;
                        case PinchBehavior.PINCH_MOVE:
                            zoomTo((float) x * mScale / y , (pinchX - positionX) / (mWidth * mScale), (pinchY - positionY) / (mHeight * mScale), false);
                            break;
                        case PinchBehavior.PINCH_END:
                            if (mScale < 1.0) {
                                zoomTo(1.0f);
                            }
                            break;
                        default:
                            break;
                    }
                }
                break;
            case ROTATE:
                if (bControl) {
                    multiCtrl = true;
                    switch (state) {
                        case RotateBehavior.ROTATE_START:
                            setRotateCenter(x, y);
                            break;
                        case RotateBehavior.ROTATE_MOVE:
                            rotateTo(y - x);
                            break;
                        case RotateBehavior.ROTATE_END:
                            mRotate += mRotateTmp;
                            mRotateTmp = 0;
                            break;
                        default:
                            break;
                    }
                }
                break;
            case LONG_CLICK:
                if (onLongClickListener != null) {
                    longClicked = true;
                    onLongClickListener.onLongClick((int)x, (int)y);
                }
                break;
            default:
                break;
        }
        return ret;
    }

    private void setRotateCenter(float x, float y) {
        rotateX = x;//x / mWidth;
        rotateY = y;//y / mHeight;
    }

    private void rotateTo(float f) {
        mRotateTmp = f;
    }

    private void zoomTo(float f) {
        zoomTo(f, 0.5f, 0.5f);
    }
    private void zoomTo(float f, float cx, float cy) {
        zoomTo(f, cx, cy, true);
    }
    private void zoomTo(float df, float cx, float cy, boolean anim) {
        float f = df < minScale ? minScale : df;
        f = df > maxScale ? maxScale : df;
        float deltaX = (mScale - f) * mWidth * cx;
        float deltaY = (mScale - f) * mHeight * cy;
        if (anim) {
            AnimationSet as = new AnimationSet(true);
            ScaleAnimation s = new ScaleAnimation(mScale, f, mScale, f);
            s.setDuration((int)(Math.abs(f - mScale) * 300));
            s.setInterpolator(new DecelerateInterpolator());
            as.addAnimation(s);
            TranslateAnimation t = new TranslateAnimation(positionX, (positionX + deltaX), positionY, (positionY + deltaY));
            t.initialize(mWidth, mHeight, mWidth, mHeight);
            t.setDuration((int)(Math.abs(f - mScale) * 300));
            t.setInterpolator(new DecelerateInterpolator());
            as.addAnimation(t);
            startAnimation(as, true);
            mScale = f;
        } else {
            positionX += deltaX;
            positionY += deltaY;
            mScale = f;
            invalidate();
        }
    }

    private void showScrollBar() {
        shouldDrawScrollBar = true;
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, 1000);
        invalidate();
    }

    private void hideScrollBar() {
        sbAnim = new AlphaAnimation(1.0f, 0.0f);
        sbAnim.setDuration(800);
        sbAnim.start();
        sbAnim.setAnimationListener(this);
        if (sbTrans == null) {
            sbTrans = new Transformation();
        }
        invalidate();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        shouldDrawScrollBar = false;
        sbAnim = null;
        invalidate();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    private boolean tryDrag(float x, float y) {
        boolean ret = true;
        boolean xOut = false;
        boolean yOut = false;
        float distance = (float) Math.sqrt(x * x + y * y);
        float px = x;
        if (px == 0) {
            px = 0.0000001f;
        }
        float ang = (float) Math.atan(y / Math.abs(px));
        if (px < 0) {
            ang = (float) (Math.PI - ang);
        }

        float dx = (float) Math.cos(ang - mRotate - mRotateTmp) * distance;
        float dy = (float) Math.sin(ang - mRotate - mRotateTmp) * distance;
        float bScale = 1;
        int bWidth = mWidth;
        int bHeight = 0;
        if (mBitmap != null) {
            bScale = (float)mWidth / mBitmap.getWidth();
            bWidth = (int) (mWidth * mScale);
            bHeight = (int) (mBitmap.getHeight() * mScale * bScale);
        } else if (mDrawable != null && mDrawable.getIntrinsicWidth() > 0) {
            bScale = (float)mWidth / mDrawable.getIntrinsicWidth();
            bWidth = (int) (mWidth * mScale);
            bHeight = (int) (mDrawable.getIntrinsicHeight() * mScale * bScale);
        } else {
            return false;
        }
        if (bWidth > mWidth) {
            if (positionX + dx > mWidth - bWidth) {
                positionX += dx;
            } else {
                positionX = mWidth - bWidth;
                xOut = true;
            }
            xOut |= positionX > 0;
            positionX = positionX > 0 ? 0 : positionX;
        } else {
            positionX = (mWidth - bWidth) / 2;
            xOut = true;
        }
        if (bHeight > mHeight) {
            if (positionY + dy > mHeight - bHeight) {
                positionY += dy;
            } else {
                positionY = mHeight - bHeight;
                yOut = true;
            }
            yOut |= positionY > 0;
            positionY = positionY > 0 ? 0 : positionY;
            //ret = false;
        } else {
            positionY = (mHeight - bHeight) / 2;
            yOut = true;
        }
        if ((xOut && yOut)
                || (yOut && Math.abs(dy) > Math.abs(dx) * 2)
                || (xOut && Math.abs(dx) > Math.abs(dy) * 2)) {
            ret = false;
        }
        if (ret) {
            showScrollBar();
        }
        invalidate();
        return ret;
    }

    private Shieldable parent;
    public void setShieldableParent(Shieldable s) {
        parent = s;
    }

    public interface LongClickListener {
        public void onLongClick(int x, int y);
        public void onCancelMenu();
    }

}
