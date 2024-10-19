package com.lody.virtual.server.notification;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.kook.librelease.R.dimen;
import com.kook.librelease.R.id;
import com.kook.librelease.R.layout;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import java.util.ArrayList;
import java.util.Iterator;

class RemoteViewsFixer {
   private static final String TAG;
   private static final boolean DEBUG = false;
   private final WidthCompat mWidthCompat = new WidthCompat();
   private int notification_min_height;
   private int notification_max_height;
   private int notification_mid_height;
   private int notification_panel_width;
   private int notification_side_padding;
   private int notification_padding;
   private NotificationCompat mNotificationCompat;
   private boolean init = false;

   RemoteViewsFixer(NotificationCompat notificationCompat) {
      this.mNotificationCompat = notificationCompat;
   }

   View toView(Context context, RemoteViews remoteViews, boolean isBig) {
      View mCache = null;

      try {
         mCache = this.createView(context, remoteViews, isBig);
      } catch (Throwable var8) {
         try {
            mCache = LayoutInflater.from(context).inflate(remoteViews.getLayoutId(), (ViewGroup)null);
         } catch (Throwable var7) {
         }
      }

      return mCache;
   }

   Bitmap createBitmap(View mCache) {
      if (mCache == null) {
         return null;
      } else {
         mCache.setDrawingCacheEnabled(true);
         mCache.buildDrawingCache();
         return mCache.getDrawingCache();
      }
   }

   private View apply(Context context, RemoteViews remoteViews) {
      View view = null;

      Exception e2;
      try {
         view = LayoutInflater.from(context).inflate(remoteViews.getLayoutId(), (ViewGroup)null, false);

         try {
            Reflect.on((Object)view).call(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQFJC1rDlkgKAguDm4jOFo=")), Reflect.on(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmwjMAZrDgobLRgDKmk0FixuEVRF"))).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KS4YPGgzNAZsJDwqLwdXPQ=="))), remoteViews.getLayoutId());
         } catch (Exception var9) {
            e2 = var9;
            VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQFJC1rDlkgKAguDm4jOFo=")), e2);
         }
      } catch (Exception var10) {
         e2 = var10;
         VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgcPmoFJAZiAVRF")), e2);
      }

      if (view != null) {
         ArrayList<Object> mActions = (ArrayList)Reflect.on((Object)remoteViews).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwY+OWwFAiVgNyhF")));
         if (mActions != null) {
            VLog.d(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoKDSh9DiggKQdfDmoOIFo=")) + mActions.size());
            Iterator var5 = mActions.iterator();

            while(var5.hasNext()) {
               Object action = var5.next();

               try {
                  Reflect.on(action).call(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoKAlo=")), view, null, null);
               } catch (Exception var8) {
                  Exception e = var8;
                  VLog.w(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Lgc6KGoKDSh9DiggKQdfDg==")), e);
               }
            }
         }
      }

      return view;
   }

   private View createView(Context context, RemoteViews remoteViews, boolean isBig) {
      if (remoteViews == null) {
         return null;
      } else {
         Context base = this.mNotificationCompat.getHostContext();
         this.init(base);
         int height = isBig ? this.notification_max_height : this.notification_min_height;
         int width = this.mWidthCompat.getNotificationWidth(base, this.notification_panel_width, height, this.notification_side_padding);
         ViewGroup frameLayout = new FrameLayout(context);
         View view1 = this.apply(context, remoteViews);
         FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1);
         params.gravity = 16;
         ((ViewGroup)frameLayout).addView(view1, params);
         if (view1 instanceof ViewGroup) {
            VLog.v(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li0MM2saMCtuNAY/LCoIPGwgFlFrDlk/Ii4YJ2YjSFo=")));
            this.fixTextView((ViewGroup)view1);
         }

         int mode = Integer.MIN_VALUE;
         View mCache = frameLayout;
         ((View)mCache).layout(0, 0, width, height);
         ((View)mCache).measure(MeasureSpec.makeMeasureSpec(width, 1073741824), MeasureSpec.makeMeasureSpec(height, mode));
         ((View)mCache).layout(0, 0, width, ((View)mCache).getMeasuredHeight());
         return mCache;
      }
   }

   private void fixTextView(ViewGroup viewGroup) {
      int count = viewGroup.getChildCount();

      for(int i = 0; i < count; ++i) {
         View v = viewGroup.getChildAt(i);
         if (v instanceof TextView) {
            TextView tv = (TextView)v;
            if (this.isSingleLine(tv)) {
               tv.setSingleLine(false);
               tv.setMaxLines(1);
            }
         } else if (v instanceof ViewGroup) {
            this.fixTextView((ViewGroup)v);
         }
      }

   }

   private boolean isSingleLine(TextView textView) {
      boolean singleLine;
      try {
         singleLine = (Boolean)Reflect.on((Object)textView).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwU2CWojPCRiDFEzKj0MVg==")));
      } catch (Exception var4) {
         singleLine = (textView.getInputType() & 131072) != 0;
      }

      return singleLine;
   }

   public RemoteViews makeRemoteViews(String key, Context pluginContext, RemoteViews contentView, boolean isBig, boolean click) {
      if (contentView == null) {
         return null;
      } else {
         PendIntentCompat pendIntentCompat = new PendIntentCompat(contentView);
         int layoutId;
         if (click && pendIntentCompat.findPendIntents() > 0) {
            layoutId = layout.custom_notification;
         } else {
            layoutId = layout.custom_notification_lite;
         }

         RemoteViews remoteViews;
         if (VERSION.SDK_INT > 19 && mirror.android.widget.RemoteViews.ctor != null) {
            remoteViews = (RemoteViews)mirror.android.widget.RemoteViews.ctor.newInstance(this.mNotificationCompat.getHostContext().getApplicationInfo(), layoutId);
         } else {
            remoteViews = new RemoteViews(this.mNotificationCompat.getHostContext().getPackageName(), layoutId);
         }

         View cache = this.toView(pluginContext, contentView, isBig);
         Bitmap bmp = this.createBitmap(cache);
         remoteViews.setImageViewBitmap(id.im_main, bmp);
         if (click && layoutId == layout.custom_notification) {
            try {
               pendIntentCompat.setPendIntent(remoteViews, this.toView(this.mNotificationCompat.getHostContext(), remoteViews, isBig), cache);
            } catch (Exception var12) {
               Exception e = var12;
               VLog.e(TAG, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGcFNCZiHAY2LBcMDmU3TStsNAocKS5SVg==")), e);
            }
         }

         return remoteViews;
      }
   }

   private void init(Context context) {
      if (!this.init) {
         this.init = true;
         if (this.notification_panel_width == 0) {
            Context systemUi = null;

            try {
               systemUi = context.createPackageContext(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmoKLANvESgeKhgYVg==")), 2);
            } catch (PackageManager.NameNotFoundException var4) {
            }

            if (VERSION.SDK_INT <= 19) {
               this.notification_side_padding = 0;
            } else {
               this.notification_side_padding = this.getDimem(context, systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDmMKAi9rESgMKQg+IGIaGiluJ1RF")), dimen.notification_side_padding);
            }

            this.notification_panel_width = this.getDimem(context, systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDmMKTTdlNygdID0mI2IVFis=")), dimen.notification_panel_width);
            if (this.notification_panel_width <= 0) {
               this.notification_panel_width = context.getResources().getDisplayMetrics().widthPixels;
            }

            this.notification_min_height = this.getDimem(context, systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDmMFPC9lMgYZLhgYIWMVFlo=")), dimen.notification_min_height);
            this.notification_max_height = this.getDimem(context, systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDmMFPDduHAYZLhgYIWMVFlo=")), dimen.notification_max_height);
            this.notification_mid_height = this.getDimem(context, systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDmMFPC9rHAYZLhgYIWMVFlo=")), dimen.notification_mid_height);
            this.notification_padding = this.getDimem(context, systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Iz4ALGUVOC99JCAgKQdfDmMKTTdrESwaLC4mVg==")), dimen.notification_padding);
         }

      }
   }

   private int getDimem(Context context, Context sysContext, String name, int defId) {
      if (sysContext != null) {
         int id = sysContext.getResources().getIdentifier(name, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LRgYDWgVBlo=")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmoKLANvESgeKhgYVg==")));
         if (id != 0) {
            try {
               return Math.round(sysContext.getResources().getDimension(id));
            } catch (Exception var7) {
            }
         }
      }

      return defId == 0 ? 0 : Math.round(context.getResources().getDimension(defId));
   }

   static {
      TAG = NotificationCompat.TAG;
   }
}
