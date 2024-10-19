package com.lody.virtual.server.notification;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.compat.BuildCompat;

class WidthCompat {
   private static final String TAG = WidthCompat.class.getSimpleName();
   private volatile int mWidth = 0;

   public int getNotificationWidth(Context context, int width, int height, int padding) {
      if (this.mWidth > 0) {
         return this.mWidth;
      } else {
         int w = this.getDefaultWidth(width, padding);
         if (BuildCompat.isEMUI()) {
            w = this.getEMUINotificationWidth(context, width, height);
         } else if (BuildCompat.isMIUI()) {
            if (VERSION.SDK_INT >= 21) {
               padding = Math.round(TypedValue.applyDimension(1, 10.0F, context.getResources().getDisplayMetrics()));
               w = this.getMIUINotificationWidth(context, width - padding * 2, height);
            } else {
               padding = Math.round(TypedValue.applyDimension(1, 25.0F, context.getResources().getDisplayMetrics()));
               w = this.getMIUINotificationWidth(context, width - padding * 2, height);
            }
         }

         this.mWidth = w;
         return w;
      }
   }

   private int getDefaultWidth(int width, int padding) {
      return VERSION.SDK_INT >= 21 ? width - padding * 2 : width;
   }

   private int getMIUINotificationWidth(Context context, int width, int height) {
      try {
         Context systemUi = context.createPackageContext(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmoKLANvESgeKhgYVg==")), 3);
         int layoutId = this.getSystemId(systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki0qP2wKNANsJCw7IzxfDm8KBi9rNx4qLRcqI2AgRV9lNFkw")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+J2owNAY=")));
         if (layoutId != 0) {
            ViewGroup viewGroup = this.createViewGroup(systemUi, layoutId);
            int lid = this.getSystemId(systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LggqP28KMC9mNDBF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgqVg==")));
            View child;
            if (lid == 0) {
               lid = this.getSystemId(systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmEVRF")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgqVg==")));
            } else {
               child = viewGroup.findViewById(lid);
               if (child != null && child instanceof ViewGroup) {
                  ((ViewGroup)child).addView(new View(systemUi));
               }
            }

            this.layout(viewGroup, width, height);
            if (lid != 0) {
               child = viewGroup.findViewById(lid);
               if (child != null) {
                  return width - child.getLeft() - child.getPaddingLeft() - child.getPaddingRight();
               }
            } else {
               int count = viewGroup.getChildCount();

               for(int i = 0; i < count; ++i) {
                  child = viewGroup.getChildAt(i);
                  if (FrameLayout.class.isInstance(child) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Oxg+LGgaLAZrAQo/KgY+MWkgElo=")).equals(child.getClass().getName()) || StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ii4YImgbJCx9ASQgKQg+PWczQT9lJCg/")).equals(child.getClass().getName())) {
                     return width - child.getLeft() - child.getPaddingLeft() - child.getPaddingRight();
                  }
               }
            }
         }
      } catch (Exception var11) {
      }

      return width;
   }

   private int getEMUINotificationWidth(Context context, int width, int height) {
      try {
         Context systemUi = context.createPackageContext(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmoKLANvESgeKhgYVg==")), 3);
         int layoutId = this.getSystemId(systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRgYDWgYGjdnHgYp")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ixg+J2owNAY=")));
         if (layoutId != 0) {
            ViewGroup viewGroup = this.createViewGroup(systemUi, layoutId);
            this.layout(viewGroup, width, height);
            int lid = this.getSystemId(systemUi, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ACGwFNCZmHx4uKQcMI2MFEgRlJCg7")), StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LAgqVg==")));
            if (lid != 0) {
               View child = viewGroup.findViewById(lid);
               return width - child.getLeft() - child.getPaddingLeft() - child.getPaddingRight();
            }

            int count = viewGroup.getChildCount();

            for(int i = 0; i < count; ++i) {
               View child = viewGroup.getChildAt(i);
               if (LinearLayout.class.isInstance(child)) {
                  return width - child.getLeft() - child.getPaddingLeft() - child.getPaddingRight();
               }
            }
         }
      } catch (Exception var11) {
      }

      return width;
   }

   private int getSystemId(Context systemUi, String name, String type) {
      return systemUi.getResources().getIdentifier(name, type, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Li4ADXojJCZiESw1KQc1DmoKLANvESgeKhgYVg==")));
   }

   private ViewGroup createViewGroup(Context context, int layoutId) {
      try {
         return (ViewGroup)LayoutInflater.from(context).inflate(layoutId, (ViewGroup)null);
      } catch (Throwable var4) {
         return new FrameLayout(context);
      }
   }

   private void layout(View view, int width, int height) {
      view.layout(0, 0, width, height);
      view.measure(MeasureSpec.makeMeasureSpec(width, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(height, Integer.MIN_VALUE));
      view.layout(0, 0, width, height);
   }
}
