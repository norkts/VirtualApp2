package com.carlos.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.styleable;

public class LabelView extends View {
   private static final int DEFAULT_DEGREES = 45;
   private String mTextContent;
   private int mTextColor;
   private float mTextSize;
   private boolean mTextBold;
   private boolean mFillTriangle;
   private boolean mTextAllCaps;
   private int mBackgroundColor;
   private float mMinSize;
   private float mPadding;
   private int mGravity;
   private Paint mTextPaint;
   private Paint mBackgroundPaint;
   private Path mPath;

   public LabelView(Context context) {
      this(context, (AttributeSet)null);
   }

   public LabelView(Context context, AttributeSet attrs) {
      super(context, attrs);
      this.mTextPaint = new Paint(1);
      this.mBackgroundPaint = new Paint(1);
      this.mPath = new Path();
      this.obtainAttributes(context, attrs);
      this.mTextPaint.setTextAlign(Align.CENTER);
   }

   private void obtainAttributes(Context context, AttributeSet attrs) {
      TypedArray ta = context.obtainStyledAttributes(attrs, styleable.LabelView);
      this.mTextContent = ta.getString(styleable.LabelView_lv_text);
      this.mTextColor = ta.getColor(styleable.LabelView_lv_text_color, Color.parseColor("#ffffff"));
      this.mTextSize = ta.getDimension(styleable.LabelView_lv_text_size, (float)this.sp2px(11.0F));
      this.mTextBold = ta.getBoolean(styleable.LabelView_lv_text_bold, true);
      this.mTextAllCaps = ta.getBoolean(styleable.LabelView_lv_text_all_caps, true);
      this.mFillTriangle = ta.getBoolean(styleable.LabelView_lv_fill_triangle, false);
      this.mBackgroundColor = ta.getColor(styleable.LabelView_lv_background_color, Color.parseColor("#FF4081"));
      this.mMinSize = ta.getDimension(styleable.LabelView_lv_min_size, this.mFillTriangle ? (float)this.dp2px(35.0F) : (float)this.dp2px(50.0F));
      this.mPadding = ta.getDimension(styleable.LabelView_lv_padding, (float)this.dp2px(3.5F));
      this.mGravity = ta.getInt(styleable.LabelView_lv_gravity, 51);
      ta.recycle();
   }

   public String getText() {
      return this.mTextContent;
   }

   public void setText(String text) {
      this.mTextContent = text;
      this.invalidate();
   }

   public int getTextColor() {
      return this.mTextColor;
   }

   public void setTextColor(int textColor) {
      this.mTextColor = textColor;
      this.invalidate();
   }

   public float getTextSize() {
      return this.mTextSize;
   }

   public void setTextSize(float textSize) {
      this.mTextSize = (float)this.sp2px(textSize);
      this.invalidate();
   }

   public boolean isTextBold() {
      return this.mTextBold;
   }

   public void setTextBold(boolean textBold) {
      this.mTextBold = textBold;
      this.invalidate();
   }

   public boolean isFillTriangle() {
      return this.mFillTriangle;
   }

   public void setFillTriangle(boolean fillTriangle) {
      this.mFillTriangle = fillTriangle;
      this.invalidate();
   }

   public boolean isTextAllCaps() {
      return this.mTextAllCaps;
   }

   public void setTextAllCaps(boolean textAllCaps) {
      this.mTextAllCaps = textAllCaps;
      this.invalidate();
   }

   public int getBgColor() {
      return this.mBackgroundColor;
   }

   public void setBgColor(int backgroundColor) {
      this.mBackgroundColor = backgroundColor;
      this.invalidate();
   }

   public float getMinSize() {
      return this.mMinSize;
   }

   public void setMinSize(float minSize) {
      this.mMinSize = (float)this.dp2px(minSize);
      this.invalidate();
   }

   public float getPadding() {
      return this.mPadding;
   }

   public void setPadding(float padding) {
      this.mPadding = (float)this.dp2px(padding);
      this.invalidate();
   }

   public int getGravity() {
      return this.mGravity;
   }

   public void setGravity(int gravity) {
      this.mGravity = gravity;
   }

   protected void onDraw(Canvas canvas) {
      int size = this.getHeight();
      this.mTextPaint.setColor(this.mTextColor);
      this.mTextPaint.setTextSize(this.mTextSize);
      this.mTextPaint.setFakeBoldText(this.mTextBold);
      this.mBackgroundPaint.setColor(this.mBackgroundColor);
      float textHeight = this.mTextPaint.descent() - this.mTextPaint.ascent();
      if (this.mFillTriangle) {
         if (this.mGravity == 51) {
            this.mPath.reset();
            this.mPath.moveTo(0.0F, 0.0F);
            this.mPath.lineTo(0.0F, (float)size);
            this.mPath.lineTo((float)size, 0.0F);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            this.drawTextWhenFill(size, -45.0F, canvas, true);
         } else if (this.mGravity == 53) {
            this.mPath.reset();
            this.mPath.moveTo((float)size, 0.0F);
            this.mPath.lineTo(0.0F, 0.0F);
            this.mPath.lineTo((float)size, (float)size);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            this.drawTextWhenFill(size, 45.0F, canvas, true);
         } else if (this.mGravity == 83) {
            this.mPath.reset();
            this.mPath.moveTo(0.0F, (float)size);
            this.mPath.lineTo(0.0F, 0.0F);
            this.mPath.lineTo((float)size, (float)size);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            this.drawTextWhenFill(size, 45.0F, canvas, false);
         } else if (this.mGravity == 85) {
            this.mPath.reset();
            this.mPath.moveTo((float)size, (float)size);
            this.mPath.lineTo(0.0F, (float)size);
            this.mPath.lineTo((float)size, 0.0F);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            this.drawTextWhenFill(size, -45.0F, canvas, false);
         }
      } else {
         double delta = (double)(textHeight + this.mPadding * 2.0F) * Math.sqrt(2.0);
         if (this.mGravity == 51) {
            this.mPath.reset();
            this.mPath.moveTo(0.0F, (float)((double)size - delta));
            this.mPath.lineTo(0.0F, (float)size);
            this.mPath.lineTo((float)size, 0.0F);
            this.mPath.lineTo((float)((double)size - delta), 0.0F);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            this.drawText(size, -45.0F, canvas, textHeight, true);
         } else if (this.mGravity == 53) {
            this.mPath.reset();
            this.mPath.moveTo(0.0F, 0.0F);
            this.mPath.lineTo((float)delta, 0.0F);
            this.mPath.lineTo((float)size, (float)((double)size - delta));
            this.mPath.lineTo((float)size, (float)size);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            this.drawText(size, 45.0F, canvas, textHeight, true);
         } else if (this.mGravity == 83) {
            this.mPath.reset();
            this.mPath.moveTo(0.0F, 0.0F);
            this.mPath.lineTo(0.0F, (float)delta);
            this.mPath.lineTo((float)((double)size - delta), (float)size);
            this.mPath.lineTo((float)size, (float)size);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            this.drawText(size, 45.0F, canvas, textHeight, false);
         } else if (this.mGravity == 85) {
            this.mPath.reset();
            this.mPath.moveTo(0.0F, (float)size);
            this.mPath.lineTo((float)delta, (float)size);
            this.mPath.lineTo((float)size, (float)delta);
            this.mPath.lineTo((float)size, 0.0F);
            this.mPath.close();
            canvas.drawPath(this.mPath, this.mBackgroundPaint);
            this.drawText(size, -45.0F, canvas, textHeight, false);
         }
      }

   }

   private void drawText(int size, float degrees, Canvas canvas, float textHeight, boolean isTop) {
      canvas.save();
      canvas.rotate(degrees, (float)size / 2.0F, (float)size / 2.0F);
      float delta = isTop ? -(textHeight + this.mPadding * 2.0F) / 2.0F : (textHeight + this.mPadding * 2.0F) / 2.0F;
      float textBaseY = (float)(size / 2) - (this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0F + delta;
      canvas.drawText(this.mTextAllCaps ? this.mTextContent.toUpperCase() : this.mTextContent, (float)(this.getPaddingLeft() + (size - this.getPaddingLeft() - this.getPaddingRight()) / 2), textBaseY, this.mTextPaint);
      canvas.restore();
   }

   private void drawTextWhenFill(int size, float degrees, Canvas canvas, boolean isTop) {
      canvas.save();
      canvas.rotate(degrees, (float)size / 2.0F, (float)size / 2.0F);
      float delta = isTop ? (float)(-size / 4) : (float)(size / 4);
      float textBaseY = (float)(size / 2) - (this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0F + delta;
      canvas.drawText(this.mTextAllCaps ? this.mTextContent.toUpperCase() : this.mTextContent, (float)(this.getPaddingLeft() + (size - this.getPaddingLeft() - this.getPaddingRight()) / 2), textBaseY, this.mTextPaint);
      canvas.restore();
   }

   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      int measuredWidth = this.measureWidth(widthMeasureSpec);
      this.setMeasuredDimension(measuredWidth, measuredWidth);
   }

   private int measureWidth(int widthMeasureSpec) {
      int specMode = MeasureSpec.getMode(widthMeasureSpec);
      int specSize = MeasureSpec.getSize(widthMeasureSpec);
      int result;
      if (specMode == 1073741824) {
         result = specSize;
      } else {
         int padding = this.getPaddingLeft() + this.getPaddingRight();
         this.mTextPaint.setColor(this.mTextColor);
         this.mTextPaint.setTextSize(this.mTextSize);
         float textWidth = this.mTextPaint.measureText(this.mTextContent + "");
         result = (int)((double)(padding + (int)textWidth) * Math.sqrt(2.0));
         if (specMode == Integer.MIN_VALUE) {
            result = Math.min(result, specSize);
         }

         result = Math.max((int)this.mMinSize, result);
      }

      return result;
   }

   protected int dp2px(float dp) {
      float scale = this.getResources().getDisplayMetrics().density;
      return (int)(dp * scale + 0.5F);
   }

   protected int sp2px(float sp) {
      float scale = this.getResources().getDisplayMetrics().scaledDensity;
      return (int)(sp * scale + 0.5F);
   }
}
