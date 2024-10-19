package com.carlos.common.widget.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.carlos.libcommon.StringFog;
import com.kook.librelease.R.color;
import com.kook.librelease.R.drawable;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;

@SuppressLint({"InflateParams"})
public class Toasty {
   private static final Typeface LOADED_TOAST_TYPEFACE = Typeface.create(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4+CG83EgNiASwzKDlXP28FMCxrARo6LhgqVg==")), 0);
   private static Typeface currentTypeface;
   private static int textSize;
   private static boolean tintIcon;
   private static boolean allowQueue;
   private static Toast lastToast;
   public static final int LENGTH_SHORT = 0;
   public static final int LENGTH_LONG = 1;

   private Toasty() {
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @StringRes int message) {
      return normal(context, context.getString(message), 0, (Drawable)null, false);
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @NonNull CharSequence message) {
      return normal(context, message, 0, (Drawable)null, false);
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @StringRes int message, Drawable icon) {
      return normal(context, context.getString(message), 0, icon, true);
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @NonNull CharSequence message, Drawable icon) {
      return normal(context, message, 0, icon, true);
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @StringRes int message, int duration) {
      return normal(context, context.getString(message), duration, (Drawable)null, false);
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @NonNull CharSequence message, int duration) {
      return normal(context, message, duration, (Drawable)null, false);
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @StringRes int message, int duration, Drawable icon) {
      return normal(context, context.getString(message), duration, icon, true);
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @NonNull CharSequence message, int duration, Drawable icon) {
      return normal(context, message, duration, icon, true);
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @StringRes int message, int duration, Drawable icon, boolean withIcon) {
      return custom(context, context.getString(message), icon, ToastyUtils.getColor(context, color.normalColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast normal(@NonNull Context context, @NonNull CharSequence message, int duration, Drawable icon, boolean withIcon) {
      return custom(context, message, icon, ToastyUtils.getColor(context, color.normalColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast warning(@NonNull Context context, @StringRes int message) {
      return warning(context, context.getString(message), 0, true);
   }

   @CheckResult
   public static Toast warning(@NonNull Context context, @NonNull CharSequence message) {
      return warning(context, message, 0, true);
   }

   @CheckResult
   public static Toast warning(@NonNull Context context, @StringRes int message, int duration) {
      return warning(context, context.getString(message), duration, true);
   }

   @CheckResult
   public static Toast warning(@NonNull Context context, @NonNull CharSequence message, int duration) {
      return warning(context, message, duration, true);
   }

   @CheckResult
   public static Toast warning(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
      return custom(context, context.getString(message), ToastyUtils.getDrawable(context, drawable.ic_error_outline_white_24dp), ToastyUtils.getColor(context, color.warningColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast warning(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
      return custom(context, message, ToastyUtils.getDrawable(context, drawable.ic_error_outline_white_24dp), ToastyUtils.getColor(context, color.warningColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast info(@NonNull Context context, @StringRes int message) {
      return info(context, context.getString(message), 0, true);
   }

   @CheckResult
   public static Toast info(@NonNull Context context, @NonNull CharSequence message) {
      return info(context, message, 0, true);
   }

   @CheckResult
   public static Toast info(@NonNull Context context, @StringRes int message, int duration) {
      return info(context, context.getString(message), duration, true);
   }

   @CheckResult
   public static Toast info(@NonNull Context context, @NonNull CharSequence message, int duration) {
      return info(context, message, duration, true);
   }

   @CheckResult
   public static Toast info(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
      return custom(context, context.getString(message), ToastyUtils.getDrawable(context, drawable.ic_info_outline_white_24dp), ToastyUtils.getColor(context, color.infoColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast info(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
      return custom(context, message, ToastyUtils.getDrawable(context, drawable.ic_info_outline_white_24dp), ToastyUtils.getColor(context, color.infoColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast success(@NonNull Context context, @StringRes int message) {
      return success(context, context.getString(message), 0, true);
   }

   @CheckResult
   public static Toast success(@NonNull Context context, @NonNull CharSequence message) {
      return success(context, message, 0, true);
   }

   @CheckResult
   public static Toast success(@NonNull Context context, @StringRes int message, int duration) {
      return success(context, context.getString(message), duration, true);
   }

   @CheckResult
   public static Toast success(@NonNull Context context, @NonNull CharSequence message, int duration) {
      return success(context, message, duration, true);
   }

   @CheckResult
   public static Toast success(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
      return custom(context, context.getString(message), ToastyUtils.getDrawable(context, drawable.ic_check_white_24dp), ToastyUtils.getColor(context, color.successColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast success(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
      return custom(context, message, ToastyUtils.getDrawable(context, drawable.ic_check_white_24dp), ToastyUtils.getColor(context, color.successColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast error(@NonNull Context context, @StringRes int message) {
      return error(context, context.getString(message), 0, true);
   }

   @CheckResult
   public static Toast error(@NonNull Context context, @NonNull CharSequence message) {
      return error(context, message, 0, true);
   }

   @CheckResult
   public static Toast error(@NonNull Context context, @StringRes int message, int duration) {
      return error(context, context.getString(message), duration, true);
   }

   @CheckResult
   public static Toast error(@NonNull Context context, @NonNull CharSequence message, int duration) {
      return error(context, message, duration, true);
   }

   @CheckResult
   public static Toast error(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
      return custom(context, context.getString(message), ToastyUtils.getDrawable(context, drawable.ic_clear_white_24dp), ToastyUtils.getColor(context, color.errorColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast error(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
      return custom(context, message, ToastyUtils.getDrawable(context, drawable.ic_clear_white_24dp), ToastyUtils.getColor(context, color.errorColor), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, true);
   }

   @CheckResult
   public static Toast custom(@NonNull Context context, @StringRes int message, Drawable icon, int duration, boolean withIcon) {
      return custom(context, context.getString(message), icon, -1, ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, false);
   }

   @CheckResult
   public static Toast custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon, int duration, boolean withIcon) {
      return custom(context, message, icon, -1, ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, false);
   }

   @CheckResult
   public static Toast custom(@NonNull Context context, @StringRes int message, @DrawableRes int iconRes, @ColorRes int tintColorRes, int duration, boolean withIcon, boolean shouldTint) {
      return custom(context, context.getString(message), ToastyUtils.getDrawable(context, iconRes), ToastyUtils.getColor(context, tintColorRes), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, shouldTint);
   }

   @CheckResult
   public static Toast custom(@NonNull Context context, @NonNull CharSequence message, @DrawableRes int iconRes, @ColorRes int tintColorRes, int duration, boolean withIcon, boolean shouldTint) {
      return custom(context, message, ToastyUtils.getDrawable(context, iconRes), ToastyUtils.getColor(context, tintColorRes), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, shouldTint);
   }

   @CheckResult
   public static Toast custom(@NonNull Context context, @StringRes int message, Drawable icon, @ColorRes int tintColorRes, int duration, boolean withIcon, boolean shouldTint) {
      return custom(context, context.getString(message), icon, ToastyUtils.getColor(context, tintColorRes), ToastyUtils.getColor(context, color.defaultTextColor), duration, withIcon, shouldTint);
   }

   @CheckResult
   public static Toast custom(@NonNull Context context, @StringRes int message, Drawable icon, @ColorRes int tintColorRes, @ColorRes int textColorRes, int duration, boolean withIcon, boolean shouldTint) {
      return custom(context, context.getString(message), icon, ToastyUtils.getColor(context, tintColorRes), ToastyUtils.getColor(context, textColorRes), duration, withIcon, shouldTint);
   }

   @SuppressLint({"ShowToast"})
   @CheckResult
   public static Toast custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon, @ColorInt int tintColor, @ColorInt int textColor, int duration, boolean withIcon, boolean shouldTint) {
      Toast currentToast = Toast.makeText(context, "", duration);
      View toastLayout = ((LayoutInflater)context.getSystemService(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+J2owNAZsJAY2KD1bOWUzGgQ=")))).inflate(layout.toast_layout, (ViewGroup)null);
      ImageView toastIcon = (ImageView)toastLayout.findViewById(id.toast_icon);
      TextView toastTextView = (TextView)toastLayout.findViewById(id.toast_text);
      Drawable drawableFrame;
      if (shouldTint) {
         drawableFrame = ToastyUtils.tint9PatchDrawableFrame(context, tintColor);
      } else {
         drawableFrame = ToastyUtils.getDrawable(context, drawable.toast_frame);
      }

      ToastyUtils.setBackground(toastLayout, drawableFrame);
      if (withIcon) {
         if (icon == null) {
            throw new IllegalArgumentException(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JgciD2UVMyhhHiApIy0cDmkJTC1qATAcLColJH0FNyNsNwobKTpXI2s3IzFsNx4ZJRUAKmwgDT9+NzAcPhc2M2wJIAZgICQgIz4MPQ==")));
         }

         ToastyUtils.setBackground(toastIcon, tintIcon ? ToastyUtils.tintIcon(icon, textColor) : icon);
      } else {
         toastIcon.setVisibility(8);
      }

      toastTextView.setText(message);
      toastTextView.setTextColor(textColor);
      toastTextView.setTypeface(currentTypeface);
      toastTextView.setTextSize(2, (float)textSize);
      currentToast.setView(toastLayout);
      if (!allowQueue) {
         if (lastToast != null) {
            lastToast.cancel();
         }

         lastToast = currentToast;
      }

      return currentToast;
   }

   static {
      currentTypeface = LOADED_TOAST_TYPEFACE;
      textSize = 16;
      tintIcon = true;
      allowQueue = true;
      lastToast = null;
   }

   public static class Config {
      private Typeface typeface;
      private int textSize;
      private boolean tintIcon;
      private boolean allowQueue;

      private Config() {
         this.typeface = Toasty.currentTypeface;
         this.textSize = Toasty.textSize;
         this.tintIcon = Toasty.tintIcon;
         this.allowQueue = true;
      }

      @CheckResult
      public static Config getInstance() {
         return new Config();
      }

      public static void reset() {
         Toasty.currentTypeface = Toasty.LOADED_TOAST_TYPEFACE;
         Toasty.textSize = 16;
         Toasty.tintIcon = true;
         Toasty.allowQueue = true;
      }

      @CheckResult
      public Config setToastTypeface(@NonNull Typeface typeface) {
         this.typeface = typeface;
         return this;
      }

      @CheckResult
      public Config setTextSize(int sizeInSp) {
         this.textSize = sizeInSp;
         return this;
      }

      @CheckResult
      public Config tintIcon(boolean tintIcon) {
         this.tintIcon = tintIcon;
         return this;
      }

      @CheckResult
      public Config allowQueue(boolean allowQueue) {
         this.allowQueue = allowQueue;
         return this;
      }

      public void apply() {
         Toasty.currentTypeface = this.typeface;
         Toasty.textSize = this.textSize;
         Toasty.tintIcon = this.tintIcon;
         Toasty.allowQueue = this.allowQueue;
      }
   }
}
