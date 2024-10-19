package com.carlos.common.imagepicker.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.callback.BitmapCropCallback;
import com.carlos.common.imagepicker.entity.CropParameters;
import com.carlos.common.imagepicker.entity.ExifInfo;
import com.carlos.common.imagepicker.entity.ImageState;
import com.carlos.common.imagepicker.util.FileUtils;
import com.carlos.common.imagepicker.util.ImageHeaderParser;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.io.IOException;

public class BitmapCropTask extends AsyncTask<Void, Void, Throwable> {
   private static final String TAG = "BitmapCropTask";
   private Bitmap mViewBitmap;
   private final RectF mCropRect;
   private final RectF mCurrentImageRect;
   private float mCurrentScale;
   private float mCurrentAngle;
   private final int mMaxResultImageSizeX;
   private final int mMaxResultImageSizeY;
   private final Bitmap.CompressFormat mCompressFormat;
   private final int mCompressQuality;
   private final String mImageInputPath;
   private final String mImageOutputPath;
   private final ExifInfo mExifInfo;
   private final BitmapCropCallback mCropCallback;
   private int mCroppedImageWidth;
   private int mCroppedImageHeight;
   private int cropOffsetX;
   private int cropOffsetY;

   public BitmapCropTask(@Nullable Bitmap viewBitmap, @NonNull ImageState imageState, @NonNull CropParameters cropParameters, @Nullable BitmapCropCallback cropCallback) {
      this.mViewBitmap = viewBitmap;
      this.mCropRect = imageState.getCropRect();
      this.mCurrentImageRect = imageState.getCurrentImageRect();
      this.mCurrentScale = imageState.getCurrentScale();
      this.mCurrentAngle = imageState.getCurrentAngle();
      this.mMaxResultImageSizeX = cropParameters.getMaxResultImageSizeX();
      this.mMaxResultImageSizeY = cropParameters.getMaxResultImageSizeY();
      this.mCompressFormat = cropParameters.getCompressFormat();
      this.mCompressQuality = cropParameters.getCompressQuality();
      this.mImageInputPath = cropParameters.getImageInputPath();
      this.mImageOutputPath = cropParameters.getImageOutputPath();
      this.mExifInfo = cropParameters.getExifInfo();
      this.mCropCallback = cropCallback;
   }

   @Nullable
   protected Throwable doInBackground(Void... params) {
      if (this.mViewBitmap == null) {
         return new NullPointerException("ViewBitmap is null");
      } else if (this.mViewBitmap.isRecycled()) {
         return new NullPointerException("ViewBitmap is recycled");
      } else if (this.mCurrentImageRect.isEmpty()) {
         return new NullPointerException("CurrentImageRect is empty");
      } else {
         float resizeScale = this.resize();
         Log.i("VirtualApp", "BitmapCropTask doInBackground ");

         try {
            this.crop(resizeScale);
            this.mViewBitmap = null;
            return null;
         } catch (Throwable var4) {
            Throwable throwable = var4;
            Log.e("VirtualApp", throwable.toString());
            return throwable;
         }
      }
   }

   private float resize() {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(this.mImageInputPath, options);
      boolean swapSides = this.mExifInfo.getExifDegrees() == 90 || this.mExifInfo.getExifDegrees() == 270;
      float scaleX = (float)(swapSides ? options.outHeight : options.outWidth) / (float)this.mViewBitmap.getWidth();
      float scaleY = (float)(swapSides ? options.outWidth : options.outHeight) / (float)this.mViewBitmap.getHeight();
      float resizeScale = Math.min(scaleX, scaleY);
      this.mCurrentScale /= resizeScale;
      resizeScale = 1.0F;
      if (this.mMaxResultImageSizeX > 0 && this.mMaxResultImageSizeY > 0) {
         float cropWidth = this.mCropRect.width() / this.mCurrentScale;
         float cropHeight = this.mCropRect.height() / this.mCurrentScale;
         if (cropWidth > (float)this.mMaxResultImageSizeX || cropHeight > (float)this.mMaxResultImageSizeY) {
            scaleX = (float)this.mMaxResultImageSizeX / cropWidth;
            scaleY = (float)this.mMaxResultImageSizeY / cropHeight;
            resizeScale = Math.min(scaleX, scaleY);
            this.mCurrentScale /= resizeScale;
         }
      }

      return resizeScale;
   }

   private boolean crop(float resizeScale) throws IOException {
      ExifInterface originalExif = new ExifInterface(this.mImageInputPath);
      this.cropOffsetX = Math.round((this.mCropRect.left - this.mCurrentImageRect.left) / this.mCurrentScale);
      this.cropOffsetY = Math.round((this.mCropRect.top - this.mCurrentImageRect.top) / this.mCurrentScale);
      this.mCroppedImageWidth = Math.round(this.mCropRect.width() / this.mCurrentScale);
      this.mCroppedImageHeight = Math.round(this.mCropRect.height() / this.mCurrentScale);
      boolean shouldCrop = this.shouldCrop(this.mCroppedImageWidth, this.mCroppedImageHeight);
      Log.i(TAG, "Should crop: " + shouldCrop);
      if (shouldCrop) {
         boolean cropped = cropCImg(this.mImageInputPath, this.mImageOutputPath, this.cropOffsetX, this.cropOffsetY, this.mCroppedImageWidth, this.mCroppedImageHeight, this.mCurrentAngle, resizeScale, this.mCompressFormat.ordinal(), this.mCompressQuality, this.mExifInfo.getExifDegrees(), this.mExifInfo.getExifTranslation());
         if (cropped && this.mCompressFormat.equals(CompressFormat.JPEG)) {
            ImageHeaderParser.copyExif(originalExif, this.mCroppedImageWidth, this.mCroppedImageHeight, this.mImageOutputPath);
         }

         return cropped;
      } else {
         FileUtils.copyFile(this.mImageInputPath, this.mImageOutputPath);
         return false;
      }
   }

   private boolean shouldCrop(int width, int height) {
      int pixelError = 1;
      pixelError += Math.round((float)Math.max(width, height) / 1000.0F);
      return this.mMaxResultImageSizeX > 0 && this.mMaxResultImageSizeY > 0 || Math.abs(this.mCropRect.left - this.mCurrentImageRect.left) > (float)pixelError || Math.abs(this.mCropRect.top - this.mCurrentImageRect.top) > (float)pixelError || Math.abs(this.mCropRect.bottom - this.mCurrentImageRect.bottom) > (float)pixelError || Math.abs(this.mCropRect.right - this.mCurrentImageRect.right) > (float)pixelError || this.mCurrentAngle != 0.0F;
   }

   public static native boolean cropCImg(String var0, String var1, int var2, int var3, int var4, int var5, float var6, float var7, int var8, int var9, int var10, int var11) throws IOException, OutOfMemoryError;

   protected void onPostExecute(@Nullable Throwable t) {
      Log.i("VirtualApp", "BitmapCropTask onPostExecute " + (this.mCropCallback == null) + "    Throwable:" + (t == null));
      if (this.mCropCallback != null) {
         if (t == null) {
            Uri uri = Uri.fromFile(new File(this.mImageOutputPath));
            this.mCropCallback.onBitmapCropped(uri, this.cropOffsetX, this.cropOffsetY, this.mCroppedImageWidth, this.mCroppedImageHeight);
         } else {
            this.mCropCallback.onCropFailure(t);
         }
      }

   }

   static {
      System.loadLibrary("common");
   }
}
