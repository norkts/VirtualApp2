package com.carlos.common.imagepicker.callback;

import android.net.Uri;
import androidx.annotation.NonNull;

public interface BitmapCropCallback {
   void onBitmapCropped(@NonNull Uri var1, int var2, int var3, int var4, int var5);

   void onCropFailure(@NonNull Throwable var1);
}
