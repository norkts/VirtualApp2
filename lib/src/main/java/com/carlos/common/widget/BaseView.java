package com.carlos.common.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public abstract class BaseView extends View {
   public ValueAnimator valueAnimator;

   public BaseView(Context context) {
      this(context, (AttributeSet)null);
   }

   public BaseView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      this.InitPaint();
   }

   public void startAnim() {
      this.stopAnim();
      this.startViewAnim(0.0F, 1.0F, 500L);
   }

   public void startAnim(int time) {
      this.stopAnim();
      this.startViewAnim(0.0F, 1.0F, (long)time);
   }

   public void stopAnim() {
      if (this.valueAnimator != null) {
         this.clearAnimation();
         this.valueAnimator.setRepeatCount(0);
         this.valueAnimator.cancel();
         this.valueAnimator.end();
         if (this.OnStopAnim() == 0) {
            this.valueAnimator.setRepeatCount(0);
            this.valueAnimator.cancel();
            this.valueAnimator.end();
         }
      }

   }

   private ValueAnimator startViewAnim(float startF, float endF, long time) {
      this.valueAnimator = ValueAnimator.ofFloat(new float[]{startF, endF});
      this.valueAnimator.setDuration(time);
      this.valueAnimator.setInterpolator(new LinearInterpolator());
      this.valueAnimator.setRepeatCount(this.SetAnimRepeatCount());
      if (1 == this.SetAnimRepeatMode()) {
         this.valueAnimator.setRepeatMode(1);
      } else if (2 == this.SetAnimRepeatMode()) {
         this.valueAnimator.setRepeatMode(2);
      }

      this.valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator valueAnimator) {
            BaseView.this.OnAnimationUpdate(valueAnimator);
         }
      });
      this.valueAnimator.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
         }

         public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
         }

         public void onAnimationRepeat(Animator animation) {
            super.onAnimationRepeat(animation);
            BaseView.this.OnAnimationRepeat(animation);
         }
      });
      if (!this.valueAnimator.isRunning()) {
         this.AnimIsRunning();
         this.valueAnimator.start();
      }

      return this.valueAnimator;
   }

   protected abstract void InitPaint();

   protected abstract void OnAnimationUpdate(ValueAnimator var1);

   protected abstract void OnAnimationRepeat(Animator var1);

   protected abstract int OnStopAnim();

   protected abstract int SetAnimRepeatMode();

   protected abstract int SetAnimRepeatCount();

   protected abstract void AnimIsRunning();

   public float getFontlength(Paint paint, String str) {
      Rect rect = new Rect();
      paint.getTextBounds(str, 0, str.length(), rect);
      return (float)rect.width();
   }

   public float getFontHeight(Paint paint, String str) {
      Rect rect = new Rect();
      paint.getTextBounds(str, 0, str.length(), rect);
      return (float)rect.height();
   }

   public float getFontHeight(Paint paint) {
      Paint.FontMetrics fm = paint.getFontMetrics();
      return fm.descent - fm.ascent;
   }
}
