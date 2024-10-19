package com.carlos.common.widget.tablayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import com.kook.librelease.R.styleable;

public class MsgView extends TextView {
   private Context context;
   private GradientDrawable gd_background;
   private int backgroundColor;
   private int cornerRadius;
   private int strokeWidth;
   private int strokeColor;
   private boolean isRadiusHalfHeight;
   private boolean isWidthHeightEqual;

   public MsgView(Context context) {
      this(context, (AttributeSet)null);
   }

   public MsgView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public MsgView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      this.gd_background = new GradientDrawable();
      this.context = context;
      this.obtainAttributes(context, attrs);
   }

   private void obtainAttributes(Context context, AttributeSet attrs) {
      TypedArray ta = context.obtainStyledAttributes(attrs, styleable.MsgView);
      this.backgroundColor = ta.getColor(styleable.MsgView_mv_backgroundColor, 0);
      this.cornerRadius = ta.getDimensionPixelSize(styleable.MsgView_mv_cornerRadius, 0);
      this.strokeWidth = ta.getDimensionPixelSize(styleable.MsgView_mv_strokeWidth, 0);
      this.strokeColor = ta.getColor(styleable.MsgView_mv_strokeColor, 0);
      this.isRadiusHalfHeight = ta.getBoolean(styleable.MsgView_mv_isRadiusHalfHeight, false);
      this.isWidthHeightEqual = ta.getBoolean(styleable.MsgView_mv_isWidthHeightEqual, false);
      ta.recycle();
   }

   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      if (this.isWidthHeightEqual() && this.getWidth() > 0 && this.getHeight() > 0) {
         int max = Math.max(this.getWidth(), this.getHeight());
         int measureSpec = MeasureSpec.makeMeasureSpec(max, 1073741824);
         super.onMeasure(measureSpec, measureSpec);
      } else {
         super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      }
   }

   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
      super.onLayout(changed, left, top, right, bottom);
      if (this.isRadiusHalfHeight()) {
         this.setCornerRadius(this.getHeight() / 2);
      } else {
         this.setBgSelector();
      }

   }

   public void setBackgroundColor(int backgroundColor) {
      this.backgroundColor = backgroundColor;
      this.setBgSelector();
   }

   public void setCornerRadius(int cornerRadius) {
      this.cornerRadius = this.dp2px((float)cornerRadius);
      this.setBgSelector();
   }

   public void setStrokeWidth(int strokeWidth) {
      this.strokeWidth = this.dp2px((float)strokeWidth);
      this.setBgSelector();
   }

   public void setStrokeColor(int strokeColor) {
      this.strokeColor = strokeColor;
      this.setBgSelector();
   }

   public void setIsRadiusHalfHeight(boolean isRadiusHalfHeight) {
      this.isRadiusHalfHeight = isRadiusHalfHeight;
      this.setBgSelector();
   }

   public void setIsWidthHeightEqual(boolean isWidthHeightEqual) {
      this.isWidthHeightEqual = isWidthHeightEqual;
      this.setBgSelector();
   }

   public int getBackgroundColor() {
      return this.backgroundColor;
   }

   public int getCornerRadius() {
      return this.cornerRadius;
   }

   public int getStrokeWidth() {
      return this.strokeWidth;
   }

   public int getStrokeColor() {
      return this.strokeColor;
   }

   public boolean isRadiusHalfHeight() {
      return this.isRadiusHalfHeight;
   }

   public boolean isWidthHeightEqual() {
      return this.isWidthHeightEqual;
   }

   protected int dp2px(float dp) {
      float scale = this.context.getResources().getDisplayMetrics().density;
      return (int)(dp * scale + 0.5F);
   }

   protected int sp2px(float sp) {
      float scale = this.context.getResources().getDisplayMetrics().scaledDensity;
      return (int)(sp * scale + 0.5F);
   }

   private void setDrawable(GradientDrawable gd, int color, int strokeColor) {
      gd.setColor(color);
      gd.setCornerRadius((float)this.cornerRadius);
      gd.setStroke(this.strokeWidth, strokeColor);
   }

   public void setBgSelector() {
      StateListDrawable bg = new StateListDrawable();
      this.setDrawable(this.gd_background, this.backgroundColor, this.strokeColor);
      bg.addState(new int[]{-16842919}, this.gd_background);
      if (VERSION.SDK_INT >= 16) {
         this.setBackground(bg);
      } else {
         this.setBackgroundDrawable(bg);
      }

   }
}
