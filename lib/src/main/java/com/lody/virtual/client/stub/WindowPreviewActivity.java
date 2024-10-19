package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.am.AttributeCache;
import mirror.android.graphics.drawable.LayerDrawable;
import mirror.com.android.internal.R_Hide;

public class WindowPreviewActivity extends Activity {
   private long startTime;

   public static void previewActivity(int userId, ActivityInfo info) {
      Context context = VirtualCore.get().getContext();
      Intent windowBackgroundIntent = new Intent(context, WindowPreviewActivity.class);

      try {
         boolean isFixedOrientationLandscape = StubManifest.isFixedOrientationLandscape(info);
         VLog.d(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("ITw9DQ==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KhcMM2wjAitmIiA5LBccLGwgBj94ER46Ji4YPGIKFhRlNBouKRccO28VAgNqJQIoJj0MOmkgOCZpIDxF")) + isFixedOrientationLandscape + StringFog.decrypt(com.kook.librelease.StringFog.decrypt("MxgYCGgjBTI=")) + info);
         if (isFixedOrientationLandscape) {
            windowBackgroundIntent = new Intent(context, WindowPreviewActivity_Land.class);
         }
      } catch (Exception var5) {
         Exception e = var5;
         e.printStackTrace();
      }

      windowBackgroundIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHgVhJDAqJi0cPg==")), userId);
      windowBackgroundIntent.putExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHjd9JwozLD0cLmgmNC9lNyQc")), info);
      windowBackgroundIntent.addFlags(268435456);
      windowBackgroundIntent.addFlags(65536);
      context.startActivity(windowBackgroundIntent);
   }

   protected void onCreate(Bundle savedInstanceState) {
      this.startTime = System.currentTimeMillis();
      this.overridePendingTransition(0, 0);
      super.onCreate(savedInstanceState);
      Intent intent = this.getIntent();
      if (intent == null) {
         this.finish();
      } else {
         ActivityInfo info = (ActivityInfo)intent.getParcelableExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHjd9JwozLD0cLmgmNC9lNyQc")));
         int userId = intent.getIntExtra(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("JysiEWYwHgVhJDAqJi0cPg==")), -1);
         if (info != null && userId != -1) {
            int theme = info.theme;
            if (theme == 0) {
               theme = info.applicationInfo.theme;
            }

            AttributeCache.Entry windowExt = AttributeCache.instance().get(info.packageName, theme, (int[])R_Hide.styleable.Window.get());
            if (windowExt != null) {
               boolean fullscreen = windowExt.array.getBoolean(R_Hide.styleable.Window_windowFullscreen.get(), false);
               boolean translucent = windowExt.array.getBoolean(R_Hide.styleable.Window_windowIsTranslucent.get(), false);
               boolean disablePreview = windowExt.array.getBoolean(R_Hide.styleable.Window_windowDisablePreview.get(), false);
               if (disablePreview) {
                  return;
               }

               if (fullscreen) {
                  this.getWindow().addFlags(1024);
               }

               Drawable drawable = null;
               AttributeCache.Entry viewEnt = AttributeCache.instance().get(info.packageName, info.theme, (int[])R_Hide.styleable.View.get());
               if (viewEnt != null) {
                  try {
                     drawable = viewEnt.array.getDrawable(R_Hide.styleable.View_background.get());
                  } catch (Throwable var14) {
                  }
               }

               if (drawable == null) {
                  try {
                     drawable = windowExt.array.getDrawable(R_Hide.styleable.Window_windowBackground.get());
                  } catch (Throwable var13) {
                  }
               }

               if (drawable != null && !this.isDrawableBroken(drawable)) {
                  this.getWindow().setBackgroundDrawable(drawable);
               } else {
                  if (!translucent) {
                     this.getWindow().setBackgroundDrawable(new ColorDrawable(-1));
                  }

                  WindowManager.LayoutParams lp = this.getWindow().getAttributes();
                  lp.dimAmount = 0.4F;
                  this.getWindow().setAttributes(lp);
                  this.getWindow().addFlags(2);
               }
            }

         } else {
            this.finish();
         }
      }
   }

   private boolean isDrawableBroken(Drawable drawable) {
      if (LayerDrawable.TYPE.isInstance(drawable) && LayerDrawable.isProjected != null) {
         try {
            LayerDrawable.isProjected.callWithException(drawable);
            return false;
         } catch (Throwable var3) {
            Throwable throwable = var3;
            VLog.e(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IS4YCGgFGj1pESw/LD0cPWULQSlvER49IxcqMw==")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Jj4+PHsKIARiATwzKAg5Om4VQSlqJyA5LD0uKmJTPFo=")), throwable);
            return true;
         }
      } else {
         return false;
      }
   }

   public void onBackPressed() {
      long time = System.currentTimeMillis();
      if (time - this.startTime > 5000L) {
         this.finish();
      }

   }

   protected void onStop() {
      super.onStop();
      this.finish();
   }
}
