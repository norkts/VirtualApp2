package com.lody.virtual.server.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.lody.virtual.StringFog;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VPackageManager;
import com.lody.virtual.helper.compat.NotificationChannelCompat;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;
import mirror.android.app.NotificationO;

@TargetApi(21)
class NotificationCompatCompatV21 extends NotificationCompatCompatV14 {
   private static final String TAG = NotificationCompatCompatV21.class.getSimpleName();

   public boolean dealNotification(int id, Notification notification, String packageName) {
      Context appContext = this.getAppContext(packageName);
      if (VERSION.SDK_INT >= 26 && VirtualCore.get().getTargetSdkVersion() >= 26 && TextUtils.isEmpty(notification.getChannelId())) {
         NotificationO.mChannelId.set(notification, NotificationChannelCompat.DEFAULT_ID);
      }

      try {
         return this.resolveRemoteViews(appContext, id, packageName, notification) || this.resolveRemoteViews(appContext, id, packageName, notification.publicVersion);
      } catch (Throwable var6) {
         VLog.e(TAG, "error deal Notification!");
         return false;
      }
   }

   private PackageInfo getOutSidePackageInfo(String packageName) {
      try {
         return VirtualCore.get().getHostPackageManager().getPackageInfo(packageName, 1024L);
      } catch (Throwable var3) {
         return null;
      }
   }

   private boolean resolveRemoteViews(Context appContext, int id, String packageName, Notification notification) {
      if (notification == null) {
         return false;
      } else {
         ApplicationInfo host = this.getHostContext().getApplicationInfo();
         PackageInfo outside = this.getOutSidePackageInfo(packageName);
         PackageInfo inside = VPackageManager.get().getPackageInfo(packageName, 1024, 0);
         boolean isInstalled = outside != null && outside.versionCode == inside.versionCode;
         this.getNotificationFixer().fixNotificationRemoteViews(appContext, notification);
         if (VERSION.SDK_INT >= 23) {
            this.getNotificationFixer().fixIcon(notification.getSmallIcon(), appContext, isInstalled);
            this.getNotificationFixer().fixIcon(notification.getLargeIcon(), appContext, isInstalled);
         } else {
            this.getNotificationFixer().fixIconImage(appContext.getResources(), notification.contentView, false, notification);
         }

         notification.icon = host.icon;
         ApplicationInfo proxyApplicationInfo;
         if (isInstalled) {
            proxyApplicationInfo = outside.applicationInfo;
         } else {
            proxyApplicationInfo = inside.applicationInfo;
         }

         proxyApplicationInfo.targetSdkVersion = 22;
         this.fixApplicationInfo(notification.tickerView, proxyApplicationInfo);
         this.fixApplicationInfo(notification.contentView, proxyApplicationInfo);
         this.fixApplicationInfo(notification.bigContentView, proxyApplicationInfo);
         this.fixApplicationInfo(notification.headsUpContentView, proxyApplicationInfo);
         Bundle bundle = (Bundle)Reflect.on((Object)notification).get("extras");
         if (bundle != null) {
            bundle.putParcelable("android.appInfo", proxyApplicationInfo);
         }

         if (VERSION.SDK_INT >= 26 && !isInstalled) {
            this.remakeRemoteViews(id, notification, appContext);
         }

         return true;
      }
   }

   private ApplicationInfo getApplicationInfo(Notification notification) {
      ApplicationInfo ai = this.getApplicationInfo(notification.tickerView);
      if (ai != null) {
         return ai;
      } else {
         ai = this.getApplicationInfo(notification.contentView);
         if (ai != null) {
            return ai;
         } else {
            if (VERSION.SDK_INT >= 16) {
               ai = this.getApplicationInfo(notification.bigContentView);
               if (ai != null) {
                  return ai;
               }
            }

            if (VERSION.SDK_INT >= 21) {
               ai = this.getApplicationInfo(notification.headsUpContentView);
               if (ai != null) {
                  return ai;
               }
            }

            return null;
         }
      }
   }

   private ApplicationInfo getApplicationInfo(RemoteViews remoteViews) {
      return remoteViews != null ? (ApplicationInfo)mirror.android.widget.RemoteViews.mApplication.get(remoteViews) : null;
   }

   private void fixApplicationInfo(RemoteViews remoteViews, ApplicationInfo ai) {
      if (remoteViews != null) {
         mirror.android.widget.RemoteViews.mApplication.set(remoteViews, ai);
      }

   }
}
