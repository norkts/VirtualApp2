package com.carlos.common.imagepicker.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.carlos.common.imagepicker.callback.BitmapLoadCallback;
import com.carlos.common.imagepicker.entity.ExifInfo;
import com.carlos.common.imagepicker.util.BitmapLoadUtils;
import com.carlos.common.imagepicker.util.FileUtils;
import com.carlos.libcommon.StringFog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapLoadTask extends AsyncTask<Void, Void, BitmapWorkerResult> {
   private static final String TAG = StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj4YLGoVJAJuJB4qKS0MKH0zQQNqJ1RF"));
   private final Context mContext;
   private Uri mInputUri;
   private Uri mOutputUri;
   private final int mRequiredWidth;
   private final int mRequiredHeight;
   private final BitmapLoadCallback mBitmapLoadCallback;

   public BitmapLoadTask(@NonNull Context context, @NonNull Uri inputUri, @Nullable Uri outputUri, int requiredWidth, int requiredHeight, BitmapLoadCallback loadCallback) {
      this.mContext = context;
      this.mInputUri = inputUri;
      this.mOutputUri = outputUri;
      this.mRequiredWidth = requiredWidth;
      this.mRequiredHeight = requiredHeight;
      this.mBitmapLoadCallback = loadCallback;
   }

   @NonNull
   protected BitmapWorkerResult doInBackground(Void... params) {
      if (this.mInputUri == null) {
         return new BitmapWorkerResult(new NullPointerException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcKGwaMyhuASwzPxcqOW8VMCVvVjwpLl86KmYKTTc="))));
      } else {
         try {
            this.processInputUri();
         } catch (IOException | NullPointerException var14) {
            Exception e = var14;
            return new BitmapWorkerResult(e);
         }

         ParcelFileDescriptor parcelFileDescriptor;
         try {
            parcelFileDescriptor = this.mContext.getContentResolver().openFileDescriptor(this.mInputUri, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Kj5SVg==")));
         } catch (FileNotFoundException var13) {
            FileNotFoundException e = var13;
            return new BitmapWorkerResult(e);
         }

         if (parcelFileDescriptor == null) {
            return new BitmapWorkerResult(new NullPointerException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ihg+KmszNCRqNAYoKAU2PWoFAgRqDjw/LD0LJGYgPDZ5Hh4+KT4hJGszGiZ7ASAwID02JXkbFiRsID83IC5SVg==")) + this.mInputUri + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg=="))));
         } else {
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, (Rect)null, options);
            if (options.outWidth != -1 && options.outHeight != -1) {
               options.inSampleSize = BitmapLoadUtils.calculateInSampleSize(options, this.mRequiredWidth, this.mRequiredHeight);
               options.inJustDecodeBounds = false;
               Bitmap decodeSampledBitmap = null;
               boolean decodeAttemptSuccess = false;

               while(!decodeAttemptSuccess) {
                  try {
                     decodeSampledBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, (Rect)null, options);
                     decodeAttemptSuccess = true;
                  } catch (OutOfMemoryError var12) {
                     OutOfMemoryError error = var12;
                     Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgAXGohFjd9JA49Iz1fLW8VATJ4HwoaKggIO2EYICRpJzAeLBc1KmsVNDVqNyw0LD0AO24ILDFqAR4bLAc6LGowETJLEVRF")), error);
                     options.inSampleSize *= 2;
                  }
               }

               if (decodeSampledBitmap == null) {
                  return new BitmapWorkerResult(new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj4YLGoVJAJLHig1LAdbPn4zMCVvVjwpLl86IGIKNCpuHgotOD4ECGUjHTRsAVk0DRY2OW8JHTZgAVRF")) + this.mInputUri + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg=="))));
               } else {
                  if (VERSION.SDK_INT >= 16) {
                     BitmapLoadUtils.close(parcelFileDescriptor);
                  }

                  int exifOrientation = BitmapLoadUtils.getExifOrientation(this.mContext, this.mInputUri);
                  int exifDegrees = BitmapLoadUtils.exifToDegrees(exifOrientation);
                  int exifTranslation = BitmapLoadUtils.exifToTranslation(exifOrientation);
                  ExifInfo exifInfo = new ExifInfo(exifOrientation, exifDegrees, exifTranslation);
                  Matrix matrix = new Matrix();
                  if (exifDegrees != 0) {
                     matrix.preRotate((float)exifDegrees);
                  }

                  if (exifTranslation != 1) {
                     matrix.postScale((float)exifTranslation, 1.0F);
                  }

                  return !matrix.isIdentity() ? new BitmapWorkerResult(BitmapLoadUtils.transformBitmap(decodeSampledBitmap, matrix), exifInfo) : new BitmapWorkerResult(decodeSampledBitmap, exifInfo);
               }
            } else {
               return new BitmapWorkerResult(new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj4AI2ojMANLHjw1IzkmOGwgBiNoDj8rLT4ACWAaESNsNFk9OD5fJ3gaFj9sDgowJAg6LG5TPDBqESg8PhcqCmhSIFBhNBkiPxYEVg==")) + this.mInputUri + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JwhSVg=="))));
            }
         }
      }
   }

   private void processInputUri() throws NullPointerException, IOException {
      String inputUriScheme = this.mInputUri.getScheme();
      Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IQcMCXsKLCljHjA3KAQHOg==")) + inputUriScheme);
      if (!StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8FSFo=")).equals(inputUriScheme) && !StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8KLFo=")).equals(inputUriScheme)) {
         if (StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmEVRF")).equals(inputUriScheme)) {
            String path = this.getFilePath();
            if (!TextUtils.isEmpty(path) && (new File(path)).exists()) {
               this.mInputUri = Uri.fromFile(new File(path));
            } else {
               try {
                  this.copyFile(this.mInputUri, this.mOutputUri);
               } catch (IOException | NullPointerException var4) {
                  Exception e = var4;
                  Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ji4AKGkVAiZiICQ+LwccCGkjBlo=")), e);
                  throw e;
               }
            }
         } else if (!StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LT4YDmgVSFo=")).equals(inputUriScheme)) {
            Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLmsVHi9iVyRKIz0bOmoFAiBrAQ4gPQhSVg==")) + inputUriScheme);
            throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcLmsVHi9iVyRKIz0bOmoFAiBrAQ4g")) + inputUriScheme);
         }
      } else {
         try {
            this.downloadFile(this.mInputUri, this.mOutputUri);
         } catch (IOException | NullPointerException var5) {
            Exception e = var5;
            Log.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JRgALWojHiV9DgozKj05OmkVQS9lESgv")), e);
            throw e;
         }
      }

   }

   private String getFilePath() {
      return ContextCompat.checkSelfPermission(this.mContext, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggcPG8jGi9iV1ksKAguD2wgAgNqAQYbPCsMGWUIFl9mDwYTJytfDGALHhNnMiwQLzsmAGYFSFo="))) == 0 ? FileUtils.getPath(this.mContext, this.mInputUri) : null;
   }

   private void copyFile(@NonNull Uri inputUri, @Nullable Uri outputUri) throws NullPointerException, IOException {
      Log.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4AKGkbOC9gHjBF")));
      if (outputUri == null) {
         throw new NullPointerException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oy0uLG8KNAZLHzAqKQMmMWoJTSZvAQIdPQQHJH0gPClsNFk9OD4YKWwaDTRlEQ4oJC02Vg==")));
      } else {
         InputStream inputStream = null;
         OutputStream outputStream = null;

         try {
            inputStream = this.mContext.getContentResolver().openInputStream(inputUri);
            outputStream = new FileOutputStream(new File(outputUri.getPath()));
            if (inputStream == null) {
               throw new NullPointerException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JAgcKGwaMF5mESw/LwdWOmkVNAR4ESAaKi4uKksaGillEQo9ODsACGoJIAVvMzw5IAdXOw==")));
            }

            byte[] buffer = new byte[1024];

            int length;
            while((length = inputStream.read(buffer)) > 0) {
               ((OutputStream)outputStream).write(buffer, 0, length);
            }
         } finally {
            BitmapLoadUtils.close(outputStream);
            BitmapLoadUtils.close(inputStream);
            this.mInputUri = this.mOutputUri;
         }

      }
   }

   private void downloadFile(@NonNull Uri inputUri, @Nullable Uri outputUri) throws NullPointerException, IOException {
   }

   protected void onPostExecute(@NonNull BitmapWorkerResult result) {
      if (result.mBitmapWorkerException == null) {
         this.mBitmapLoadCallback.onBitmapLoaded(result.mBitmapResult, result.mExifInfo, this.mInputUri.getPath(), this.mOutputUri == null ? null : this.mOutputUri.getPath());
      } else {
         this.mBitmapLoadCallback.onFailure(result.mBitmapWorkerException);
      }

   }

   public static class BitmapWorkerResult {
      Bitmap mBitmapResult;
      ExifInfo mExifInfo;
      Exception mBitmapWorkerException;

      public BitmapWorkerResult(@NonNull Bitmap bitmapResult, @NonNull ExifInfo exifInfo) {
         this.mBitmapResult = bitmapResult;
         this.mExifInfo = exifInfo;
      }

      public BitmapWorkerResult(@NonNull Exception bitmapWorkerException) {
         this.mBitmapWorkerException = bitmapWorkerException;
      }
   }
}
