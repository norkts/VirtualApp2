package com.carlos.common.imagepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import com.carlos.common.imagepicker.callback.OverlayViewChangeListener;
import com.carlos.common.imagepicker.util.RectUtils;
import com.kook.librelease.R.color;
import com.kook.librelease.R.dimen;
import com.kook.librelease.R.styleable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OverlayView extends View {
   public static final int FREESTYLE_CROP_MODE_DISABLE = 0;
   public static final int FREESTYLE_CROP_MODE_ENABLE = 1;
   public static final int FREESTYLE_CROP_MODE_ENABLE_WITH_PASS_THROUGH = 2;
   public static final boolean DEFAULT_SHOW_CROP_FRAME = true;
   public static final boolean DEFAULT_SHOW_CROP_GRID = true;
   public static final boolean DEFAULT_CIRCLE_DIMMED_LAYER = false;
   public static final int DEFAULT_FREESTYLE_CROP_MODE = 0;
   public static final int DEFAULT_CROP_GRID_ROW_COUNT = 2;
   public static final int DEFAULT_CROP_GRID_COLUMN_COUNT = 2;
   private final RectF mCropViewRect;
   private final RectF mTempRect;
   protected int mThisWidth;
   protected int mThisHeight;
   protected float[] mCropGridCorners;
   protected float[] mCropGridCenter;
   private int mCropGridRowCount;
   private int mCropGridColumnCount;
   private float mTargetAspectRatio;
   private float[] mGridPoints;
   private boolean mShowCropFrame;
   private boolean mShowCropGrid;
   private boolean mCircleDimmedLayer;
   private int mDimmedColor;
   private Path mCircularPath;
   private Paint mDimmedStrokePaint;
   private Paint mCropGridPaint;
   private Paint mCropFramePaint;
   private Paint mCropFrameCornersPaint;
   private int mFreestyleCropMode;
   private float mPreviousTouchX;
   private float mPreviousTouchY;
   private int mCurrentTouchCornerIndex;
   private int mTouchPointThreshold;
   private int mCropRectMinSize;
   private int mCropRectCornerTouchAreaLineLength;
   private OverlayViewChangeListener mCallback;
   private boolean mShouldSetupCropBounds;

   public OverlayView(Context context) {
      this(context, (AttributeSet)null);
   }

   public OverlayView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public OverlayView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      this.mCropViewRect = new RectF();
      this.mTempRect = new RectF();
      this.mGridPoints = null;
      this.mCircularPath = new Path();
      this.mDimmedStrokePaint = new Paint(1);
      this.mCropGridPaint = new Paint(1);
      this.mCropFramePaint = new Paint(1);
      this.mCropFrameCornersPaint = new Paint(1);
      this.mFreestyleCropMode = 0;
      this.mPreviousTouchX = -1.0F;
      this.mPreviousTouchY = -1.0F;
      this.mCurrentTouchCornerIndex = -1;
      this.mTouchPointThreshold = this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_rect_corner_touch_threshold);
      this.mCropRectMinSize = this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_rect_min_size);
      this.mCropRectCornerTouchAreaLineLength = this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_rect_corner_touch_area_line_length);
      this.init();
   }

   public OverlayViewChangeListener getOverlayViewChangeListener() {
      return this.mCallback;
   }

   public void setOverlayViewChangeListener(OverlayViewChangeListener callback) {
      this.mCallback = callback;
   }

   @NonNull
   public RectF getCropViewRect() {
      return this.mCropViewRect;
   }

   /** @deprecated */
   @Deprecated
   public boolean isFreestyleCropEnabled() {
      return this.mFreestyleCropMode == 1;
   }

   /** @deprecated */
   @Deprecated
   public void setFreestyleCropEnabled(boolean freestyleCropEnabled) {
      this.mFreestyleCropMode = freestyleCropEnabled ? 1 : 0;
   }

   public int getFreestyleCropMode() {
      return this.mFreestyleCropMode;
   }

   public void setFreestyleCropMode(int mFreestyleCropMode) {
      this.mFreestyleCropMode = mFreestyleCropMode;
      this.postInvalidate();
   }

   public void setCircleDimmedLayer(boolean circleDimmedLayer) {
      this.mCircleDimmedLayer = circleDimmedLayer;
   }

   public void setCropGridRowCount(@IntRange(from = 0L) int cropGridRowCount) {
      this.mCropGridRowCount = cropGridRowCount;
      this.mGridPoints = null;
   }

   public void setCropGridColumnCount(@IntRange(from = 0L) int cropGridColumnCount) {
      this.mCropGridColumnCount = cropGridColumnCount;
      this.mGridPoints = null;
   }

   public void setShowCropFrame(boolean showCropFrame) {
      this.mShowCropFrame = showCropFrame;
   }

   public void setShowCropGrid(boolean showCropGrid) {
      this.mShowCropGrid = showCropGrid;
   }

   public void setDimmedColor(@ColorInt int dimmedColor) {
      this.mDimmedColor = dimmedColor;
   }

   public void setCropFrameStrokeWidth(@IntRange(from = 0L) int width) {
      this.mCropFramePaint.setStrokeWidth((float)width);
   }

   public void setCropGridStrokeWidth(@IntRange(from = 0L) int width) {
      this.mCropGridPaint.setStrokeWidth((float)width);
   }

   public void setCropFrameColor(@ColorInt int color) {
      this.mCropFramePaint.setColor(color);
   }

   public void setCropGridColor(@ColorInt int color) {
      this.mCropGridPaint.setColor(color);
   }

   public void setTargetAspectRatio(float targetAspectRatio) {
      this.mTargetAspectRatio = targetAspectRatio;
      if (this.mThisWidth > 0) {
         this.setupCropBounds();
         this.postInvalidate();
      } else {
         this.mShouldSetupCropBounds = true;
      }

   }

   public void setupCropBounds() {
      int height = (int)((float)this.mThisWidth / this.mTargetAspectRatio);
      int width;
      if (height > this.mThisHeight) {
         width = (int)((float)this.mThisHeight * this.mTargetAspectRatio);
         int halfDiff = (this.mThisWidth - width) / 2;
         this.mCropViewRect.set((float)(this.getPaddingLeft() + halfDiff), (float)this.getPaddingTop(), (float)(this.getPaddingLeft() + width + halfDiff), (float)(this.getPaddingTop() + this.mThisHeight));
      } else {
         width = (this.mThisHeight - height) / 2;
         this.mCropViewRect.set((float)this.getPaddingLeft(), (float)(this.getPaddingTop() + width), (float)(this.getPaddingLeft() + this.mThisWidth), (float)(this.getPaddingTop() + height + width));
      }

      if (this.mCallback != null) {
         this.mCallback.onCropRectUpdated(this.mCropViewRect);
      }

      this.updateGridPoints();
   }

   private void updateGridPoints() {
      this.mCropGridCorners = RectUtils.getCornersFromRect(this.mCropViewRect);
      this.mCropGridCenter = RectUtils.getCenterFromRect(this.mCropViewRect);
      this.mGridPoints = null;
      this.mCircularPath.reset();
      this.mCircularPath.addCircle(this.mCropViewRect.centerX(), this.mCropViewRect.centerY(), Math.min(this.mCropViewRect.width(), this.mCropViewRect.height()) / 2.0F, Direction.CW);
   }

   protected void init() {
      if (VERSION.SDK_INT < 18) {
         this.setLayerType(1, (Paint)null);
      }

   }

   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
      super.onLayout(changed, left, top, right, bottom);
      if (changed) {
         left = this.getPaddingLeft();
         top = this.getPaddingTop();
         right = this.getWidth() - this.getPaddingRight();
         bottom = this.getHeight() - this.getPaddingBottom();
         this.mThisWidth = right - left;
         this.mThisHeight = bottom - top;
         if (this.mShouldSetupCropBounds) {
            this.mShouldSetupCropBounds = false;
            this.setTargetAspectRatio(this.mTargetAspectRatio);
         }
      }

   }

   protected void onDraw(Canvas canvas) {
      super.onDraw(canvas);
      this.drawDimmedLayer(canvas);
      this.drawCropGrid(canvas);
   }

   public boolean onTouchEvent(MotionEvent event) {
      if (!this.mCropViewRect.isEmpty() && this.mFreestyleCropMode != 0) {
         float x = event.getX();
         float y = event.getY();
         if ((event.getAction() & 255) == 0) {
            this.mCurrentTouchCornerIndex = this.getCurrentTouchIndex(x, y);
            boolean shouldHandle = this.mCurrentTouchCornerIndex != -1;
            if (!shouldHandle) {
               this.mPreviousTouchX = -1.0F;
               this.mPreviousTouchY = -1.0F;
            } else if (this.mPreviousTouchX < 0.0F) {
               this.mPreviousTouchX = x;
               this.mPreviousTouchY = y;
            }

            return shouldHandle;
         } else if ((event.getAction() & 255) == 2 && event.getPointerCount() == 1 && this.mCurrentTouchCornerIndex != -1) {
            x = Math.min(Math.max(x, (float)this.getPaddingLeft()), (float)(this.getWidth() - this.getPaddingRight()));
            y = Math.min(Math.max(y, (float)this.getPaddingTop()), (float)(this.getHeight() - this.getPaddingBottom()));
            this.updateCropViewRect(x, y);
            this.mPreviousTouchX = x;
            this.mPreviousTouchY = y;
            return true;
         } else {
            if ((event.getAction() & 255) == 1) {
               this.mPreviousTouchX = -1.0F;
               this.mPreviousTouchY = -1.0F;
               this.mCurrentTouchCornerIndex = -1;
               if (this.mCallback != null) {
                  this.mCallback.onCropRectUpdated(this.mCropViewRect);
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   private void updateCropViewRect(float touchX, float touchY) {
      this.mTempRect.set(this.mCropViewRect);
      switch (this.mCurrentTouchCornerIndex) {
         case 0:
            this.mTempRect.set(touchX, touchY, this.mCropViewRect.right, this.mCropViewRect.bottom);
            break;
         case 1:
            this.mTempRect.set(this.mCropViewRect.left, touchY, touchX, this.mCropViewRect.bottom);
            break;
         case 2:
            this.mTempRect.set(this.mCropViewRect.left, this.mCropViewRect.top, touchX, touchY);
            break;
         case 3:
            this.mTempRect.set(touchX, this.mCropViewRect.top, this.mCropViewRect.right, touchY);
            break;
         case 4:
            this.mTempRect.offset(touchX - this.mPreviousTouchX, touchY - this.mPreviousTouchY);
            if (this.mTempRect.left > (float)this.getLeft() && this.mTempRect.top > (float)this.getTop() && this.mTempRect.right < (float)this.getRight() && this.mTempRect.bottom < (float)this.getBottom()) {
               this.mCropViewRect.set(this.mTempRect);
               this.updateGridPoints();
               this.postInvalidate();
            }

            return;
      }

      boolean changeHeight = this.mTempRect.height() >= (float)this.mCropRectMinSize;
      boolean changeWidth = this.mTempRect.width() >= (float)this.mCropRectMinSize;
      this.mCropViewRect.set(changeWidth ? this.mTempRect.left : this.mCropViewRect.left, changeHeight ? this.mTempRect.top : this.mCropViewRect.top, changeWidth ? this.mTempRect.right : this.mCropViewRect.right, changeHeight ? this.mTempRect.bottom : this.mCropViewRect.bottom);
      if (changeHeight || changeWidth) {
         this.updateGridPoints();
         this.postInvalidate();
      }

   }

   private int getCurrentTouchIndex(float touchX, float touchY) {
      int closestPointIndex = -1;
      double closestPointDistance = (double)this.mTouchPointThreshold;

      for(int i = 0; i < 8; i += 2) {
         double distanceToCorner = Math.sqrt(Math.pow((double)(touchX - this.mCropGridCorners[i]), 2.0) + Math.pow((double)(touchY - this.mCropGridCorners[i + 1]), 2.0));
         if (distanceToCorner < closestPointDistance) {
            closestPointDistance = distanceToCorner;
            closestPointIndex = i / 2;
         }
      }

      if (this.mFreestyleCropMode == 1 && closestPointIndex < 0 && this.mCropViewRect.contains(touchX, touchY)) {
         return 4;
      } else {
         return closestPointIndex;
      }
   }

   protected void drawDimmedLayer(@NonNull Canvas canvas) {
      canvas.save();
      if (this.mCircleDimmedLayer) {
         canvas.clipPath(this.mCircularPath, Op.DIFFERENCE);
      } else {
         canvas.clipRect(this.mCropViewRect, Op.DIFFERENCE);
      }

      canvas.drawColor(this.mDimmedColor);
      canvas.restore();
      if (this.mCircleDimmedLayer) {
         canvas.drawCircle(this.mCropViewRect.centerX(), this.mCropViewRect.centerY(), Math.min(this.mCropViewRect.width(), this.mCropViewRect.height()) / 2.0F, this.mDimmedStrokePaint);
      }

   }

   protected void drawCropGrid(@NonNull Canvas canvas) {
      if (this.mShowCropGrid) {
         if (this.mGridPoints == null && !this.mCropViewRect.isEmpty()) {
            this.mGridPoints = new float[this.mCropGridRowCount * 4 + this.mCropGridColumnCount * 4];
            int index = 0;

            int i;
            for(i = 0; i < this.mCropGridRowCount; ++i) {
               this.mGridPoints[index++] = this.mCropViewRect.left;
               this.mGridPoints[index++] = this.mCropViewRect.height() * (((float)i + 1.0F) / (float)(this.mCropGridRowCount + 1)) + this.mCropViewRect.top;
               this.mGridPoints[index++] = this.mCropViewRect.right;
               this.mGridPoints[index++] = this.mCropViewRect.height() * (((float)i + 1.0F) / (float)(this.mCropGridRowCount + 1)) + this.mCropViewRect.top;
            }

            for(i = 0; i < this.mCropGridColumnCount; ++i) {
               this.mGridPoints[index++] = this.mCropViewRect.width() * (((float)i + 1.0F) / (float)(this.mCropGridColumnCount + 1)) + this.mCropViewRect.left;
               this.mGridPoints[index++] = this.mCropViewRect.top;
               this.mGridPoints[index++] = this.mCropViewRect.width() * (((float)i + 1.0F) / (float)(this.mCropGridColumnCount + 1)) + this.mCropViewRect.left;
               this.mGridPoints[index++] = this.mCropViewRect.bottom;
            }
         }

         if (this.mGridPoints != null) {
            canvas.drawLines(this.mGridPoints, this.mCropGridPaint);
         }
      }

      if (this.mShowCropFrame) {
         canvas.drawRect(this.mCropViewRect, this.mCropFramePaint);
      }

      if (this.mFreestyleCropMode != 0) {
         canvas.save();
         this.mTempRect.set(this.mCropViewRect);
         this.mTempRect.inset((float)this.mCropRectCornerTouchAreaLineLength, (float)(-this.mCropRectCornerTouchAreaLineLength));
         canvas.clipRect(this.mTempRect, Op.DIFFERENCE);
         this.mTempRect.set(this.mCropViewRect);
         this.mTempRect.inset((float)(-this.mCropRectCornerTouchAreaLineLength), (float)this.mCropRectCornerTouchAreaLineLength);
         canvas.clipRect(this.mTempRect, Op.DIFFERENCE);
         canvas.drawRect(this.mCropViewRect, this.mCropFrameCornersPaint);
         canvas.restore();
      }

   }

   protected void processStyledAttributes(@NonNull TypedArray a) {
      this.mCircleDimmedLayer = a.getBoolean(styleable.ucrop_UCropView_ucrop_circle_dimmed_layer, false);
      this.mDimmedColor = a.getColor(styleable.ucrop_UCropView_ucrop_dimmed_color, this.getResources().getColor(color.ucrop_color_default_dimmed));
      this.mDimmedStrokePaint.setColor(this.mDimmedColor);
      this.mDimmedStrokePaint.setStyle(Style.STROKE);
      this.mDimmedStrokePaint.setStrokeWidth(1.0F);
      this.initCropFrameStyle(a);
      this.mShowCropFrame = a.getBoolean(styleable.ucrop_UCropView_ucrop_show_frame, true);
      this.initCropGridStyle(a);
      this.mShowCropGrid = a.getBoolean(styleable.ucrop_UCropView_ucrop_show_grid, true);
   }

   private void initCropFrameStyle(@NonNull TypedArray a) {
      int cropFrameStrokeSize = a.getDimensionPixelSize(styleable.ucrop_UCropView_ucrop_frame_stroke_size, this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_frame_stoke_width));
      int cropFrameColor = a.getColor(styleable.ucrop_UCropView_ucrop_frame_color, this.getResources().getColor(color.ucrop_color_default_crop_frame));
      this.mCropFramePaint.setStrokeWidth((float)cropFrameStrokeSize);
      this.mCropFramePaint.setColor(cropFrameColor);
      this.mCropFramePaint.setStyle(Style.STROKE);
      this.mCropFrameCornersPaint.setStrokeWidth((float)(cropFrameStrokeSize * 3));
      this.mCropFrameCornersPaint.setColor(cropFrameColor);
      this.mCropFrameCornersPaint.setStyle(Style.STROKE);
   }

   private void initCropGridStyle(@NonNull TypedArray a) {
      int cropGridStrokeSize = a.getDimensionPixelSize(styleable.ucrop_UCropView_ucrop_grid_stroke_size, this.getResources().getDimensionPixelSize(dimen.ucrop_default_crop_grid_stoke_width));
      int cropGridColor = a.getColor(styleable.ucrop_UCropView_ucrop_grid_color, this.getResources().getColor(color.ucrop_color_default_crop_grid));
      this.mCropGridPaint.setStrokeWidth((float)cropGridStrokeSize);
      this.mCropGridPaint.setColor(cropGridColor);
      this.mCropGridRowCount = a.getInt(styleable.ucrop_UCropView_ucrop_grid_row_count, 2);
      this.mCropGridColumnCount = a.getInt(styleable.ucrop_UCropView_ucrop_grid_column_count, 2);
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface FreestyleMode {
   }
}
