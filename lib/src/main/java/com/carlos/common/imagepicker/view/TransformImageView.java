package com.carlos.common.imagepicker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.callback.BitmapLoadCallback;
import com.carlos.common.imagepicker.entity.ExifInfo;
import com.carlos.common.imagepicker.util.BitmapLoadUtils;
import com.carlos.common.imagepicker.util.FastBitmapDrawable;
import com.carlos.common.imagepicker.util.RectUtils;
import com.carlos.libcommon.StringFog;

public class TransformImageView extends ImageView {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRcMP2ogLC5gJyw3IQdXOWkFGlNqASg+"));
   private static final int RECT_CORNER_POINTS_COORDS = 8;
   private static final int RECT_CENTER_POINT_COORDS = 2;
   private static final int MATRIX_VALUES_COUNT = 9;
   protected final float[] mCurrentImageCorners;
   protected final float[] mCurrentImageCenter;
   private final float[] mMatrixValues;
   protected Matrix mCurrentImageMatrix;
   protected int mThisWidth;
   protected int mThisHeight;
   protected TransformImageListener mTransformImageListener;
   private float[] mInitialImageCorners;
   private float[] mInitialImageCenter;
   protected boolean mBitmapDecoded;
   protected boolean mBitmapLaidOut;
   private int mMaxBitmapSize;
   private String mImageInputPath;
   private String mImageOutputPath;
   private ExifInfo mExifInfo;

   public TransformImageView(Context context) {
      this(context, (AttributeSet)null);
   }

   public TransformImageView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public TransformImageView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      this.mCurrentImageCorners = new float[8];
      this.mCurrentImageCenter = new float[2];
      this.mMatrixValues = new float[9];
      this.mCurrentImageMatrix = new Matrix();
      this.mBitmapDecoded = false;
      this.mBitmapLaidOut = false;
      this.mMaxBitmapSize = 0;
      this.init();
   }

   public void setTransformImageListener(TransformImageListener transformImageListener) {
      this.mTransformImageListener = transformImageListener;
   }

   public void setScaleType(ImageView.ScaleType scaleType) {
      if (scaleType == ScaleType.MATRIX) {
         super.setScaleType(scaleType);
      } else {
         Log.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLmsVHi9iVyQPLy0iCGkmBj9sESsbPQYAKmAVBSN9JCwqKT4AXW4KID96JQ4OKBYqGmNTPDNuJy83Lj4tOGwaLCtiEVRF")));
      }

   }

   public void setMaxBitmapSize(int maxBitmapSize) {
      this.mMaxBitmapSize = maxBitmapSize;
   }

   public int getMaxBitmapSize() {
      if (this.mMaxBitmapSize <= 0) {
         this.mMaxBitmapSize = BitmapLoadUtils.calculateMaxBitmapSize(this.getContext());
      }

      return this.mMaxBitmapSize;
   }

   public void setImageBitmap(Bitmap bitmap) {
      this.setImageDrawable(new FastBitmapDrawable(bitmap));
   }

   public String getImageInputPath() {
      return this.mImageInputPath;
   }

   public String getImageOutputPath() {
      return this.mImageOutputPath;
   }

   public ExifInfo getExifInfo() {
      return this.mExifInfo;
   }

   public void setImageUri(@NonNull Uri imageUri, @Nullable Uri outputUri) throws Exception {
      int maxBitmapSize = this.getMaxBitmapSize();
      BitmapLoadUtils.decodeBitmapInBackground(this.getContext(), imageUri, outputUri, maxBitmapSize, maxBitmapSize, new BitmapLoadCallback() {
         public void onBitmapLoaded(@NonNull Bitmap bitmap, @NonNull ExifInfo exifInfo, @NonNull String imageInputPath, @Nullable String imageOutputPath) {
            TransformImageView.this.mImageInputPath = imageInputPath;
            TransformImageView.this.mImageOutputPath = imageOutputPath;
            TransformImageView.this.mExifInfo = exifInfo;
            TransformImageView.this.mBitmapDecoded = true;
            TransformImageView.this.setImageBitmap(bitmap);
         }

         public void onFailure(@NonNull Exception bitmapWorkerException) {
            Log.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRcMP2ogLC5gJyw3IQdXOWkFGlNqASg+")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iy4cW2sVAiRmASw/PTkmL2kgBgllATguLhUuCGMFSFo=")), bitmapWorkerException);
            if (TransformImageView.this.mTransformImageListener != null) {
               TransformImageView.this.mTransformImageListener.onLoadFailure(bitmapWorkerException);
            }

         }
      });
   }

   public float getCurrentScale() {
      return this.getMatrixScale(this.mCurrentImageMatrix);
   }

   public float getMatrixScale(@NonNull Matrix matrix) {
      return (float)Math.sqrt(Math.pow((double)this.getMatrixValue(matrix, 0), 2.0) + Math.pow((double)this.getMatrixValue(matrix, 3), 2.0));
   }

   public float getCurrentAngle() {
      return this.getMatrixAngle(this.mCurrentImageMatrix);
   }

   public float getMatrixAngle(@NonNull Matrix matrix) {
      return (float)(-(Math.atan2((double)this.getMatrixValue(matrix, 1), (double)this.getMatrixValue(matrix, 0)) * 57.29577951308232));
   }

   public void setImageMatrix(Matrix matrix) {
      super.setImageMatrix(matrix);
      this.mCurrentImageMatrix.set(matrix);
      this.updateCurrentImagePoints();
   }

   @Nullable
   public Bitmap getViewBitmap() {
      return this.getDrawable() != null && this.getDrawable() instanceof FastBitmapDrawable ? ((FastBitmapDrawable)this.getDrawable()).getBitmap() : null;
   }

   public void postTranslate(float deltaX, float deltaY) {
      if (deltaX != 0.0F || deltaY != 0.0F) {
         this.mCurrentImageMatrix.postTranslate(deltaX, deltaY);
         this.setImageMatrix(this.mCurrentImageMatrix);
      }

   }

   public void postScale(float deltaScale, float px, float py) {
      if (deltaScale != 0.0F) {
         this.mCurrentImageMatrix.postScale(deltaScale, deltaScale, px, py);
         this.setImageMatrix(this.mCurrentImageMatrix);
         if (this.mTransformImageListener != null) {
            this.mTransformImageListener.onScale(this.getMatrixScale(this.mCurrentImageMatrix));
         }
      }

   }

   public void postRotate(float deltaAngle, float px, float py) {
      if (deltaAngle != 0.0F) {
         this.mCurrentImageMatrix.postRotate(deltaAngle, px, py);
         this.setImageMatrix(this.mCurrentImageMatrix);
         if (this.mTransformImageListener != null) {
            this.mTransformImageListener.onRotate(this.getMatrixAngle(this.mCurrentImageMatrix));
         }
      }

   }

   protected void init() {
      this.setScaleType(ScaleType.MATRIX);
   }

   protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
      super.onLayout(changed, left, top, right, bottom);
      if (changed || this.mBitmapDecoded && !this.mBitmapLaidOut) {
         left = this.getPaddingLeft();
         top = this.getPaddingTop();
         right = this.getWidth() - this.getPaddingRight();
         bottom = this.getHeight() - this.getPaddingBottom();
         this.mThisWidth = right - left;
         this.mThisHeight = bottom - top;
         this.onImageLaidOut();
      }

   }

   protected void onImageLaidOut() {
      Drawable drawable = this.getDrawable();
      if (drawable != null) {
         float w = (float)drawable.getIntrinsicWidth();
         float h = (float)drawable.getIntrinsicHeight();
         Log.d(TAG, String.format(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgIP2gzNyhhJAYiKAQHOmAJGix+MygvIBhSVg==")), (int)w, (int)h));
         RectF initialImageRect = new RectF(0.0F, 0.0F, w, h);
         this.mInitialImageCorners = RectUtils.getCornersFromRect(initialImageRect);
         this.mInitialImageCenter = RectUtils.getCenterFromRect(initialImageRect);
         this.mBitmapLaidOut = true;
         if (this.mTransformImageListener != null) {
            this.mTransformImageListener.onLoadComplete();
         }

      }
   }

   protected float getMatrixValue(@NonNull Matrix matrix, @IntRange(from = 0L,to = 9L) int valueIndex) {
      matrix.getValues(this.mMatrixValues);
      return this.mMatrixValues[valueIndex];
   }

   protected void printMatrix(@NonNull String logPrefix, @NonNull Matrix matrix) {
      float x = this.getMatrixValue(matrix, 2);
      float y = this.getMatrixValue(matrix, 5);
      float rScale = this.getMatrixScale(matrix);
      float rAngle = this.getMatrixAngle(matrix);
      Log.d(TAG, logPrefix + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ODo6DWsaMARjAR0iPxgDOmg0Iyg=")) + x + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186J3knIFo=")) + y + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186KWszJCRiDQU8")) + rScale + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("M186P2ojPCRiDQU8")) + rAngle + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("PhcIVg==")));
   }

   private void updateCurrentImagePoints() {
      this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCorners, this.mInitialImageCorners);
      this.mCurrentImageMatrix.mapPoints(this.mCurrentImageCenter, this.mInitialImageCenter);
   }

   public interface TransformImageListener {
      void onLoadComplete();

      void onLoadFailure(@NonNull Exception var1);

      void onRotate(float var1);

      void onScale(float var1);
   }
}
