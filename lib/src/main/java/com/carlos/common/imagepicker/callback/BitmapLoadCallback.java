package com.carlos.common.imagepicker.callback;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.carlos.common.imagepicker.entity.ExifInfo;

public interface BitmapLoadCallback {
   void onBitmapLoaded(@NonNull Bitmap var1, @NonNull ExifInfo var2, @NonNull String var3, @Nullable String var4);

   void onFailure(@NonNull Exception var1);
}
