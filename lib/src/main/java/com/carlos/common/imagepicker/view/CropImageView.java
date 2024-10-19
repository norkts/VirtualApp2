package com.carlos.common.imagepicker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.callback.BitmapCropCallback;
import com.carlos.common.imagepicker.callback.CropBoundsChangeListener;
import com.carlos.common.imagepicker.entity.CropParameters;
import com.carlos.common.imagepicker.entity.ImageState;
import com.carlos.common.imagepicker.task.BitmapCropTask;
import com.carlos.common.imagepicker.util.CubicEasing;
import com.carlos.common.imagepicker.util.RectUtils;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.styleable;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public class CropImageView extends TransformImageView {
   public static final int DEFAULT_MAX_BITMAP_SIZE = 0;
   public static final int DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = 500;
   public static final float DEFAULT_MAX_SCALE_MULTIPLIER = 10.0F;
   public static final float SOURCE_IMAGE_ASPECT_RATIO = 0.0F;
   public static final float DEFAULT_ASPECT_RATIO = 0.0F;
   private final RectF mCropRect;
   private final Matrix mTempMatrix;
   private float mTargetAspectRatio;
   private float mMaxScaleMultiplier;
   private CropBoundsChangeListener mCropBoundsChangeListener;
   private Runnable mWrapCropBoundsRunnable;
   private Runnable mZoomImageToPositionRunnable;
   private float mMaxScale;
   private float mMinScale;
   private int mMaxResultImageSizeX;
   private int mMaxResultImageSizeY;
   private long mImageToWrapCropBoundsAnimDuration;

   public CropImageView(Context context) {
      this(context, (AttributeSet)null);
   }

   public CropImageView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public CropImageView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      this.mCropRect = new RectF();
      this.mTempMatrix = new Matrix();
      this.mMaxScaleMultiplier = 10.0F;
      this.mZoomImageToPositionRunnable = null;
      this.mMaxResultImageSizeX = 0;
      this.mMaxResultImageSizeY = 0;
      this.mImageToWrapCropBoundsAnimDuration = 500L;
   }

   public void cropAndSaveImage(@NonNull Bitmap.CompressFormat compressFormat, int compressQuality, @Nullable BitmapCropCallback cropCallback) {
      this.cancelAllAnimations();
      this.setImageToWrapCropBounds(false);
      ImageState imageState = new ImageState(this.mCropRect, RectUtils.trapToRect(this.mCurrentImageCorners), this.getCurrentScale(), this.getCurrentAngle());
      CropParameters cropParameters = new CropParameters(this.mMaxResultImageSizeX, this.mMaxResultImageSizeY, compressFormat, compressQuality, this.getImageInputPath(), this.getImageOutputPath(), this.getExifInfo());
      (new BitmapCropTask(this.getViewBitmap(), imageState, cropParameters, cropCallback)).execute(new Void[0]);
   }

   public float getMaxScale() {
      return this.mMaxScale;
   }

   public float getMinScale() {
      return this.mMinScale;
   }

   public float getTargetAspectRatio() {
      return this.mTargetAspectRatio;
   }

   public void setCropRect(RectF cropRect) {
      this.mTargetAspectRatio = cropRect.width() / cropRect.height();
      this.mCropRect.set(cropRect.left - (float)this.getPaddingLeft(), cropRect.top - (float)this.getPaddingTop(), cropRect.right - (float)this.getPaddingRight(), cropRect.bottom - (float)this.getPaddingBottom());
      this.calculateImageScaleBounds();
      this.setImageToWrapCropBounds();
   }

   public void setTargetAspectRatio(float targetAspectRatio) {
      Log.i("VirtualApp", "setTargetAspectRatio " + targetAspectRatio);
      Drawable drawable = this.getDrawable();
      if (drawable == null) {
         this.mTargetAspectRatio = targetAspectRatio;
      } else {
         if (targetAspectRatio == 0.0F) {
            this.mTargetAspectRatio = (float)drawable.getIntrinsicWidth() / (float)drawable.getIntrinsicHeight();
         } else {
            this.mTargetAspectRatio = targetAspectRatio;
         }

         if (this.mCropBoundsChangeListener != null) {
            this.mCropBoundsChangeListener.onCropAspectRatioChanged(this.mTargetAspectRatio);
         }

      }
   }

   @Nullable
   public CropBoundsChangeListener getCropBoundsChangeListener() {
      return this.mCropBoundsChangeListener;
   }

   public void setCropBoundsChangeListener(@Nullable CropBoundsChangeListener cropBoundsChangeListener) {
      this.mCropBoundsChangeListener = cropBoundsChangeListener;
   }

   public void setMaxResultImageSizeX(@IntRange(from = 10L) int maxResultImageSizeX) {
      this.mMaxResultImageSizeX = maxResultImageSizeX;
   }

   public void setMaxResultImageSizeY(@IntRange(from = 10L) int maxResultImageSizeY) {
      this.mMaxResultImageSizeY = maxResultImageSizeY;
   }

   public void setImageToWrapCropBoundsAnimDuration(@IntRange(from = 100L) long imageToWrapCropBoundsAnimDuration) {
      if (imageToWrapCropBoundsAnimDuration > 0L) {
         this.mImageToWrapCropBoundsAnimDuration = imageToWrapCropBoundsAnimDuration;
      } else {
         throw new IllegalArgumentException("Animation duration cannot be negative value.");
      }
   }

   public void setMaxScaleMultiplier(float maxScaleMultiplier) {
      this.mMaxScaleMultiplier = maxScaleMultiplier;
   }

   public void zoomOutImage(float deltaScale) {
      this.zoomOutImage(deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
   }

   public void zoomOutImage(float scale, float centerX, float centerY) {
      if (scale >= this.getMinScale()) {
         this.postScale(scale / this.getCurrentScale(), centerX, centerY);
      }

   }

   public void zoomInImage(float deltaScale) {
      this.zoomInImage(deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
   }

   public void zoomInImage(float scale, float centerX, float centerY) {
      if (scale <= this.getMaxScale()) {
         this.postScale(scale / this.getCurrentScale(), centerX, centerY);
      }

   }

   public void postScale(float deltaScale, float px, float py) {
      if (deltaScale > 1.0F && this.getCurrentScale() * deltaScale <= this.getMaxScale()) {
         super.postScale(deltaScale, px, py);
      } else if (deltaScale < 1.0F && this.getCurrentScale() * deltaScale >= this.getMinScale()) {
         super.postScale(deltaScale, px, py);
      }

   }

   public void postRotate(float deltaAngle) {
      this.postRotate(deltaAngle, this.mCropRect.centerX(), this.mCropRect.centerY());
   }

   public void cancelAllAnimations() {
      this.removeCallbacks(this.mWrapCropBoundsRunnable);
      this.removeCallbacks(this.mZoomImageToPositionRunnable);
   }

   public void setImageToWrapCropBounds() {
      this.setImageToWrapCropBounds(true);
   }

   public void setImageToWrapCropBounds(boolean animate) {
      if (this.mBitmapLaidOut && !this.isImageWrapCropBounds()) {
         float currentX = this.mCurrentImageCenter[0];
         float currentY = this.mCurrentImageCenter[1];
         float currentScale = this.getCurrentScale();
         float deltaX = this.mCropRect.centerX() - currentX;
         float deltaY = this.mCropRect.centerY() - currentY;
         float deltaScale = 0.0F;
         this.mTempMatrix.reset();
         this.mTempMatrix.setTranslate(deltaX, deltaY);
         float[] tempCurrentImageCorners = Arrays.copyOf(this.mCurrentImageCorners, this.mCurrentImageCorners.length);
         this.mTempMatrix.mapPoints(tempCurrentImageCorners);
         boolean willImageWrapCropBoundsAfterTranslate = this.isImageWrapCropBounds(tempCurrentImageCorners);
         if (willImageWrapCropBoundsAfterTranslate) {
            float[] imageIndents = this.calculateImageIndents();
            deltaX = -(imageIndents[0] + imageIndents[2]);
            deltaY = -(imageIndents[1] + imageIndents[3]);
         } else {
            RectF tempCropRect = new RectF(this.mCropRect);
            this.mTempMatrix.reset();
            this.mTempMatrix.setRotate(this.getCurrentAngle());
            this.mTempMatrix.mapRect(tempCropRect);
            float[] currentImageSides = RectUtils.getRectSidesFromCorners(this.mCurrentImageCorners);
            deltaScale = Math.max(tempCropRect.width() / currentImageSides[0], tempCropRect.height() / currentImageSides[1]);
            deltaScale = deltaScale * currentScale - currentScale;
         }

         if (animate) {
            this.post(this.mWrapCropBoundsRunnable = new WrapCropBoundsRunnable(this, this.mImageToWrapCropBoundsAnimDuration, currentX, currentY, deltaX, deltaY, currentScale, deltaScale, willImageWrapCropBoundsAfterTranslate));
         } else {
            this.postTranslate(deltaX, deltaY);
            if (!willImageWrapCropBoundsAfterTranslate) {
               this.zoomInImage(currentScale + deltaScale, this.mCropRect.centerX(), this.mCropRect.centerY());
            }
         }
      }

   }

   private float[] calculateImageIndents() {
      this.mTempMatrix.reset();
      this.mTempMatrix.setRotate(-this.getCurrentAngle());
      float[] unrotatedImageCorners = Arrays.copyOf(this.mCurrentImageCorners, this.mCurrentImageCorners.length);
      float[] unrotatedCropBoundsCorners = RectUtils.getCornersFromRect(this.mCropRect);
      this.mTempMatrix.mapPoints(unrotatedImageCorners);
      this.mTempMatrix.mapPoints(unrotatedCropBoundsCorners);
      RectF unrotatedImageRect = RectUtils.trapToRect(unrotatedImageCorners);
      RectF unrotatedCropRect = RectUtils.trapToRect(unrotatedCropBoundsCorners);
      float deltaLeft = unrotatedImageRect.left - unrotatedCropRect.left;
      float deltaTop = unrotatedImageRect.top - unrotatedCropRect.top;
      float deltaRight = unrotatedImageRect.right - unrotatedCropRect.right;
      float deltaBottom = unrotatedImageRect.bottom - unrotatedCropRect.bottom;
      float[] indents = new float[]{deltaLeft > 0.0F ? deltaLeft : 0.0F, deltaTop > 0.0F ? deltaTop : 0.0F, deltaRight < 0.0F ? deltaRight : 0.0F, deltaBottom < 0.0F ? deltaBottom : 0.0F};
      this.mTempMatrix.reset();
      this.mTempMatrix.setRotate(this.getCurrentAngle());
      this.mTempMatrix.mapPoints(indents);
      return indents;
   }

   protected void onImageLaidOut() {
      super.onImageLaidOut();
      Drawable drawable = this.getDrawable();
      if (drawable != null) {
         float drawableWidth = (float)drawable.getIntrinsicWidth();
         float drawableHeight = (float)drawable.getIntrinsicHeight();
         if (this.mTargetAspectRatio == 0.0F) {
            this.mTargetAspectRatio = drawableWidth / drawableHeight;
         }

         int height = (int)((float)this.mThisWidth / this.mTargetAspectRatio);
         int width;
         if (height > this.mThisHeight) {
            width = (int)((float)this.mThisHeight * this.mTargetAspectRatio);
            int halfDiff = (this.mThisWidth - width) / 2;
            this.mCropRect.set((float)halfDiff, 0.0F, (float)(width + halfDiff), (float)this.mThisHeight);
         } else {
            width = (this.mThisHeight - height) / 2;
            this.mCropRect.set(0.0F, (float)width, (float)this.mThisWidth, (float)(height + width));
         }

         this.calculateImageScaleBounds(drawableWidth, drawableHeight);
         this.setupInitialImagePosition(drawableWidth, drawableHeight);
         if (this.mCropBoundsChangeListener != null) {
            this.mCropBoundsChangeListener.onCropAspectRatioChanged(this.mTargetAspectRatio);
         }

         if (this.mTransformImageListener != null) {
            this.mTransformImageListener.onScale(this.getCurrentScale());
            this.mTransformImageListener.onRotate(this.getCurrentAngle());
         }

      }
   }

   protected boolean isImageWrapCropBounds() {
      return this.isImageWrapCropBounds(this.mCurrentImageCorners);
   }

   protected boolean isImageWrapCropBounds(float[] imageCorners) {
      this.mTempMatrix.reset();
      this.mTempMatrix.setRotate(-this.getCurrentAngle());
      float[] unrotatedImageCorners = Arrays.copyOf(imageCorners, imageCorners.length);
      this.mTempMatrix.mapPoints(unrotatedImageCorners);
      float[] unrotatedCropBoundsCorners = RectUtils.getCornersFromRect(this.mCropRect);
      this.mTempMatrix.mapPoints(unrotatedCropBoundsCorners);
      return RectUtils.trapToRect(unrotatedImageCorners).contains(RectUtils.trapToRect(unrotatedCropBoundsCorners));
   }

   protected void zoomImageToPosition(float scale, float centerX, float centerY, long durationMs) {
      if (scale > this.getMaxScale()) {
         scale = this.getMaxScale();
      }

      float oldScale = this.getCurrentScale();
      float deltaScale = scale - oldScale;
      this.post(this.mZoomImageToPositionRunnable = new ZoomImageToPosition(this, durationMs, oldScale, deltaScale, centerX, centerY));
   }

   private void calculateImageScaleBounds() {
      Drawable drawable = this.getDrawable();
      if (drawable != null) {
         this.calculateImageScaleBounds((float)drawable.getIntrinsicWidth(), (float)drawable.getIntrinsicHeight());
      }
   }

   private void calculateImageScaleBounds(float drawableWidth, float drawableHeight) {
      float widthScale = Math.min(this.mCropRect.width() / drawableWidth, this.mCropRect.width() / drawableHeight);
      float heightScale = Math.min(this.mCropRect.height() / drawableHeight, this.mCropRect.height() / drawableWidth);
      this.mMinScale = Math.min(widthScale, heightScale);
      this.mMaxScale = this.mMinScale * this.mMaxScaleMultiplier;
   }

   private void setupInitialImagePosition(float drawableWidth, float drawableHeight) {
      float cropRectWidth = this.mCropRect.width();
      float cropRectHeight = this.mCropRect.height();
      float widthScale = this.mCropRect.width() / drawableWidth;
      float heightScale = this.mCropRect.height() / drawableHeight;
      float initialMinScale = Math.max(widthScale, heightScale);
      float tw = (cropRectWidth - drawableWidth * initialMinScale) / 2.0F + this.mCropRect.left;
      float th = (cropRectHeight - drawableHeight * initialMinScale) / 2.0F + this.mCropRect.top;
      this.mCurrentImageMatrix.reset();
      this.mCurrentImageMatrix.postScale(initialMinScale, initialMinScale);
      this.mCurrentImageMatrix.postTranslate(tw, th);
      this.setImageMatrix(this.mCurrentImageMatrix);
   }

   protected void processStyledAttributes(@NonNull TypedArray a) {
      float targetAspectRatioX = Math.abs(a.getFloat(styleable.ucrop_UCropView_ucrop_aspect_ratio_x, 0.0F));
      float targetAspectRatioY = Math.abs(a.getFloat(styleable.ucrop_UCropView_ucrop_aspect_ratio_y, 0.0F));
      if (targetAspectRatioX != 0.0F && targetAspectRatioY != 0.0F) {
         this.mTargetAspectRatio = targetAspectRatioX / targetAspectRatioY;
      } else {
         this.mTargetAspectRatio = 0.0F;
      }

   }

   private static class ZoomImageToPosition implements Runnable {
      private final WeakReference<CropImageView> mCropImageView;
      private final long mDurationMs;
      private final long mStartTime;
      private final float mOldScale;
      private final float mDeltaScale;
      private final float mDestX;
      private final float mDestY;

      public ZoomImageToPosition(CropImageView cropImageView, long durationMs, float oldScale, float deltaScale, float destX, float destY) {
         this.mCropImageView = new WeakReference(cropImageView);
         this.mStartTime = System.currentTimeMillis();
         this.mDurationMs = durationMs;
         this.mOldScale = oldScale;
         this.mDeltaScale = deltaScale;
         this.mDestX = destX;
         this.mDestY = destY;
      }

      public void run() {
         CropImageView cropImageView = (CropImageView)this.mCropImageView.get();
         if (cropImageView != null) {
            long now = System.currentTimeMillis();
            float currentMs = (float)Math.min(this.mDurationMs, now - this.mStartTime);
            float newScale = CubicEasing.easeInOut(currentMs, 0.0F, this.mDeltaScale, (float)this.mDurationMs);
            if (currentMs < (float)this.mDurationMs) {
               cropImageView.zoomInImage(this.mOldScale + newScale, this.mDestX, this.mDestY);
               cropImageView.post(this);
            } else {
               cropImageView.setImageToWrapCropBounds();
            }

         }
      }
   }

   private static class WrapCropBoundsRunnable implements Runnable {
      private final WeakReference<CropImageView> mCropImageView;
      private final long mDurationMs;
      private final long mStartTime;
      private final float mOldX;
      private final float mOldY;
      private final float mCenterDiffX;
      private final float mCenterDiffY;
      private final float mOldScale;
      private final float mDeltaScale;
      private final boolean mWillBeImageInBoundsAfterTranslate;

      public WrapCropBoundsRunnable(CropImageView cropImageView, long durationMs, float oldX, float oldY, float centerDiffX, float centerDiffY, float oldScale, float deltaScale, boolean willBeImageInBoundsAfterTranslate) {
         this.mCropImageView = new WeakReference(cropImageView);
         this.mDurationMs = durationMs;
         this.mStartTime = System.currentTimeMillis();
         this.mOldX = oldX;
         this.mOldY = oldY;
         this.mCenterDiffX = centerDiffX;
         this.mCenterDiffY = centerDiffY;
         this.mOldScale = oldScale;
         this.mDeltaScale = deltaScale;
         this.mWillBeImageInBoundsAfterTranslate = willBeImageInBoundsAfterTranslate;
      }

      public void run() {
         CropImageView cropImageView = (CropImageView)this.mCropImageView.get();
         if (cropImageView != null) {
            long now = System.currentTimeMillis();
            float currentMs = (float)Math.min(this.mDurationMs, now - this.mStartTime);
            float newX = CubicEasing.easeOut(currentMs, 0.0F, this.mCenterDiffX, (float)this.mDurationMs);
            float newY = CubicEasing.easeOut(currentMs, 0.0F, this.mCenterDiffY, (float)this.mDurationMs);
            float newScale = CubicEasing.easeInOut(currentMs, 0.0F, this.mDeltaScale, (float)this.mDurationMs);
            if (currentMs < (float)this.mDurationMs) {
               cropImageView.postTranslate(newX - (cropImageView.mCurrentImageCenter[0] - this.mOldX), newY - (cropImageView.mCurrentImageCenter[1] - this.mOldY));
               if (!this.mWillBeImageInBoundsAfterTranslate) {
                  cropImageView.zoomInImage(this.mOldScale + newScale, cropImageView.mCropRect.centerX(), cropImageView.mCropRect.centerY());
               }

               if (!cropImageView.isImageWrapCropBounds()) {
                  cropImageView.post(this);
               }
            }

         }
      }
   }
}
