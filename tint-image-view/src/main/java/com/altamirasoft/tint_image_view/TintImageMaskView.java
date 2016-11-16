package com.altamirasoft.tint_image_view;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.ImageView;


/**
 * Created by bdhwan on 2016. 4. 1..
 */
public class TintImageMaskView extends ImageView {


    private Bitmap mImage;
    private Bitmap mMask;
    private Bitmap finalBit;
    private Paint maskPaint;
    private Paint imagePaint;
    private Paint colorPaint;

    Canvas tempCanvas;


    //color to change
    int targetColor;

    //current color
    int currentColor;

    //check animation status
    boolean isAnimating = false;

    PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    Rect src;
    Rect dst;



    public TintImageMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);

        src = new Rect();
        dst = new Rect();
        colorPaint = new Paint();
        maskPaint = new Paint();
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        imagePaint = new Paint();
        imagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        tempCanvas = new Canvas();
        currentColor = context.obtainStyledAttributes(attrs, R.styleable.TintImageMaskView).getColor(R.styleable.TintImageMaskView_initColor, Color.parseColor("#000000"));


    }

    public int getCurrentColor() {
        return currentColor;
    }


    //color animation
    public void changeColor(final int target, long startDelay, long duration, Interpolator interpolator) {

        targetColor = target;

        if (isAnimating) {
            return;
        }
        isAnimating = true;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor, target);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                currentColor = (int) animator.getAnimatedValue();
                invalidate();
            }

        });

        colorAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                currentColor = target;
                invalidate();

                isAnimating = false;
                if (currentColor != targetColor) {
                    changeColor(targetColor);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        colorAnimation.setStartDelay(startDelay);
        colorAnimation.setDuration(duration);
        colorAnimation.setInterpolator(interpolator);
        colorAnimation.start();

    }

    public void changeColor(final int target, long startDelay, long duration) {
        changeColor(target, 0, 500, PathInterpolatorCompat.create(0.33f, 0f, 0.10f, 1.0f));
    }

    public void changeColor(final int target, long startDelay) {
        changeColor(target, 0, 500);
    }

    public void changeColor(final int target) {
        changeColor(target, 0);
    }


    @Override
    protected void onDraw(Canvas canvas) {


        if(currentColor==Color.TRANSPARENT){
            super.onDraw(canvas);
        }
        else{

            int width = getWidth();
            int height = getHeight();

            if (width != 0 && mMask == null) {
                mMask = ((BitmapDrawable) getDrawable()).getBitmap();
                src.set(0,0,mMask.getWidth(),mMask.getHeight());
                dst.set(0,0,width,height);
                Log.d("log","src = "+src.toString()+", dst = "+dst.toString());
                mImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                finalBit = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }


            if (mImage != null && mMask != null) {
                tempCanvas.setBitmap(mImage);
                colorPaint.setColor(currentColor);
                tempCanvas.drawColor(currentColor);

                tempCanvas.setBitmap(finalBit);
                tempCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);


                tempCanvas.drawBitmap(mImage, 0, 0, maskPaint);
                maskPaint.setXfermode(mode);
                tempCanvas.drawBitmap(mMask, src, dst, maskPaint);
                maskPaint.setXfermode(null);
                super.onDraw(canvas);
                canvas.drawBitmap(finalBit, 0, 0, colorPaint);
            }
        }




    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mMask != null){
            mMask.recycle();
            mImage.recycle();
            finalBit.recycle();
            mMask = null;
        }
        invalidate();
    }
}
