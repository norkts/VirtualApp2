package com.lody.virtual.server.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build.VERSION;
import android.widget.RemoteViews;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.compat.BuildCompat;
import com.lody.virtual.helper.utils.BitmapUtils;
import com.lody.virtual.helper.utils.Reflect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mirror.com.android.internal.R_Hide;

class NotificationFixer {
   private static final String TAG;
   private NotificationCompat mNotificationCompat;

   NotificationFixer(NotificationCompat notificationCompat) {
      this.mNotificationCompat = notificationCompat;
   }

   private static void fixNotificationIcon(Context context, Notification notification, Notification.Builder builder) {
      if (VERSION.SDK_INT < 23) {
         builder.setSmallIcon(notification.icon);
         builder.setLargeIcon(notification.largeIcon);
      } else {
         Icon icon = notification.getSmallIcon();
         if (icon != null) {
            Bitmap bitmap = BitmapUtils.drawableToBitmap(icon.loadDrawable(context));
            if (bitmap != null) {
               Icon newIcon = Icon.createWithBitmap(bitmap);
               builder.setSmallIcon(newIcon);
            }
         }

         Icon largeIcon = notification.getLargeIcon();
         if (largeIcon != null) {
            Bitmap bitmap = BitmapUtils.drawableToBitmap(largeIcon.loadDrawable(context));
            if (bitmap != null) {
               Icon newIcon = Icon.createWithBitmap(bitmap);
               builder.setLargeIcon(newIcon);
            }
         }
      }

   }

   @TargetApi(23)
   void fixIcon(Icon icon, Context appContext, boolean installed) {
      if (icon != null) {
         int type = (Integer)mirror.android.graphics.drawable.Icon.mType.get(icon);
         if (type == 2) {
            if (installed) {
               mirror.android.graphics.drawable.Icon.mObj1.set(icon, appContext.getResources());
               mirror.android.graphics.drawable.Icon.mString1.set(icon, appContext.getPackageName());
            } else {
               Drawable drawable = icon.loadDrawable(appContext);
               Bitmap bitmap = BitmapUtils.drawableToBitmap(drawable);
               mirror.android.graphics.drawable.Icon.mObj1.set(icon, bitmap);
               mirror.android.graphics.drawable.Icon.mString1.set(icon, (Object)null);
               mirror.android.graphics.drawable.Icon.mType.set(icon, 1);
            }
         }

      }
   }

   @TargetApi(21)
   void fixNotificationRemoteViews(Context pluginContext, Notification notification) {
      Notification.Builder rebuild = null;

      try {
         rebuild = (Notification.Builder)Reflect.on(Notification.Builder.class).create(pluginContext, notification).get();
      } catch (Exception var5) {
      }

      if (rebuild != null) {
         Notification renotification = rebuild.build();
         if (notification.tickerView == null) {
            notification.tickerView = renotification.tickerView;
         }

         if (notification.contentView == null) {
            notification.contentView = renotification.contentView;
         }

         if (notification.bigContentView == null) {
            notification.bigContentView = renotification.bigContentView;
         }

         if (notification.headsUpContentView == null) {
            notification.headsUpContentView = renotification.headsUpContentView;
         }
      }

   }

   boolean fixRemoteViewActions(Context appContext, boolean installed, RemoteViews remoteViews) {
      boolean hasIcon = false;
      if (remoteViews != null) {
         int systemIconViewId = R_Hide.id.icon.get();
         List<BitmapReflectionAction> mNew = new ArrayList();
         ArrayList<Object> mActions = (ArrayList)Reflect.on((Object)remoteViews).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwY+OWwFAiVgNyhF")));
         if (mActions != null) {
            int count = mActions.size();
            int i = count - 1;

            label80:
            while(true) {
               if (i < 0) {
                  Iterator var16 = mNew.iterator();

                  while(true) {
                     if (!var16.hasNext()) {
                        break label80;
                     }

                     BitmapReflectionAction action = (BitmapReflectionAction)var16.next();
                     remoteViews.setBitmap(action.viewId, action.methodName, action.bitmap);
                  }
               }

               Object action = mActions.get(i);
               if (action != null) {
                  if (action.getClass().getSimpleName().endsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IRguIGwIOC9iATgWIz0iI24jRSRrDzgqKggYKWAzSFo=")))) {
                     mActions.remove(action);
                  } else if (ReflectionActionCompat.isInstance(action)) {
                     int viewId = (Integer)Reflect.on(action).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4YM2wxAiw=")));
                     String methodName = (String)Reflect.on(action).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("IwguLGUFGixoNCA3KAhSVg==")));
                     int type = (Integer)Reflect.on(action).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")));
                     Object value = Reflect.on(action).get(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNFo=")));
                     if (!hasIcon) {
                        hasIcon = viewId == systemIconViewId;
                        if (hasIcon && type == 4 && (Integer)value == 0) {
                           hasIcon = false;
                        }
                     }

                     if (methodName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLH0VEjdiJDAAKAgqDWUgRSlrAVRF")))) {
                        mNew.add(new BitmapReflectionAction(viewId, StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLH0VEjdiJDAQKQg2D24gTVo=")), BitmapUtils.drawableToBitmap(appContext.getResources().getDrawable((Integer)value))));
                        mActions.remove(action);
                     } else if (methodName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGQFNDBmEVRF"))) && type == 4) {
                        Reflect.on(action).set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KRcYKGgVSFo=")), 9);
                        Reflect.on(action).set(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("KT4+DmwVNFo=")), appContext.getResources().getString((Integer)value));
                     } else if (methodName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGIFJCpiDlEUKi4uVg==")))) {
                        mActions.remove(action);
                     } else if (methodName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLGMjJCljJDgqKi4MDmk2RStsJwYwKS42Jw==")))) {
                        mActions.remove(action);
                     } else if (methodName.equals(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("Ki4uLH0VEjdiJDBKOzscVg==")))) {
                        Uri uri = (Uri)value;
                        if (!uri.getScheme().startsWith(StringFog.decrypt(com.kook.librelease.StringFog.decrypt("LBcqLG8FSFo=")))) {
                           mActions.remove(action);
                        }
                     } else if (VERSION.SDK_INT >= 23 && value instanceof Icon) {
                        Icon icon = (Icon)value;
                        this.fixIcon(icon, appContext, installed);
                     }
                  }
               }

               --i;
            }
         }

         if (VERSION.SDK_INT < 21) {
            mirror.android.widget.RemoteViews.mPackage.set(remoteViews, VirtualCore.get().getHostPkg());
         }
      }

      return hasIcon;
   }

   void fixIconImage(Resources resources, RemoteViews remoteViews, boolean hasIconBitmap, Notification notification) {
      if (remoteViews != null && notification.icon != 0) {
         if (this.mNotificationCompat.isSystemLayout(remoteViews)) {
            try {
               int id = R_Hide.id.icon.get();
               if (!hasIconBitmap && notification.largeIcon == null) {
                  Drawable drawable = null;
                  Bitmap bitmap = null;

                  try {
                     drawable = resources.getDrawable(notification.icon);
                     drawable.setLevel(notification.iconLevel);
                     bitmap = BitmapUtils.drawableToBitmap(drawable);
                  } catch (Throwable var9) {
                  }

                  remoteViews.setImageViewBitmap(id, bitmap);
                  if (BuildCompat.isEMUI() && notification.largeIcon == null) {
                     notification.largeIcon = bitmap;
                  }
               }
            } catch (Throwable var10) {
               Throwable e = var10;
               e.printStackTrace();
            }

         }
      }
   }

   static {
      TAG = NotificationCompat.TAG;
   }

   private static class BitmapReflectionAction {
      int viewId;
      String methodName;
      Bitmap bitmap;

      BitmapReflectionAction(int viewId, String methodName, Bitmap bitmap) {
         this.viewId = viewId;
         this.methodName = methodName;
         this.bitmap = bitmap;
      }
   }
}
