package com.carlos.common.widget.toast;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build.VERSION;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.kook.librelease.R.drawable;

final class ToastyUtils {
   private ToastyUtils() {
   }

   static Drawable tintIcon(@NonNull Drawable drawable, @ColorInt int tintColor) {
      drawable.setColorFilter(tintColor, Mode.SRC_IN);
      return drawable;
   }

   static Drawable tint9PatchDrawableFrame(@NonNull Context context, @ColorInt int tintColor) {
      NinePatchDrawable toastDrawable = (NinePatchDrawable)getDrawable(context, drawable.toast_frame);
      return tintIcon(toastDrawable, tintColor);
   }

   static void setBackground(@NonNull View view, Drawable drawable) {
      if (VERSION.SDK_INT >= 16) {
         view.setBackground(drawable);
      } else {
         view.setBackgroundDrawable(drawable);
      }

   }

   static Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
      return ContextCompat.getDrawable(context, id);
   }

   static int getColor(@NonNull Context context, @ColorRes int color) {
      return ContextCompat.getColor(context, color);
   }
}
