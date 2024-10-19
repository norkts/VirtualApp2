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
         Context systemUi = context.createPackageContext("com.android.systemui", 3);
         int layoutId = this.getSystemId(systemUi, "status_bar_notification_row", "layout");
         if (layoutId != 0) {
            ViewGroup viewGroup = this.createViewGroup(systemUi, layoutId);
            int lid = this.getSystemId(systemUi, "adaptive", "id");
            View child;
            if (lid == 0) {
               lid = this.getSystemId(systemUi, "content", "id");
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
                  if (FrameLayout.class.isInstance(child) || "LatestItemView".equals(child.getClass().getName()) || "SizeAdaptiveLayout".equals(child.getClass().getName())) {
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
         Context systemUi = context.createPackageContext("com.android.systemui", 3);
         int layoutId = this.getSystemId(systemUi, "time_axis", "layout");
         if (layoutId != 0) {
            ViewGroup viewGroup = this.createViewGroup(systemUi, layoutId);
            this.layout(viewGroup, width, height);
            int lid = this.getSystemId(systemUi, "content_view_group", "id");
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
      return systemUi.getResources().getIdentifier(name, type, "com.android.systemui");
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
