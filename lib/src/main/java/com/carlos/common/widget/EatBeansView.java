package com.carlos.common.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

public class EatBeansView extends BaseView {
   int eatSpeed = 5;
   private Paint mPaint;
   private Paint mPaintEye;
   private float mWidth = 0.0F;
   private float mHigh = 0.0F;
   private float mPadding = 5.0F;
   private float eatErWidth = 60.0F;
   private float eatErPositionX = 0.0F;
   private float beansWidth = 10.0F;
   private float mAngle = 34.0F;
   private float eatErStartAngle;
   private float eatErEndAngle;

   public EatBeansView(Context context) {
      super(context);
      this.eatErStartAngle = this.mAngle;
      this.eatErEndAngle = 360.0F - 2.0F * this.eatErStartAngle;
   }

   public EatBeansView(Context context, AttributeSet attrs) {
      super(context, attrs);
      this.eatErStartAngle = this.mAngle;
      this.eatErEndAngle = 360.0F - 2.0F * this.eatErStartAngle;
   }

   public EatBeansView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      this.eatErStartAngle = this.mAngle;
      this.eatErEndAngle = 360.0F - 2.0F * this.eatErStartAngle;
   }

   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      this.mWidth = (float)this.getMeasuredWidth();
      this.mHigh = (float)this.getMeasuredHeight();
   }

   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      float eatRightX = this.mPadding + this.eatErWidth + this.eatErPositionX;
      RectF rectF = new RectF(this.mPadding + this.eatErPositionX, this.mHigh / 2.0F - this.eatErWidth / 2.0F, eatRightX, this.mHigh / 2.0F + this.eatErWidth / 2.0F);
      canvas.drawArc(rectF, this.eatErStartAngle, this.eatErEndAngle, true, this.mPaint);
      canvas.drawCircle(this.mPadding + this.eatErPositionX + this.eatErWidth / 2.0F, this.mHigh / 2.0F - this.eatErWidth / 4.0F, this.beansWidth / 2.0F, this.mPaintEye);
      int beansCount = (int)((this.mWidth - this.mPadding * 2.0F - this.eatErWidth) / this.beansWidth / 2.0F);

      for(int i = 0; i < beansCount; ++i) {
         float x = (float)(beansCount * i) + this.beansWidth / 2.0F + this.mPadding + this.eatErWidth;
         if (x > eatRightX) {
            canvas.drawCircle(x, this.mHigh / 2.0F, this.beansWidth / 2.0F, this.mPaint);
         }
      }

   }

   private void initPaint() {
      this.mPaint = new Paint();
      this.mPaint.setAntiAlias(true);
      this.mPaint.setStyle(Style.FILL);
      this.mPaint.setColor(-1);
      this.mPaintEye = new Paint();
      this.mPaintEye.setAntiAlias(true);
      this.mPaintEye.setStyle(Style.FILL);
      this.mPaintEye.setColor(-16777216);
   }

   public void setViewColor(int color) {
      this.mPaint.setColor(color);
      this.postInvalidate();
   }

   public void setEyeColor(int color) {
      this.mPaintEye.setColor(color);
      this.postInvalidate();
   }

   protected void InitPaint() {
      this.initPaint();
   }

   protected void OnAnimationUpdate(ValueAnimator valueAnimator) {
      float mAnimatedValue = (Float)valueAnimator.getAnimatedValue();
      this.eatErPositionX = (this.mWidth - 2.0F * this.mPadding - this.eatErWidth) * mAnimatedValue;
      this.eatErStartAngle = this.mAngle * (1.0F - (mAnimatedValue * (float)this.eatSpeed - (float)((int)(mAnimatedValue * (float)this.eatSpeed))));
      this.eatErEndAngle = 360.0F - this.eatErStartAngle * 2.0F;
      this.invalidate();
   }

   protected void OnAnimationRepeat(Animator animation) {
   }

   protected int OnStopAnim() {
      this.eatErPositionX = 0.0F;
      this.postInvalidate();
      return 1;
   }

   protected int SetAnimRepeatMode() {
      return 1;
   }

   protected void AnimIsRunning() {
   }

   protected int SetAnimRepeatCount() {
      return -1;
   }
}
