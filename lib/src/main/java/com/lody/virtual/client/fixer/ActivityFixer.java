package com.lody.virtual.client.fixer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import mirror.com.android.internal.R_Hide;

public final class ActivityFixer {
   private ActivityFixer() {
   }

   public static void fixActivity(Activity activity) {
      Context baseContext = activity.getBaseContext();

      try {
         TypedArray typedArray = activity.obtainStyledAttributes((int[])R_Hide.styleable.Window.get());
         if (typedArray != null) {
            boolean showWallpaper = typedArray.getBoolean(R_Hide.styleable.Window_windowShowWallpaper.get(), false);
            if (showWallpaper) {
               activity.getWindow().setBackgroundDrawable(WallpaperManager.getInstance(activity).getDrawable());
            }

            boolean fullscreen = typedArray.getBoolean(R_Hide.styleable.Window_windowFullscreen.get(), false);
            if (fullscreen) {
               activity.getWindow().addFlags(1024);
            }

            typedArray.recycle();
         }
      } catch (Throwable var9) {
         Throwable e = var9;
         e.printStackTrace();
      }

      if (VERSION.SDK_INT >= 21) {
         Intent intent = activity.getIntent();
         ApplicationInfo applicationInfo = baseContext.getApplicationInfo();
         PackageManager pm = activity.getPackageManager();
         if (intent != null && activity.isTaskRoot()) {
            try {
               String label = applicationInfo.loadLabel(pm) + "";
               Bitmap icon = null;
               Drawable drawable = applicationInfo.loadIcon(pm);
               if (drawable instanceof BitmapDrawable) {
                  icon = ((BitmapDrawable)drawable).getBitmap();
               }

               activity.setTaskDescription(new ActivityManager.TaskDescription(label, icon));
            } catch (Throwable var8) {
               Throwable e = var8;
               e.printStackTrace();
            }
         }
      }

   }
}
