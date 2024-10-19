package com.carlos.common.imagepicker.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import com.kook.librelease.R.color;
import com.kook.librelease.R.dimen;

public class HorizontalProgressWheelView extends View {
   private final Rect mCanvasClipBounds;
   private ScrollingListener mScrollingListener;
   private float mLastTouchedPosition;
   private Paint mProgressLinePaint;
   private int mProgressLineWidth;
   private int mProgressLineHeight;
   private int mProgressLineMargin;
   private boolean mScrollStarted;
   private float mTotalScrollDistance;
   private int mMiddleLineColor;

   public HorizontalProgressWheelView(Context context) {
      this(context, (AttributeSet)null);
   }

   public HorizontalProgressWheelView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public HorizontalProgressWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      this.mCanvasClipBounds = new Rect();
      this.init();
   }

   @TargetApi(21)
   public HorizontalProgressWheelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
      super(context, attrs, defStyleAttr, defStyleRes);
      this.mCanvasClipBounds = new Rect();
   }

   public void setScrollingListener(ScrollingListener scrollingListener) {
      this.mScrollingListener = scrollingListener;
   }

   public void setMiddleLineColor(@ColorInt int middleLineColor) {
      this.mMiddleLineColor = middleLineColor;
      this.invalidate();
   }

   public boolean onTouchEvent(MotionEvent event) {
      switch (event.getAction()) {
         case 0:
            this.mLastTouchedPosition = event.getX();
            break;
         case 1:
            if (this.mScrollingListener != null) {
               this.mScrollStarted = false;
               this.mScrollingListener.onScrollEnd();
            }
            break;
         case 2:
            float distance = event.getX() - this.mLastTouchedPosition;
            if (distance != 0.0F) {
               if (!this.mScrollStarted) {
                  this.mScrollStarted = true;
                  if (this.mScrollingListener != null) {
                     this.mScrollingListener.onScrollStart();
                  }
               }

               this.onScrollEvent(event, distance);
            }
      }

      return true;
   }

   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      canvas.getClipBounds(this.mCanvasClipBounds);
      int linesCount = this.mCanvasClipBounds.width() / (this.mProgressLineWidth + this.mProgressLineMargin);
      float deltaX = this.mTotalScrollDistance % (float)(this.mProgressLineMargin + this.mProgressLineWidth);
      this.mProgressLinePaint.setColor(this.getResources().getColor(color.ucrop_color_progress_wheel_line));

      for(int i = 0; i < linesCount; ++i) {
         if (i < linesCount / 4) {
            this.mProgressLinePaint.setAlpha((int)(255.0F * ((float)i / (float)(linesCount / 4))));
         } else if (i > linesCount * 3 / 4) {
            this.mProgressLinePaint.setAlpha((int)(255.0F * ((float)(linesCount - i) / (float)(linesCount / 4))));
         } else {
            this.mProgressLinePaint.setAlpha(255);
         }

         canvas.drawLine(-deltaX + (float)this.mCanvasClipBounds.left + (float)(i * (this.mProgressLineWidth + this.mProgressLineMargin)), (float)this.mCanvasClipBounds.centerY() - (float)this.mProgressLineHeight / 4.0F, -deltaX + (float)this.mCanvasClipBounds.left + (float)(i * (this.mProgressLineWidth + this.mProgressLineMargin)), (float)this.mCanvasClipBounds.centerY() + (float)this.mProgressLineHeight / 4.0F, this.mProgressLinePaint);
      }

      this.mProgressLinePaint.setColor(this.mMiddleLineColor);
      canvas.drawLine((float)this.mCanvasClipBounds.centerX(), (float)this.mCanvasClipBounds.centerY() - (float)this.mProgressLineHeight / 2.0F, (float)this.mCanvasClipBounds.centerX(), (float)this.mCanvasClipBounds.centerY() + (float)this.mProgressLineHeight / 2.0F, this.mProgressLinePaint);
   }

   private void onScrollEvent(MotionEvent event, float distance) {
      this.mTotalScrollDistance -= distance;
      this.postInvalidate();
      this.mLastTouchedPosition = event.getX();
      if (this.mScrollingListener != null) {
         this.mScrollingListener.onScroll(-distance, this.mTotalScrollDistance);
      }

   }

   private void init() {
      this.mMiddleLineColor = ContextCompat.getColor(this.getContext(), color.ucrop_color_progress_wheel_line);
      this.mProgressLineWidth = this.getContext().getResources().getDimensionPixelSize(dimen.ucrop_width_horizontal_wheel_progress_line);
      this.mProgressLineHeight = this.getContext().getResources().getDimensionPixelSize(dimen.ucrop_height_horizontal_wheel_progress_line);
      this.mProgressLineMargin = this.getContext().getResources().getDimensionPixelSize(dimen.ucrop_margin_horizontal_wheel_progress_line);
      this.mProgressLinePaint = new Paint(1);
      this.mProgressLinePaint.setStyle(Style.STROKE);
      this.mProgressLinePaint.setStrokeWidth((float)this.mProgressLineWidth);
   }

   public interface ScrollingListener {
      void onScrollStart();

      void onScroll(float var1, float var2);

      void onScrollEnd();
   }
}
